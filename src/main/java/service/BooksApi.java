package service;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;

import java.util.ArrayList;

import javax.inject.Named;

@Api(
    name = "booksapi",
    version = "v1",
    scopes = {Constants.EMAIL_SCOPE},
    clientIds = {Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID},
    description = "Books Api"
)
public class BooksApi {

    private static String extractDefaultDisplayNameFromEmail(String email) {
        return email == null ? null : email.substring(0, email.indexOf("@"));
    }
	
    @ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
    public Profile saveProfile(final User user, ProfileForm pf) throws UnauthorizedException {

        String userId = null;
        String mainEmail = null;
        String displayName = "Your name will go here";

        
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }

        if (pf.getDisplayName()!=null) displayName = pf.getDisplayName();
        
        userId = user.getUserId();
        mainEmail = user.getEmail();

        if (pf.getDisplayName()==null) displayName = extractDefaultDisplayNameFromEmail(mainEmail);

        Profile profile = getProfile(user);
        if (profile == null)
        	profile = new Profile(userId, displayName, mainEmail);
        else
        	profile.update(pf.getDisplayName());

        ofy().save().entity(profile).now();

        return profile;
    }
    
    @ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
    public Profile getProfile(final User user) throws UnauthorizedException {
    	if (user == null) {
    		throw new UnauthorizedException("Authorization required");
        }

        String userId = user.getUserId();
        Key<Profile> key = Key.create(Profile.class,userId);
        Profile profile = ofy().load().key(key).now();
        return profile;
    }
    
    @ApiMethod(name = "addBook", path = "book", httpMethod = HttpMethod.POST)
    public Book addBook(final BookForm bookForm) {
    	Book book = new Book (bookForm);
    	ofy().save().entity(book).now();
    	return book;
    }

    @ApiMethod(name = "getBook", path = "book", httpMethod = HttpMethod.GET)
    public Book getBook(final User user, @Named("websafeBookKey") final String websafeBookKey) {
    	Key<Book> key = Key.create(websafeBookKey);
        Book book = ofy().load().key(key).now();
        int liked = 0; 
        if (user != null) 
        {
        	Profile profile = getProfileFromUser(user);
        	if (profile.getLiked().contains(websafeBookKey)) {
        		liked = 1;
        	} else if (profile.getDisliked().contains(websafeBookKey)) {
        		liked = -1;
        	}
        }    
        book.setLiked(liked);
    	return book;
    }
    
    @ApiMethod(name = "likeBook", path = "book/{websafeBookKey}/like", httpMethod = HttpMethod.POST)
    public Profile likeBook(final User user, @Named("websafeBookKey") final String websafeBookKey) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Profile profile = getProfileFromUser(user);
        Book book = getBook(user, websafeBookKey);
        profile.likeBook(book);
        ofy().save().entities(profile, book).now();
        return profile;
    }
    
    @ApiMethod(name = "dislikeBook", path = "book/{websafeBookKey}/dislike", httpMethod = HttpMethod.POST)
    public Profile dislikeBook(final User user, @Named("websafeBookKey") final String websafeBookKey) throws UnauthorizedException {
        if (user == null) {
            throw new UnauthorizedException("Authorization required");
        }
        Profile profile = getProfileFromUser(user);
        Book book = getBook(user, websafeBookKey);
        profile.dislikeBook(book);
        ofy().save().entities(profile, book).now();
        return profile;
    }
    
    private static Objectify ofy() {
    	return OfyService.ofy();
    }
	
    private static Profile getProfileFromUser(User user) {
        Profile profile = ofy().load().key(Key.create(Profile.class, user.getUserId())).now();
        if (profile == null) {
            String email = user.getEmail();
            profile = new Profile(user.getUserId(),extractDefaultDisplayNameFromEmail(email), email);
        }
        return profile;
    }
	
}
