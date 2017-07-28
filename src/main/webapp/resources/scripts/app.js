window.app = {};
window.seditor = {};
var app = window.app;
var seditor = window.seditor;

requirejs.config({
	baseUrl: './scripts',
	shim: {
		jquery: {
			exports : '$'
		},
		bootstrap: {
            deps: [ 'jquery']
        },
        bootstrapdatetimepicker: {
            deps: [ 'bootstrap','moment']
        },
        bootbox: {
            deps: [ 'bootstrap']
        },
        openlayers: {
        	exports : 'ol'
        },
        seditor: {
            deps: [ 'bootbox','bootstrapdatetimepicker']
        },
	},
	paths: {
        jquery: 'lib/jquery/jquery.min',
        bootstrap: 'lib/bootstrap/bootstrap.min',
        moment: 'lib/moment/moment-with-locales',
        bootstrapdatetimepicker: 'lib/bootstrap/bootstrap.datetimepicker',
        bootbox: 'lib/bootstrap/bootbox.min',
        proj4: 'lib/proj4/proj4',
        openlayers: 'lib/openlayers/ol',
		seditor: 'lib/seditor/seditor'
    }
});

requirejs(
	[
		'proj4',
		'openlayers',
		'jquery',
		'bootbox',
		'helper/functions',
		'configuration/config',
		'seditor'
	], 
	function (proj4,ol,jQuery,bootbox) {
		app.loadConfiguration = function() {
			var workspace = getUrlParameter('workspace');
			if( workspace === undefined ) {
				// PopUp : Veuillez renseigner un espace de travail + syntaxe
				console.error('Pas de workspace précisé !');
			    bootbox.prompt(
			    	"Quelle est la clef de l'espace de travail ?", 
			    	function(result){ 
			    		console.log(result);
			    		window.location = window.location+'?workspace=' + result;
			    	})
				return;
			}
	
			$.getJSON(config.serverURL+'/api/configuration', { workspace: workspace }, function(reponse) {
				app.configuration = reponse;
		
				if( app.configuration.projection.code != 'EPSG:4326' && app.configuration.projection.code != 'EPSG:3857' ) {
					proj4.defs(app.configuration.projection.code, app.configuration.projection.def);
				}
				
				if( app.configuration.featureType == 'POINT' ) {
					app.configuration.featureTypes = ['Point'];
				}
				if( app.configuration.featureType == 'LINESTRING' ) {
					app.configuration.featureTypes = ['LineString'];
				}
				if( app.configuration.featureType == 'POLYGON' ) {
					app.configuration.featureTypes = ['Polygon','Circle'];
				}
		
				app.init();		
			}).fail(function(erreur) {
				console.warn('Problème de chargement de la configuration.');
				console.error('Erreur '+erreur.status+' : '+erreur.statusText);
			});
		};
		
		app.init = function() {
			ol.proj.setProj4(proj4);
			
			var styleFunction = (function() {
				var styles = {};
				styles['nochange'] = new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(255, 255, 255, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: '#ffcc33',
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#ffcc33'
						})
					})
				});
				
				styles['created'] = new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(0, 0, 255, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: '#0000ff',
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#0000ff'
						})
					})
				});
				
				styles['modified'] = new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(255, 0, 255, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: '#ff00ff',
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#ff00ff'
						})
					})
				});
				
				styles['deleted'] = new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(255, 0, 0, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: '#ff0000',
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#ff0000'
						})
					})
				});
				
				styles['default'] = new ol.style.Style({
					fill: new ol.style.Fill({
						color: 'rgba(96, 96, 96, 0.2)'
					}),
					stroke: new ol.style.Stroke({
						color: '#606060',
						width: 2
					}),
					image: new ol.style.Circle({
						radius: 7,
						fill: new ol.style.Fill({
							color: '#606060'
						})
					})
				});
				
				return function(feature) {
					return styles[feature.get('state')] || styles['default'];
				};
			})();
			
			app.features = new ol.Collection();
	
			app.editedLayer = new ol.layer.Vector({
				source: new ol.source.Vector({
					wrapX: false,
					features: app.features
				}),
				style: styleFunction
			});
			
			app.map = new ol.Map({
				layers: [
					new ol.layer.Tile({
						source: new ol.source.OSM()
					}),
					app.editedLayer
				],
				target: 'map',
				controls: ol.control.defaults({
		  			attributionOptions: ({
		    				collapsible: false
		  			})
				}),
				view: new ol.View({
					projection: app.configuration.projection.code,
					center: [app.configuration.center.x,app.configuration.center.y],
					zoom: app.configuration.center.zoom
				})
			});
			
			app.layersList = [];
			app.snapVectorSourcesList = [];
			app.configuration.layers.forEach(function(item) {
				var wmsLayer = new ol.layer.Image({
					source: new ol.source.ImageWMS({
						url: item.url+'/wms',
						params: {'LAYERS': item.typename},
						serverType: 'geoserver',
						crossOrigin: 'anonymous',
						visible: item.visible
					})
				});
				wmsLayer.set('name',item.typename);
				app.layersList.push(wmsLayer);
				
				if(item.snappable) {
					var vectorSource = new ol.source.Vector({
						format: new ol.format.GeoJSON(),
						url: function(extent) {
							return item.url+'/wfs?service=WFS&version=1.1.0&request=GetFeature&maxFeatures=3000&typename='+item.typename+'&outputFormat=application/json&srsname='+app.configuration.projection.code+'&bbox='+extent.join(',')+','+app.configuration.projection.code;
						},
						strategy: ol.loadingstrategy.bbox,
						projection: app.configuration.projection.code
					});
					vectorSource.set('name',item.typename);
					app.snapVectorSourcesList.push(vectorSource);
				}
			});
			
			app.editor = seditor.initialize(app.map,{
				workspace: app.configuration.workspace,
				geometryTypes: app.configuration.featureTypes,
				features: app.features,
				attributes: app.configuration.attributes,
				serviceURL: config.serverURL,
				accessLevel: app.configuration.accessLevel,
				editLayer: app.editedLayer,
				user: app.configuration.user,
				layers: app.layersList,
				snapSources: app.snapVectorSourcesList
			});
		};
		
		app.loadConfiguration();
	}
);
