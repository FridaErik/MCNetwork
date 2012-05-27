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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Frida
 *
 */
public class ImageUploadGUI extends FormPanel {


	//final FormPanel uploadForm = new FormPanel();

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
	Long userId;
	ImageUploadGUI itself;

	/**
	 * 
	 */
	public ImageUploadGUI(Long userid) {
		itself=this;
		System.out.println("userid f�r image: "+userid);
		userId=userid;
		idLabel.setVisible(false);
		//"P�hittig" l�sning f�r att f� �ver Id't till uploadService
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
		SimplePanel submit = new SimplePanel();
		HTML submitBtn = new HTML("Uppdatera", true);
		ClickHandler updateClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				
				blobService.getBlobStoreUploadUrl(new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						// Set the form action to the newly created
						// blobstore upload URL
						itself.setAction(result.toString());
						// Submit the form to complete the upload
						
						itself.submit();
						itself.reset();
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});	
			}
		};
		submitBtn.addClickHandler(updateClickHandler);
		submit.add(submitBtn);
		submit.setWidth("120px");
		submit.addStyleName("GreenBtn");
		submit.setHeight("20px");
		mainVerticalPanel.add(submit);

		this.setWidget(mainVerticalPanel);

		// The upload form, when submitted, will trigger an HTTP call to the
		// servlet.  The following parameters must be set
		this.setEncoding(FormPanel.ENCODING_MULTIPART);
		this.setMethod(FormPanel.METHOD_POST);

		// Set Names for the text boxes so that they can be retrieved from the
		// HTTP call as parameters
		titleTextBox.setName("titleTextBox");
		descriptionTextBox.setName("descriptionTextBox");
		idTextBox.setName("idTextBox");
		upload.setName("upload");

		//this.add(uploadForm);

		this.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				System.out.println("event.getResults(): "+event.getResults());
				HTML notification = new HTML("Din bild &auml;r uppdaterad</br> Det kan ta en stund innan bilden syns p&aring; din sida.", true);
				mainVerticalPanel.add(notification);
			}
		});
	}	
}




