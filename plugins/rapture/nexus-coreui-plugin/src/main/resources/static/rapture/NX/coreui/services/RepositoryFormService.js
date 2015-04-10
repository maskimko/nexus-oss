Ext.define('NX.coreui.services.RepositoryFormService', {
  singleton: true,
  mixins: [
    'NX.LogAware'
  ],
  requires: [
    'NX.I18n'
  ],

  facets: {

    proxy: {
      fields: function() {
        return [
          {
            id: 'proxy.remoteUrl',
            label: 'Remote URL',
            helpText: NX.I18n.get('LEGACY_ADMIN_REPOSITORIES_SETTINGS_REMOTE_HELP'),
            required: true
          },
          {
            id: 'proxy.artifactMaxAge',
            type: 'number',
            label: 'Maximum Artifact Age',
            required: true,
            minValue: -1,
            maxValue: 3600
          }
        ]
      },
      toMap: function(properties, attributes) {
        Ext.apply(attributes, {
          proxy: {
            remoteUrl: properties['proxy.remoteUrl'],
            artifactMaxAge: parseInt(properties['proxy.artifactMaxAge'])
          }  
        });
      },
      toProperties: function(attributes, properties) {
        Ext.apply(properties, {
          'proxy.remoteUrl': attributes.proxy.remoteUrl,
          'proxy.artifactMaxAge': attributes.proxy.artifactMaxAge
        });
      }
    },
    negativeCache: {
      fields: Ext.emptyFn,
      toMap: Ext.emptyFn,
      toProperties: Ext.emptyFn
    },
    view: {
      fields: Ext.emptyFn,
      toMap: Ext.emptyFn,
      toProperties: Ext.emptyFn
    },
    storage: {
      fields: function() {
        return [
          {
            id: 'storage.writePolicy',
            type: 'combo',
            label: 'Write Policy',
            required: true,
            storeApi: 'coreui_Repository.readWritePolicies'
          }
        ];
      },
      toMap: function(properties, attributes) {
        Ext.apply(attributes, {
          storage: {
            writePolicy: properties['storage.writePolicy']
          }
        });
      },
      toProperties: function(attributes, properties) {
        Ext.apply(properties, {
          'storage.writePolicy': attributes.storage.writePolicy
        });
      }
    },
    maven: {
      fields: function() {
        return [
          {
            id: 'maven.versionPolicy',
            type: 'combo',
            label: 'Version Policy',
            required: true,
            storeApi: 'coreui_Repository.readVersionPolicies'
          },
          {
            id: 'maven.checksumPolicy',
            type: 'combo',
            label: 'Checksum Policy',
            required: true,
            storeApi: 'coreui_Repository.readChecksumPolicies'
          },
          {
            id: 'maven.strictContentTypeValidation',
            type: 'checkbox',
            label: 'Strict Content Type Validation',
            required: true
          }
        ]
      },
      toMap: function(properties, attributes) {
        Ext.apply(attributes, {
          maven: {
            versionPolicy: properties['maven.versionPolicy'],
            checksumPolicy: properties['maven.checksumPolicy'],
            strictContentTypeValidation: Boolean.valueOf(properties['maven.strictContentTypeValidation'])
          }
        });
      },
      toProperties: function(attributes, properties) {
        Ext.apply(properties, {
          'maven.versionPolicy': attributes.maven.versionPolicy,
          'maven.checksumPolicy': attributes.maven.checksumPolicy,
          'maven.strictContentTypeValidation': attributes.maven.strictContentTypeValidation
        });
      }
    },
    httpclient: {
      fields: function() {
        return [
          {
            id: 'httpclient.connection.retries',
            type: 'number',
            label: 'Connection Retries',
            required: true,
            minValue: 0,
            maxValue: 20
          },
          {
            id: 'httpclient.connection.timeout',
            type: 'number',
            label: 'Connection Timeout',
            required: true,
            minValue: 1,
            maxValue: 10000
          }
        ]
      },
      toMap: function(properties, attributes) {
        Ext.apply(attributes, {
          httpclient: { 
            connection: {
              retries: parseInt(properties['httpclient.connection.retries']),
              timeout: parseInt(properties['httpclient.connection.timeout'])
            }
          }
        });
      },
      toProperties: function(attributes, properties) {
        Ext.apply(properties, {
          'httpclient.connection.retries': attributes.httpclient.connection.retries,
          'httpclient.connection.timeout': attributes.httpclient.connection.timeout
        });
      }
    },
    group: {
      fields: function() {
        return [
          {
            id: 'group.memberNames',
            idMapping: 'name',
            type: 'combo',
            label: 'Group Members',
            required: true,
            storeApi: 'coreui_Repository.read',
            multiSelect: true
          }
        ]
      },
      toMap: function(properties, attributes) {
        Ext.apply(attributes, {
          group: {
            memberNames: properties['group.memberNames'].split(',')
          }
        });
      },
      toProperties: function(attributes, properties) {
        Ext.apply(properties, {
          'group.memberNames': attributes.group.memberNames
        });
      }
    }
  },
  getRecipe: function(recipeName) {
    var me = this, recipes = {
      'maven2-proxy': [
        me.facets.proxy, me.facets.maven, me.facets.storage, me.facets.httpclient
      ],
      'maven2-hosted': [
        me.facets.maven, me.facets.storage
      ],
      'maven2-group': [
        me.facets.maven, me.facets.storage, me.facets.group
      ],
      'nuget-hosted': [],
      'nuget-proxy': [
        me.facets.proxy,
        me.facets.httpclient
      ],
      'nuget-group': [me.facets.group]
    }, recipe = recipes[recipeName];
    if (!recipe) {
      me.logDebug('Unable to find recipe for recipe: ' + recipeName);
    }
    return recipe || [];
  },
  recipeName: function(model) {
    return model.get('format') + '-' + model.get('type');
  },
  fieldsForModel: function(model) {
    return this.fieldsFor(this.recipeName(model));
  },
  fieldsFor: function(recipeName) {
    var me = this, items = [];
    var recipe = me.getRecipe(recipeName);
    Ext.each(recipe, function(facet) {
      items.push.apply(items, facet.fields());
    });
    return items;
  },
  propertiesForModel: function(model) {
    var me = this, attributes = model.get('attributesMap'), properties = {};
    var recipe = me.getRecipe(me.recipeName(model));
    Ext.each(recipe, function(facet) {
      facet.toProperties(attributes, properties);      
    });
    return properties;
  },
  mapAttributes: function(recipeName, properties) {
    var me = this, attributes = {};
    var recipe = me.getRecipe(recipeName);
    Ext.each(recipe, function(facet) {
      facet.toMap(properties, attributes);
    });
    return Ext.encode(attributes);
  }
});
