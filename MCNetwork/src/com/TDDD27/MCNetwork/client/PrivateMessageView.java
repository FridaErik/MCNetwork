/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**Klass för att visa privata meddelande, 
 * Skriver ut en lista av MessagePreviews och
 * om man klickar på dem visas hela meddelandet
 * (MessageView) samt möjligheten att svara 
 * (MessgeForm) 
 * @author Frida&Erik
 *
 */
public class PrivateMessageView extends HorizontalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private VerticalPanel msgPanel = new VerticalPanel();
	private ScrollPanel scrollPnl = new ScrollPanel();
	private MCUser loggedInUser=null;
	private LoginInfo loginInfo = null;
	private MCNetwork parent;
	private VerticalPanel leftpanel = new VerticalPanel();
	private VerticalPanel rightpanel = new VerticalPanel();
	private PrivateMessageView privmsgview;

	/**Kontruktor
	 * Skapar GUI så användare kan läsa sin privata meddelande
	 * @param myParent 
	 * 
	 */
	public PrivateMessageView(MCNetwork myParent) {
		privmsgview=this;
		this.add(leftpanel);
		this.add(rightpanel);
		this.addStyleName("PrivateMessageView");
		rightpanel.addStyleName("PrivateMessageView_rightpanel");
		rightpanel.setVisible(false);
		parent=myParent;
		getLoggedInUser();
		scrollPnl.add(msgPanel);
		scrollPnl.setHeight("340px");
		scrollPnl.setWidth("270px");
		leftpanel.add(scrollPnl);
		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("privMsgView");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("privMsgView");
		History.fireCurrentHistoryState();	
		//HISTORY

	}
	/**
	 * Metod för att hämta GoogleID från den inloggade användaren
	 * För att sedan anropa getDBUser som hämtar användaren från 
	 * databasen
	 */
	protected void getLoggedInUser() {
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {

			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("Hittade en inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID());
			}
		});

	}
	/**
	 * Hämtar användaren från databasen med hjälp av ett GoogleID
	 * Anropar sedan metod för att hämta och skriva ut meddelande (Message)
	 * @param userID
	 */
	private void getDBUser(String userID) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska visa sina meddelanden...(Userview)");
			}
			@Override
			public void onSuccess(MCUser result) {
				loggedInUser=result;
				System.out.println("loggedInUser.getId(): "+loggedInUser.getId()+" Listan.size()"+loggedInUser.getMcList().size());
				setMsgPanel();
				//Hämta och skriv ut denna users privata meddelanden i panelen
			}
		};
		testService.getUserByID(userID, callback);
	}
	/**
	 * Metod som hämtar en lista med användarens meddelande
	 * samt anropar intern metoden för att skriva ut meddelanden 
	 * som MessagePreviews
	 */
	private void setMsgPanel() {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<Message>> callback = new AsyncCallback<ArrayList<Message>>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
			@Override
			public void onSuccess(ArrayList<Message> result) {
				if(result!=null){
					System.out.println("Hämtad lista med messages");
					printOutMsg(result);
				}
				else{
					System.out.println("Hittade inga messages");
				}
			}


			private void printOutMsg(ArrayList<Message> result) {
				for( Message a : result){
					MessagePreview msgpreview = new MessagePreview(a, privmsgview, parent);
					msgPanel.add(msgpreview);
				}

			}
		};
		Boolean priv=true;
		testService.getRecievedMessage(loggedInUser.getId(), priv, callback);

	}

	/**
	 * Skapar den högra panelen, när man klickar på en MessagePreview så anropar den klassen
	 * denna metoden för att ladda hela meddelandet och en svarspanel i den högrapanelen.
	 * @param msg Meddelandet som ska skrivas ut
	 */
	public void setUpRightPanel(Message msg) {
		rightpanel.clear();
		MessageView centerwidget = new MessageView(msg, parent);
		rightpanel.add(centerwidget);
		MessageForm replyform = new MessageForm(msg.getresieverid(), msg.getsenderid(), parent, true, msg.getMessage());
		HTML replytitle = new HTML("<bold>Svara</bold>");
		rightpanel.add(replytitle);
		rightpanel.add(replyform);
		rightpanel.setVisible(true);
	}
	/**
	 * Hanterar historiken
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("privMsgView")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}
	
	//Getter & Setter
	public VerticalPanel getRightpanel() {
		return rightpanel;
	}
	public void setRightpanel(VerticalPanel rightpanel) {
		this.rightpanel = rightpanel;
	}
}
