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
package org.sonatype.nexus.configuration.model;

import java.util.Collection;

import org.sonatype.nexus.common.throwables.ConfigurationException;
import org.sonatype.nexus.configuration.RevertableConfiguration;
import org.sonatype.nexus.validation.ValidationResponse;
import org.sonatype.nexus.validation.ValidationResponseException;

import com.thoughtworks.xstream.XStream;

public abstract class AbstractRevertableConfiguration<C>
    implements RevertableConfiguration<C>
{
  private final XStream xstream = new XStream();

  private volatile C originalConfiguration;

  private volatile C changedConfiguration;

  public C getConfiguration(boolean forWrite) {
    if (forWrite) {
      synchronized (this) {
        if (getOriginalConfiguration() != null && getChangedConfiguration() == null) {
          // copy it
          final C copy = copyObject(getOriginalConfiguration(), null);

          copyTransients(getOriginalConfiguration(), copy);

          setChangedConfiguration(copy);
        }
      }

      return getChangedConfiguration();

    }
    else {
      return getOriginalConfiguration();
    }
  }

  protected XStream getXStream() {
    return xstream;
  }

  protected C getOriginalConfiguration() {
    return originalConfiguration;
  }

  public void setOriginalConfiguration(C originalConfiguration) {
    this.originalConfiguration = originalConfiguration;
  }

  protected C getChangedConfiguration() {
    return changedConfiguration;
  }

  public void setChangedConfiguration(C changedConfiguration) {
    this.changedConfiguration = changedConfiguration;
  }

  @SuppressWarnings("unchecked")
  protected C copyObject(C source, C target) {
    if (source == null && target == null) {
      return null;
    }
    else if (source instanceof Collection<?> && target != null) {
      // one exception is config object is actually a list, we need to keep the same instance
      ((Collection) target).clear();

      ((Collection) target).addAll((Collection) source);

      return target;
    }
    else if (target == null) {
      // "clean" deep copy
      return (C) getXStream().fromXML(getXStream().toXML(source));
    }
    else {
      // "overlay" actually
      return (C) getXStream().fromXML(getXStream().toXML(source), target);
    }
  }

  protected void copyTransients(C source, C destination) {
    // usually none, but see CRepository
  }

  protected boolean isThisDirty() {
    return getChangedConfiguration() != null;
  }

  public boolean isDirty() {
    return isThisDirty();
  }

  public void validateChanges() {
    if (isThisDirty()) {
      checkValidationResponse(doValidateChanges(getChangedConfiguration()));
    }
  }

  public synchronized void commitChanges() {
    if (isThisDirty()) {
      try {
        checkValidationResponse(doValidateChanges(getChangedConfiguration()));
      }
      catch (Exception e) {
        rollbackChanges();
        throw new ConfigurationException(e);
      }

      // nice, isn't it?
      copyObject(getChangedConfiguration(), getOriginalConfiguration());

      copyTransients(getChangedConfiguration(), getOriginalConfiguration());

      setChangedConfiguration(null);
    }
  }

  public void rollbackChanges() {
    if (isThisDirty()) {
      setChangedConfiguration(null);
    }
  }

  // ==

  protected void checkValidationResponse(ValidationResponse response) {
    if (response != null && !response.isValid()) {
      throw new ValidationResponseException(response);
    }
  }

  protected ValidationResponse doValidateChanges(C changedConfiguration) {
    // This method should be overridden to perform validation on underlying model
    return new ValidationResponse();
  }
}
