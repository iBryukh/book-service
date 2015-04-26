var MOST_LIKED_BOOKS_GRID_ID = '#books-liked-grid';
var RANDOM_BOOKDS_GRID_ID = '#random-grid';
var LIMIT = 12;
var BOOKS_IN_ROW = 4;

function load(){
    gapi.client.bookapi.queryBooks({'limit': LIMIT, 'type': 0}).execute(function(response){
        generate_page(MOST_LIKED_BOOKS_GRID_ID, response.items);
    });
    gapi.client.bookapi.queryBooks({'limit': LIMIT, 'type': 1}).execute(function(response){
        generate_page(RANDOM_BOOKDS_GRID_ID, response.items);
    });
}

function generate_page(id, booksArray) {
    var i = 0;
    while(i < booksArray.length){
        var div = document.createElement('div');
        $(div).addClass('row');
        BOOKS_IN_ROW = (booksArray.length - i < 4) ? booksArray.length-i : 4;
        for(var j = 0; j < BOOKS_IN_ROW; ++j){
            $(div).append(generate_book(booksArray[i++]));
        }
        $(id).append(div);
    }
}

function generate_book(book){
    var div = document.createElement('div');
    $(div).addClass('col-md-3 one-book');
    $(div).append(book_cover(book));
    $(div).append(about_book(book));
    
    return div;
}

function book_cover(book){
    var a = document.createElement('a');
    $(a).attr('href', "book.html?id="+book['websafeKey']);
    $(a).append("<img class='book-cover' src='" + book['image'] + "' />");

    return a;
}

function about_book(book){
    var div = document.createElement('div');
    $(div).addClass('mark');
    $(div).append("<p class='author'>" + book['author']+"</p>");
    $(div).append("<a href='book.html?id="+book['websafeKey']+"'><p class='story-name'>"+book['title']+"</p>");
    $(div).append("<i class='fa fa-thumbs-up fa-2x'></i>" + book['likes']);
    $(div).append("<i class='fa fa-thumbs-down fa-2x' style='margin-left: 5px;'></i>"+book['dislikes']);

    return div;
}