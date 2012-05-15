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
public class MessagePreview extends VerticalPanel {
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private HTML sender= new HTML("", true);
	private HTML resiver= new HTML("", true);
	private HorizontalPanel senderResiever = new HorizontalPanel();
	private HTML msgArea = new HTML("", true);
	private Message message;
	private MCNetwork parent;
	/**
	 * @param privmsgview 
	 * 
	 */
	public MessagePreview(Message msg, final PrivateMessageView privmsgview, MCNetwork myparent) {
		this.addStyleName("MessagePreview");
		this.message=msg;
		this.parent=myparent;
		setSender(msg.getsenderid());
		setReciever(msg.getresieverid());
		senderResiever.add(sender);
		senderResiever.add(resiver);
		this.add(senderResiever);
		int Cindex= msg.getDatum().toString().indexOf('C');
		HTML dateHTML = new HTML("<date>"+msg.getDatum().toString().substring(0, Cindex-1)+"</date>", true);
		this.add(dateHTML);
		//msgArea.setReadOnly(true);
		int endindex=0;
		if(msg.getMessage().length()<=30){
			endindex=msg.getMessage().length();
		}else{
			endindex=30;
		}
		//System.out.println(endindex);
		msgArea.setHTML(msg.getMessage().substring(0, endindex)+"...");
		ClickHandler resieverClickHandler = new ClickHandler() {
			public void onClick(ClickEvent event) {
				MessageView centerwidget = new MessageView(message, parent);
				privmsgview.setUpRightPanel(centerwidget, message.getresieverid(), message.getsenderid());
			}
		};
		msgArea.addClickHandler(resieverClickHandler);
		msgArea.setStyleName("MessagePreview_msgtext");
		msgArea.setWidth("230px");
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
			public void onSuccess(final MCUser result) {
				resiver.setHTML("<bold>  Till: "+ result.getFirstName()+" "+result.getLastName()+"</bold>");
				resiver.setStyleName("MsgPreview_Clickable");
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
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				sender.setHTML("<bold>Fr&aring;n: "+ result.getFirstName()+" "+result.getLastName()+" - </bold>");
				sender.setStyleName("MsgPreview_Clickable");
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
