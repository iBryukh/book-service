package service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import service.BooksApi.Message;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import com.google.api.client.util.Base64;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiMethod.HttpMethod;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.condition.IfNotZero;

@Cache
@Entity
public class Book {
	
	@Id
	private Long id;
	private String title;
	private String author;
	private int year;
	@Index (IfNotZero.class)
	private int likes;
	private int dislikes;
	private List<String> genre = new ArrayList<String>(0);
	private List<String> quotes = new ArrayList<String>(0);
	private List<String> tags = new ArrayList<String>(0);
	private String annotation;
	private String image;
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
		if (bookForm.getImage()!=null) {
			try {
				this.image = saveImage(bookForm.getImage(), title);
			} catch (Exception e) {
				this.image = "";
			}
		} else {
			this.image = "";
		}
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
	public String getImage() throws DbxException {
		return DropboxClient.getClient().createTemporaryDirectUrl(image).url;
	}

	private static String saveImage(String image, String title) throws Exception {
		String[] split = image.split(";base64,");
		String type = split[0];
		type = type.split("/")[1];
		image = split[1];
		String fileName = "cover."+type;
		byte[] bytes = Base64.decodeBase64(image);
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		String url;
		try {
			DbxEntry.File uploadedFile = DropboxClient.getClient().uploadFile("/covers/" + title + "/" + fileName,
					DbxWriteMode.add(), bytes.length, bis);
			url = uploadedFile.path;
		} finally {
			bis.close();
		}
		
		return url;
	}
	
}