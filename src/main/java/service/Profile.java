package service;

import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;


@Cache
@Entity
public class Profile {
	String displayName;
	String mainEmail;


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

	/**
     * Just making the default constructor private.
     */
    private Profile() {}

	public void update(String displayName) {
		if (displayName!=null) this.displayName = displayName;
	}
	
}
