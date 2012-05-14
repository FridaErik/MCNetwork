/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;

/**
 * @author Frida
 *
 */
public class MessageView extends VerticalPanel {
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private HTML sender= new HTML("", true);
	private HTML resiver= new HTML("", true);
	private HorizontalPanel senderResiever = new HorizontalPanel();
	private HTML msgArea = new HTML("", true);
	
	private MCNetwork parent;
	/**
	 * 
	 */
	public MessageView(Message msg, MCNetwork parent) {
		this.parent=parent;
		System.out.println("Skapar message");
		senderResiever.add(sender);
		setSender(msg.getsenderid());
		setReciever(msg.getresieverid());
		senderResiever.add(resiver);
		this.add(senderResiever);
		//msgArea.setReadOnly(true);
		msgArea.setHTML(msg.getMessage());
		msgArea.setStyleName("msgtext");
		msgArea.setWidth("150px");
		System.out.println("Meddelandet �r: "+msg.getMessage());
		this.add(msgArea);
		
		
	}
	private void setReciever(Long resieverid) {
		
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure n�r person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				resiver.setHTML("  Till: "+ result.getFirstName()+" "+result.getLastName());
				resiver.setStyleName("Clickable");
				ClickHandler resieverClickHandler = new ClickHandler() {
					public void onClick(ClickEvent event) {
						SendToUserPage(result);
					}
				};
				resiver.addClickHandler(resieverClickHandler);
			}
		};
		testService.getUser(resieverid, callback);
		
	}
	//TODO
	private void setSender(Long senderid) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure n�r person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				sender.setHTML("Fr&aring;n: "+ result.getFirstName()+" "+result.getLastName()+" - ");
				sender.setStyleName("Clickable");
				ClickHandler senderClickHandler = new ClickHandler() {
					public void onClick(ClickEvent event) {
						SendToUserPage(result);
					}
				};
				sender.addClickHandler(senderClickHandler);
				
			}
			
		};
		testService.getUser(senderid, callback);
		
	}
	protected void SendToUserPage(MCUser mcuser) {
		UserView centerwidget = new UserView(mcuser, parent);
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);

	}

}
