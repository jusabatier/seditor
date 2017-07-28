define('lib/seditor/controls/ModifyFeatureControl',
	[
		'openlayers',
		'bootbox',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol,bootbox) {
		seditor.ModifyFeatureControl = function(opt_options) {
			var options = opt_options || {};
			
			this.interactions = [];
			
			var select = new ol.interaction.Select({
				wrapX: false,
				layers: options.layers,
				features: options.selectedFeatures
			});
			select.setActive(false);
			select.on('select', function(event) {
				if( event.selected.length > 0 ) {
					if( options.checkFeatureEditAllowed === undefined || options.checkFeatureEditAllowed(event.selected[0]) ) {
						if( ol.events.condition.noModifierKeys(event.mapBrowserEvent) ) {
							options.modifyCallback(event);
						}
					} else {
						this.getFeatures().remove(event.selected[0]);
						bootbox.alert("Vous n'avez pas les droits pour modifier cette geometrie.");
					}
				}
			});
			select.on('change:active', function(event) {
				if(event.oldValue) this.getFeatures().clear();
			});
			options.map.addInteraction(select);
			this.interactions.push(select);
			
			var modify = new ol.interaction.Modify({
				features: select.getFeatures(),
				deleteCondition: function(event) {
					return ol.events.condition.altKeyOnly(event) &&
					ol.events.condition.singleClick(event);
				}
			});
			modify.setActive(false);
			modify.on('modifyend', function(event) {
				event.features.forEach(function(item) {
					if( item.get('state') != 'created' ) item.set('state','modified');
				});
			});
			options.map.addInteraction(modify);
			this.interactions.push(modify);
	
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-modify-feature'
			});
		}
		ol.inherits(seditor.ModifyFeatureControl, seditor.ActiveControl);
	}
);
