package com.TDDD27.MCNetwork.client;



import java.util.ArrayList;



import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;

import com.google.gwt.user.client.rpc.InvocationException;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;

import com.google.gwt.user.client.ui.HTML;

import com.google.gwt.user.client.ui.HorizontalPanel;

import com.google.gwt.user.client.ui.VerticalPanel;



public class UserView extends VerticalPanel implements ValueChangeHandler{
	private MCNetwork parent;
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	//private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel leftPanel = new HorizontalPanel();
	private FlexTable infoTable = new FlexTable();
	private HTML title = new HTML("", true);
	private MCUser viewUser;
	private LoginInfo loginInfo = null;
	private VerticalPanel msgPanel = new VerticalPanel();
	private ScrollPanel scrollPnl = new ScrollPanel();

	public UserView(Long id, MCNetwork myparent) {
		parent=myparent;
		this.add(title);
		MCUser mcuser = getUser(id);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		topPanel.add(leftPanel);
		topPanel.add(leftPanel);
		setMsgPanel();
		msgPanel.setStyleName("UserViewMsgPanel");
		scrollPnl.add(msgPanel);
		rightPanel.add(scrollPnl);
		topPanel.add(rightPanel);
		this.add(bottomPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		sendPriMsgBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Boolean priv=true;
				createMsgForm(priv);
			}
		});
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		sendPubMsgBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Boolean priv=false;
				createMsgForm(priv);
			}
		});
		
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
	public UserView(MCUser mcuser, final MCNetwork myparent) {
		viewUser=mcuser;
		parent=myparent;
		this.add(title);
		Image img = new Image("images/question-mark-icon_21147438.jpg");
		img.setHeight("100px");
		leftPanel.add(img);
		setUserInfo(viewUser);
		topPanel.add(leftPanel);
		HTML msgTitle = new HTML("<h2>Meddelande: </h2>", true);
		msgPanel.add(msgTitle);
		msgPanel.setWidth("250px");
		//scrollPnl.setSize("260px", "400px");
		setMsgPanel();
		msgPanel.setStyleName("UserViewMsgPanel");
		
		
		scrollPnl.add(msgPanel);
		rightPanel.add(scrollPnl);
		topPanel.add(rightPanel);
		this.add(bottomPanel);
		this.add(topPanel);
		Button addBtn = new Button("Bli kompis");
		Button sendPriMsgBtn = new Button("Skicka meddelande");
		sendPriMsgBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Boolean priv=true;
				createMsgForm(priv);
			}
		});
		Button sendPubMsgBtn = new Button("Skriv p&aring; v&auml;ggen");
		sendPubMsgBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				Boolean priv=false;
				createMsgForm(priv);
			}
		});
		
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
	private void setMsgPanel() {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<Message>> callback = new AsyncCallback<ArrayList<Message>>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			public void onSuccess(ArrayList<Message> result) {
				if(result!=null){
					System.out.println("Hämtad lista med messages");
					printOutMsg(result);
				}
				else{
					System.out.println("Hittade inga messages");
				}
			}


			private void printOutMsg(ArrayList<Message> result) {
				for( Message a : result){
					MessageView msgview = new MessageView(a, parent);
					msgPanel.add(msgview);
				}

			}
		};
		Boolean priv=false;
		testService.getRecievedMessage(viewUser.getId(), priv, callback);

	}
	protected void createMsgForm(final Boolean priv) {
		System.out.println("steg 1");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {

			}
			public void onSuccess(LoginInfo result) {
				System.out.println("Hittade en inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID(), priv);
			}
		});

	}
	private void getDBUser(String userID, final Boolean priv) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<MCUser>> callback = new AsyncCallback<ArrayList<MCUser>>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(ArrayList<MCUser> result) {
				MessageForm msgform = new MessageForm( result.get(0), viewUser, parent, priv);
				parent.centerPanel.clear();
				parent.centerPanel.add(msgform);
			}
		};
		testService.getUserByID(userID, callback);
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