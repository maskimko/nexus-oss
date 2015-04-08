Ext.define('NX.coreui.services.RepositoryFormService', {
  requires: [
    'NX.coreui.services.ClientStores'
  ],
  //facets :{
  //  proxy: Ext.emptyFn,
  //  negativeCache: Ext.emptyFn,
  //  view: Ext.emptyFn,
  //  storage: function(data) {
  //    return [
  //      new Ext.form.FieldSet({name: 'storage', title: 'Storage', items:[
  //        new Ext.form.Text({name: 'writePolicy', fieldLabel: 'Write Policy', value: data ? data.writePolicy : null})    
  //      ]})
  //    ]
  //    //"writePolicy" : "ALLOW"
  //
  //  },
  //  maven: function(data) {
  //    //"strictContentTypeValidation" : false,
  //    //    "versionPolicy" : "MIXED",
  //    //    "checksumPolicy" : "WARN"
  //  },
  //  httpclient: function(data){
  //    //"connection" : {
  //    //  "retries" : 3,
  //    //      "timeout" : 1500
  //    //}
  //  }
  //},

  statics: {
    fieldsFor: function(attributesMap) {

      //var writePolicies = Ext.create('Ext.data.Store', {
      //      fields: ['name'],
      //      data: [
      //        {'name': 'ALLOW'},
      //        {'name': 'ALLOW_ONCE'},
      //        {'name': 'DENY'}
      //      ]
      //    }),
      //    versionPolicies = Ext.create('Ext.data.Store', {
      //      fields: ['name'],
      //      data: [
      //        {'name': 'RELEASE'},
      //        {'name': 'SNAPSHOT'},
      //        {'name': 'MIXED'}
      //      ]
      //    }),
      //    checksumPolicies = Ext.create('Ext.data.Store', {
      //      fields: ['name'],
      //      data: [
      //        {'name': 'IGNORE'},
      //        {'name': 'WARN'},
      //        {'name': 'STRICT_IF_EXISTS'},
      //        {'name': 'STRICT'}
      //      ]
      //    });

      var facets = {
        proxy: function(data) {
          //"remoteUrl" : "http://repo1.maven.org/maven2/",
          //    "artifactMaxAge" : 3600
        },
        negativeCache: Ext.emptyFn,
        view: Ext.emptyFn,
        storage: function(data) {
          return [
            Ext.create('Ext.form.ComboBox', {
              fieldLabel: 'Write Policy',
              name: 'writePolicy',
              store: NX.coreui.services.ClientStores.writePolicies,
              queryMode: 'local',
              displayField: 'name',
              valueField: 'name',
              value: data ? data.writePolicy : null
            })
          ]
        },
        maven: function(data) {
          return [
              //Ext.create('Ext.form.CheckBox', {
              //  
              //}),
            Ext.create('Ext.form.ComboBox', {
              fieldLabel: 'Version Policy',
              store: NX.coreui.services.ClientStores.versionPolicies,
              name: 'versionPolicy',
              queryMode: 'local',
              displayField: 'name',
              valueField: 'name',
              value: data ? data.versionPolicy : null
            }),
            Ext.create('Ext.form.ComboBox', {
              fieldLabel: 'Checksum Policy',
              store: NX.coreui.services.ClientStores.checksumPolicies,
              name: 'checksumPolicy',
              queryMode: 'local',
              displayField: 'name',
              valueField: 'name',
              value: data ? data.checksumPolicy : null
            })
          ]
          //"strictContentTypeValidation" : false,
          //    "versionPolicy" : "MIXED",
          //    "checksumPolicy" : "WARN"
        },
        httpclient: function(data) {
          //"connection" : {
          //  "retries" : 3,
          //      "timeout" : 1500
          //}
        }
      };
      var items = [];
      Ext.iterate(attributesMap, function(key, value) {
        console.log(key);
        var facet = facets[key];
        if (!facet) {
          console.log('No facet configured for: ' + key);
        }
        else {
          items.push.apply(items, facet(value));
          //items.push(new Ext.form.FieldSet({title: Ext.String.capitalize(key)}));
          //Ext.iterate(value, function(k, v) {
          //  items.push(new Ext.form.Text({
          //        name: 'attributes.' + key + '.' + k,
          //        itemId: 'attributes.' + key + '.' + k,
          //        fieldLabel: Ext.String.capitalize(k),
          //        value: v
          //      })
          //  );
          //});  
        }

      });
      return items;
    }
  }
});
