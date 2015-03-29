package service;

import javax.jdo.annotations.*;

import com.google.appengine.api.datastore.Blob;
import com.googlecode.objectify.annotation.*;

@Entity
public class MyImage {
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String name;

    @Persistent
    Blob image;

    public MyImage() { }
    public MyImage(String name, Blob image) {
        this.name = name; 
        this.image = image;
    }

    // JPA getters and setters and empty contructor
    // ...
    public Blob getImage()              { return image; }
    public void setImage(Blob image)    { this.image = image; }
}