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
package org.sonatype.nexus.index.tasks.descriptors;

import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.formfields.RepositoryCombobox;
import org.sonatype.nexus.formfields.StringTextFormField;
import org.sonatype.nexus.proxy.maven.maven2.Maven2ContentClass;
import org.sonatype.nexus.scheduling.Task;
import org.sonatype.nexus.scheduling.TaskConfiguration;
import org.sonatype.nexus.scheduling.TaskDescriptorSupport;

public abstract class AbstractIndexTaskDescriptor<T extends Task>
    extends TaskDescriptorSupport<T>
{
  public AbstractIndexTaskDescriptor(Class<T> type, String name) {
    super(type, name,
        new RepositoryCombobox(
            TaskConfiguration.REPOSITORY_ID_KEY,
            "Repository",
            "Select the Maven repository to " + name.toLowerCase() + " the index.",
            FormField.MANDATORY
        ).includeAnEntryForAllRepositories().includingAnyOfContentClasses(Maven2ContentClass.ID),
        new StringTextFormField(
            TaskConfiguration.PATH_KEY,
            "Repository path",
            "Enter a repository path to run the task in recursively (ie. \"/\" for root or \"/org/apache\").",
            FormField.OPTIONAL)
    );
  }
}