package com.TDDD27.MCNetwork.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Klass för menyn på sidan
 * @author Frida&Erik
 *
 */
public class MyMenu extends MenuBar {
	public static MCNetwork myParent;

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
			this.addItem(mcMI);
			MenuItem frMI = new MenuItem("Mina vänner", friendsCmd);
			this.addItem(frMI);
		}
		MenuItem filterMI = new MenuItem("Hitta andra", filterCmd);
		this.addItem(filterMI);
		
	}

	/**
	 * Metod för att öppna ett Userform
	 */
	static Command registerUserCmd = new Command()
	{
		@Override
		public void execute()
		{
			System.out.println("Klick i menyn");
			Userform centerwidget = new Userform(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	/**
	 * Metod för att öppna en PrivateMessageView
	 */
	static Command meddelandeCmd = new Command()
	{
		@Override
		public void execute()
		{
			PrivateMessageView centerwidget = new PrivateMessageView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	/**
	 * Metod för att gå tillbaka till startsidan
	 */
	static Command startCmd = new Command()
	{
		@Override
		public void execute()
		{
			VerticalPanel centerwidget = new VerticalPanel();
			HTML starttext = new HTML("<H1>V&auml;lkommen till MC Network<H1/>", true);
			centerwidget.add(starttext);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	/**
	 * Metod för att öppna ett filterform för att söka efter andra användare
	 */
	static Command filterCmd = new Command()
	{
		@Override
		public void execute()
		{
			FilterForm centerwidget = new FilterForm(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	/**
	 * Metod för att öppna MotorcyklarView
	 */
	static Command motorcyklarCmd = new Command()
	{
		@Override
		public void execute()
		{
			MotorcyklarView centerwidget = new MotorcyklarView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};
	
	static Command friendsCmd = new Command()
	{
		@Override
		public void execute()
		{
			FriendsView centerwidget = new FriendsView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};



}
