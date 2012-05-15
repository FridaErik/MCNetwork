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
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessageForm extends FormPanel implements ValueChangeHandler {
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private Grid grid = new Grid(5, 2);

	private TextArea textAreaMeddelande = new TextArea();
	private HTML textHTMLMeddelande = new HTML("<p>Meddelande</p>", true);
	private HTML errorMeddelande = new HTML("", true);
	private HTML goToReciever;
	private MCUser mySender=null;
	private MCUser myResiver=null;
	private Boolean myPrivate;
	private HTML confirmation = new HTML("", true);
	private VerticalPanel frame = new VerticalPanel();



	public MessageForm(final MCUser sender, final MCUser resiever, MCNetwork theparent, final Boolean priv){
		this.mySender=sender;
		this.myResiver=resiever;
		this.myPrivate=priv;
		parent=theparent;
		Boolean large=true;
		setUpGUI(large);
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
	public MessageForm(final long senderid, final Long resiverid, MCNetwork theparent, final Boolean priv){
		
		getResiver(resiverid);
		getSender(senderid);//Fixar det grafiska
		this.myPrivate=priv;
		parent=theparent;

	}



	private void getResiver(Long resiverid) {
		Boolean isResiver=true;
		getUser(resiverid, isResiver);

	}
	private void getSender(long senderid) {
		Boolean isResiver=false;
		getUser(senderid, isResiver);

	}
	private void getUser(Long id, final Boolean isResiver) {
		final Long userid = id;
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure in getUser");
				try {
					throw caught;
				} catch (IncompatibleRemoteServiceException e) {
					// this client is not compatible with the server; cleanup and refresh the 
					// browser
				} catch (InvocationException e) {
					// the call didn't complete cleanly
				} catch (Throwable e) {
					// last resort -- a very unexpected exception
				}
			}

			@Override
			public void onSuccess(MCUser result) {
				if(isResiver){
					myResiver=result;
					System.out.println("Resiver satt");
				}
				else{
					mySender=result;
					System.out.println("Sender satt");
				}
				if(mySender!=null && myResiver!=null){
					System.out.println("Redo för GUI");
					Boolean large=false;
					setUpGUI(large);
				}

			}
		};
		testService.getUser(id, callback);
	}

	private void setUpGUI(Boolean large) {
		HTML title=null;
		if(myPrivate==true && large){
			title = new HTML("<H1>Skicka privat meddelande till "+myResiver.getFirstName()+"</H1>", true);
			frame.add(title);
		}else if(large){
			title = new HTML("<H1>Skriv n&arintg;t p&aring;"+myResiver.getFirstName()+" v&auml;gg</H1>", true);
			frame.add(title);
		}
		goToReciever=setUpReciverLink(myResiver);
		
		if(large){
			textAreaMeddelande.setSize("500px", "200px");
		}else{
			textAreaMeddelande.setSize("350px", "125px");
		}
		confirmation.setVisible(false);
		grid.setWidget(0, 0, confirmation);
		grid.setWidget(1, 1, errorMeddelande);
		grid.setWidget(1, 0, textAreaMeddelande);
		Button sendBtn = new Button("Skicka");
		grid.setWidget(2, 0, sendBtn);
		grid.setWidget(2, 1, goToReciever);
		frame.add(grid);
		this.add(frame);
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
						textAreaMeddelande.setText("");
						confirmation.setHTML("Meddelandet har skickats till "+myResiver.getFirstName());
						confirmation.setStyleName("confirmation");
						confirmation.setVisible(true);
					}
				};
				Message msg= new Message(mySender.getId(), myResiver.getId(), msgString, myPrivate);
				testService.storeMsg(msg, callback);


			}
		});

	}




	private HTML setUpReciverLink(final MCUser resiver) {
		HTML toReturn = new HTML("", true);
		toReturn.setHTML("  G&aring; till "+ resiver.getFirstName()+":s sida >>");
		toReturn.setStyleName("Clickable");
		ClickHandler resieverClickHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				SendToUserPage(resiver);
			}

			private void SendToUserPage(MCUser resiver) {
				UserView centerwidget = new UserView(resiver, parent);
				parent.centerPanel.clear();
				parent.centerPanel.add(centerwidget);

			}
		};
		toReturn.addClickHandler(resieverClickHandler);
		return toReturn;
	}




	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("messageform")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}
}

