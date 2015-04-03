package service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Cache
@Entity
public class Profile {
	private String displayName;
	private String mainEmail;
	private List <String> liked = new ArrayList<String>(0);
	private List <String> disliked = new ArrayList<String>(0);

	@Id String userId;
	
    public Profile (String userId, String displayName, String mainEmail) {
    	this.userId = userId;
    	this.displayName = displayName;
    	this.mainEmail = mainEmail;
    }
    
	public String getDisplayName() {
		return displayName;
	}

	public String getMainEmail() {
		return mainEmail;
	}

	public String getUserId() {
		return userId;
	}

	public List<String> getLiked() {
		return liked;
	}
	
	public List<String> getDisliked() {
		return disliked;
	}
	
	/**
     * Just making the default constructor private.
     */
    private Profile() {}

	public void update(String displayName) {
		if (displayName!=null) this.displayName = displayName;
	}
	
	public void likeBook(Book book) {
		if (book!=null && !liked.contains(book.getWebsafeKey())) {
			book.like(disliked.contains(book.getWebsafeKey()));
			liked.add(book.getWebsafeKey());
			disliked.remove(book.getWebsafeKey());
		}
	}
	
	public void dislikeBook(Book book) {
		if (book!=null && !disliked.contains(book.getWebsafeKey())) {
			book.dislike(liked.contains(book.getWebsafeKey()));
			disliked.add(book.getWebsafeKey());
			liked.remove(book.getWebsafeKey());
		}
	}
	
}
