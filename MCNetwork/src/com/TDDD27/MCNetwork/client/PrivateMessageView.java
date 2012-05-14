/**
 * 
 */
package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * @author Frida
 *
 */


public class PrivateMessageView extends VerticalPanel{
	private static TestServiceAsync testService = GWT.create(TestService.class);
	private VerticalPanel msgPanel = new VerticalPanel();
	private ScrollPanel scrollPnl = new ScrollPanel();
	private MCUser loggedInUser=null;
	private LoginInfo loginInfo = null;
	private MCNetwork parent;

	/**
	 * @param myParent 
	 * 
	 */
	public PrivateMessageView(MCNetwork myParent) {
		parent=myParent;
		// TODO Auto-generated constructor stub
		Boolean priv=true;
		getLoggedInUser(priv);
		scrollPnl.add(msgPanel);
		this.add(scrollPnl);
		
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
					MessageView msgview = new MessageView(a, parent);
					msgPanel.add(msgview);
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
}
