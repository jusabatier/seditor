define('lib/seditor/controls/CustomToolbarControl',
	[
		'openlayers'
	],
	function(ol) {
		seditor.CustomToolbarControl = function(opt_options) {
			var options = opt_options || {};
	
			var this_ = this;
			
			this.activeControls = [];
			
			this.appendControl = function(control) {
				this_.element.appendChild(control.element);
				if( control instanceof seditor.ActiveControl ) this.activeControls.push(control);
			}
			
			this.disableActiveControls = function() {
				this.activeControls.forEach(function(item,index) {
					item.setActive(false);
				});
			}
			
			var element = document.createElement('div');
			element.className = 'ol-unselectable ol-control';
			element.id = 'seditor-toolbar'
	
			if( options.children && options.children.length > 0 ) {
				for(var i = 0 ; i<options.children.length ; i++) {
					element.appendChild(options.children[i].element);
				}
			}
	
			ol.control.Control.call(this, {
				element: element,
				target: options.target
			});
		};
		ol.inherits(seditor.CustomToolbarControl, ol.control.Control);
	}
);
