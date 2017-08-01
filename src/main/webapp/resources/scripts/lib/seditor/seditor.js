define('seditor',
	[
		'openlayers',
		'bootbox',
		'bootstrapdatetimepicker',
		'lib/seditor/controls/LayersListControl',
		'lib/seditor/controls/CustomToolbarControl',
		'lib/seditor/controls/CreateFeatureControl',
		'lib/seditor/controls/ModifyFeatureControl',
		'lib/seditor/controls/SelectFeatureControl',
		'lib/seditor/controls/DragFeatureControl',
		'lib/seditor/controls/DeleteFeatureControl',
		'lib/seditor/controls/NavigationControl',
		'lib/seditor/controls/SnappingControl',
		'lib/seditor/controls/SaveFeaturesControl'
	],
	function(ol, bootbox) {
		seditor.initialize = function(map, options) {
			seditor.showLoadingDialog();
			
			seditor.map = map;
	
			if(!options) options = {};
			seditor.editLayer = options.editLayer;
			seditor.attributes = options.attributes;
			seditor.features = options.features;
			seditor.serviceURL = options.serviceURL;
			seditor.workspace = options.workspace;
			seditor.accessLevel = options.accessLevel;
			seditor.userName = options.user;
			seditor.layers = options.layers;
			seditor.snapSources = options.snapSources;
			
			seditor.selectedFeatures = new ol.Collection();
			seditor.toolbar = new seditor.CustomToolbarControl();
			
			// Add WMS layers
			seditor.layers.forEach(function(item) {
				seditor.map.getLayers().insertAt(1,item);
			});
			// and layerlist's control
			seditor.map.addControl(new seditor.LayersListControl({
				layers: seditor.layers
			}));
			
			// Generate toolbar
			if( seditor.accessLevel > 1 ) {
				for(var i=0 ; i < options.geometryTypes.length ; i++) {
					seditor.toolbar.appendControl(new seditor.CreateFeatureControl({
						geometryType: options.geometryTypes[i],
						features: seditor.features,
						map: seditor.map,
						toolbar: seditor.toolbar,
						createCallback: seditor.showFeatureForm
					}));
				}
				seditor.toolbar.appendControl(new seditor.ModifyFeatureControl({
					features: options.features,
					layers: [seditor.editLayer],
					map: seditor.map,
					toolbar: seditor.toolbar,
					modifyCallback: seditor.showFeatureForm,
					selectedFeatures: seditor.selectedFeatures,
					checkFeatureEditAllowed: seditor.isFeatureOwner
				}));
				seditor.toolbar.appendControl(new seditor.SelectFeatureControl({
					layers: [seditor.editLayer],
					map: seditor.map,
					toolbar: seditor.toolbar,
					selectedFeatures: seditor.selectedFeatures,
					checkFeatureEditAllowed: seditor.isFeatureOwner
				}));
				seditor.toolbar.appendControl(new seditor.DragFeatureControl({
					layers: [seditor.editLayer],
					map: seditor.map,
					toolbar: seditor.toolbar,
					selectedFeatures: seditor.selectedFeatures,
					checkFeatureEditAllowed: seditor.isFeatureOwner
				}));
				seditor.toolbar.appendControl(new seditor.DeleteFeatureControl({
					features: seditor.features,
					selectedFeatures: seditor.selectedFeatures
				}));
				seditor.toolbar.appendControl(new seditor.SnappingControl({
					map: seditor.map,
					snapSources: seditor.snapSources,
					editedFeatures: seditor.features
				}));
			}
			
			var navControl = new seditor.NavigationControl({
				map: seditor.map,
				toolbar: seditor.toolbar,
				layers: seditor.layers,
				editedLayer: seditor.editLayer
			});
			seditor.toolbar.appendControl(navControl);
			navControl.setActive(true);
			
			seditor.toolbar.appendControl(new seditor.SaveFeaturesControl({
				features: seditor.features,
				serviceURL: seditor.serviceURL,
				workspace: seditor.workspace
			}));
			
			seditor.map.addControl(seditor.toolbar);
			
			seditor.loadAllFeatures();
		};
		
		seditor.isFeatureOwner = function(feature) {
			if( seditor.accessLevel > 2 || ( feature.get('author') == seditor.userName && seditor.userName ) ) return true;
			return false;
		}
		
		seditor.showLoadingDialog = function() {
			seditor.loadingDialog = bootbox.dialog({
				message: '<p style="text-align:center;">Chargement...<br/><img src="images/ajax-loader.gif" /></p>',
				title: 'sEditor',
				onEscape: false,
				backdrop: false,
				closeButton: false
			});
		};
		
		seditor.showFeatureForm = function(event) {
			var html = "";
			html += "<p>Note : les changements ne seront répercutés dans la BDD q'une fois après avoir cliqué sur l'icone de sauvegarde.</p>";
			
			html += "<div class='form-horizontal'><fieldset>";
			seditor.attributes.forEach(function(item,index) {
				html += "<div class='form-group'>";
				html += "<label class='col-md-4 control-label' for='"+item.name+"-input'>"+item.label+"</label>";
				html += "<div class='col-md-4'>";
				switch(item.type) {
					case 'text':
						html += "<input id='"+item.name+"-input' name='"+item.name+"' type='text' class='form-control input-md' />";
						break;
			
					case 'textarea':
						html += "<textarea class='form-control' id='"+item.name+"-input' name='"+item.name+"'></textarea>";
						break;
			
					case 'checkbox':
						item.datasource.forEach(function(subitem, subindex) {
							html += "<div class='checkbox'>";
							html += "<label for='"+item.name+"-input-"+subindex+"'>";
							html += "<input type='checkbox' name='"+item.name+"' id='"+item.name+"-input-"+subindex+"' value='"+subitem.value+"' /> ";
							html += subitem.label;
							html += "</label></div>";
						});
						break;
				
					case 'radio':
						item.datasource.forEach(function(subitem, subindex) {
							html += "<div class='radio'>";
							html += "<label for='"+item.name+"-input-"+subindex+"'>";
							html += "<input type='radio' name='"+item.name+"' id='"+item.name+"-input-"+subindex+"' value='"+subitem.value+"' /> ";
							html += subitem.label;
							html += "</label></div>";
						});
						break;
			
					case 'date':
						html += "<input id='"+item.name+"-input' name='"+item.name+"' type='text' class='form-control input-md'></input>";
						html += "<span class='add-on'><i data-date-icon='icon-calendar'></i></span>";
						break;
				}
				html += "</div></div>";
			});
			html += "</fieldset></div>";
	
			var object = $('<div/>').html(html).contents();
			seditor.attributes.forEach(function(item,index) {
				if( item.type == 'date' ) object.find('#'+item.name+'-input').datetimepicker({format: "DD/MM/YYYY"});
			});
			
			var feature = null;
			
			if( event.type == 'select' && event.selected.length > 0 ) feature = event.selected[0];
			else if( event.type == 'drawend' ) feature = event.feature;
			
			/********** Ouverture POPUP **************/
			var dialog = bootbox.confirm({
		    	title: "Attributs de la geometrie",
		    	message: object,
		    	callback: function(result) {
					if(result) {
						var attributes = {}, error = false;
			   			seditor.attributes.forEach(function(item) {
			   				if( item.type == "checkbox" || item.type == "radio" ) {
			   					var value = "";
			   					$('input[name="'+item.name+'"]:checked').each(function(){ value += this.value+',' });
			   					if(value.length > 0)
			   						attributes[item.name] = value.substring(0,value.length-1);
			   					else attributes[item.name] = "";
			   				} else {
			   					attributes[item.name] = $('#'+item.name+'-input').val();
			   				}
			   				
							if( !attributes[item.name] && item.required == "true" ) { 
								alert('Veuillez renseigner le champ "'+item.name+'".');
								error=true;
							}
						});
						if(error) return false;
						else {
							feature.setProperties(attributes);
							if( event.type == 'drawend' ) feature.set('state','created');
							if( event.type == 'select' ) feature.set('state','modified');
						}
					} else if( event.type == 'drawend' ) {
						seditor.features.remove(feature);
					}
					
					seditor.attributes.forEach(function(item) {
						$('#input-'+item.name).val('');
					});
				}
		    });
		    
		    if( event.type == 'select' && event.selected.length > 0 ) {
		    	seditor.attributes.forEach(function(item) {
		    		if( item.type == "checkbox" || item.type == "radio" ) {
		    			var values = feature.get(item.name);
		    			if( values ) {
		    				values= values.split(",");
			    			$('input[name="'+item.name+'"]').each(function(){
			    				if( $.inArray(this.value,values) > -1 ) $(this).prop('checked', true); 
			    			});
		    			}
		    		}else {
		    			$('#'+item.name+'-input').val(feature.get(item.name));
		    		}
    			});
		    }
		};
		
		seditor.loadAllFeatures = function() {
			$.getJSON(seditor.serviceURL+'/api/list', { workspace: seditor.workspace }, function(reponse) {
				if( reponse.statut == "success" ) {
					var formatter = new ol.format.GeoJSON();
					if( reponse.features.length > 0 ) {
						var lsFeatures = formatter.readFeatures(reponse);
						lsFeatures.forEach(function(item,index) {lsFeatures[index].set('state','nochange');});
						seditor.features.extend(lsFeatures);
					}
					bootbox.hideAll();
				} else if( reponse.statut == "restricted" ) {
					bootbox.hideAll();
					bootbox.alert("Vous n'avez pas les permissions necessaires pour consulter cet espace de travail.", function() {
						if( seditor.userName == "" ) window.location += "&login";
					});
				}
			}).fail(function(erreur) {
				console.warn('Problème de chargement des géometries de l\'espace de travail.');
				console.error('Erreur '+erreur.status+' : '+erreur.statusText);
			});
		}
	}
);
