package com.TDDD27.MCNetwork.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;


public class MyMenu extends MenuBar {
	public static MCNetwork myParent;

	public MyMenu() {
		// TODO Auto-generated constructor stub
	}
	public MyMenu(MCNetwork parent, boolean loggedIn) {
		myParent=parent;
		MenuItem hemMI = new MenuItem("Startsida", startCmd);
		this.addItem(hemMI);
		if(loggedIn){
			System.out.println("Menyn har registrerat att en användare är inloggad");
			MenuItem regMI = new MenuItem("Uppdatera uppgifter", registerUserCmd);
			this.addItem(regMI);
			MenuItem msgMI = new MenuItem("Mina meddelanden", meddelandeCmd);
			this.addItem(msgMI);
			MenuItem mcMI = new MenuItem("Mina motorcyklar", motorcyklarCmd);
			this.addItem(msgMI);
		}
		MenuItem filterMI = new MenuItem("Hitta andra", filterCmd);
		this.addItem(filterMI);
		
	}

	
	static Command registerUserCmd = new Command()
	{
		public void execute()
		{
			System.out.println("Klick i menyn");
			Userform centerwidget = new Userform(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	static Command meddelandeCmd = new Command()
	{
		public void execute()
		{
			PrivateMessageView centerwidget = new PrivateMessageView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	static Command startCmd = new Command()
	{
		public void execute()
		{
			VerticalPanel centerwidget = new VerticalPanel();
			HTML starttext = new HTML("<H1>V&auml;lkommen till MC Network<H1/>", true);
			centerwidget.add(starttext);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	//hej
	static Command filterCmd = new Command()
	{
		public void execute()
		{
			FilterForm centerwidget = new FilterForm(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	static Command motorcyklarCmd = new Command()
	{
		public void execute()
		{
			MotorcyklarView centerwidget = new MotorcyklarView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};

	public MyMenu(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}

	public MyMenu(boolean vertical, Resources resources) {
		super(vertical, resources);
		// TODO Auto-generated constructor stub
	}

}
