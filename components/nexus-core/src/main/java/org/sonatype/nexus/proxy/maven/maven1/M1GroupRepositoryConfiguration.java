/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2015 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.proxy.maven.maven1;

import org.sonatype.nexus.proxy.maven.AbstractMavenGroupRepositoryConfiguration;

import org.codehaus.plexus.util.xml.Xpp3Dom;

/**
 * @deprecated To be removed once Maven1 support is removed.
 */
@Deprecated
public class M1GroupRepositoryConfiguration
    extends AbstractMavenGroupRepositoryConfiguration
{
  public M1GroupRepositoryConfiguration(Xpp3Dom configuration) {
    super(configuration);
  }

}
