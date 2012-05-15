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

/**
 * @author Frida
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

	/**
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
		Boolean priv=true;
		getLoggedInUser(priv);
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
	private void setMsgPanel() {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<Message>> callback = new AsyncCallback<ArrayList<Message>>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska skapa meddelande...(Userview)");
			}
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
	protected void getLoggedInUser(final Boolean priv) {
		System.out.println("steg 1");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {

			}
			public void onSuccess(LoginInfo result) {
				System.out.println("Hittade en inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID(), priv);
			}
		});

	}

	private void getDBUser(String userID, final Boolean priv) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<MCUser>> callback = new AsyncCallback<ArrayList<MCUser>>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure när person ska visa sina meddelanden...(Userview)");
			}
			@Override
			public void onSuccess(ArrayList<MCUser> result) {
				loggedInUser=result.get(0);
				setMsgPanel();
				//Hämta och skriv ut denna users privata meddelanden i panelen
			}
		};
		testService.getUserByID(userID, callback);
	}
	public VerticalPanel getRightpanel() {
		return rightpanel;
	}
	public void setRightpanel(VerticalPanel rightpanel) {
		this.rightpanel = rightpanel;
	}
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("privMsgView")){
            parent.centerPanel.clear();
            parent.centerPanel.add(this);
        }

	}
	public void setUpRightPanel(MessageView centerwidget, Long resiverid, Long senderid) {
		rightpanel.clear();
		rightpanel.add(centerwidget);
		MessageForm replyform = new MessageForm(resiverid, senderid, parent, true);
		HTML replytitle = new HTML("<bold>Svara</bold>");
		rightpanel.add(replytitle);
		rightpanel.add(replyform);
		rightpanel.setVisible(true);
		/*privmsgview.getRightpanel().clear();
		privmsgview.getRightpanel().add(centerwidget);
		privmsgview.getRightpanel().add(new MessageForm(message.getresieverid(), message.getsenderid(), parent, message.getPriv()));
		privmsgview.getRightpanel().setVisible(true);*/
		
	}
}
