/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-2015 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */

package org.sonatype.nexus.repository.storage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

import org.sonatype.nexus.blobstore.api.BlobStore;
import org.sonatype.nexus.blobstore.api.BlobStoreManager;
import org.sonatype.nexus.common.stateguard.Guarded;
import org.sonatype.nexus.orient.DatabaseInstance;
import org.sonatype.nexus.repository.FacetSupport;
import org.sonatype.nexus.repository.config.Configuration;
import org.sonatype.nexus.repository.config.ConfigurationFacet;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import org.hibernate.validator.constraints.NotEmpty;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.sonatype.nexus.common.stateguard.StateGuardLifecycleSupport.State.STARTED;

/**
 * Default {@link StorageFacet} implementation.
 *
 * @since 3.0
 */
@Named
public class StorageFacetImpl
    extends FacetSupport
    implements StorageFacet
{
  private final BlobStoreManager blobStoreManager;

  private final Provider<DatabaseInstance> databaseInstanceProvider;

  private final BucketEntityAdapter bucketEntityAdapter;

  private final ComponentEntityAdapter componentEntityAdapter;

  private final AssetEntityAdapter assetEntityAdapter;

  @VisibleForTesting
  static final String CONFIG_KEY = "storage";

  @VisibleForTesting
  static class Config
  {
    @NotEmpty
    public String blobStoreName = "default";

    //@NotNull
    public WritePolicy writePolicy;

    @Override
    public String toString() {
      return getClass().getSimpleName() + "{" +
          "blobStoreName='" + blobStoreName + '\'' +
          ", writePolicy=" + writePolicy +
          '}';
    }
  }

  private Config config;

  private Bucket bucket;

  @Inject
  public StorageFacetImpl(final BlobStoreManager blobStoreManager,
                          final @Named(ComponentDatabase.NAME) Provider<DatabaseInstance> databaseInstanceProvider,
                          final BucketEntityAdapter bucketEntityAdapter,
                          final ComponentEntityAdapter componentEntityAdapter,
                          final AssetEntityAdapter assetEntityAdapter)
  {
    this.blobStoreManager = checkNotNull(blobStoreManager);
    this.databaseInstanceProvider = checkNotNull(databaseInstanceProvider);

    this.bucketEntityAdapter = checkNotNull(bucketEntityAdapter);
    this.componentEntityAdapter = checkNotNull(componentEntityAdapter);
    this.assetEntityAdapter = checkNotNull(assetEntityAdapter);
  }

  @Override
  protected void doValidate(final Configuration configuration) throws Exception {
    facet(ConfigurationFacet.class).validateSection(configuration, CONFIG_KEY, Config.class);
  }

  @Override
  protected void doConfigure(final Configuration configuration) throws Exception {
    config = facet(ConfigurationFacet.class).readSection(configuration, CONFIG_KEY, Config.class);
    log.debug("Config: {}", config);
  }

  @Override
  protected void doInit(final Configuration configuration) throws Exception {
    initSchema();
    initBucket();
    super.doInit(configuration);
  }

  private void initSchema() {
    try (ODatabaseDocumentTx db = databaseInstanceProvider.get().connect()) {
      bucketEntityAdapter.register(db);
      componentEntityAdapter.register(db);
      assetEntityAdapter.register(db);
    }
  }

  private void initBucket() {
    // get or create the bucket for the repository and set bucketId for fast lookup later
    try (ODatabaseDocumentTx db = databaseInstanceProvider.get().acquire()) {
      String repositoryName = getRepository().getName();
      bucket = bucketEntityAdapter.getByRepositoryName(db, repositoryName);
      if (bucket == null) {
        bucketEntityAdapter.add(db, bucket = new Bucket().repositoryName(repositoryName));
        db.commit();
      }
    }
  }

  @Override
  protected void doDestroy() throws Exception {
    config = null;
  }

  @Override
  protected void doDelete() throws Exception {
    // TODO: Make this a soft delete and cleanup later so it doesn't block for large repos.
    try (StorageTx tx = openStorageTx()) {
      tx.deleteBucket(tx.getBucket());
    }
  }

  @Override
  @Guarded(by = STARTED)
  public StorageTx openTx() {
    return openStorageTx();
  }

  private StorageTx openStorageTx() {
    BlobStore blobStore = blobStoreManager.get(config.blobStoreName);
    return new StorageTxImpl(
        new BlobTx(blobStore), databaseInstanceProvider.get().acquire(), bucket, config.writePolicy,
        bucketEntityAdapter, componentEntityAdapter, assetEntityAdapter
    );
  }

  @Override
  @Guarded(by = STARTED)
  public <T> void visit(final Supplier<Cursor<T>> cursorSupplier,
                        final Visitor<T> visitor)
  {
    checkNotNull(cursorSupplier);
    checkNotNull(visitor);

    log.debug("Visiting started: cursorSupplier={}, visitor={}", cursorSupplier, visitor);
    try {
      try (StorageTx tx = openTx()) {
        visitor.before(tx);
      }
      final List<T> nodes = Lists.newArrayList();
      try (Cursor<T> cursor = cursorSupplier.get()) {
        while (true) {
          nodes.clear();
          try (StorageTx tx = openTx()) {
            Iterables.addAll(
                nodes,
                cursor.next(tx)
            );
          }

          if (nodes.isEmpty()) {
            break;
          }

          try (StorageTx tx = openTx()) {
            for (T node : nodes) {
              final T freshNode = cursor.node(tx, node);
              if (freshNode != null) {
                visitor.visit(tx, freshNode);
              }
            }
          }
        }
        log.debug("Components cursor exhausted: cursor={}, visitor={}", cursor, visitor);
      }
      try (StorageTx tx = openTx()) {
        visitor.after(tx);
      }
      log.debug("Visiting components finished: cursorSupplier={}, visitor={}",
          cursorSupplier, visitor);
    }
    catch (Exception e) {
      log.warn("Visiting components failed: cursorSupplier={}, visitor={}",
          cursorSupplier, visitor, e);
      visitor.failure(e);
      Throwables.propagateIfPossible(e);
      throw Throwables.propagate(e);
    }
  }
}
