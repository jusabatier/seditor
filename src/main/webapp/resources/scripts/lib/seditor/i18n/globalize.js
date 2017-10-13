define('seditorGlobalize',
	[
		'globalizeMessage'
	],
	function() {
		const Globalize = require("globalize");
		const likelySubtags = require("json!lib/cldr/data/supplemental/likelySubtags.json");
		const plurals = require("json!lib/cldr/data/supplemental/plurals.json");
		const ordinals = require("json!lib/cldr/data/supplemental/ordinals.json");
		
		Globalize.load(likelySubtags);
		Globalize.load(plurals);
		Globalize.load(ordinals);
		
		Globalize.loadMessages({
			en: {
				loading: "Loading",
				savingNote: "Note: Changes will only be reflected in the DB once after clicking on the save icon.",
				geometryAttributesTitle: "Geometry's attributes",
				fieldRequired: 'Please provide the "{field}" field.',
				workspaceInsufficientPrivileges: "You don't have the necessary permissions to view this workspace.",
				featureInsufficientPrivileges: "You don't have the right to modify this geometry.",
				drawFeature: "Draw",
				deleteFeature: "Delete selected features",
				dragFeature: "Drag features",
				layerList: "Available layers",
				modifyFeature: "Modify features",
				navigation: "Navigate",
				property: "Property",
				value: "Value",
				saveFeatures: "Save changes to DB",
				switchBackground: "Switch brackground"
			},
			fr: {
				loading: "Chargement",
				savingNote: "Note : les changements ne seront répercutés dans la BDD qu'une fois après avoir cliqué sur l'icone de sauvegarde.",
				geometryAttributesTitle: "Attributs de la geometrie",
				fieldRequired: 'Veuillez renseigner le champ "{field}".',
				workspaceInsufficientPrivileges: "Vous n'avez pas les permissions necessaires pour consulter cet espace de travail.",
				featureInsufficientPrivileges: "Vous n'avez pas les droits pour modifier cette geometrie.",
				drawFeature: "Tracer de",
				deleteFeature: "Supprimer les entités sélectionnées",
				dragFeature: "Déplacer des geometries",
				layerList: "Couches disponibles",
				modifyFeature: "Modifier des géometries",
				navigation: "Naviguer sur la carte",
				property: "Propriété",
				value: "Valeur",
				saveFeatures: "Sauvegarder les modifications sur la BDD",
				switchBackground: "Changer le fond de plan"
			}
		});
		
		var lang = navigator.language || navigator.userLanguage;
		Globalize.locale(lang);
		
		return Globalize;
	}
);
