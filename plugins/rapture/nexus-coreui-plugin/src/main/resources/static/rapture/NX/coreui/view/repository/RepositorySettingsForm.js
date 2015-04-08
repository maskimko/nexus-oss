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
 * Repository "Settings" form.
 *
 * @since 3.0
 */
Ext.define('NX.coreui.view.repository.RepositorySettingsForm', {
  extend: 'NX.view.SettingsForm',
  alias: 'widget.nx-coreui-repository-settings-form',
  requires: [
    'NX.Conditions',
    'NX.I18n',
    'NX.coreui.services.RepositoryFormService'
  ],

  api: {
    submit: 'NX.direct.coreui_Repository.update'
  },
  settingsFormSuccessMessage: function(data) {
    return NX.I18n.get('ADMIN_REPOSITORIES_UPDATE_SUCCESS') + data['name'];
  },

  editableMarker: NX.I18n.get('ADMIN_REPOSITORIES_UPDATE_ERROR'),

  initComponent: function() {
    var me = this;

    me.editableCondition = me.editableCondition || NX.Conditions.isPermitted('nexus:repositories', 'update');

    var items = [
      new Ext.form.Text({
        name: 'name',
        itemId: 'name',
        fieldLabel: NX.I18n.get('ADMIN_REPOSITORIES_SETTINGS_NAME'),
        readOnly: true
      }),
      new Ext.form.TextArea({
        xtype: 'textarea',
        name: 'attributes',
        fieldLabel: NX.I18n.get('ADMIN_REPOSITORIES_SETTINGS_ATTRIBUTES'),
        height: 300,
        allowBlank: true,
        cls: 'nx-log-viewer-field'
      }),
      //{ xtype: 'nx-coreui-formfield-settingsfieldset' }
    ];
    me.items = items;

    me.callParent(arguments);

    //TODO - listen for changes to the recipe and update form as appropriate
    me.on('recordloaded', function(form, record) {
      me.items.clear();
      me.items.addAll(items);
      console.log('recordloaded: ');
      me.items.addAll(NX.coreui.services.RepositoryFormService.fieldsFor(record.get('attributesMap')));
      //Ext.iterate(record.get('attributesMap'), function(key, value) {
      //  //console.log(key);
      //  //console.log(value);
      //  me.items.add(new Ext.form.FieldSet({title: Ext.String.capitalize(key)}));
      //  Ext.iterate(value, function(k, v) {
      //    me.items.add(new Ext.form.Text({
      //          name: 'attributes.' + key + '.' + k,
      //          itemId: 'attributes.' + key + '.' + k,
      //          fieldLabel: Ext.String.capitalize(k),
      //          value: v
      //        })
      //    );
      //  });
      //});
    });
    me.on('beforeaction', function(form, action, eOpts) {
    console.log(form);  
    console.log(action);  
    console.log(eOpts);  
    });
  }
});
