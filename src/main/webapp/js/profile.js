var RECOMENDED_BOOKS_GRID_ID = 'books-recomended-grid';

function load(){
	gapi.client.load('oauth2','v2', function() {
       	signin(false, function() {
			gapi.client.oauth2.userinfo.get().execute(function(response){
				writeToCookie(response);
				gapi.client.bookapi.getProfile().execute(function(response){
					var user = JSON.parse(getCookie(COOKIE_NAME));
					$('#name').text('Name: ' + user.name);
					$('#email').text('Email: ' + user.email);
					$('#photo').attr("src", user.picture);
					generate_page(RECOMENDED_BOOKS_GRID_ID, response.recommended);
			    });
			});
		});
	});
}