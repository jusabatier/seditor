define('lib/seditor/controls/LayersListControl',
	[
		'openlayers',
		'bootbox'
	],
	function(ol,bootbox) {
		seditor.LayersListControl = function(opt_options) {
			var options = opt_options || {};
			
			this.layers = options.layers;
			
			var this_ = this;
			
			var button = document.createElement('button');
			button.className = 'seditor-button';
			
			this.layerlist = document.createElement('div');
			this.layerlist.className = 'seditor-layers-div';
			this.layers.forEach(function(item) {
				var layerItem = document.createElement('p');
				item.layerVisibilityCheckbox = document.createElement('input');
				item.layerVisibilityCheckbox.type = "checkbox";
				item.layerVisibilityCheckbox.checked = item.getVisible();
				item.layerVisibilityCheckbox.onchange = function() {
					item.setVisible(!item.getVisible());
				};
				layerItem.appendChild(item.layerVisibilityCheckbox);
				layerItem.appendChild(document.createTextNode(item.get('name')));
				this_.layerlist.appendChild(layerItem);
			});
			
			var handleClick = function() {
				this_.layerlist.classList.toggle('visible');
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
        	
        	var element = document.createElement('div');
        	element.className = 'ol-unselectable ol-control seditor-layers-list';
        	element.appendChild(button);
        	var wrapper = document.createElement('div');
        	wrapper.className = 'seditor-layers-list-wrapper';
        	wrapper.appendChild(element);
        	wrapper.appendChild(this.layerlist);
			
			ol.control.Control.call(this, {
				element: wrapper,
				target: options.target
			});
		}
		ol.inherits(seditor.LayersListControl, ol.control.Control);
	}
);
