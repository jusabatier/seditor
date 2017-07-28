function confirmDeleteWorkspace(message, link) {
	bootbox.confirm({
		message: message,
		callback: function(result) {
			if(result) {
				window.location.href = link;
			}
		}
	});
}

function parseUrl(url) {
	var a = document.createElement('a');
	a.href = url;
	return a;
}

function isSameOrigin(url) {
	var currentPage = window.location || window.document.location;
	return (parseUrl(url).hostname === currentPage.hostname );
}