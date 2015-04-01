/**
 * Created by Oleh Kurpiak on 3/29/2015.
 */
/*preview*/
var loadFile = function(event) {
    var output = document.getElementById('cover');
    output.src = URL.createObjectURL(event.target.files[0]);
};

$(document).ready(function() {
    $("a.plus").click(function(){
        $("#quotes").append("<textarea class='multi-line' type='text'></textarea><br />");
    });
});



function init() {
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', null, rootpath);
}

function onBookLoaded (response) {
    //if(response.getTitle)
    for(var key in response)
        alert(key + " " + 	response[key]);
}
function loadCallback () {
    //  ввімкни кнопки всякі

    //по кнопці зробити
    //document.getElementById('file-input').addEventListener('change',readSingleFile, false);
    //alert(document.getElementById('file-input').files[0]);
}

function create(){
    var request = {};
    var author = document.getElementById('author').value;
    if(!author){
        alert("author null");
        return;
    }
    var title = document.getElementById('title').value;
    if(!title){
        alert("title null");
        return;
    }
    var file = document.getElementById('file-input').files[0];
    if (!file) {
        alert("file null");
        return;
    }
    request['author'] = author;
    request['title'] = title;
    var quotes = document.getElementById('quotes').value;
    if(quotes)
        request['quotes'] = quotes;

    var annotation = document.getElementById('annotation').value;
    if(annotation)
        request['annotation'] = annotation;

    var genre = document.getElementById('genre').value;
    if(genre)
        request['genre'] = genre;

    var year = document.getElementById('year').value;
    if(year)
        request['year'] = year;

    for(var key in request)
        alert(key + " " + 	request[key]);
    var reader = new FileReader();
    reader.onload = function(e) {
        var image = e.target.result;
        request['image'] = image;
        gapi.client.bookapi.addBook(request).execute(onBookLoaded);
    };
    reader.readAsDataURL(file);
}

