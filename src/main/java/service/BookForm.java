package service;

import java.util.ArrayList;
import java.util.List;

public class BookForm {
	
	private String title;
	private String author;
	private int year;
	private List<String> genre;
	private List<String> quotes;
	private List<String> tags;
	private String annotation;
	private String image;
	
	private BookForm(){}
	
	public String getImage() {
		return image;
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
	
}
