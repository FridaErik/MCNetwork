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

	VerticalPanel mainVerticalPanel = new VerticalPanel();
	HorizontalPanel hp1 = new HorizontalPanel();
	HorizontalPanel hp2 = new HorizontalPanel();
	HorizontalPanel hp0 = new HorizontalPanel();
	HTML titleLabel = new HTML("Title", true);
	HTML idLabel = new HTML("ID", true);
	HTML descriptionLabel = new HTML("Description", true);
	TextBox titleTextBox = new TextBox();
	TextBox idTextBox = new TextBox();
	TextBox descriptionTextBox = new TextBox();
	FileUpload upload = new FileUpload();
	Button submitButton = new Button("Submit");
	Long userId;

	FlexTable resultsTable = new FlexTable();
	/**
	 * 
	 */
	public ImageUploadGUI(Long userid) {
		System.out.println("userid: "+userid);
		userId=userid;
		idLabel.setVisible(false);
		//"Påhittig" lösning för att få över Id't till uploadService
		idTextBox.setText(userid.toString());
		idTextBox.setVisible(false);
		hp0.add(idLabel);
		hp0.add(idTextBox);
		hp1.add(titleLabel);
		hp1.add(titleTextBox);
		hp2.add(descriptionLabel);
		hp2.add(descriptionTextBox);

		mainVerticalPanel.add(hp0);
		mainVerticalPanel.add(hp1);
		mainVerticalPanel.add(hp2);
		mainVerticalPanel.add(upload);

		mainVerticalPanel.add(submitButton);
		mainVerticalPanel.add(resultsTable);

		hp1.setSpacing(5);
		hp2.setSpacing(5);
		mainVerticalPanel.setSpacing(5);

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
				System.out.println("onSubmitComplete");
				//TODO skicka till users sida
			}




		});

	}

	public void getPicture(Long id) {

		if(id!=null){
			//Make another call to the Blob Service to retrieve the meta-data
			blobService.getPicture(id, new AsyncCallback<Picture>() {

				@Override
				public void onSuccess(Picture result) {

					Image image = new Image();
					image.setUrl(result.getImageUrl());

					//Use Getters from the Picture object to load the FlexTable
					resultsTable.setWidget(0, 0, image);
					resultsTable.setText(1, 0, result.getTitle());
					resultsTable.setText(2, 0, result.getDescription());

				}
				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		}else{
			System.out.println("picture.id==null");
		}


	}
	


}




