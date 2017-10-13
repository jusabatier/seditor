define('lib/seditor/controls/SaveFeaturesControl',
	[
		'openlayers',
		'bootbox',
		'seditorGlobalize'
	],
	function(ol,bootbox,i18n) {
		seditor.SaveFeaturesControl = function(opt_options) {
			var options = opt_options || {};
			
			this.features = options.features;
			this.active = false;
			this.featureToSave = new ol.Collection();
			
			this.setActive = function(active) {
				if(active) {
					var className = ' ' + this.element.className + ' ';
					this.element.className = ~className.indexOf(' disabled ') ?
			 			className.replace(' disabled ', ' '):
			 			className;
				} else {
					var className = ' ' + this.element.className + ' ';
			 		this.element.className = ~className.indexOf(' disabled ') ?
		     			className :
		         		this.element.className + ' disabled';
				}
				this.active = active;
			}
			
			var button = document.createElement('button');
			button.className = 'ol-control seditor-button seditor-save-features disabled';
			button.title = i18n.formatMessage("saveFeatures");
	
			var this_ = this;
			
			this.featureToSave.on('change:length', function(event) {
				this_.setActive(( this.getLength() > 0 ));
			});
			
			this.features.on('add',function(event) {
				event.element.on('propertychange',function(event) {
					if( event.key == "state" && 
						event.target.get('state') != 'nochange' && 
						( event.oldValue == 'nochange' || event.oldValue == undefined )
					)
						this_.featureToSave.push(event.target);
				});
			});
			
			this.features.on('remove', function(event) {
				this_.featureToSave.remove(event.element);
			});
			
			var handleClick = function() {
				if( this_.active ) {
					var GeoJSONParser = new ol.format.GeoJSON(), 
						GeoJSONFeatures = GeoJSONParser.writeFeatures(this_.featureToSave.getArray(),{});
						
						$.post(options.serviceURL+'/api/persist', { workspace: options.workspace, data: GeoJSONFeatures }, function(reponse) {
							if( reponse.statut == "success" ) {
								var messageDiv = document.createElement('div');
								messageDiv.style.textAlign = 'center';
								if( reponse.reponses.length > 0 ) {
									var firstLine = document.createElement('h4');
									firstLine.appendChild(document.createTextNode(
										reponse.reponses.length+' modification(s) envoyée(s) au serveur avec succès  : '
									));
									messageDiv.appendChild(firstLine);
									
									var reponsesList = document.createElement('ul');
									reponsesList.style.listStyle = 'none';
									for( var i = 0 ; i < reponse.reponses.length ; i++ ) {
										var reponseElem = document.createElement('li');
										var reponseFirstLine = document.createElement('p');
										reponseFirstLine.style.margin = 0;
										reponseFirstLine.innerHTML = '<b>Géometrie n°'+i+' : </b>';
										var color = 'grey';
										switch(reponse.reponses[i].result.statut) {
											case 'error': 
												color = 'red';
												break;
											case 'restricted':
												color = 'orange';
												break;
											case 'success':
												color = 'green';
												break;
										}
										reponseFirstLine.innerHTML += reponse.reponses[i].action+' / <span style="color: '+color+';">'+reponse.reponses[i].result.statut+'</span>';
										reponseElem.appendChild(reponseFirstLine);
										
										
										var detailGeom = document.createElement('div');
										detailGeom.className = 'servReponseDetailGeom';
										detailGeom.style.display = 'none';
										var tableHtml = '<table  class="table table-striped .table-condensed"><thead><tr><th style="text-align: center;">Proprieté</th><th style="text-align: center;">Valeur</th></tr></thead><tbody>';
										for(var propertyName in reponse.reponses[i].element.properties) {
											if( propertyName != 'state' ) {
												tableHtml += '<tr>';
												tableHtml += '<td>'+propertyName+'</td>';
												tableHtml += '<td>'+reponse.reponses[i].element.properties[propertyName]+'</td>';
												tableHtml += '</tr>';
											}
										}
										tableHtml += '</tbody></table>';
										detailGeom.innerHTML = tableHtml;
										
										var detailGeomLink = document.createElement('p');
										detailGeomLink.appendChild(document.createTextNode('Détail de la géometrie [+]'));
										detailGeomLink.onclick = function() {
											$(this).parent().children('.servReponseDetailGeom').slideToggle();
										};
										detailGeomLink.style.cursor = 'pointer';
										reponseElem.appendChild(detailGeomLink);
										reponseElem.appendChild(detailGeom);
										
										if( reponse.reponses[i].result.statut == 'error' ) {
											var detailError = document.createElement('div');
											detailError.className = 'servReponseDetailError';
											detailError.style.display = 'none';
											detailError.appendChild(document.createTextNode(
												reponse.reponses[i].result.message
											));
											
											var detailErrorLink = document.createElement('p');
											detailErrorLink.appendChild(document.createTextNode('Détail de l\'erreur [+]'));
											detailErrorLink.onclick = function() {
												$(this).parent().children('.servReponseDetailError').slideToggle();
											};
											detailErrorLink.style.cursor = 'pointer';
											reponseElem.appendChild(detailErrorLink);
											reponseElem.appendChild(detailError);
										}
										reponsesList.appendChild(reponseElem);
									}
									messageDiv.appendChild(reponsesList);
								} else {
									messageDiv.appendChild(document.createTextNode('Aucune géometrie modifiée'));
								}
								
								bootbox.alert({
									size: "large",
									title: 'Réponse du serveur',
									message: messageDiv,
									callback: function() {
										this_.features.clear();
										seditor.loadAllFeatures();
									}
								});
							}
						}, 'json');
				} else {
					bootbox.alert("Aucune géometrie modifiée à sauvegarder.");
				}
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
			
			ol.control.Control.call(this, {
				element: button,
				target: options.target
			});
		}
		ol.inherits(seditor.SaveFeaturesControl, ol.control.Control);
	}
);
