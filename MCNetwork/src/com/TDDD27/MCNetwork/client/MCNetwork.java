package com.TDDD27.MCNetwork.client;


import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * Huvudklassen som "håller" i alla andra klasser
 */
public class MCNetwork implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";



	private static TestServiceAsync testService = GWT.create(TestService.class);
	private startView start = new startView();
	public DockPanel structurePanel;
	public VerticalPanel centerPanel;
	private MyMenu menuBar;
	private LoginInfo loginInfo = null;
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private MCUser loggedInUser=null;

	public MCUser getLoggedInUser() {
		return loggedInUser;
	}
	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		RootPanel.get().clear();
		// Gör först en loginkontroll innan det grafiska laddas.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
			}

			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				getDBUser(result);
				//loadMCNetwork();
			}
		});
	}
	/**
	 * Hämtar en användare från databasen med
	 * hjälp av ett GoogleID
	 * @param userID GoogleID 
	 * @return 
	 */
	private void getDBUser(LoginInfo info) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(MCUser result) {
				loggedInUser=result;
				loadMCNetwork();
			}
		};


		testService.getUserByID(info.getUserID(), callback);
	}
	/**
	 * Tar fram det grafiska för huvudklassen MCNetwork som funkar som en omgivande 
	 * ram för övriga grafiska klasser i projektet.
	 */
	private void loadMCNetwork() {
		
		
		
		VerticalPanel northPanel = new VerticalPanel();
		HorizontalPanel topNorth = new HorizontalPanel();
		HTML northwidget1 = new HTML("<MainTitle> MC Network</MainTitle>", true);
		SearchPanel searchpanel = new SearchPanel();
		searchpanel.addStyleName("searchPanel");
		topNorth.add(northwidget1);
		//topNorth.add(searchpanel);
		if(loginInfo.isLoggedIn()){
			signOutLink.setHref(loginInfo.getLogoutUrl());
			northPanel.add(signOutLink);	
		}
		northPanel.add(topNorth);

		centerPanel=new VerticalPanel();
		centerPanel.addStyleName("centerPanel");
		centerPanel.setWidth("900px");
		centerPanel.setHeight("400px");
		centerPanel.clear();
		start= new startView(this, loginInfo, loggedInUser);
		//TODO
		centerPanel.add(start);
		structurePanel=new DockPanel();
		structurePanel.addStyleName("structurePanel");
		menuBar=new MyMenu(this, loginInfo.isLoggedIn());
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
		

	}
	
	//Getter & Setter
	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	public void setLoggedInUser(MCUser result) {
		loggedInUser=result;
		
	}


	
	







}
