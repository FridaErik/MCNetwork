package com.TDDD27.MCNetwork.client;



import java.util.ArrayList;



import com.TDDD27.MCNetwork.shared.User;

import com.google.gwt.core.client.GWT;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

import com.google.gwt.user.client.rpc.InvocationException;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.VerticalPanel;



public class UserView extends VerticalPanel{

	private static TestServiceAsync testService = GWT.create(TestService.class);
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	private FlexTable infoTable = new FlexTable();



	public UserView(Long id) {


		User user = getUser(id);
		//System.out.println(user.geteMail());
		//infoTable = setUserInfo(user);
		
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		bottomPanel.add(addBtn);
		bottomPanel.add(sendPriMsgBtn);
		bottomPanel.add(sendPubMsgBtn);
		this.add(bottomPanel);
		
	}
	public UserView(User user) {
		//User user = getUser(id);
		//System.out.println(user.geteMail());
		setUserInfo(user);
		this.add(bottomPanel);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		bottomPanel.add(addBtn);
		bottomPanel.add(sendPriMsgBtn);
		bottomPanel.add(sendPubMsgBtn);
		this.add(bottomPanel);
	}



	protected void setUserInfo(User user) {

		
		infoTable.setWidget(1, 1, new HTML("Namn: ", true));
		infoTable.setWidget(1, 2, new HTML(user.getFirstName() + " " + user.getLastName(), true));
		infoTable.setWidget(2, 1, new HTML("E-mail: ", true));
		infoTable.setWidget(2, 2, new HTML(user.geteMail(), true));
		infoTable.setWidget(3, 1, new HTML("Bostadsort: ", true));
		infoTable.setWidget(3, 2, new HTML(user.getRegion() + ", " + user.getCity(), true));
		infoTable.setWidget(4, 1, new HTML("Antal k&ouml;rda mil: ", true));
		infoTable.setWidget(4, 2, new HTML(Integer.toString(user.getMilesDriven()), true));
		
		

		//TODO Skriv ut hela tabellen





		rightPanel.add(infoTable);





	}





	private User getUser(Long id){


		final Long userid = id;





		AsyncCallback<User> callback = new AsyncCallback<User>() {


			public void onFailure(Throwable caught) {

				System.out.println("failure in getUser");

				try {

					throw caught;

				} catch (IncompatibleRemoteServiceException e) {

					// this client is not compatible with the server; cleanup and refresh the 

					// browser

				} catch (InvocationException e) {

					// the call didn't complete cleanly

				} catch (Throwable e) {

					// last resort -- a very unexpected exception

				}

			}



			@Override

			public void onSuccess(User result) {

				System.out.println("succes in get user");

				if(!(result.getFirstName()==null)){

					//User user = new User();

					System.out.println(result.getFirstName());

					setUserInfo(result);

					//user = result; //Det borde ju vara såhär enkelt kan man tycka...

				}

				else{


					setErrorMessage(userid);


				}



			}

		};



		testService.getUser(id, callback);


		//System.out.println(user.getCity());


		return null;
	}

	private void setErrorMessage(Long userid) {

		HTML errorLabel = new HTML("Ooops! N&aring;got gick fel.", true);

		//errorLabel.setStyleName("errorLabel");

		add(errorLabel);


	}
}