package com.TDDD27.MCNetwork.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TDDD27.MCNetwork.client.DatabaseService;
import com.TDDD27.MCNetwork.client.DatabaseServiceAsync;
import com.TDDD27.MCNetwork.shared.Picture;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**
 * Serverklass för hantering av uppladdning från ImageUploadGUI
 * Baserat på http://www.fishbonecloud.com/2010/12/tutorial-gwt-application-for-storing.html
 * @author Frida&Erik
 *
 */
@SuppressWarnings("serial")
public class UploadServiceImpl extends HttpServlet {
	//The FormPanel must submit to a servlet that extends HttpServlet  
	//RemoteServiceServlet cannot be used

	//Start Blobstore and Objectify Sessions
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	Objectify ofy = ObjectifyService.begin();

	/*	static {
		//ObjectifyService.register(Picture.class);
	}*/

	//Override the doPost method to store the Blob's meta-data
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("upload");	

		//Ta bort Blobs och Pictures innan ny användarbild lagras
		ArrayList<String> result=DatabaseServiceImpl.deleteUserPicKey(Long.parseLong(req.getParameter("idTextBox")));

		//Get the parameters from the request to populate the Picture object
		Picture picture = new Picture();
		picture.setDescription(req.getParameter("descriptionTextBox"));
		picture.setTitle(req.getParameter("titleTextBox"));
		picture.setUserId(Long.parseLong(req.getParameter("idTextBox")));
		//Map the ImageURL to the blobservice servlet, which will serve the image
		picture.setImageUrl("/mcnetwork/blobservice?blob-key=" + blobKey.getKeyString());

		ofy.put(picture);
		//Lagra bild id't hos användaren
		DatabaseServiceImpl.setUserPic(Long.parseLong(req.getParameter("idTextBox")), picture.id);

		//Redirect recursively to this servlet (calls doGet)
		res.sendRedirect("/mcnetwork/uploadservice?id=" + picture.id);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		//Send the meta-data id back to the client in the HttpServletResponse response
		String id = req.getParameter("id");
		resp.setHeader("Content-Type", "text/html");
		resp.getWriter().println(id);

	}

}

