var ID = getJsonFromUrl()['id'];
var BOOK;

function init(){
	var rootpath = "https://" + window.location.host + "/_ah/api";
    gapi.client.load('bookapi', 'v1', load, rootpath);
}	

function load(){
	gapi.client.bookapi.getBook({'websafeBookKey': ID}).execute(ex);
	gapi.client.bookapi.getComments({'websafeBookKey': ID}).execute(comments);
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
		//alert('please log in and try again');
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

function ex(response){
	if(response.code)
		window.location = "404.html";
	BOOK = response;
	generatePage();
}

function generatePage(){
	var ul = document.createElement('ul');
	ul.appendChild(bookCover());
	ul.appendChild(information());

	document.getElementById('about-book-container').appendChild(ul);
}

function bookCover(){
	var li = document.createElement('li');	
	li.className = 'book-cover';
	li.innerHTML = '<img src="'+BOOK['image']+'" />';

	return li;
}

function information(){
	var li = document.createElement('li');	
	li.className = 'information';
	li.innerHTML += '<p class="author-name">'+BOOK['author']+'</p>';
	li.innerHTML += '<p class="story-name">'+BOOK['title']+'</p>';
    li.innerHTML += '<i class="fa fa-thumbs-up fa-2x"></i><span id="like">'+BOOK['likes']+'</span>';
    li.innerHTML += '<i class="fa fa-thumbs-down fa-2x" style="margin-left: 15px;"></i><span id="dislike">'+BOOK['dislikes']+'</span>';
    li.innerHTML += '<div class="annotations">'+BOOK['annotation']+'</div>';
    if(BOOK['quotes']){
	    var quotes = "";
	    for(var i = 0; i < BOOK['quotes'].length; ++i){
	    	quotes += '<div class="quote">'
			quotes += '<p>#'+(i+1)+'</p>';
			quotes += '<i>'+BOOK['quotes'][i]+'</i>';
			quotes += '</div>';
	    }
	    li.innerHTML += '<div class="quotes">'+quotes+'</div>';
	}
	return li;
}

function comments(response){
	if(response.code)
		return;
	if(response.items){
		var commentList = response.items;
		for(var i = 0; i < commentList.length; ++i){
			comment(commentList[i]);
		}
	}
}

function comment(data){
	document.getElementById('comments').innerHTML += '<div class="comment"><p class="author">'+data['authorName']+'</p><p class="content">'+data['comment']+'</p></div><hr width="30%">';
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