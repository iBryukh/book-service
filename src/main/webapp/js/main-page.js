var BOOKS_IN_ROW = 4;
var MOST_LIKED_BOOKS_GRID_ID = 'books-liked-grid';
var RANDOM_BOOKDS_GRID_ID = 'random-grid';
var CURRENT_ID;

function init(){
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}

function load(){
    gapi.client.bookapi.queryBooks({'limit': 20, 'type': 0}).execute(function(response){
        generate_page(MOST_LIKED_BOOKS_GRID_ID, response['items']);
    });
    gapi.client.bookapi.queryBooks({'limit': 20, 'type': 1}).execute(function(response){
        generate_page(RANDOM_BOOKDS_GRID_ID, response['items']);
    });
}
function generate_page(id, booksArray) {
    for(var i = 0; i <booksArray.length; ++i){
    	generate_book(booksArray[i], id);
    }
}

function generate_book(book, id){
    var div = document.createElement('div');
    div.className = 'col-md-3 one-book';
    div.appendChild(book_cover(book));
    div.appendChild(about_book(book));
    document.getElementById(id).appendChild(div);
}

function book_cover(book){
    var a = document.createElement('a');
    a.href = "book.html?id="+book['websafeKey'];
    a.innerHTML = "<img class='book-cover' src='" + book['image'] + "' />";

    return a;
}

function about_book(book){
    var div = document.createElement('div');
    div.className = 'mark';
    div.innerHTML = "<p class='author'>" + book['author']+"</p>";
    div.innerHTML += "<a href='book.html?id="+book['websafeKey']+"'><p class='story-name'>"+book['title']+"</p>";
    div.innerHTML += "<i class='fa fa-thumbs-up fa-2x'></i>" + book['likes'];
    div.innerHTML += "<i class='fa fa-thumbs-down fa-2x' style='margin-left: 5px;'></i>"+book['dislikes'];

    return div;
}