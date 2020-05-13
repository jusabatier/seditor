window.config = {};
var config = window.config;

config.serverURL = "/seditor";

config.backgrounds = [
	{
		url: 'https://opendata.agglo-lepuyenvelay.fr/geowebcache/service/wmts',
		layer: 'pci:cadastre',
		title: 'Cadastre',
		attribution: 'DGFiP'
	},
	{
		url: 'https://opendata.agglo-lepuyenvelay.fr/geowebcache/service/wmts',
		layer: 'rasters:ortho_2017',
		title: 'RTGE',
		attribution: 'CRAIG, CAPeV, SDEHL, SGEV, SAEV, Enedis, FEDER'
	},
	{
		url: 'https://opendata.agglo-lepuyenvelay.fr/geowebcache/service/wmts',
		layer: 'ign:orthohr',
		title: 'Photographies aériennes',
		attribution: 'IGN'
	},
	{
		url: 'https://opendata.agglo-lepuyenvelay.fr/geowebcache/service/wmts',
		layer: 'ign:cartes',
		title: 'Cartes IGN',
		attribution: 'IGN'
	},
	{
		url: 'https://opendata.agglo-lepuyenvelay.fr/geowebcache/service/wmts',
		layer: 'ign:plan',
		title: 'Plan IGN',
		attribution: 'IGN'
	}
];
