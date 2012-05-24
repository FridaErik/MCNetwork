package com.TDDD27.MCNetwork.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Klass f�r menyn p� sidan
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
			System.out.println("Menyn har registrerat att en anv�ndare �r inloggad");
			MenuItem regMI = new MenuItem("Uppdatera uppgifter", registerUserCmd);
			this.addItem(regMI);
			MenuItem msgMI = new MenuItem("Mina meddelanden", meddelandeCmd);
			this.addItem(msgMI);
			MenuItem mcMI = new MenuItem("Mina motorcyklar", motorcyklarCmd);
			this.addItem(mcMI);
			MenuItem frMI = new MenuItem("Mina v�nner", friendsCmd);
			this.addItem(frMI);
		}
		MenuItem filterMI = new MenuItem("Hitta andra", filterCmd);
		this.addItem(filterMI);
		
	}

	/**
	 * Metod f�r att �ppna ett Userform
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
	 * Metod f�r att �ppna en PrivateMessageView
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
	 * Metod f�r att g� tillbaka till startsidan
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
	 * Metod f�r att �ppna ett filterform f�r att s�ka efter andra anv�ndare
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
	 * Metod f�r att �ppna MotorcyklarView
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
