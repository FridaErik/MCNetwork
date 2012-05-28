package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class startView extends VerticalPanel implements ValueChangeHandler{
	

	
	private VerticalPanel loginPanel = new VerticalPanel();
	private HTML loginLabel = new HTML("Please sign in to your Google Account to access the StockWatcher application.", true);
	private Anchor signInLink = new Anchor("Sign In");
	private MCNetwork parent;

	public startView(MCNetwork parent, LoginInfo info, MCUser loggedInUser) {
		this.parent=parent;
		HTML starttext = new HTML("<H1>V&auml;lkommen till MC Network <H1/>", true);
		HTML infotext = new HTML("<p>Detta &auml;r ett socialt n&auml;tverk f&ouml;r personer i Sverige som &auml;ger eller &auml;r intresserad av motorcyklar." +
				" Syftet med n&auml;tverket &auml;r att du skall f&aring; kontakt med andra som delar ditt intresse s&aring; ni kan utbyta erfarenheter och tips.</p>", true);
		infotext.setWidth("700px");
		this.add(starttext);
		this.add(infotext);
		if(!info.isLoggedIn()){
			signInLink.setHref(info.getLoginUrl());
			loginPanel.add(loginLabel);
			loginPanel.add(signInLink);
			this.add(loginPanel);
		}
		
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("start");
		}
		History.newItem("start");
		History.fireCurrentHistoryState();
		
		
		/*//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("friendsview");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("friendsview");
		History.fireCurrentHistoryState();		
		//HISTORY*/
	}
	
	public startView(){
		
	}

	@Override
	/**
	 * Hanterar Historiken
	 */
	public void onValueChange(ValueChangeEvent event) {
		System.out.println("Current State : " + event.getValue());

		if (event.getValue().equals("start")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}
	}

}
