package com.TDDD27.MCNetwork.server;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.TDDD27.MCNetwork.shared.Picture;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

//The FormPanel must submit to a servlet that extends HttpServlet  
//RemoteServiceServlet cannot be used
@SuppressWarnings("serial")
public class UploadServiceImpl extends HttpServlet {

  //Start Blobstore and Objectify Sessions
  BlobstoreService blobstoreService = BlobstoreServiceFactory
      .getBlobstoreService();
  Objectify ofy = ObjectifyService.begin();

  static {
    //ObjectifyService.register(Picture.class);
  }

  //Override the doPost method to store the Blob's meta-data
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
    BlobKey blobKey = blobs.get("upload");

    //Get the parameters from the request to populate the Picture object
    Picture picture = new Picture();
    picture.setDescription(req.getParameter("descriptionTextBox"));
    picture.setTitle(req.getParameter("titleTextBox"));
    picture.setUserId(Long.parseLong(req.getParameter("idTextBox")));
    //System.out.println("bilden registreras till id: "+Long.parseLong(req.getParameter("idTextBox")));
    //Map the ImageURL to the blobservice servlet, which will serve the image
    //System.out.println("/mcnetwork/blobservice?blob-key= + blobKey.getKeyString(): "+"/mcnetwork/blobservice?blob-key=" + blobKey.getKeyString());
    picture.setImageUrl("/mcnetwork/blobservice?blob-key=" + blobKey.getKeyString());

    ofy.put(picture);
    TestServiceImpl.setUserPic(Long.parseLong(req.getParameter("idTextBox")), picture.id);

    //Redirect recursively to this servlet (calls doGet)
    //System.out.println("Resultatet av doPost i UploadServiceImpl: "+"/mcnetwork/uploadservice?id=" + picture.id);
    res.sendRedirect("/mcnetwork/uploadservice?id=" + picture.id);
    //res.getWriter().print(picture.id); 
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

