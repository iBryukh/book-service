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
		if (field == null || field.length() < 3) return null;
		while (field.contains("  ")) field = field.replace("  ", " ");
		while (field.contains(" ")) field = field.replace(' ', '|');
		field.toLowerCase();
		List<Book> founded = new ArrayList<Book>();
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
}
