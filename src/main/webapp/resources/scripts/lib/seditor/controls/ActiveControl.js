define('lib/seditor/controls/ActiveControl',
	[
		'openlayers'
	],
	function(ol) {
		seditor.ActiveControl = function(opt_options) {
			var options = opt_options || {};
			
			this.interactions = options.interactions || [];
			this.toolbar = options.toolbar;
			this.active = false;
			
			this.setActive = function(active) {
				this.interactions.forEach(function(item) { item.setActive(active); });
				if(active) {
					var className = ' ' + this.element.className + ' ';
					this.element.className = ~className.indexOf(' active ') ?
		     			className :
		         		this.element.className + ' active';
				} else {
					var className = ' ' + this.element.className + ' ';
					this.element.className = ~className.indexOf(' active ') ?
			 			className.replace(' active ', ' '):
			 			className;
				}
				this.active = active;
			};
			
			var button = document.createElement('button');
			button.className = 'ol-control seditor-button '+options.className;
	
			var this_ = this;
			var handleClick = function() {
				if( this_.active ) return;
				this_.toolbar.disableActiveControls();
				this_.setActive(true);
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
			
			ol.control.Control.call(this, {
				element: button,
				target: options.target
			});
		}
		ol.inherits(seditor.ActiveControl, ol.control.Control);
	}
);
