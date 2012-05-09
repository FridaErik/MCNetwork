package com.TDDD27.MCNetwork.client;


import java.util.List;

import com.TDDD27.MCNetwork.shared.FieldVerifier;
import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MCNetwork implements EntryPoint, ValueChangeHandler {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";



	private static TestServiceAsync testService = GWT.create(TestService.class);

	public DockPanel structurePanel;
	public VerticalPanel centerPanel;
	public VerticalPanel centerwidget = new VerticalPanel();
	private MyMenu menuBar;


	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access the StockWatcher application.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					System.out.println("loggedIn");
					loadMCNetwork();
				} else {
					System.out.println("Not loggedIn");
					loadLogin();
				}
			}
		});
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		//RootPanel.get("stockList").add(loginPanel);
		RootPanel.get().add(loginPanel);
	}

	private void loadMCNetwork() {
		// Set up sign out hyperlink.
	    signOutLink.setHref(loginInfo.getLogoutUrl());
		VerticalPanel northPanel = new VerticalPanel();
		HorizontalPanel topNorth = new HorizontalPanel();
		HTML northwidget1 = new HTML("<MainTitle> MC Network</MainTitle>", true);
		SearchPanel searchpanel = new SearchPanel();
		searchpanel.addStyleName("searchPanel");
		topNorth.add(northwidget1);
		topNorth.add(searchpanel);
		northPanel.add(signOutLink);
		northPanel.add(topNorth);

		centerPanel=new VerticalPanel();
		centerPanel.addStyleName("centerPanel");
		centerPanel.setWidth("900px");
		centerPanel.setHeight("400px");
		//.setPixelSize(214, 200);
		HTML starttext = new HTML("<H1>V&auml;lkommen till MC Network<H1/>", true);
		centerwidget.add(starttext);
		//UserView test = new UserView((long) 44);
		//centerwidget.add(test);
		centerPanel.clear();
		centerPanel.add(centerwidget);
		structurePanel=new DockPanel();
		structurePanel.addStyleName("structurePanel");
		menuBar=new MyMenu(this);
		menuBar.setWidth("898px");
		northPanel.add(menuBar);
		structurePanel.add(northPanel, DockPanel.NORTH);
		structurePanel.add(centerPanel, DockPanel.CENTER);
		VerticalPanel southwidget = new VerticalPanel();
		southwidget.setWidth("900px");
		southwidget.addStyleName("southwidget");
		HTML bottomtext = new HTML("<subtext>Developed by Frik, frik@gmail.com</subtext>", true);
		southwidget.add(bottomtext);
		structurePanel.add(southwidget, DockPanel.SOUTH);
		RootPanel.get().add(structurePanel);
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("start");
		}
		History.fireCurrentHistoryState();

	}


	@Override
	public void onValueChange(ValueChangeEvent event) {
		System.out.println("Current State : " + event.getValue());

		if (event.getValue().equals("start")){
			centerPanel.clear();
			centerPanel.add(centerwidget);
		}
	}







}
