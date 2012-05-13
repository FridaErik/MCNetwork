/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

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
	/**
	 * 
	 */
	public MessageView(Message msg) {
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
		System.out.println("Meddelandet är: "+msg.getMessage());
		this.add(msgArea);
		
		
	}
	private void setReciever(Long resieverid) {
		
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(MCUser result) {
				resiver.setHTML(" Till: "+ result.getFirstName()+" "+result.getLastName());
				
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
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(MCUser result) {
				sender.setHTML("Fr&aring;n: "+ result.getFirstName()+" "+result.getLastName()+" ");
				
			}
		};
		testService.getUser(senderid, callback);
		
	}

}
