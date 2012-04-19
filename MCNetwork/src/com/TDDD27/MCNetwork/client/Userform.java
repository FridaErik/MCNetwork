/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;
import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author Frida
 *
 */
public class Userform extends FormPanel {
	private static TestServiceAsync testService = GWT.create(TestService.class);


	private Grid grid = new Grid(11, 3);
	private FileUpload upload = new FileUpload();
	private TextBox textBoxFnamn = new TextBox();
	private Label textLabelFnamn = new Label("Förnamn");
	private Label errorFnamn = new Label("");
	private TextBox textBoxLnamn = new TextBox();
	private Label textLabelLnamn = new Label("Efternamn");
	private Label errorLnamn = new Label("");
	private TextBox textBoxEmail = new TextBox();
	private Label textLabelEmail = new Label("Email");
	private Label errorEmail = new Label("");
	private TextBox textBoxCity = new TextBox();
	private Label textLabelCity = new Label("Stad");
	private Label errorCity = new Label("");

	private Label textLabelGender = new Label("Kön");
	private RadioButton btn1 = new RadioButton("group", "Man");
	private RadioButton btn2 = new RadioButton("group", "Kvinna");
	private RadioButton btn3 = new RadioButton("group", "Okänd");
	private HorizontalPanel radioBtnPanel = new HorizontalPanel();
	private Label errorGender = new Label("");

	private TextBox textBoxBYear = new TextBox();
	private Label textLabelBYear = new Label("Födelseår");
	private Label errorBYear = new Label("");
	private TextBox textBoxMiles = new TextBox();
	private Label textLabelMiles = new Label("Antal körda mil (ca)");
	private Label errorMiles = new Label("");
	private Boolean submitOK = true;

	private Label fileLabel = new Label("Upload Something");
	private Button submit = new Button("Submit");

	public Userform() {
		super();
		
		textBoxFnamn.setName("textBoxFnamn");
		textBoxLnamn.setName("textBoxLnamn");
		textBoxEmail.setName("textBoxEmail");
		textBoxCity.setName("textBoxCity");
		textBoxBYear.setName("textBoxBYear");


		grid.setWidget(0, 0, textLabelFnamn);
		grid.setWidget(0, 1, textBoxFnamn);
		grid.setWidget(0, 2, errorFnamn);
		grid.setWidget(1, 0, textLabelLnamn);
		grid.setWidget(1, 1, textBoxLnamn);
		grid.setWidget(1, 2, errorLnamn);
		grid.setWidget(2, 0, textLabelEmail);
		grid.setWidget(2, 1, textBoxEmail);
		grid.setWidget(2, 2, errorEmail);
		grid.setWidget(3, 0, textLabelCity);
		grid.setWidget(3, 1, textBoxCity);
		grid.setWidget(3, 2, errorCity);
		grid.setWidget(4, 0, textLabelGender);
		radioBtnPanel.add(btn1);
		radioBtnPanel.add(btn2);
		radioBtnPanel.add(btn3);
		grid.setWidget(4, 1, radioBtnPanel);
		grid.setWidget(4, 2, errorGender);
		grid.setWidget(5, 0, textLabelBYear);
		grid.setWidget(5, 1, textBoxBYear);
		grid.setWidget(5, 2, errorBYear);
		grid.setWidget(6, 0, textLabelMiles);
		grid.setWidget(6, 1, textBoxMiles);
		grid.setWidget(6, 2, errorMiles);
		upload.setName("upload");
		grid.setWidget(9, 0, fileLabel);
		grid.setWidget(9, 1, upload);

		submit.addClickHandler(new ClickHandler() {

			public void onClick(ClickEvent event) {
				submit();	
			}
		});
		grid.setWidget(10, 0, submit);

		//setAction("/someAction");

		setEncoding(FormPanel.ENCODING_MULTIPART);
		setMethod(FormPanel.METHOD_POST);
		setWidget(grid);
		setStyleName("formPanel");

		addSubmitHandler(new SubmitHandler() {

			@Override
			public void onSubmit(SubmitEvent event) {				
				submitOK = true;
				
				String fn = textBoxFnamn.getText();
				checkFName(fn);
				String ln = textBoxLnamn.getText();
				checkLName(ln);
				String em = textBoxEmail.getText();
				checkLEmail(em);
				String c = textBoxCity.getText();
				String g;
				if (btn1.getValue()){
					g="Man";
				}
				else if(btn2.getValue()){
					g="Kvinna";
				}
				else{
					g="okänd";
				}
				int by=0;
				System.out.println("1. by = "+by);
				try {
					by = Integer.parseInt(textBoxBYear.getValue());
					System.out.println("2. by= "+by);
				} catch (NumberFormatException e) {
					errorBYear.setText("Ogiltigt årtal");
					submitOK=false;
				}
				System.out.println("3. by= "+by);
				int m=0;
				try {
					m = Integer.parseInt(textBoxMiles.getValue());
				} catch (NumberFormatException e) {
					errorBYear.setText("Ogiltigt årtal");
					submitOK=false;
				}
				
				

				if(submitOK){
					User user = new User(fn, ln, by, em, c, g, m);
					addUser(user);
				}


			}

		});
	}

	protected void checkLEmail(String em) {
		String[] tokens = em.split("@");
	    if(tokens.length != 2 ||
	    		 tokens[0].isEmpty() || 
	    		 tokens[1].isEmpty() ){
	    	errorEmail.setText("ogiltig email");
	    	submitOK=false;
	    }
		
	}
	protected void checkFName(String text) {
		boolean valid = text.matches("[a-öA-Ö]*");	
		if(!valid || text.equals("") || text.length()<2){
			errorFnamn.setText("Ogiltigt namn");
			submitOK=false;
		}
	}
	protected void checkLName(String text) {
		boolean valid = text.matches("[a-öA-Ö]*");	
		if(!valid || text.equals("") || text.length()<2){
			errorLnamn.setText("Ogiltigt namn");
			submitOK=false;
		}
	}

	private void addUser(User user) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(List<User> result) {
				for( User u : result){
					System.out.println(u.getfirstName());
				}

			}
		};


		// Make the call to server
		testService.storeUser(user, callback);

	}



}
