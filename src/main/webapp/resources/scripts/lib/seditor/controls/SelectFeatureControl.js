define('lib/seditor/controls/SelectFeatureControl',
	[
		'openlayers',
		'bootbox',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol,bootbox) {
		seditor.SelectFeatureControl = function(opt_options) {
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
						bootbox.alert("Vous n'avez pas les droits pour modifier cette geometrie.");
					}
				}
			});
			options.map.addInteraction(select);
			this.interactions.push(select);
	
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-select-feature'
			});
		}
		ol.inherits(seditor.SelectFeatureControl, seditor.ActiveControl);
	}
);
