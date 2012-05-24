package com.TDDD27.MCNetwork.server;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;

public class SampleUploadServlet extends UploadAction {

	private static final long serialVersionUID = 1L;

	//Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
	/**
	 * Maintain a list with received files and their content types. 
	 */
	//Hashtable<String, File> receivedFiles = new Hashtable<String, File>();

	/**
	 * Override executeAction to save the received files in a custom place
	 * and delete this items from session.  
	 */
	@Override
	public String executeAction(HttpServletRequest request, List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		int cont = 0;
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				cont ++;
				try {

					/// Create a temporary file placed in the default system temp folder
					File dir = new File("C://Users/Frida/Git/MCNetwork/MCNetwork/war/images");
					File file = File.createTempFile("productpic-", ".jpg", dir); 
					item.write(file);

					/// Send a customized message to the client.
					response = file.getAbsolutePath();
					System.out.println("File saved as " + file.getAbsolutePath());
				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		/// Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		/// Send your customized message to the client.
		return response;
	}

	/**
	 * Get the content of an uploaded file.
	 */

	/*@Override
	    public void getUploadedFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    String fieldName = request.getParameter(UConsts.PARAM_SHOW);
	    File f = receivedFiles.get(fieldName);
	    if (f != null) {
	      response.setContentType(receivedContentTypes.get(fieldName));
	      FileInputStream is = new FileInputStream(f);
	      copyFromInputStreamToOutputStream(is, response.getOutputStream());
	    } else {
	      renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
	   }
	  }*/

	/**
	 * Remove a file when the user sends a delete request.
	 */
	/*@Override
	  public void removeItem(HttpServletRequest request, String fieldName)  throws UploadActionException {
	    File file = receivedFiles.get(fieldName);
	    receivedFiles.remove(fieldName);
	    receivedContentTypes.remove(fieldName);
	    if (file != null) {
	      file.delete();
	    }
	  }*/


}
