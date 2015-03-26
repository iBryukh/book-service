package service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Cache
@Entity
public class Book {
	
	@Id
	private Long id;
	private String title;
	private String author;
	private int year;
	private int likes;
	private int dislikes;
	private List<String> genre = new ArrayList<String>(0);
	private List<String> quotes = new ArrayList<String>(0);
	private List<String> tags = new ArrayList<String>(0);
	private String annotation;
	private int userlike;
	
	private Book(){}
	
	public Book(BookForm bookForm) {
		if (bookForm.getTitle()!=null) this.title = bookForm.getTitle();
		else this.title = "";
		if (bookForm.getAuthor()!=null) this.author = bookForm.getAuthor();
		else this.author= "";
		this.year = bookForm.getYear();
		this.likes = 0;
		this.dislikes = 0;
		if (bookForm.getGenre()!=null)this.genre = bookForm.getGenre();
		if (bookForm.getQuotes()!=null)this.quotes = bookForm.getQuotes();
		if (bookForm.getTags()!=null)this.tags = bookForm.getTags();
		if (bookForm.getAnnotation()!=null)this.annotation = bookForm.getAnnotation();
		else this.annotation = "";
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	public int getYear() {
		return year;
	}
	public int getLikes() {
		return likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public List<String> getGenre() {
		return genre;
	}
	public List<String> getQuotes() {
		return quotes;
	}
	public List<String> getTags() {
		return tags;
	}
	public String getAnnotation() {
		return annotation;
	}
	public long getId() {
		return id;
	}
	public int getLiked () {
		return this.userlike;
	}
	public void setLiked(int liked) {
		this.userlike = liked;
	}
    public String getWebsafeKey() {
        return Key.create(Book.class, id).getString();
    }
	public void like (Boolean bool) {
		likes++;
		if (bool) dislikes--;
	}
	public void dislike (Boolean bool) {
		dislikes++;
		if (bool) likes--;
	}
	
}