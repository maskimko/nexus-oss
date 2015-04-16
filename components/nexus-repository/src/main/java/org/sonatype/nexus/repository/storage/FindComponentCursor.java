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

import java.util.Formatter;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.sonatype.nexus.repository.Repository;
import org.sonatype.sisu.goodies.common.ComponentSupport;

import com.google.common.base.Strings;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * A component cursor that uses {@link StorageTx#findComponents(String, Map, Iterable, String)} method combined with
 * skip-limit paging to create "chunks".
 *
 * @since 3.0
 */
public class FindComponentCursor
    extends ComponentSupport
    implements ComponentCursor
{
  private static final int DEFAULT_PAGE_SIZE = 100;

  @Nullable
  private final String whereClause;

  @Nullable
  private final Map<String, Object> parameters;

  @Nullable
  private final Iterable<Repository> repositories;

  private final String querySuffix;

  private final int pageSize;

  public FindComponentCursor(final @Nullable String whereClause,
                             final @Nullable Map<String, Object> parameters,
                             final @Nullable Iterable<Repository> repositories,
                             final @Nullable String querySuffix)
  {
    this(whereClause, parameters, repositories, querySuffix, DEFAULT_PAGE_SIZE);
  }

  public FindComponentCursor(final @Nullable String whereClause,
                             final @Nullable Map<String, Object> parameters,
                             final @Nullable Iterable<Repository> repositories,
                             final @Nullable String querySuffix,
                             final int pageSize)
  {
    checkArgument(pageSize > 0, "must be a positive integer: %s", pageSize);

    this.whereClause = whereClause;
    this.parameters = parameters;
    this.repositories = repositories;
    this.querySuffix = querySuffix;
    this.pageSize = pageSize;
  }

  @Nonnull
  @Override
  public Cursor open() {
    return new FCursor(
        whereClause,
        parameters,
        repositories,
        querySuffix,
        pageSize
    );
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "whereClause='" + whereClause + '\'' +
        ", parameters=" + parameters +
        ", repositories=" + repositories +
        ", querySuffix='" + querySuffix + '\'' +
        ", pageSize=" + pageSize +
        '}';
  }

  private static class FCursor
      implements Cursor
  {
    @Nullable
    private final String whereClause;

    @Nullable
    private final Map<String, Object> parameters;

    @Nullable
    private final Iterable<Repository> repositories;

    private final String pagingQuerySuffix;

    private final Formatter formatter;

    private final int pageSize;

    private int skip;

    private int limit;

    public FCursor(final @Nullable String whereClause,
                   final @Nullable Map<String, Object> parameters,
                   final @Nullable Iterable<Repository> repositories,
                   final @Nullable String querySuffix,
                   final int pageSize)
    {
      checkArgument(pageSize > 0, "must be a positive integer: %s", pageSize);

      this.whereClause = whereClause;
      this.parameters = parameters;
      this.repositories = repositories;
      final StringBuilder stringBuilder = new StringBuilder();
      if (!Strings.isNullOrEmpty(querySuffix)) {
        stringBuilder.append(querySuffix).append(" ");
      }
      stringBuilder.append("skip %s limit %s");
      this.pagingQuerySuffix = stringBuilder.toString();
      this.formatter = new Formatter(Locale.US);

      this.pageSize = pageSize;
      this.skip = 0;
      this.limit = this.pageSize;
    }

    @Nonnull
    @Override
    public Iterable<Component> next(final StorageTx tx) {
      try {
        return tx.findComponents(
            whereClause,
            parameters,
            repositories,
            formatter.format(pagingQuerySuffix, skip, limit).toString()
        );
      }
      finally {
        skip = limit;
        limit = limit + pageSize;
      }
    }

    @Override
    public void close() {
      // nop
    }

    @Override
    public String toString() {
      return getClass().getSimpleName() + "{" +
          "whereClause='" + whereClause + '\'' +
          ", parameters=" + parameters +
          ", repositories=" + repositories +
          ", pagingQuerySuffix='" + pagingQuerySuffix + '\'' +
          ", formatter=" + formatter +
          ", pageSize=" + pageSize +
          ", skip=" + skip +
          ", limit=" + limit +
          '}';
    }
  }
}
