var BOOKS_IN_ROW = 4;
var books_array = [];

function init(){
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}

function load(){
    gapi.client.bookapi.queryBooks().execute(ex);
}

function ex(response){
    books_array = response['items'];
    generate_page();
}

function generate_page() {
    for(var i = 0; i <books_array.length; ++i){
        generate_row(i);
    }
}

function generate_row(index){
    for(var i = 0; i < BOOKS_IN_ROW; ++i){
        generate_book(BOOKS_IN_ROW*index + i);
    }
}

function generate_book(index){
    var div = document.createElement('div');
    div.className = 'col-md-3 one-book';
    div.appendChild(book_cover(index));
    div.appendChild(about_book(index));
    document.getElementById('books-grid').appendChild(div);
}

function book_cover(index){
    var a = document.createElement('a');
    a.href = "book.html?id="+books_array[index]['websafeKey'];
    a.innerHTML = "link";
    //a.innerHTML = "<img class='book-cover' src='" + books_array[index]['cover'] + "' />";

    return a;
}

function about_book(index){
    var div = document.createElement('div');
    div.className = 'mark';
    //div.innerHTML = "<p class='author'>" + books_array[index]['author']+"</p>";
    div.innerHTML += "<p class='story-name'>"+books_array[index]['title']+"</p>";
    div.innerHTML += "<i class='fa fa-thumbs-up fa-2x'></i>" + books_array[index]['likes'];
    div.innerHTML += "<i class='fa fa-thumbs-down fa-2x'></i>"+books_array[index]['dislikes'];

    return div;
}