package com.TDDD27.MCNetwork.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;


/* --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS-- 

//OBS alla widgets som skall läggas till i mitten på structruePanel skall heta centerwidget

 --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS--  --OBS-- */

public class MyMenu extends MenuBar {
	public static MCNetwork myParent;

	public MyMenu() {
		// TODO Auto-generated constructor stub
	}
	public MyMenu(MCNetwork parent) {
		myParent=parent;
		MenuItem hemMI = new MenuItem("Startsida", startCmd);
		this.addItem(hemMI);
		MenuItem regMI = new MenuItem("Registrering", registerUserCmd);
		this.addItem(regMI);
		
	}

	
	static Command registerUserCmd = new Command()
	{
		public void execute()
		{
			Userform centerwidget = new Userform();
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

	public MyMenu(Resources resources) {
		super(resources);
		// TODO Auto-generated constructor stub
	}

	public MyMenu(boolean vertical, Resources resources) {
		super(vertical, resources);
		// TODO Auto-generated constructor stub
	}

}
