define('lib/seditor/controls/SnappingControl',
	[
		'openlayers',
		'bootbox'
	],
	function(ol,bootbox) {
		seditor.SnappingControl = function(opt_options) {
			var options = opt_options || {};
			
			this.active = false;
			this.snapSources = [];
			this.editedFeatures = options.editedFeatures;
			
			var this_ = this;
			
			var snapEditedFeatures = {};
			snapEditedFeatures.interaction = new ol.interaction.Snap({
				features: this.editedFeatures
			});
			snapEditedFeatures.name = 'Couche d\'edition';
			snapEditedFeatures.interaction.setActive(true);
			options.map.addInteraction(snapEditedFeatures.interaction);
			this.snapSources.push(snapEditedFeatures);
			
			options.snapSources.forEach(function(item){
				options.map.on('moveend',function(event){
					if( this_.active ) item.clear();
				});
				var vectorLayer = new ol.layer.Vector({
					source: item,
					visible: false,
					opacity: 0
				});
				app.map.addLayer(vectorLayer);
				
				var snap = {};
				snap.interaction = new ol.interaction.Snap({
					source: item
				});
				snap.name = item.get('name');
				snap.layer = vectorLayer;
				snap.interaction.setActive(false);
				options.map.addInteraction(snap.interaction);
				this_.snapSources.push(snap);
			});
			
			this.optionlist = document.createElement('div');
			this.optionlist.className = 'seditor-snapping-div';
			var activeItemBlock = document.createElement('p');
			var activeCheckbox = document.createElement('input');
			activeCheckbox.type = "checkbox";
			activeCheckbox.onchange = function() {
				this_.setActive(!this_.active);
			};
			activeItemBlock.appendChild(activeCheckbox);
			activeItemBlock.appendChild(document.createTextNode('Snapping actif'));
			this.optionlist.appendChild(activeItemBlock);
			
			this.optionlist.appendChild(document.createElement('p'));
			
			this.snapSources.forEach(function(item){
				var layerblock = document.createElement('p');
				item.checkbox = document.createElement('input');
				item.checkbox.type = "checkbox";
				item.checkbox.onchange = function() {
					if( this_.active ) {
						item.interaction.setActive(item.checkbox.checked);
						if( item.layer ) item.layer.setVisible(item.checkbox.checked);
					}
				};
				layerblock.appendChild(item.checkbox);
				layerblock.appendChild(document.createTextNode(item.name));
				this_.optionlist.appendChild(layerblock);
			});
			
			this.setActive = function(active) {
				if( active ) {
					this_.snapSources.forEach(function(item){
						item.interaction.setActive(item.checkbox.checked);
						if( item.layer ) item.layer.setVisible(item.checkbox.checked);
					});
				} else {
					this_.snapSources.forEach(function(item){
						item.interaction.setActive(false);
						if( item.layer ) item.layer.setVisible(false);
					});
				}
				
				if(active) {
					var className = ' ' + this.element.className + ' ';
					this.element.className = ~className.indexOf(' enabled ') ?
		     			className :
		         		this.element.className + ' enabled';
				} else {
					var className = ' ' + this.element.className + ' ';
			 		this.element.className = ~className.indexOf(' enabled ') ?
			 			className.replace(' enabled ', ' '):
			 			className;
				}
				this.active = active;
			}
			
			var button = document.createElement('button');
			button.className = 'seditor-button seditor-snapping';
			
			var handleClick = function() {
				this_.optionlist.classList.toggle('visible');
			};
			
			button.addEventListener('click', handleClick, false);
        	button.addEventListener('touchstart', handleClick, false);
        	
        	var element = document.createElement('div');
        	element.className = 'ol-unselectable seditor-snapping';
        	element.appendChild(button);
        	var wrapper = document.createElement('div');
        	wrapper.className = 'ol-control seditor-snapping-wrapper';
        	wrapper.appendChild(element);
        	wrapper.appendChild(this.optionlist);
			
			ol.control.Control.call(this, {
				element: wrapper,
				target: options.target
			});
		}
		ol.inherits(seditor.SnappingControl, ol.control.Control);
	}
);
