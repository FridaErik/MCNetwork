package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Klass för att visa en användares lista av 
 * vŠnner 
 * @author Frida
 *
 */
public class FriendsView extends VerticalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private LoginInfo loginInfo = null;
	private FlexTable friendTable = new FlexTable();
	private ArrayList<MCUser> myFriends = new ArrayList<MCUser>();
	private ArrayList<Long> FriendsList = new ArrayList<Long>();

	
	public FriendsView(MCNetwork myParent) {
		super();
		System.out.println("Skapar ny FriendsView");
		parent=myParent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				//setWidget(new HTML("<H1>Det verkar inte finnas en inloggad användare men då ska inte fliken uppdatera" + " din uppgifter synas i menyn, nåt blir fel</H1>", true));
			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("FriendsView har registrerat att en användare är inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID());
			}
		});


	}

	/**
	 * Hämtar en användare med hjälp av Google-id:t
	 * och anropar metoden för att skriva ut dennes motorcyklar.
	 * @param userID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MCUser getDBUser(String userID) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(MCUser result) {
				System.out.println("Hämtat DBUSer med ID från Google");
				if(result==null){
					System.out.println("Använderan finns inte i databasen");
					//Hit ska vi aldrig komma fšr att man ser inte knappen i menyn om man inte Šr med i databasen
				}
				else{
					loggedInUser=result;
					System.out.println("Hittade en!!!! (i FriendsView)");
					printFriends(loggedInUser);
				}
			}
		};

		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("friendsview");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("friendsview");
		History.fireCurrentHistoryState();		
		//HISTORY

		testService.getUserByID(userID, callback);
		return null;
	}

	/**
	 * Skriver ut användarens vänner
	 * i en tabell
	 * @param theUser
	 */
	protected void printFriends(MCUser theUser) {
		FriendsList = loggedInUser.getFriendsList();

		System.out.println("loggedInUser.getId(): "+loggedInUser.getId()+" Listan.size()"+FriendsList.size());
		if(!FriendsList.isEmpty()){
			//FriendsList = loggedInUser.getFriendsList();
			myFriends = getFriendsByID(FriendsList);
			//System.out.println("loggedInUser.getId(): "+loggedInUser.getId()+" Listan.size()"+myFriends.size());
			
			
		}

		this.add(friendTable);

	}
	
	protected void printList(ArrayList<MCUser> list){
		for(int i=0; i<list.size(); i++){
			//final Long FriendId = FriendsList.get(i); 
			final MCUser myFriend = list.get(i);
			Button delete = new Button("Delete");
			System.out.println(myFriend.getFirstName());
			HTML friendWidget = new HTML();
			friendWidget.setHTML(myFriend.getFirstName()+" "+myFriend.getLastName());
			friendWidget.setStyleName("Clickable");
			
			ClickHandler resieverClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			SendToUserPage(myFriend);
			}
			};

			friendWidget.addClickHandler(resieverClickHandler);

			friendTable.setWidget(i, 1, friendWidget);
			delete.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteFriend(myFriend, loggedInUser);	
				}
			});
			
			friendTable.setWidget(i, 2, delete);
		}
	}
	
	protected void SendToUserPage(MCUser mcuser) {

		UserView centerwidget = new UserView(mcuser, parent);
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);

		}
	
	protected void deleteFriend(MCUser friend, MCUser loggedInUser2) {
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
				parent.centerPanel.clear();
				parent.centerPanel.add(new FriendsView(parent));
			}
		};

		testService.removeFriendship(friend, loggedInUser, callback);
	}

	protected ArrayList<MCUser> getFriendsByID(ArrayList<Long> FriendsID) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<MCUser>> callback = new AsyncCallback<ArrayList<MCUser>>() {
			@Override
			public void onFailure(Throwable caught) {
				myFriends = null;
			}
			@Override
			public void onSuccess(ArrayList<MCUser> result) {
				myFriends = result;
				System.out.println("List.size(): "+result.size());
				printList(result);
			}
		};

		testService.getFriendsByID(FriendsID, callback);
		return myFriends;
	}
	
	
	

	/**Hanterar historiken
	 * 
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("friendsview")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}

}
