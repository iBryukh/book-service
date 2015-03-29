

function init() {

	var rootpath = "https://" + window.location.host + "/_ah/api";
	gapi.client.load('bookapi', 'v1', loadCallback, rootpath);
}

function readSingleFile(e) {
	var file = e.target.files[0];
	if (!file) {
		return;
	}
	var reader = new FileReader();
	reader.onload = function(e) {
		var contents = e.target.result;
		uploadBook(contents);
	};
	reader.readAsDataURL(file);
}

function uploadBook(contents) {
	//ось тут допиши джейсон з форми
	//поле "назва книги" обов'язкове
	gapi.client.bookapi.addBook({title:"new title", image:contents})
	.execute(onBookLoaded);
}

function onBookLoaded (response) {
	//розбери відповідь
	alert(response.title + " " + response.image);	
}

function loadCallback () {	
	//  ввімкни кнопки всякі
	
	//по кнопці зробити 
	document.getElementById('file-input').addEventListener('change',
			readSingleFile, false);
}








