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
	 * Klass för en preview av ett meddelande som visar grundinfo 
	 * om ett Message, avsändare, mottagare, tidpunkt, 
	 * början på meddelandet och möjligheten att radera meddelandet.
	 * Används av klassen PrivateMessageView
	 * @param msg Meddelandet som ska visas
	 * @param privmsgview Den PrivateMessageView som instansen är en del av.
	 * @param myparent MCNetwork huvudklassen, behövs för att kunna påverka 
	 * vad användaren ser på ett enkelt sätt.
	 */
	public MessagePreview(final Message msg, final PrivateMessageView privmsgview, MCNetwork myparent) {
		this.addStyleName("MessagePreview");
		this.message=msg;
		this.parent=myparent;
		setSender(msg.getsenderid());
		setReciever(msg.getresieverid());
		senderResiever.add(sender);
		senderResiever.add(resiver);
		this.add(senderResiever);
		int Cindex= msg.getDatum().toString().indexOf('C');
		HorizontalPanel container1 = new HorizontalPanel();
		HTML dateHTML = new HTML("<date>"+msg.getDatum().toString().substring(0, Cindex-1)+" </date>", true);
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
		container1.add(dateHTML);
		container1.add(deleteHTML);
		this.add(container1);
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
			@Override
			public void onClick(ClickEvent event) {
				
				privmsgview.setUpRightPanel(msg);
			}
		};
		msgArea.addClickHandler(resieverClickHandler);
		msgArea.setStyleName("MessagePreview_msgtext");
		msgArea.setWidth("230px");
		this.add(msgArea);


	}
	/**
	 * Metod för att radera meddelande, anropar servern för att radera från databasen
	 * @param msg meddelandet som ska raderas.
	 */
	protected void deleteMsg(Message msg) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(Boolean result) {
				if(result){
					PrivateMessageView centerwidget = new PrivateMessageView(parent);
					parent.centerPanel.clear();
					parent.centerPanel.add(centerwidget);
				}
			}
		};
		testService.deleteMsg(msg.getId(), callback);

	}
	/**
	 * Hämtar mottagare med hjälp av ett id
	 * @param resieverid
	 */
	private void setReciever(Long resieverid) {

		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				resiver.setHTML("<bold> Till: "+ result.getFirstName()+" "+result.getLastName()+"</bold>");
				resiver.setStyleName("MsgPreview_Clickable");
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
	 * Hämtar sändaren med hjälp av id
	 * @param senderid
	 */
	private void setSender(Long senderid) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(final MCUser result) {
				sender.setHTML("<bold>Fr&aring;n: "+ result.getFirstName()+" "+result.getLastName()+" -</bold>");
				sender.setStyleName("MsgPreview_Clickable");
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
	 * Metod för att skicka användaren till en MCUser:s användarsida.
	 * @param mcuser
	 */
	protected void SendToUserPage(MCUser mcuser) {
		UserView centerwidget = new UserView(mcuser, parent);
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);

	}

}
