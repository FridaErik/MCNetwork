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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
/**
 * Klass f�r att visa en anv�ndares lista av 
 * v�nner samt f�r att kunna ta bort v�nner.
 * @author Frida
 *
 */
public class FriendsView extends VerticalPanel implements ValueChangeHandler{
	private static DatabaseServiceAsync testService = GWT.create(DatabaseService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private LoginInfo loginInfo = null;
	private FlexTable friendTable = new FlexTable();
	private ArrayList<MCUser> myFriends = new ArrayList<MCUser>();
	private ArrayList<Long> FriendsList = new ArrayList<Long>();

	/**
	 * Kontruktor som genrerar GUI'n, beh�ver myParent f�r att kunna 
	 * ladda in nya sidor i huvudPanelen (MCNetwork)
	 * @param myParent
	 */
	public FriendsView(MCNetwork myParent) {
		super();
		System.out.println("Skapar ny FriendsView");
		parent=myParent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				//setWidget(new HTML("<H1>Det verkar inte finnas en inloggad anv�ndare men d� ska inte fliken uppdatera" + " din uppgifter synas i menyn, n�t blir fel</H1>", true));
			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("FriendsView har registrerat att en anv�ndare �r inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID());
			}
		});
	}

	/**
	 * H�mtar en anv�ndare med hj�lp av Google-id:t
	 * och anropar metoden f�r att skriva ut dennes motorcyklar.
	 * @param userID
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private MCUser getDBUser(String userID) {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
		}
		// Set up the callback object.
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(MCUser result) {
				System.out.println("H�mtat DBUSer med ID fr�n Google");
				if(result==null){
					System.out.println("Anv�nderan finns inte i databasen");
					//Hit ska vi aldrig komma f�r att man ser inte knappen i menyn om man inte �r med i databasen
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
	 * Skriver ut anv�ndarens v�nner
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

		}else{
			HTML noFriends = new HTML("<H2>Du har inga kompisar &auml;n</H2>", true);
			friendTable.setWidget(0, 0, noFriends);
		}
		this.add(friendTable);
	}

	/**
	 * Skriver ut listan av v�nner i en FlexTable
	 * @param list
	 */
	protected void printList(ArrayList<MCUser> list){
		HTML friends = new HTML("<H1>Dina kompisar</H1>", true);
		friendTable.setWidget(0, 0, friends);
		for(int i=1; i<=list.size(); i++){
			final MCUser myFriend = list.get(i-1);
			SimplePanel delete = new SimplePanel();
			HTML deleteBtn = new HTML("Ta bort", true);
			ClickHandler deleteClickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					deleteFriend(myFriend, loggedInUser);
				}
			};
			deleteBtn.addClickHandler(deleteClickHandler);
			deleteBtn.setWidth("58px");
			deleteBtn.setHeight("18px");
			delete.add(deleteBtn);
			delete.setWidth("60px");
			delete.addStyleName("GreenBtn");
			delete.setHeight("20px");
			
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
			friendTable.setWidget(i, 2, delete);
		}
	}

	
	/**
	 * Metod som laddar in en anv�ndares sida i
	 * huvudPanelen (MCNetwork)
	 * @param mcuser
	 */
	protected void SendToUserPage(MCUser mcuser) {
		UserView centerwidget = new UserView(mcuser, parent);
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);
	}

	
	/**
	 * Raderar en anv�ndare fr�n den inloggade personens v�nlista
	 * @param friend Den som tas bort
	 * @param loggedInUser2 Personen som tar bort en v�n
	 */
	protected void deleteFriend(MCUser friend, MCUser loggedinuser) {
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
				parent.centerPanel.clear();
				parent.centerPanel.add(new FriendsView(parent));
			}
		};
		testService.removeFriendship(friend, loggedinuser, callback);
	}

	/**
	 * H�mtar en lista med MCUsers baserad p� en lista av user-idn
	 * som �r den lista som varje anv�ndare har med sina v�nner.
	 * @param FriendsID Lista med id
	 * @return Lista med Users
	 */
	protected ArrayList<MCUser> getFriendsByID(ArrayList<Long> FriendsID) {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
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
