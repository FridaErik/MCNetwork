/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.NamedFrame;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Frida
 *
 */
public class ImageUploadGUI extends VerticalPanel {


	final FormPanel uploadForm = new FormPanel();

	// Use an RPC call to the Blob Service to get the blobstore upload url
	BlobServiceAsync blobService = GWT.create(BlobService.class);

	private Grid grid = new Grid(3, 2);
	VerticalPanel mainVerticalPanel = new VerticalPanel();
	HTML titleLabel = new HTML("Title", true);
	HTML idLabel = new HTML("ID", true);
	HTML descriptionLabel = new HTML("Description", true);
	TextBox titleTextBox = new TextBox();
	TextBox idTextBox = new TextBox();
	TextBox descriptionTextBox = new TextBox();
	FileUpload upload = new FileUpload();
	Button submitButton = new Button("Submit");
	Long userId;

	/**
	 * 
	 */
	public ImageUploadGUI(Long userid) {
		System.out.println("userid för image: "+userid);
		userId=userid;
		idLabel.setVisible(false);
		//"Påhittig" lösning för att få över Id't till uploadService
		idTextBox.setText(userid.toString());
		idTextBox.setVisible(false);
		
		grid.setWidget(0, 0, idLabel);
		grid.setWidget(0, 1, idTextBox);
		grid.setWidget(1, 0, titleLabel);
		grid.setWidget(1, 1, titleTextBox);
		grid.setWidget(2, 0, descriptionLabel);
		grid.setWidget(2, 1, descriptionTextBox);

		mainVerticalPanel.add(grid);
		mainVerticalPanel.add(upload);
		mainVerticalPanel.add(submitButton);

		uploadForm.setWidget(mainVerticalPanel);
		


		// The upload form, when submitted, will trigger an HTTP call to the
		// servlet.  The following parameters must be set
		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Set Names for the text boxes so that they can be retrieved from the
		// HTTP call as parameters
		titleTextBox.setName("titleTextBox");
		descriptionTextBox.setName("descriptionTextBox");
		idTextBox.setName("idTextBox");
		upload.setName("upload");



		this.add(uploadForm);

		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				blobService.getBlobStoreUploadUrl(new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						// Set the form action to the newly created
						// blobstore upload URL
						uploadForm.setAction(result.toString());
						// Submit the form to complete the upload
						uploadForm.submit();
						uploadForm.reset();

					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});

			}
		});

		uploadForm.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				HTML notification = new HTML("Din bild är uppdaterad", true);
				mainVerticalPanel.add(notification);
			}




		});

	


	}
	


}




