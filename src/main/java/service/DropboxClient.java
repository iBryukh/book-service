package service;

import java.util.Locale;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxRequestConfig;

public class DropboxClient {

	private static DbxClient client;
	
	public static DbxClient getClient () {
		if (client == null) {
			DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(
				"BooksService/1.0", Locale.getDefault().toString(), NoHttpsRequestor.Instance);
		
			String authAccessToken = "dANzZIkRsdAAAAAAAAAABzJy0QNIL0ZlVdUVwjdJBMl3_wD5TNfxcjNWwLVKLsdI";
			client = new DbxClient(dbxRequestConfig, authAccessToken);
		}
		return client;
	}
	
}
