package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Startsidan, det första som 
 * syns när man kommer in.
 * @author Frida&Erik
 *
 */
public class startView extends VerticalPanel implements ValueChangeHandler{
	

	
	private HorizontalPanel loginPanel = new HorizontalPanel();
	private HTML loginLabel = new HTML("F&oumlr att f&aring; tillg&aring;ng till sidan m&aring;ste du logga in med ett Google-konto h&auml;r: ", true);
	private Anchor signInLink = new Anchor("Logga in");
	private MCNetwork parent;

	/**
	 * Kontsruktor
	 * @param parent MCNetwork huvudfilen
	 * @param info Info om någon är inloggad eller ej
	 * @param loggedInUser Personen som är inloggad, null om ingen är det
	 */
	public startView(MCNetwork parent, LoginInfo info, MCUser loggedInUser) {
		this.addStyleName("startView");
		this.parent=parent;
		HTML starttext = new HTML("<H1>V&auml;lkommen till MC Network <H1/>", true);
		starttext.addStyleName("start_center");
		HTML infotext = new HTML("<p>Detta &auml;r ett socialt n&auml;tverk f&ouml;r personer i Sverige som &auml;ger eller &auml;r intresserad av motorcyklar." +
				" Syftet med n&auml;tverket &auml;r att du skall f&aring; kontakt med andra som delar ditt intresse s&aring; ni kan utbyta erfarenheter och tips.</p>", true);
		infotext.addStyleName("start_center");
		infotext.setWidth("700px");
		this.add(starttext);
		this.add(infotext);
		Image img = new Image("images/startbild-mc.jpg");
		img.setStyleName("start_img");
		img.setWidth("300px");
		this.add(img);
		if(!info.isLoggedIn()){
			signInLink.setHref(info.getLoginUrl());
			loginPanel.addStyleName("start_loginPanel");
			loginPanel.add(loginLabel);
			loginPanel.add(signInLink);
			signInLink.setWidth("120px");
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
	
	/**
	 * Tom konstruktor
	 */
	public startView(){
		
	}

	/**
	 * Hanterar Historiken
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		System.out.println("Current State : " + event.getValue());

		if (event.getValue().equals("start")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}
	}

}
