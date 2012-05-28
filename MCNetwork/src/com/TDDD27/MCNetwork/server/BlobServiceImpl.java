package com.TDDD27.MCNetwork.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.TDDD27.MCNetwork.client.BlobService;
import com.TDDD27.MCNetwork.client.DatabaseService;
import com.TDDD27.MCNetwork.client.DatabaseServiceAsync;
import com.TDDD27.MCNetwork.shared.Picture;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

/**Används för uppladdning av bilder
 * Baseras på http://www.fishbonecloud.com/2010/12/tutorial-gwt-application-for-storing.html
 * @author Frida&Erik
 *
 */
@SuppressWarnings("serial")
public class BlobServiceImpl extends RemoteServiceServlet implements
BlobService {

	
	//Start a GAE BlobstoreService session and Objectify session
	BlobstoreService blobstoreService = BlobstoreServiceFactory
			.getBlobstoreService();
	Objectify ofy = ObjectifyService.begin();


	//Programmet klagar om vi gör detta i båda BlobService och UploadService, 
	//testar att kommentera bort det här
	//Register the Objectify Service for the Picture entity
	static {
		ObjectifyService.register(Picture.class);
	}

	/**
	 * Generate a Blobstore Upload URL from the GAE BlobstoreService
	 */
	@Override
	public String getBlobStoreUploadUrl() {

		//Map the UploadURL to the uploadservice which will be called by
		//submitting the FormPanel
		System.out.println("Resultatet av getBlobStoreUploadUrl i BlobstoreServiceImpl "+ blobstoreService.createUploadUrl("/mcnetwork/uploadservice"));
		return blobstoreService.createUploadUrl("/mcnetwork/uploadservice");
	}

	/**
	 * Retrieve the Blob's meta-data from the Datastore using Objectify
	 */
	@Override
	public Picture getPicture(Long id) {

		long l = id;
		Picture picture = ofy.get(Picture.class, l);
		System.out.println("Skickar ut Picture från getPicture(id) i BlobstoreServiceImpl");
		return picture;
	}


	/**
	 * Override doGet to serve blobs.  This will be called automatically by the Image Widget
	 * in the client
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
		blobstoreService.serve(blobKey, resp);
	}
}