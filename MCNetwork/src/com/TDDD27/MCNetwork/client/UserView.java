package com.TDDD27.MCNetwork.client;



import java.util.ArrayList;



import com.TDDD27.MCNetwork.shared.MCUser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

import com.google.gwt.user.client.rpc.InvocationException;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.VerticalPanel;



public class UserView extends VerticalPanel implements ValueChangeHandler{
	private MCNetwork parent;
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel leftPanel = new HorizontalPanel();
	private FlexTable infoTable = new FlexTable();
	private HTML title = new HTML("", true);

	public UserView(Long id, MCNetwork myparent) {
		parent=myparent;
		this.add(title);
		MCUser mcuser = getUser(id);
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
		//HISTORY
				History.addValueChangeHandler(this);
				String initToken = History.getToken();
				if(initToken.length()==0){
					History.newItem("mcuser");
					System.out.println("HistoryToken = 0");
				}
				History.newItem("mcuser");
				History.fireCurrentHistoryState();		
				//HISTORY
	}
	@SuppressWarnings("unchecked")
	public UserView(MCUser mcuser, MCNetwork myparent) {
		parent=myparent;
		this.add(title);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		setUserInfo(mcuser);
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
		//HISTORY
				History.addValueChangeHandler(this);
				String initToken = History.getToken();
				if(initToken.length()==0){
					History.newItem("mcuser");
					System.out.println("HistoryToken = 0");
				}
				History.newItem("mcuser");
				History.fireCurrentHistoryState();		
				//HISTORY
	}

	protected void setUserInfo(MCUser mcuser) {
		System.out.println("<userH1>"+mcuser.getFirstName()+" "+mcuser.getLastName()+"</userH1>");
		title = new HTML("<h1>"+mcuser.getFirstName()+" "+mcuser.getLastName()+"<h1>", true);
		this.add(title);
		infoTable.setWidget(2, 1, new HTML("<bold>E-mail: </bold>"+mcuser.geteMail(), true));
		infoTable.setWidget(3, 1, new HTML("<bold>Bostadsort: </bold>"+mcuser.getRegion() + ", " + mcuser.getCity(), true));
		infoTable.setWidget(4, 1, new HTML("<bold>Antal k&ouml;rda mil: </bold>"+Integer.toString(mcuser.getMilesDriven()), true));	

		leftPanel.add(infoTable);
	}

	private MCUser getUser(Long id){
		final Long userid = id;
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
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
			public void onSuccess(MCUser result) {
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
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("mcuser")){
            parent.centerPanel.clear();
            parent.centerPanel.add(this);
        }
		
		
	}
}