package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import logic.CosineSimilarity;

import com.google.appengine.api.users.User;
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
	private List <String> recommended = new ArrayList<String>(0);

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
	
	public List<String> getRecommended() {
		return recommended;
	}
	
    private Profile() {}

	public void update(String displayName) {
		if (displayName!=null) this.displayName = displayName;
	}
	
	public void likeBook(final User user, Book book) {
		if (book!=null && !liked.contains(book.getWebsafeKey())) {
			book.like(disliked.contains(book.getWebsafeKey()));
			liked.add(book.getWebsafeKey());
			disliked.remove(book.getWebsafeKey());
			updateRecommended(user);
		}
	}
	
	public void dislikeBook(final User user, Book book) {
		if (book!=null && !disliked.contains(book.getWebsafeKey())) {
			book.dislike(liked.contains(book.getWebsafeKey()));
			disliked.add(book.getWebsafeKey());
			liked.remove(book.getWebsafeKey());
			updateRecommended(user);
		}
	}
	
	private void updateRecommended(final User user){
		if (liked.size()==0) return;
		List<Value> rate = new ArrayList<Value>();
		for (int i=0;;i+=50){
			List<Book> db = OfyService.ofy().load().type(Book.class).offset(i).limit(50).list();
			if (db.size()==0) break;
			List<Book> these = new ArrayList<Book>(0);
			for (String b: liked){
				these.add(new BooksApi().getBook(user, b));
			}
			for (Book b: db){
				for (Book b2: these){
					if (these.contains(b)) continue;
					rate.add(new Value (b, new CosineSimilarity().Cosine_Similarity_Score(b.getAnnotation(), b2.getAnnotation())));
				}
			}
		}
		Collections.sort(rate, new Comparator<Value>() {
			 public int compare(Value o1, Value o2) {
		            if (o2.getRate() < o1.getRate()) return -1;
		            return 1;
		     }
		});
		recommended = new ArrayList<String>(0);
		int i = 0;
		for (Value b:rate) {
			if (!recommended.contains(b.getBook().getWebsafeKey())) {
				recommended.add(b.getBook().getWebsafeKey());
				++i;
			}
			if (i > 30) return;
		}
	}
	
	private class Value{
		private Book book;
		private Double rate;
		public Value(Book book, Double rate) {
			this.book = book;
			this.rate = rate;
		}
		public Book getBook() {
			return book;
		}
		public Double getRate() {
			return rate;
		}
	}
}
