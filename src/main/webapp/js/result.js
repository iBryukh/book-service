var Q = getJsonFromUrl()['q'];
var RESULT_BOOKS_GRID_ID = '#books-result-grid';

function load(){
    gapi.client.bookapi.queryBooks({'type': 2, 'field': Q}).execute(function(response){
        Q = Q.replace('+', ' ');
        if(response.items){
            $('.caption').text('Result for \''+Q+'\'');
            generate_page(RESULT_BOOKS_GRID_ID, response['items']);
        } else {
        	$('.caption').text('No result for \''+Q+'\'');
        }
    });
}