package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

public class MessageForm extends FormPanel implements ValueChangeHandler {
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private Grid grid = new Grid(5, 2);

	private TextArea textAreaMeddelande = new TextArea();
	private HTML textHTMLMeddelande = new HTML("<p>Meddelande</p>", true);
	private HTML errorMeddelande = new HTML("", true);



	public MessageForm(final MCUser sender, final MCUser resiever, MCNetwork theparent, final Boolean priv){
		parent=theparent;
		HTML sender_reciever = new HTML("<MsgH1>Fr&aring;n "+sender.getFirstName()+ " "+ sender.getLastName()+" till "+resiever.getFirstName()+ " "+ resiever.getLastName()+"</MsgH1>");
		textAreaMeddelande.setSize("600px", "250px");
		grid.setWidget(0, 0, sender_reciever);
		grid.setWidget(1, 0, textHTMLMeddelande);
		grid.setWidget(1, 1, errorMeddelande);
		grid.setWidget(2, 0, textAreaMeddelande);
		Button sendBtn = new Button("Skicka");
		grid.setWidget(3, 0, sendBtn);
		this.add(grid);
		sendBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				String msgString = textAreaMeddelande.getText();
				System.out.println(msgString);

				if (testService == null) {
					testService = GWT.create(TestService.class);
				}
				// Set up the callback object.
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						System.out.println("failure när person ska skapa meddelande...(Userview)");
					}
					@Override
					public void onSuccess(Boolean result) {

					}
				};
				Message msg= new Message(sender.getId(), resiever.getId(), msgString, priv);
				testService.storeMsg(msg, callback);


			}
		});
		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("messageform");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("messageform");
		History.fireCurrentHistoryState();		
		//HISTORY
	}




	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("messageform")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}
}

