define('lib/seditor/controls/DeleteFeatureControl',
	[
		'openlayers',
		'bootbox'
	],
	function(ol,bootbox) {
		seditor.DeleteFeatureControl = function(opt_options) {
			var options = opt_options || {};
			
			this.selected = options.selectedFeatures;
			this.features = options.features;
			this.active = false;
			
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
			button.className = 'ol-control seditor-button seditor-delete-feature disabled';
	
			var this_ = this;
			
			this.selected.on('change:length',function(event) {
				this_.setActive(( this.getLength() > 0 ));
			});
			
			var handleClick = function() {
				if( this_.active ) {
					this_.selected.forEach(function(item) {
						if( item.get('state') == 'created' ) this_.features.remove(item);
						else item.set('state','deleted');
					});
					this_.selected.clear();
				} else {
					bootbox.alert("Veuillez sélectionner au moins une géometrie à supprimer.");
				}
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
			
			ol.control.Control.call(this, {
				element: button,
				target: options.target
			});
		}
		ol.inherits(seditor.DeleteFeatureControl, ol.control.Control);
	}
);
