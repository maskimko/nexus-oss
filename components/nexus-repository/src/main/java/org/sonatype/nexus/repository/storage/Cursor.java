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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * A generic node cursor.
 *
 * @since 3.0
 */
public interface Cursor<T>
    extends AutoCloseable
{
  /**
   * Provides a chunk of node IDs from cursor. If no more chunks, empty iterator is returned, never returns {@code
   * null}. It is not guaranteed that the calls will happen using same {@link StorageTx}, so the cursor must keep
   * some internal state and probably implement pagination on it's own.
   */
  @Nonnull
  Iterable<T> next(StorageTx tx);

  /**
   * Provides a fresh instance of the node, using current transaction. Might return {@code null} for various reasons,
   * for example node got deleted between {@link #next(StorageTx)} and this method invocation.
   */
  @Nullable
  T node(StorageTx tx, T node);

  /**
   * Closes the cursor, making the instance ineligible for further use.
   */
  void close();
}
