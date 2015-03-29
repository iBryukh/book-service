package service;

import java.io.*;
import java.util.*;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 





import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;

public class Upload extends HttpServlet {

	public void doPost(HttpServletRequest req, HttpServletResponse res) {
	    // Get the image representation
	    ServletFileUpload upload = new ServletFileUpload();
	    FileItemIterator iter = upload.getItemIterator(req);
	    FileItemStream imageItem = iter.next();
	    InputStream imgStream = imageItem.openStream();

	    // construct our entity objects
	    Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
	    MyImage myImage = new MyImage(imageItem.getName(), imageBlob);

	    // persist image
	    PersistenceManager pm = PMF.get().getPersistenceManager();
	    pm.makePersistent(myImage);
	    pm.close();

	    // respond to query
	    res.setContentType("text/plain");
	    res.getOutputStream().write("OK!".getBytes());
	}
	
}
