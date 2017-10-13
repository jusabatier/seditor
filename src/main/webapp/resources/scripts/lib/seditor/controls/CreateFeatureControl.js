define('lib/seditor/controls/CreateFeatureControl',
	[
		'openlayers',
		'seditorGlobalize',
		'lib/seditor/controls/ActiveControl'
	],
	function(ol,i18n) {
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
			
			var title = i18n.formatMessage("drawFeature")+" "+options.geometryType;
			
			seditor.ActiveControl.call(this, {
				target: options.target,
				interactions: this.interactions,
				toolbar: options.toolbar,
				className: 'seditor-create-'+options.geometryType.toLowerCase(),
				title: title
			});
		}
		ol.inherits(seditor.CreateFeatureControl, seditor.ActiveControl);
	}
);
