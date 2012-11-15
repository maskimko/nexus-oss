/**
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2012 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.yum.plugin.event;

import static org.sonatype.appcontext.internal.Preconditions.checkNotNull;

import org.sonatype.nexus.proxy.repository.Repository;

public class YumRepositoryGenerateEvent
{

    private final Repository repository;

    public YumRepositoryGenerateEvent( final Repository repository )
    {
        this.repository = checkNotNull( repository );
    }

    public Repository getRepository()
    {
        return repository;
    }

}
