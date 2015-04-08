Ext.define('NX.coreui.services.ClientStores', {
  statics: {
    writePolicies: Ext.create('Ext.data.Store', {
      fields: ['name'],
      data: [
        {'name': 'ALLOW'},
        {'name': 'ALLOW_ONCE'},
        {'name': 'DENY'}
      ]
    }),
    versionPolicies: Ext.create('Ext.data.Store', {
      fields: ['name'],
      data: [
        {'name': 'RELEASE'},
        {'name': 'SNAPSHOT'},
        {'name': 'MIXED'}
      ]
    }),
    checksumPolicies: Ext.create('Ext.data.Store', {
      fields: ['name'],
      data: [
        {'name': 'IGNORE'},
        {'name': 'WARN'},
        {'name': 'STRICT_IF_EXISTS'},
        {'name': 'STRICT'}
      ]
    })
  }
});
