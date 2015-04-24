document.onunload = function(){eraseCookie(COOKIE_NAME);};

var client_id = '293348945701-eem80tf926k9ak0tpd7ktae6d158n5o2.apps.googleusercontent.com';
var scopes = 'https://www.googleapis.com/auth/userinfo.email';
var COOKIE_NAME = 'userdata';

function writeToCookie(response){
    var user = {};
    if(!response.code){
        user.name = response.name;
        user.id = response.id;
        user.session = true;
    }
    
    setCookie(COOKIE_NAME,JSON.stringify(user));
    console.log(document.cookie);
}

function signin(mode, callback) {
    gapi.auth.authorize({client_id: client_id,scope: scopes, immediate: mode}, callback);
}
$(document).ready(function(){
    $('#exit').text((document.cookie.indexOf('session') == -1)?'Sing In':'Log Out');
    $('#exit').click(function(){
        if(document.cookie.indexOf('session') != -1){
            $('#exit').text('Sing In');
            eraseCookie(COOKIE_NAME);
        } else {
            gapi.client.load('oauth2','v2', function() {
                signin(false, function handleAuth() {
                    gapi.client.oauth2.userinfo.get().execute(writeToCookie);
                });
            });
            $('#exit').text('Log Out');
        }
    });
});

function setCookie(name, value, expires, path, domain, secure) {
    document.cookie = name + "=" + escape(value) +
    ((expires == null) ? "" : "; expires=" + expires.toGMTString()) +
    ((path == null) ? "" : "; path=" + path) +
    ((domain == null) ? "" : "; domain=" + domain) +
    ((secure == null) ? "" : "; secure");
}

function getCookie(name){
    var cname = name + "=";
    var dc = document.cookie;
    if (dc.length > 0) {
        begin = dc.indexOf(cname);
        if (begin != -1) {
            begin += cname.length;
            end = dc.indexOf(";", begin);
            if (end == -1) 
                end = dc.length;
            return unescape(dc.substring(begin, end));
        }
    }
    return null;
}

function eraseCookie (name,path,domain) {
    if (getCookie(name)) {
        document.cookie = name + "=" +
        ((path == null) ? "" : "; path=" + path) +
        ((domain == null) ? "" : "; domain=" + domain) +
        "; expires=Thu, 01-Jan-70 00:00:01 GMT";
    }
}
