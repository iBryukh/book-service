var IMAGE = null;
function init() {
    var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', null, rootpath);
}

$(document).ready(function() {
    $('body').on('click', 'a.fa-plus', function(){
        $('#quotes').append("<a class='fa fa-plus fa-2x add-remove' title='Add More Quotes'></a><textarea class='multi-line' type='text' placeholder='Quote'></textarea><br />");
        $(this).removeClass('fa-plus');
        $(this).addClass('fa-minus');
    });
    $('body').on('click', 'a.fa-minus', function (event) {
        $(this).next().remove();
        $(this).next().remove();
        $(this).remove();
    });
    $('#load-file').on('change', function(event){
        preview(this);
    });
    $('#submit').click(function(){
        var request = {};
        var author = $('#author').val();
        var title = $('#title').val();
        var year = validYear($('#year').val());
        var genre = makeGenres($('#genres').val());
        var annotation = $('#annotation').val();
        var quotes = makeQuotes($('#quotes textarea'));
        if(!author || !title || !year || !annotation || !IMAGE){
            alert("field null");
            return;
        }

        request['author'] = author;
        request['title'] = title;
        request['quotes'] = quotes;
        request['annotation'] = annotation;
        request['genre'] = genre;
        request['year'] = year;
        //request['tags'] = [];
        request['image'] = IMAGE;
        gapi.client.bookapi.addBook(request).execute(onBookLoaded);
    });
});

function preview(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#cover').attr('src', e.target.result);
            IMAGE = e.target.result;
        }
        reader.readAsDataURL(input.files[0]);
    }
}

function onBookLoaded (response) {
    if(!response.code)
        window.location.href = 'book.html?id'+response['websafeKey'];
    else
        alert('problems');
}

function validYear(year){
    if(year.length != 4){
        alert('incorrect year');
        return null;
    }
    if(year < 0 || year > 2015){
        alert('incorrect year');
        return null;
    }
    return year;
}

function makeGenres(genre){
    if(!genre)
        return null;
    return genre.split(',');
}

function makeQuotes(arrayOfQuotes){
    var quotes = [];
    for(var i = 0; i < arrayOfQuotes.length; ++i){
        if(arrayOfQuotes[i].textLength != 0)
            quotes.push(arrayOfQuotes[i].value);
    }
    return quotes;
}