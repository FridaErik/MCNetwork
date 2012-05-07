package com.TDDD27.MCNetwork.client;


import java.util.List;

import com.TDDD27.MCNetwork.shared.FieldVerifier;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
public class MCNetwork implements EntryPoint {
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
	private MyMenu menuBar;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		VerticalPanel northPanel = new VerticalPanel();
		HorizontalPanel topNorth = new HorizontalPanel();
		HTML northwidget1 = new HTML("<MainTitle> MC Network</MainTitle>", true);
		SearchPanel searchpanel = new SearchPanel();
		searchpanel.addStyleName("searchPanel");
		topNorth.add(northwidget1);
		topNorth.add(searchpanel);
		northPanel.add(topNorth);
		VerticalPanel centerwidget = new VerticalPanel();
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
	}
	
	
	
	
	

	
}
