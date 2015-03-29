package service;

import com.google.api.server.spi.Constant;

public class Constants {
  public static final String WEB_CLIENT_ID = "293348945701-eem80tf926k9ak0tpd7ktae6d158n5o2.apps.googleusercontent.com";
  public static final String ANDROID_CLIENT_ID = "replace this with your Android client ID";
  public static final String IOS_CLIENT_ID = "replace this with your iOS client ID";
  public static final String ANDROID_AUDIENCE = WEB_CLIENT_ID;
  public static final String API_EXPLORER_CLIENT_ID = Constant.API_EXPLORER_CLIENT_ID;
  public static final String CLIENT_SECRET = "VHMLvArj74FaHhkjwD_rtH4f";
  
  public static final String EMAIL_SCOPE = "https://www.googleapis.com/auth/userinfo.email";
  public static final String REDIRECT_URI = "http://books-service.appspot.com/oauth2callback";
}
