package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;



public class EditUserView extends HorizontalPanel implements ValueChangeHandler{

	private VerticalPanel leftPanel=new VerticalPanel();
	private VerticalPanel rightPanel=new VerticalPanel();
	private MCNetwork parent;

	public EditUserView(MCNetwork myparent){
		parent=myparent;
		HTML titleLeft = new HTML("<H1>Personuppgifter</H1>", true);
		leftPanel.add(titleLeft);
		leftPanel.add(new Userform(parent));
		HTML titleRight = new HTML("<H1>Bild</H1>", true);
		rightPanel.add(titleRight);
		rightPanel.add(new ImageUploadGUI(parent.getLoggedInUser().getId()));
		leftPanel.addStyleName("editUserViewLeft");
		rightPanel.addStyleName("editUserViewRight");
		
		
		this.add(leftPanel);
		this.add(rightPanel);
		
		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("update");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("update");
		History.fireCurrentHistoryState();		
		//HISTORY
	}
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("registration")){
			System.out.println("HISTORY SKIT!!!");
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}


}
