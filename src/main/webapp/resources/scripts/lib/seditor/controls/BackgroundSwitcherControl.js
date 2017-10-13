define('lib/seditor/controls/BackgroundSwitcherControl',
	[
		'openlayers',
		'seditorGlobalize'
	],
	function(ol, i18n) {
		seditor.BackgroundSwitcherControl = function(opt_options) {
			var options = opt_options || {};
			
			var button = document.createElement('button');
			button.className = 'seditor-button seditor-switch-background';
			button.title = i18n.formatMessage("switchBackground");
	
			var this_ = this;
			
			var handleClick = function() {
				app.switchBackground();
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
        	
        	var element = document.createElement('div');
        	element.className = 'ol-control switch-background-wrapper';
        	element.appendChild(button);
			
			ol.control.Control.call(this, {
				element: element,
				target: options.target
			});
		}
		ol.inherits(seditor.BackgroundSwitcherControl, ol.control.Control);
	}
);
