function getJsonFromUrl() {
    var query = location.search.substr(1);
    var result = {};
    query.split("&").forEach(function(part) {
        var item = part.split("=");
        result[item[0]] = decodeURIComponent(item[1]);
    });
    return result;
}

function init(){
	var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}