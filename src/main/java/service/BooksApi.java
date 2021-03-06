package service;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.google.api.server.spi.response.UnauthorizedException;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.cmd.Query;

import java.util.List;
import static service.OfyService.ofy;
import static logic.BookSearch.searchBook;
import javax.inject.Named;

/**
 * Class that provides an API for the whole service
 */
@Api(name = "bookapi", version = "v1", scopes = { Constants.EMAIL_SCOPE }, clientIds = {
		Constants.WEB_CLIENT_ID, Constants.API_EXPLORER_CLIENT_ID }, description = "Books Api")
public class BooksApi {

	private static String extractDefaultDisplayNameFromEmail(String email) {
		return email == null ? null : email.substring(0, email.indexOf("@"));
	}

	/**
	 * Saves the profile
	 * @param user
	 * @param pf
	 * @return
	 * @throws UnauthorizedException
	 */
	@ApiMethod(name = "saveProfile", path = "profile", httpMethod = HttpMethod.POST)
	public Profile saveProfile(final User user, ProfileForm pf)
			throws UnauthorizedException {

		String userId = null;
		String mainEmail = null;
		String displayName = "Your name will go here";

		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		if (pf.getDisplayName() != null)
			displayName = pf.getDisplayName();

		userId = user.getUserId();
		mainEmail = user.getEmail();

		if (pf.getDisplayName() == null)
			displayName = extractDefaultDisplayNameFromEmail(mainEmail);

		Profile profile = getProfile(user);
		if (profile == null)
			profile = new Profile(userId, displayName, mainEmail);
		else
			profile.update(pf.getDisplayName());

		ofy().save().entity(profile).now();

		return profile;
	}

	/**
	 * Returns the profile
	 * @param user
	 * @return User profile
	 * @throws UnauthorizedException
	 */
	@ApiMethod(name = "getProfile", path = "profile", httpMethod = HttpMethod.GET)
	public Profile getProfile(final User user) throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}

		String userId = user.getUserId();
		Key<Profile> key = Key.create(Profile.class, userId);
		Profile profile = ofy().load().key(key).now();
		if (profile==null) {
			profile = getProfileFromUser(user);
			ofy().save().entity(profile).now();
		}
		return profile;
	}

	@ApiMethod(name = "addBook", path = "book", httpMethod = HttpMethod.POST)
	public Book addBook(final BookForm bookForm) {
		Book book = new Book(bookForm);
		ofy().save().entity(book).now();
		return book;
	}

	@ApiMethod(name = "getBook", path = "book", httpMethod = HttpMethod.GET)
	public Book getBook(final User user,
			@Named("websafeBookKey") final String websafeBookKey) {
		Key<Book> key = Key.create(websafeBookKey);
		Book book = ofy().load().key(key).now();
		int liked = 0;
		if (user != null) {
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
	public Message likeBook(final User user,
			@Named("websafeBookKey") final String websafeBookKey)
			throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
		Profile profile = getProfileFromUser(user);
		Book book = getBook(user, websafeBookKey);
		profile.likeBook(user, book);
		ofy().save().entities(profile, book).now();
		return new Message(book.getLikes()+","+book.getDislikes());
	}

	@ApiMethod(name = "dislikeBook", path = "book/{websafeBookKey}/dislike", httpMethod = HttpMethod.POST)
	public Message dislikeBook(final User user,
			@Named("websafeBookKey") final String websafeBookKey)
			throws UnauthorizedException {
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
		Profile profile = getProfileFromUser(user);
		Book book = getBook(user, websafeBookKey);
		profile.dislikeBook(user, book);
		ofy().save().entities(profile, book).now();
		return new Message(book.getLikes()+","+book.getDislikes());
	}

	@ApiMethod(name = "queryBooks", path = "books", httpMethod = HttpMethod.POST)
	public List<Book> queryBooks(final BookQuery q) {
		int limit = (q.getLimit() == 0) ? 10 : q.getLimit();
		Query<Book> query = null;
		
		if (q.getType() == 0)
			query = ofy().load().type(Book.class).order("-likes")
					.offset(q.getOffset()).limit(limit);
		else if (q.getType()==1){
			int size = ofy().load().type(Book.class).count();
			int random = 0;
			if (size > limit) random = (int) (Math.random()*(size-limit));
			query = ofy().load().type(Book.class).offset(random).limit(limit);
		} else if (q.getType()==2) {
			return searchBook(q.getField());
		}
		List<Book> books = query.list();
		return books;
	}
	
	@ApiMethod(name = "commentBook", path = "book/{websafeBookKey}/comment", httpMethod = HttpMethod.POST)
	public Comment commentBook(final User user,
			@Named("websafeBookKey") final String websafeBookKey, final CommentForm form) throws UnauthorizedException {
		if (form.getComment()==null || form.getComment().length()<2) return null;
		if (user == null) {
			throw new UnauthorizedException("Authorization required");
		}
        String userId = user.getUserId();
        Key<Profile> profileKey = Key.create(Profile.class,userId);
        Key<Book> bookKey = Key.create(websafeBookKey);
        final Key<Comment> commentKey = OfyService.factory().allocateId(bookKey, Comment.class);
        final long commentId = commentKey.getId();
		Comment comment = new Comment(commentId, bookKey, profileKey, form);
		ofy().save().entity(comment).now();
		return comment;
	}
	
	@ApiMethod(name = "getComments", path = "book/{websafeBookKey}/getcomments", httpMethod = HttpMethod.POST)
	public List<Comment> getComments(@Named("websafeBookKey") final String websafeBookKey) {
		Key<Comment> bookKey = Key.create(websafeBookKey);
        Query query = ofy().load().type(Comment.class).ancestor(bookKey);
        return query.list();
	}

	class Message {
		private String message;

		public Message(String message) {
			this.message = message;
		}

		public Message() {
			this.message = "";
		}

		public String getMessage() {
			return message;
		}

	}

	private static Objectify ofy() {
		return OfyService.ofy();
	}

	private static Profile getProfileFromUser(User user) {
		Profile profile = ofy().load()
				.key(Key.create(Profile.class, user.getUserId())).now();
		if (profile == null) {
			String email = user.getEmail();
			profile = new Profile(user.getUserId(),
					extractDefaultDisplayNameFromEmail(email), email);
		}
		return profile;
	}

}
