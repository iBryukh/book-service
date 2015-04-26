var ID = getJsonFromUrl()['id'];
var BOOK;

function load(){
	gapi.client.bookapi.getBook({'websafeBookKey': ID}).execute(book);
	gapi.client.bookapi.getComments({'websafeBookKey': ID}).execute(function(response){
		if(response['items']){
			var commentList = response['items'];
			for(var i = 0; i < commentList.length; ++i)
				comment(commentList[i]);
		}
	});
}

function addComment(response){
	if(!response.code){
		comment(response);
		$('#comment-text').val('');
	} else {
		gapi.client.load('oauth2','v2', function() {
	       	signin(false, function() {
				gapi.client.oauth2.userinfo.get().execute(function(response){
					writeToCookie(response);
					addNewComment();
				});
			});
		});
	}
}

function func(response, type){
	if(!response.code){
		var marks = response.message.split(',');
		$('#like').text(marks[0]);
		$('#dislike').text(marks[1]);
	} else {
		gapi.client.load('oauth2','v2', function() {
	       	signin(false, function() {
				gapi.client.oauth2.userinfo.get().execute(function(response){
					writeToCookie(response);
					if(type == 1){
						gapi.client.bookapi.likeBook({'websafeBookKey': ID}).execute(function(response){
							func(response, 1);
						});
					} else {
						gapi.client.bookapi.dislikeBook({'websafeBookKey': ID}).execute(function(response){
							func(response, 2);
						});
					}
				});
			});
		});
	}
}

function book(response){
	if(response.code)
		window.location = "404.html";
	$('#cover').attr('src', response['image']);
	$('#author-name').text(response['author']);
	$('#story-name').text(response['title']);
	$('#like').text(response['likes']);
	$('#dislike').text(response['dislikes']);
	$('#annotations').text(response['annotation']);
	if(response['quotes']){
	    for(var i = 0; i < response['quotes'].length; ++i)
	    	$('#quotes').append('<div class="quote"><p>#'+(i+1)+'</p><i>'+response['quotes'][i]+'</i></div>');
	} else {
		$('#quotes').remove();
	}
}

function comment(data){
	$('#comments').append('<div class="comment"><p class="author">'+data['authorName']+'</p><p class="content">'
		+data['comment']+'</p></div><hr width="30%">');
}

function addNewComment(){
	var comment = $('#comment-text').val();
	if(comment.length > 0)
		gapi.client.bookapi.commentBook({'websafeBookKey': ID, 'comment': comment}).execute(addComment);
}

$(document).ready(function(){
	$('#submit').click(function(){
		addNewComment();
	});
	$('body').on('click', '.fa-thumbs-up', function(){
		gapi.client.bookapi.likeBook({'websafeBookKey': ID}).execute(function(response){
			func(response, 1);
		});
	});
	$('body').on('click', '.fa-thumbs-down', function(){
		gapi.client.bookapi.dislikeBook({'websafeBookKey': ID}).execute(function(response){
			func(response, 2);
		});
	});
});