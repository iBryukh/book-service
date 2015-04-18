var ID = getJsonFromUrl()['id'];
var BOOK;

function init(){
	auth();
	var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}	

function load(){
	gapi.client.bookapi.getBook({'websafeBookKey': ID}).execute(ex);
}

function ex(response){
	BOOK = response;
	generate_page();
}

function generate_page(){
	var div = document.createElement('div');
	div.appendChild(book_cover());
	div.appendChild(about_book());

	document.getElementById('about-book-container').appendChild(div);
}

function book_cover(){
	var span = document.createElement('span');
	//span.innerHTML = "<img src="+ BOOK['cover'] + " />";

	return span;
}

function about_book(){
	var div = document.createElement('div');
	div.className = 'about-book';
	//div.innerHTML = '<p class="author-name">' + BOOK['autho'] + '</p>';
	div.innerHTML += '<p class="story-name">' + BOOK['title'] + '</p>';
	div.innerHTML += '<i class="fa fa-thumbs-up fa-2x"></i>' + BOOK['likes'];
	div.innerHTML += '<i class="fa fa-thumbs-down fa-2x" style="margin-left: 15px;"></i>' + BOOK['dislikes'];
	//div.innerHTML += '<div class="annotations">' + BOOK['annotations'] + '</div>';

	return div;
}

