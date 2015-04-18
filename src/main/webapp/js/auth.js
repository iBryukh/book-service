var client_id = '293348945701-eem80tf926k9ak0tpd7ktae6d158n5o2.apps.googleusercontent.com';
var scopes = 'https://www.googleapis.com/auth/userinfo.email';
var USER = null;

function handleAuth() {
    var request = gapi.client.oauth2.userinfo.get().execute(function(resp) {
        if (!resp.code) {
            USER = resp;
            window.name = true;
        } else {
            window.name = false;
        }
    });
}
        
function signin(mode, callback) {
    gapi.auth.authorize({client_id: client_id,scope: scopes, immediate: mode},callback);
}
    
function auth() {
    var callback = function() {
        signin(false, handleAuth);
    };

    if(!window.name){
        gapi.client.load('oauth2','v2', function() {
            signin(false, handleAuth);
        });
    }
}