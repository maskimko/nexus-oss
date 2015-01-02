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
package org.sonatype.security.realms.ldap.internal.persist;

import java.util.List;

import com.sonatype.security.ldap.realms.persist.model.CLdapServerConfiguration;

import org.sonatype.configuration.validation.InvalidConfigurationException;

public interface LdapConfigurationManager
{

  void clearCache();

  List<CLdapServerConfiguration> listLdapServerConfigurations();

  CLdapServerConfiguration getLdapServerConfiguration(String id)
      throws InvalidConfigurationException, LdapServerNotFoundException;

  void updateLdapServerConfiguration(CLdapServerConfiguration ldapServerConfiguration)
      throws InvalidConfigurationException, LdapServerNotFoundException;

  void addLdapServerConfiguration(CLdapServerConfiguration ldapServerConfiguration)
      throws InvalidConfigurationException;

  void deleteLdapServerConfiguration(String id) throws InvalidConfigurationException, LdapServerNotFoundException;

  void setServerOrder(List<String> orderdServerIds) throws InvalidConfigurationException;

}
