define('lib/seditor/controls/CreateFeatureControl',
	[
		'openlayers',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol) {
		seditor.CreateFeatureControl = function(opt_options) {
			var options = opt_options || {};
			
			this.interactions = [];
			
			var draw = new ol.interaction.Draw({
				features: options.features,
				type: options.geometryType
			});
			draw.setActive(false);
			draw.on('drawend', options.createCallback);
			options.map.addInteraction(draw);
			this.interactions.push(draw);
	
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-create-'+options.geometryType.toLowerCase()
			});
		}
		ol.inherits(seditor.CreateFeatureControl, seditor.ActiveControl);
	}
);
