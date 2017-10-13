define('lib/seditor/controls/NavigationControl',
	[
		'openlayers',
		'seditorGlobalize',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol,i18n) {
		seditor.NavigationControl = function(opt_options) {
			var options = opt_options || {};
			
			this.interactions = [];
			this.popup = new ol.Overlay({
        		element: document.getElementById('popup')
      		});
			options.map.addOverlay(this.popup);
			this.editedLayer = options.editedLayer;
			
			var this_ = this;
			
			options.map.on('singleclick',function(event){
				if( this_.active ) {
					var view = options.map.getView(),
						wmsUrlList = [];
				
					options.layers.forEach(function(item) {
						if( item.getVisible() ) {
							var wmsURL = {};
							wmsURL.name = item.get('name');
							wmsURL.url = item.getSource().getGetFeatureInfoUrl(
				    			event.coordinate, 
				    			view.getResolution(), 
				    			view.getProjection(),
				    			{'INFO_FORMAT': 'application/json'}
				    		);
				    		wmsUrlList.push(wmsURL);
				    	}
					});
					
					var lstAjax = [];
					wmsUrlList.forEach(function(item){
						lstAjax.push(
							$.getJSON(item)
						);
					});
					
					$.when.apply(undefined,lstAjax).done(function() {
						if( lstAjax.length == 1 ) {
							arguments = [arguments];
						}
						
						var editedFeatures = [], featureCount = 0;
						options.map.forEachFeatureAtPixel(event.pixel, function (feature) {
							editedFeatures.push(feature);
							featureCount++;
						});
						
						var tabList = document.createElement('ul');
						tabList.className = 'nav nav-pills';
						
						if( editedFeatures.length > 0 ) {
							var pill = document.createElement('li');
							pill.className = 'active';
							var link = document.createElement('a');
							link.href = '#edited-layer-reponse';
							link.setAttribute('data-toggle','pill');
							var linkText = document.createTextNode("Edition");
							link.appendChild(linkText);
							pill.appendChild(link);
							tabList.appendChild(pill);
						}
						
						var first = true;
						for( var index = 0 ; index < arguments.length ; index++ ) {
							var reponse = arguments[index];
							if( reponse[1] == "success" && reponse[0].features.length > 0 ) {
								var pill = document.createElement('li');
								if( editedFeatures.length < 1 && first ) {
									pill.className = 'active';
									first = false;
								}
								var link = document.createElement('a');
								link.href = '#reponse-'+index;
								link.setAttribute('data-toggle','pill');
								var linkText = document.createTextNode(wmsUrlList[index].name);
								link.appendChild(linkText);
								pill.appendChild(link);
								tabList.appendChild(pill);
							}
						}
						
						var tabContent = document.createElement('div');
						tabContent.className = 'tab-content'
						if( editedFeatures.length > 0 ) {
							var tab = document.createElement('div');
							tab.className = 'tab-pane fade in active';
							tab.id = 'edited-layer-reponse';
							
							editedFeatures.forEach(function(feature) {
								var properties = feature.getProperties();
								var tableDiv = document.createElement('div');
								tableDiv.className = 'table-responsive';
								
								var html = "";
								html += '<table class="table table-striped">';
								html += '<thead><tr>';
								html += '<th>'+i18n.formatMessage("property")+'</th>';
								html += '<th>'+i18n.formatMessage("value")+'</th>';
								html += '</tr></thead>';
								html += '<tbody>';
								for(var propertyName in properties) {
									if( propertyName != 'geometry' ) {
										html += '<tr>';
										html += '<td>'+propertyName+'</td>';
										html += '<td>'+properties[propertyName]+'</td>';
										html += '</tr>';
									}
								}
								html += '</tbody>';
								html += '</table">';
								
								tableDiv.innerHTML = html;
								tab.appendChild(tableDiv);
							});
							tabContent.appendChild(tab);
						}
						first = true;
						for( var index = 0 ; index < arguments.length ; index++ ) {
							var reponse = arguments[index];
							if( reponse[1] == "success" && reponse[0].features.length > 0 ) {
								var tab = document.createElement('div');
								tab.className = 'tab-pane fade';
								if( editedFeatures.length < 1 && first ) {
									tab.className += ' in active';
									first = false;
								}
								tab.id = 'reponse-'+index;
								
								reponse[0].features.forEach(function(feature) {
									featureCount++;
									var properties = feature.properties;
									var tableDiv = document.createElement('div');
									tableDiv.className = 'table-responsive';
									
									var html = "";
									html += '<table class="table table-striped">';
									html += '<thead><tr>';
									html += '<th>'+i18n.formatMessage("property")+'</th>';
									html += '<th>'+i18n.formatMessage("value")+'</th>';
									html += '</tr></thead>';
									html += '<tbody>';
									for(var propertyName in properties) {
										html += '<tr>';
										html += '<td>'+propertyName+'</td>';
										html += '<td>'+properties[propertyName]+'</td>';
										html += '</tr>';
									}
									html += '</tbody>';
									html += '</table">';
									
									tableDiv.innerHTML = html;
									tab.appendChild(tableDiv);
								});
								tabContent.appendChild(tab);
							}
						}
						
						var element = this_.popup.getElement();
						
						var titleDiv = document.createElement('div');
						titleDiv.appendChild(tabList);
						var closeButton = document.createElement('button');
						closeButton.classList = 'close';
						closeButton.type = 'button';
						closeButton.onclick = function(event) {
							$(element).popover('destroy');
						};
						closeButton.innerHTML = '&times;';
						titleDiv.appendChild(closeButton);
						
						$(element).popover('destroy');
						if( featureCount > 0 ) {
							this_.popup.setPosition(event.coordinate);
							$(element).popover({
								'placement': 'top',
								'animation': false,
								'html': true,
								'content': tabContent,
								'title': titleDiv
							});
							$(element).popover('show');
						
							$('.popover-title .nav-pills a').on('click', function (e) {
								e.preventDefault();
								$(this).tab('show');
							});
						}
					});
				}
			});
			
			var title = i18n.formatMessage("navigation");
	
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-navigation',
				title: title
			});
		}
		ol.inherits(seditor.NavigationControl, seditor.ActiveControl);
	}
);
