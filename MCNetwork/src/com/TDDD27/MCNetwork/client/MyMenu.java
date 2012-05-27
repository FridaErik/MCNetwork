package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
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
			if(parent.getLoggedInUser()!=null){
				MenuItem myPageMI = new MenuItem("Min sida", myPageCmd);
				this.addItem(myPageMI);
				MenuItem msgMI = new MenuItem("Mina meddelanden", meddelandeCmd);
				this.addItem(msgMI);
				MenuItem mcMI = new MenuItem("Mina motorcyklar", motorcyklarCmd);
				this.addItem(mcMI);
				MenuItem frMI = new MenuItem("Mina kompisar", friendsCmd);
				this.addItem(frMI);
			}
			else{
				MenuItem regMI = new MenuItem("Registrera dig", registerCmd);
				this.addItem(regMI);
			}
			
		}
		MenuItem filterMI = new MenuItem("Hitta andra", filterCmd);
		this.addItem(filterMI);
		
	}
	static Command registerCmd = new Command()
	{
		@Override
		public void execute()
		{
			EditUserView centerwidget = new EditUserView(myParent);
			myParent.centerPanel.clear();
			myParent.centerPanel.add(centerwidget);
		}
	};

	/**
	 * Metod för att öppna ett Userform
	 */
	static Command myPageCmd = new Command()
	{
		@Override
		public void execute()
		{
			TestServiceAsync testService = GWT.create(TestService.class);
			final Long userid = myParent.getLoggedInUser().getId();
			AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
				@Override
				public void onFailure(Throwable caught) {
				}
				@Override
				public void onSuccess(MCUser result) {
					myParent.setLoggedInUser(result);
					UserView centerwidget = new UserView(myParent.getLoggedInUser(), myParent);
					myParent.centerPanel.clear();
					myParent.centerPanel.add(centerwidget);
				}
			};
			testService.getUser(userid, callback);
			
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
			startView centerwidget = new startView(myParent, myParent.getLoginInfo() ,myParent.getLoggedInUser());
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
