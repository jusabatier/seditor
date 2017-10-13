getUrlParameter = function(sParam) {
    var sPageURL = decodeURIComponent(window.location.search.substring(1)),
        sURLVariables = sPageURL.split('&'),
        sParameterName,
        i;

    for (i = 0; i < sURLVariables.length; i++) {
        sParameterName = sURLVariables[i].split('=');

        if (sParameterName[0] === sParam) {
            return sParameterName[1] === undefined ? true : sParameterName[1];
        }
    }
};

checkDomain = function(url) {
	if( url.indexOf('//') === 0 ) { url = location.protocol + url; }
	return url.toLowerCase().replace(/([a-z])?:\/\//,'$1').split('/')[0];
}
		
isExternal = function(url) {
	return ( ( url.indexOf(':') > -1 || url.indexOf('//') > -1 ) && checkDomain(location.href) !== checkDomain(url) );
}