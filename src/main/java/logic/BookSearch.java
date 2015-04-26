package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import service.Book;
import static service.OfyService.ofy;

public class BookSearch {
	public static final int ALL_FIELDS = 0;
	public static final int ONLY_AUTHOR = 1;
	
	public static List<Book> searchBook(String query, int fields){
		List<Book> founded = new ArrayList<Book>();
		if (query == null) return founded;
		query = prepareRegex(query);
		if (query.length() < 3) return founded;
		
		for (int i=0;;i+=50){
			List<Book> list = ofy().load().type(Book.class).offset(i).limit(50).list();
			if (list.size()==0) break;
			for (Book b: list) {
				String[] input = null;
				if (fields==ALL_FIELDS)
					input = new String[] {b.getTitle().toLowerCase(), b.getAuthor().toLowerCase(), b.getAnnotation().toLowerCase(), b.getGenre().toString().toLowerCase()};
				else
					input = new String[] {b.getAuthor().toLowerCase()};
				Pattern p = Pattern.compile(query);
				for (int j = 0; j < input.length; ++j) {
					Matcher m = p.matcher(input[j]);
					if (m.find()) {
						founded.add(b);
						break;
					}
				}
			}
		}
		return founded;
	}
	
	public static List<Book> searchBook(String query){
		return searchBook(query, ALL_FIELDS);
	}
	
	public static String prepareRegex (String s) {
		s = s.replaceAll("[^a-zA-Z0-9+]", "+");
		while (s.contains("++")) s = s.replace("++", "+");
		String[] a = s.split("\\+");
		s = "";
		for (String i: a)
			if(i.length() > 1) s += i + '|';
		if (s.length() < 3) return "";
		s = s.substring(0, s.length()-1);
		return s.toLowerCase();
	}
}
