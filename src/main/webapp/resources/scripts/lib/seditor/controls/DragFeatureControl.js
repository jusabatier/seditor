define('lib/seditor/controls/DragFeatureControl',
	[
		'openlayers',
		'seditorGlobalize',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol,i18n) {
		seditor.DragFeatureControl = function(opt_options) {
			var options = opt_options || {};
			
			this.interactions = [];
			
			var select = new ol.interaction.Select({
				wrapX: false,
				layers: options.layers,
				features: options.selectedFeatures
			});
			select.setActive(false);
			select.on('change:active', function(event) {
				if(event.oldValue) this.getFeatures().clear();
			});
			select.on('select',function(event) {
				if( event.selected.length > 0 ) {
					if( options.checkFeatureEditAllowed !== undefined && !options.checkFeatureEditAllowed(event.selected[0]) ) {
						this.getFeatures().remove(event.selected[0]);
						bootbox.alert(i18n.formatMessage("featureInsufficientPrivileges"));
					}
				}
			});
			options.map.addInteraction(select);
			this.interactions.push(select);
			
			var translate = new ol.interaction.Translate({
				features: select.getFeatures(),
				layers: options.layers
			});
			options.map.addInteraction(translate);
			translate.setActive(false);
			translate.on('translateend', function(event) {
				event.features.forEach(function(item) {
					if( item.get('state') != 'created' ) item.set('state','modified');
				});
			});
			this.interactions.push(translate);
			
			var title = i18n.formatMessage('dragFeature');
			
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-drag-feature',
				title: title
			});
		}
		ol.inherits(seditor.DragFeatureControl, seditor.ActiveControl);
	}
);
