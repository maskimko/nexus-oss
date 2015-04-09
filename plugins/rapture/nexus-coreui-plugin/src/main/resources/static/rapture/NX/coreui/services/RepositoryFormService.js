Ext.define('NX.coreui.services.RepositoryFormService', {
  singleton: true,
  mixins: [
    'NX.LogAware'
  ],
  requires: [
    'NX.I18n'
  ],

  facetFields: {
    proxy: function(attributesMap) {
      var data = attributesMap['proxy'];
      return [
        {
          id: 'proxy.remoteUrl',
          label: 'Remote URL',
          helpText: NX.I18n.get('LEGACY_ADMIN_REPOSITORIES_SETTINGS_REMOTE_HELP'),
          required: true,
          initialValue: data ? data.remoteUrl : null
        },
        {
          id: 'proxy.artifactMaxAge',
          type: 'number',
          label: 'Maximum Artifact Age',
          required: true,
          initialValue: data ? data.artifactMaxAge : null,
          minValue: -1,
          maxValue: 3600
        }
      ]
    },
    negativeCache: Ext.emptyFn,
    view: Ext.emptyFn,
    storage: function(attributesMap) {
      var data = attributesMap['storage'];
      return [
        {
          id: 'storage.writePolicies',
          type: 'combo',
          label: 'Write Policy',
          required: true,
          storeApi: 'coreui_Repository.readWritePolicies',
          initialValue: data ? data.writePolicy : null
        }
      ];
    },
    maven: function(attributesMap) {
      var data = attributesMap['maven'];
      return [
        {
          id: 'maven.versionPolicy',
          type: 'combo',
          label: 'Version Policy',
          required: true,
          storeApi: 'coreui_Repository.readVersionPolicies',
          initialValue: data ? data.versionPolicy : null
        },
        {
          id: 'maven.checksumPolicy',
          type: 'combo',
          label: 'Checksum Policy',
          required: true,
          storeApi: 'coreui_Repository.readChecksumPolicies',
          initialValue: data ? data.checksumPolicy : null
        },
        {
          id: 'maven.strictContentTypeValidation',
          type: 'checkbox',
          label: 'Strict Content Type Validation',
          required: true,
          initialValue: data ? data.strictContentTypeValidation : null
        }
      ]
    },
    httpclient: function(attributesMap) {
      var data = attributesMap['httpclient'];
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
    }
  },
  getRecipe: function(recipeName) {
    var me = this, recipes = {
      'maven2-proxy': [
        me.facetFields.proxy, me.facetFields.maven, me.facetFields.storage, me.facetFields.httpclient
      ],
      'maven2-hosted': [
        me.facetFields.maven, me.facetFields.storage
      ],
      'maven2-group': [
        me.facetFields.maven, me.facetFields.storage/*, me.facetFields.group*/
      ],
      'nuget-hosted': [],
      'nuget-proxy':[
        me.facetFields.proxy,
        me.facetFields.httpclient
      ],
      'nuget-group': [ /*me.facetFields.group*/]
    }, recipe = recipes[recipeName];
    if (!recipe) {
      me.logDebug('Unable to find recipe for format: ' + format + ' and type: ' + type);
    }
    return recipe || [];
  },
  fieldsForModel: function(model) {
    return this.fieldsFor(model.get('format') + '-' + model.get('type'), model.get('attributesMap'));
  },
  fieldsFor: function(recipeName, attributesMap) {
    var me = this, items = [];
    var recipe = me.getRecipe(recipeName);
    Ext.each(recipe, function(facet) {
      items.push.apply(items, facet(attributesMap || {}));
    });
    return items;  
  },
  propertiesForModel : function(model) {
    var me = this, attributesMap = model.get('attributesMap'), properties = {};
    //TODO - trnaslate these per 'facet'
    for (var property in attributesMap) {
      if (attributesMap.hasOwnProperty(property)) {
        Ext.iterate(attributesMap[property], function (k,v) {
           properties[property + '.' + k] = v;
        });
      }
    }
    return properties;
  },
  mapAttributes: function(record, attributes) {
    //TODO - translate these back into attributes map
    console.log('mapping attributes: ');
    console.log(attributes);
  }
});
