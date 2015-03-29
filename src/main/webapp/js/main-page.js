/**
 * Created by Oleh Kurpiak on 3/26/2015.
 */
var BOOKS_IN_ROW = 4;
var books_array = [];
books_array.push({
    author: 'Author Name 1',
    title: 'The Story Name',
    cover: 'resources/cover2.jpg',
    likes: 120,
    dislikes: 10,
    book_id: 'sbrtd5hs6qsbr64'
});
books_array.push({
    author: 'Author Name 2',
    title: 'The Story Name',
    cover: 'resources/cover2.jpg',
    likes: 120,
    dislikes: 10,
    book_id: 'sbrtd5hs6qsbr64'
});
/*
function init(){
	var rootpath = "https://" + window.location.host + "/_ah/api";
	gapi.client.load('booksapi', 'v1', start, rootpath);
}

function start(){
	alert('start');
	var request = gapi.client.booksapi.message();
	alert('a');
	request.execute(sayHelloCallback);
}

function sayHelloCallback (response){
	alert('call back');
	alert(response);	
}
*/

function on_load(){
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
    a.href = "book.html?id="+books_array[index]['book_id'];
    a.innerHTML = "<img class='book-cover' src='" + books_array[index]['cover'] + "' />";

    return a;
}

function about_book(index){
    var div = document.createElement('div');
    div.className = 'mark';
    div.innerHTML = "<p class='author'>" + books_array[index]['author']+"</p>";
    div.innerHTML += "<p class='story-name'>"+books_array[index]['title']+"</p>";
    div.innerHTML += "<i class='fa fa-thumbs-up fa-2x'></i>" + books_array[index]['likes'];
    div.innerHTML += "<i class='fa fa-thumbs-down fa-2x'></i>"+books_array[index]['dislikes'];

    return div;
}