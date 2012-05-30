/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**Klass för att visa ett meddelande
 * Anropas av MessagePreview och liknar den på många sätt
 * men MessageView visar hela meddelandet inte bara en del.
 * @author Frida&Erik
 *
 */
public class MessageView extends VerticalPanel {
	private static DatabaseServiceAsync testService = GWT.create(DatabaseService.class);
	private HTML sender= new HTML("", true);
	private HTML resiver= new HTML("", true);
	private HorizontalPanel senderResiever = new HorizontalPanel();
	private HTML msgArea = new HTML("", true);
	private MCNetwork parent;
	
	/**
	 * Konstruktur som skriver ut meddelande, sändare, mottagare 
	 * Har även radera funktionalitet
	 */
	public MessageView(final Message msg, final MCNetwork parent) {
		this.parent=parent;
		senderResiever.add(sender);
		setSender(msg.getsenderid());
		setReciever(msg.getresieverid());
		senderResiever.add(resiver);
		this.add(senderResiever);
		//Kontroll så att en användare bara kan radera sina meddelande som de fått eller skickat
		if(parent.getLoggedInUser().getId().equals(msg.getresieverid()) || parent.getLoggedInUser().getId().equals(msg.getsenderid())){
			HTML deleteHTML = new HTML("  Radera", true);
		deleteHTML.addStyleName("MsgPreview_Clickable");
		ClickHandler deleteClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				parent.centerPanel.clear();
				deleteMsg(msg);
			}
		};
		deleteHTML.addClickHandler(deleteClickHandler);
		this.add(deleteHTML);
		}
		msgArea.setHTML(msg.getMessage());
		msgArea.setStyleName("msgtext");
		if(msg.getPriv()){
			msgArea.setWidth("470px");
		}
		else{
			msgArea.setWidth("245px");
		}
		this.add(msgArea);
	}
	
	/**
	 * Hämtar en användare med ett userid och sätter 
	 * Mottagaren för meddelandet.
	 * @param resieverid
	 */
	private void setReciever(Long resieverid) {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				resiver.setHTML("  Till: "+ result.getFirstName()+" "+result.getLastName());
				resiver.setStyleName("Clickable");
				ClickHandler resieverClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						SendToUserPage(result);
					}
				};
				resiver.addClickHandler(resieverClickHandler);
			}
		};
		testService.getUser(resieverid, callback);
	}
	/**
	 * Hämtar en användare med ett userid och sätter 
	 * Sändare för meddelandet.
	 * @param senderid
	 */
	private void setSender(Long senderid) {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				sender.setHTML("Fr&aring;n: "+ result.getFirstName()+" "+result.getLastName()+" - ");
				sender.setStyleName("Clickable");
				ClickHandler senderClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						SendToUserPage(result);
					}
				};
				sender.addClickHandler(senderClickHandler);
				
			}
			
		};
		testService.getUser(senderid, callback);
		
	}
	/**
	 * Tar bort det som visas i centerPanel i huvudklassen 
	 * och visare istället användarens (mcuser) sida.
	 * @param mcuser
	 */
	protected void SendToUserPage(MCUser mcuser) {
		UserView centerwidget = new UserView(mcuser, parent);
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);

	}
	/**
	 * Metod för att radera ett meddelande ut databasen
	 * @param msg
	 */
	protected void deleteMsg(final Message msg) {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
		}
		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					if(msg.getPriv()==true){
						PrivateMessageView centerwidget = new PrivateMessageView(parent);
						parent.centerPanel.clear();
						parent.centerPanel.add(centerwidget);
					}
					else{
						UserView centerwidget = new UserView(msg.getresieverid(), parent);
						parent.centerPanel.clear();
						parent.centerPanel.add(centerwidget);
					}
				}
			}
		};
		testService.deleteMsg(msg.getId(), callback);
	}
}
