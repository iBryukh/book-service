package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import service.Book;
import static service.OfyService.ofy;

public class BookSearch {
	public static List<Book> searchBook(String field)
	{
		List<Book> founded = new ArrayList<Book>();
		if (field == null) return founded;
		field = prepareRegex(field);
		if (field.length() < 3) return founded;
		
		for (int i=0;;i+=50){
			List<Book> list = ofy().load().type(Book.class).offset(i).limit(50).list();
			if (list.size()==0) break;
			for (Book b: list) {
				String[] input = {b.getTitle().toLowerCase(), b.getAuthor().toLowerCase(), b.getAnnotation().toLowerCase()};
				Pattern p = Pattern.compile(field);
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
	
	private static String prepareRegex (String s) {
		s = s.replaceAll("[^a-zA-Z0-9+]", "");
		while (s.contains("++")) s = s.replace("++", "+");
		s = s.replace('+', '|');
		return s.toLowerCase();
	}
}
