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
	private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel leftPanel = new HorizontalPanel();
	private FlexTable infoTable = new FlexTable();
	private HTML title = new HTML("", true);

	public UserView(Long id) {
		this.add(title);
		User user = getUser(id);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		this.add(bottomPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		bottomPanel.add(addBtn);
		bottomPanel.add(sendPriMsgBtn);
		bottomPanel.add(sendPubMsgBtn);
		this.add(bottomPanel);
		topPanel.addStyleName("topPanel");
		bottomPanel.addStyleName("bottomPanel");
		leftPanel.addStyleName("leftPanel");
		rightPanel.addStyleName("rightPanel");
		
	}
	public UserView(User user) {
		this.add(title);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		setUserInfo(user);
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		this.add(bottomPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		bottomPanel.add(addBtn);
		bottomPanel.add(sendPriMsgBtn);
		bottomPanel.add(sendPubMsgBtn);
		this.add(bottomPanel);
		topPanel.addStyleName("topPanel");
		bottomPanel.addStyleName("bottomPanel");
		leftPanel.addStyleName("leftPanel");
		rightPanel.addStyleName("rightPanel");
	}

	protected void setUserInfo(User user) {
		System.out.println("<userH1>"+user.getFirstName()+" "+user.getLastName()+"</userH1>");
		title = new HTML("<h1>"+user.getFirstName()+" "+user.getLastName()+"<h1>", true);
		this.add(title);
		infoTable.setWidget(2, 1, new HTML("<bold>E-mail: </bold>"+user.geteMail(), true));
		infoTable.setWidget(3, 1, new HTML("<bold>Bostadsort: </bold>"+user.getRegion() + ", " + user.getCity(), true));
		infoTable.setWidget(4, 1, new HTML("<bold>Antal k&ouml;rda mil: </bold>"+Integer.toString(user.getMilesDriven()), true));	

		leftPanel.add(infoTable);
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
					System.out.println(result.getFirstName());
					setUserInfo(result);
				}
				else{
					setErrorMessage(userid);
				}
			}
		};
		testService.getUser(id, callback);
		return null;
	}

	private void setErrorMessage(Long userid) {
		HTML errorLabel = new HTML("Ooops! N&aring;got gick fel.", true);
		add(errorLabel);
	}
}