package com.TDDD27.MCNetwork.client;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Klass för att skriva och skicka meddelande
 * Används av andra klasser och fristående.
 * @author Frida&Erik
 */
public class MessageForm extends FormPanel implements ValueChangeHandler {
	private static DatabaseServiceAsync testService = GWT.create(DatabaseService.class);

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
	private String prevMsg;


	/**
	 * Skapar ett message form med mottagare, sändare och om meddelande är privat eller ej.
	 * Den har även koppling till MCNetwork för att kunna påverka vad användaren ser på ett enkelt sätt.
	 * @param sender användaren som skickar meddelande
	 * @param resiever användare som tar emot meddelande
	 * @param theparent MCnetwork (huvudklassen)
	 * @param priv Om meddelande ska vara privat eller inte
	 */
	public MessageForm(final MCUser sender, final MCUser resiever, MCNetwork theparent, final Boolean priv, String prevMsg){
		this.mySender=sender;
		this.myResiver=resiever;
		this.myPrivate=priv;
		parent=theparent;
		this.prevMsg=prevMsg;
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
	/**
	 * Skapar ett message form med mottagare, sändare och om meddelande är privat eller ej.
	 * Den har även koppling till MCNetwork för att kunna påverka vad användaren ser på ett enkelt sätt.
	 * @param senderid id för användaren som skickar meddelande
	 * @param resieverid id för användare som tar emot meddelande
	 * @param theparent MCnetwork (huvudklassen)
	 * @param priv Om meddelande ska vara privat eller inte
	 */
	public MessageForm(final long senderid, final Long resiverid, MCNetwork theparent, final Boolean priv, String prevMsg){

		getResiver(resiverid);
		getSender(senderid);//Fixar det grafiska
		this.myPrivate=priv;
		parent=theparent;
		this.prevMsg=prevMsg;

	}


	/**
	 * Sätter användaren som ska ta emot meddelandet 
	 * om man bara hade tillgång till id
	 * @param resiverid
	 */
	private void getResiver(Long resiverid) {
		Boolean isResiver=true;
		getUser(resiverid, isResiver);
	}
	/**
	 * Sätter användaren som ska skicka meddelandet 
	 * om man bara hade tillgång till id 
	 * @param senderid
	 */
	private void getSender(long senderid) {
		Boolean isResiver=false;
		getUser(senderid, isResiver);

	}
	/**
	 * Hämtar en användare och sätter den till antingen användare eller mottagare
	 * @param id
	 * @param isResiver om användaren är mottagare av meddelandet
	 * så sätts isResiver till true.
	 */
	private void getUser(Long id, final Boolean isResiver) {
		final Long userid = id;
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
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
	/**
	 * Skapar det grafiska som krävs för att ta in den data som behövs för att skicka meddelande
	 * @param large Om det ska vara en stor eller liten message form, beror på var den används. 
	 * Om true så får den en storlek lämplig för att användas ensam och false skapar en som
	 * är lagom stor för att användas på en sida med andra GUI:s
	 */
	private void setUpGUI(Boolean large) {
		HTML title=null;
		if(myPrivate==true && large){
			title = new HTML("<H1>Skicka privat meddelande till "+myResiver.getFirstName()+"</H1>", true);
			frame.add(title);
		}else if(large){
			title = new HTML("<H1>Skriv n&aring;t p&aring; "+myResiver.getFirstName()+"s v&auml;gg</H1>", true);
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
		
		//Knappen
		SimplePanel sendBtn = new SimplePanel();
		HTML sendMsgBtn = new HTML("Skicka", true);
		ClickHandler sendMsgClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String msgString = textAreaMeddelande.getText();
				System.out.println(msgString);

				if (testService == null) {
					testService = GWT.create(DatabaseService.class);
				}
				// Set up the callback object.
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					@Override
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
		};
		sendMsgBtn.addClickHandler(sendMsgClickHandler);
		sendMsgBtn.setWidth("68px");
		sendMsgBtn.setHeight("18px");
		sendBtn.add(sendMsgBtn);
		sendBtn.setWidth("70px");
		sendBtn.addStyleName("GreenBtn");
		sendBtn.setHeight("20px");
		grid.setWidget(2, 0, sendBtn);
		
		
		grid.setWidget(2, 1, goToReciever);
		if(prevMsg!=null){
			textAreaMeddelande.setText("\n \n"+'"'+prevMsg+'"');
		}
		frame.add(grid);
		this.add(frame);

	}



	/**
	 * Skapar en länk till användarens som man ska skicka till.
	 * @param resiver användaren som ska länkas till
	 * @return Returnerar en HTML som funkar som en länk 
	 */
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


	/**
	 * Hanterar historiken
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("messageform")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}
}

