var BOOKS_IN_ROW = 4;
var MOST_LIKED_BOOKS_GRID_ID = 'books-liked-grid';
var RANDOM_BOOKDS_GRID_ID = 'random-grid';
var CURRENT_ID;
var booksArray = [];

function init(){
    auth();
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}

function load(){
    gapi.client.bookapi.queryBooks({'limit': 20, 'type': 0}).execute(liked);
    gapi.client.bookapi.queryBooks({'limit': 20, 'type': 1}).execute(random);
}

function liked(response){
    booksArray = response['items'];
    generate_page(MOST_LIKED_BOOKS_GRID_ID);
}

function random(response){
    booksArray = response['items'];
    generate_page(RANDOM_BOOKDS_GRID_ID);
}


function generate_page(id) {
    for(var i = 0; i <booksArray.length; ++i){
        for(var j = 0; j < BOOKS_IN_ROW; ++j){
            generate_book(BOOKS_IN_ROW*i + j, id);
        }
    }
}

function generate_book(index, id){
    var div = document.createElement('div');
    div.className = 'col-md-3 one-book';
    div.appendChild(book_cover(index));
    div.appendChild(about_book(index));
    document.getElementById(id).appendChild(div);
}

function book_cover(index){
    var a = document.createElement('a');
    a.href = "book.html?id="+booksArray[index]['websafeKey'];
    a.innerHTML = "<img class='book-cover' src='" + booksArray[index]['image'] + "' />";

    return a;
}

function about_book(index){
    var div = document.createElement('div');
    div.className = 'mark';
    div.innerHTML = "<p class='author'>" + booksArray[index]['author']+"</p>";
    div.innerHTML += "<p class='story-name'>"+booksArray[index]['title']+"</p>";
    div.innerHTML += "<i class='fa fa-thumbs-up fa-2x'></i>" + booksArray[index]['likes'];
    div.innerHTML += "<i class='fa fa-thumbs-down fa-2x' style='margin-left: 5px;'></i>"+booksArray[index]['dislikes'];

    return div;
}