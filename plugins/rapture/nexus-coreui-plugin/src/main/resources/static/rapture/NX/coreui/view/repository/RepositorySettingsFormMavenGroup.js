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
/*global Ext, NX*/

/**
 * Repository "Settings" form for a Maven Group repository.
 *
 * @since 3.0
 */
Ext.define('NX.coreui.view.repository.RepositorySettingsFormMavenGroup', {
  extend: 'NX.coreui.view.repository.RepositorySettingsForm',
  alias: 'widget.nx-coreui-repository-settings-form-maven2-group',
  requires: [
    'NX.Conditions',
    'NX.I18n'
  ],

  initComponent: function() {
    var me = this;

    me.items = [
      { xtype: 'nx-coreui-repository-settings-facet-view'},
      { xtype: 'nx-coreui-repository-settings-facet-storage'},
      { xtype: 'nx-coreui-repository-settings-facet-maven2'},
      { xtype: 'nx-coreui-repository-settings-facet-group', format: 'maven2' }
    ];

    me.callParent(arguments);
  }
});