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
package org.sonatype.nexus.testsuite.task.nexus533;

import java.util.Calendar;
import java.util.Date;

import org.sonatype.nexus.index.tasks.UpdateIndexTask;
import org.sonatype.nexus.index.tasks.descriptors.UpdateIndexTaskDescriptor;
import org.sonatype.nexus.rest.model.ScheduledServiceOnceResource;
import org.sonatype.nexus.rest.model.ScheduledServicePropertyResource;

import org.apache.commons.lang.time.DateUtils;

public class Nexus533TaskOnceIT
    extends AbstractNexusTasksIntegrationIT<ScheduledServiceOnceResource>
{

  private static ScheduledServiceOnceResource scheduledTask;

  @Override
  public ScheduledServiceOnceResource getTaskScheduled() {
    if (scheduledTask == null) {
      scheduledTask = new ScheduledServiceOnceResource();
      scheduledTask.setEnabled(true);
      scheduledTask.setId(null);
      scheduledTask.setName("taskOnce");
      scheduledTask.setSchedule("once");
      // A future date
      Date startDate = DateUtils.addDays(new Date(), 10);
      startDate = DateUtils.round(startDate, Calendar.DAY_OF_MONTH);
      scheduledTask.setStartDate(String.valueOf(startDate.getTime()));
      scheduledTask.setStartTime("03:30");

      scheduledTask.setTypeId(UpdateIndexTask.class.getName());

      ScheduledServicePropertyResource prop = new ScheduledServicePropertyResource();
      prop.setKey("repositoryId");
      prop.setValue("all_repo");
      scheduledTask.addProperty(prop);
    }
    return scheduledTask;
  }

  @Override
  public void updateTask(ScheduledServiceOnceResource scheduledTask) {
    scheduledTask.setStartTime("00:00");
  }

}
