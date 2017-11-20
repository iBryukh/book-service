package service;

import java.util.Locale;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

public class DropboxClient {

	private static DbxClientV2 client;
	private static final String authAccessToken = "dANzZIkRsdAAAAAAAAAABzJy0QNIL0ZlVdUVwjdJBMl3_wD5TNfxcjNWwLVKLsdI";
	
	public static DbxClientV2 getClient () {
		if (client == null) {
			DbxRequestConfig dbxRequestConfig = new DbxRequestConfig(
					"BooksService/1.0", Locale.getDefault().toString());
			client = new DbxClientV2(dbxRequestConfig, authAccessToken);
		}
		return client;
	}
	
}
