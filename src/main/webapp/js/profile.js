var RECOMENDED_BOOKS_GRID_ID = '#books-recomended-grid';

function load(){
	gapi.client.load('oauth2','v2', function() {
       	signin(false, function() {
			gapi.client.oauth2.userinfo.get().execute(function(response){
				writeToCookie(response);
				gapi.client.bookapi.getProfile().execute(function(resp){
					var user = JSON.parse(getCookie(COOKIE_NAME));
					$('#name').text('Name: ' + user.name);
					$('#email').text('Email: ' + user.email);
					$('#photo').attr("src", user.picture);
					if(resp.recommended){
						generate_page(RECOMENDED_BOOKS_GRID_ID, resp.recommended);
					} else {
						alert('error');
						$('.books-container').remove();
					}
			    });
			});
		});
	});
}