package service;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

/**
 * Class that represents comments entity in the project
 */
@Cache
@Entity
public class Comment {
    @Id
    private long id;
	String comment;
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Profile> profileKey;
    @Parent
    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    private Key<Book> bookKey;
    
    
    private Comment(){}

    /**
     * Creates a comment from a CommentForm
     * @param id
     * @param bookKey
     * @param profileKey
     * @param form
     */
    public Comment (final long id, Key<Book> bookKey, Key<Profile> profileKey, final CommentForm form) {
        this.id = id;
        this.profileKey = profileKey;
        this.bookKey = bookKey;
        this.comment = form.getComment();
    }
    
    public String getAuthorId() {
        return profileKey.getString();
    }
    public String getAuthorName() {
        Profile user = OfyService.ofy().load().key(getProfileKey()).now();
        if (user == null) {
            return "Anonymous";
        } else {
            return user.getDisplayName();
        }
    }
    public String getWebsafeKey() {
        return Key.create(Comment.class, id).getString();
    }
	public long getId() {
		return id;
	}
	public String getComment() {
		return comment;
	}
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<Profile> getProfileKey() {
		return profileKey;
	}
    
    
}
