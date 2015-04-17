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

import org.sonatype.sisu.goodies.common.ComponentSupport;

/**
 * A generic node visitor.
 *
 * @since 3.0
 */
public class Visitor<T>
    extends ComponentSupport
{
  /**
   * Always invoked before visiting begins, with a new TX.
   */
  public void before(StorageTx tx) {}

  /**
   * Invoked for each visited node, with a new TX.
   */
  public void visit(StorageTx tx, T node) {}

  /**
   * Always invoked in case of visiting is finished cleanly, with a new TX.
   */
  public void after(StorageTx tx) {}

  /**
   * Always invoked in case of visiting is interrupted with an exception. This method will be invoked even if this
   * visitor's {@link #visit(StorageTx, Object)} method threw.
   */
  public void failure(Exception e) {}
}
