var Q = getJsonFromUrl()['q'];
var RESULT_BOOKS_GRID_ID = 'books-result-grid';

function init(){
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}

function load(){
    gapi.client.bookapi.queryBooks({'type': 2, 'field': Q}).execute(function(response){
        generate_page(RESULT_BOOKS_GRID_ID, response['items']);
    });
}