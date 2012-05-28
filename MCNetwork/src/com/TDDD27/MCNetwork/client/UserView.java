package com.TDDD27.MCNetwork.client;



import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.TDDD27.MCNetwork.shared.Picture;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Klass för att visa en användarsida, dvs, information om en
 * MCUser lagrad i databasen
 * @author Frida
 *
 */
public class UserView extends VerticalPanel implements ValueChangeHandler{
	private MCNetwork parent;
	private static DatabaseServiceAsync testService = GWT.create(DatabaseService.class);
	// Use an RPC call to the Blob Service to get the blobstore upload url
	BlobServiceAsync blobService = GWT.create(BlobService.class);

	private HorizontalPanel btnPanel = new HorizontalPanel();
	private HorizontalPanel topPanel = new HorizontalPanel();
	private HorizontalPanel bottomPanel = new HorizontalPanel();
	private VerticalPanel rightPanel = new VerticalPanel();
	//private VerticalPanel messagePanel = new VerticalPanel();
	private HorizontalPanel leftUpperPanel = new HorizontalPanel();
	private HorizontalPanel leftLowerPanel = new HorizontalPanel();
	private VerticalPanel leftPanel = new VerticalPanel();
	
	private FlexTable infoTable = new FlexTable();
	private HTML title = new HTML("", true);
	private MCUser viewUser;
	private MCUser myself = null;
	private LoginInfo loginInfo = null;
	private VerticalPanel msgPanel = new VerticalPanel();
	private ScrollPanel scrollPnl = new ScrollPanel();
	private Image img;
	private FlexTable MCTable = new FlexTable();

	/**
	 * Kontruktor som hämtar User med id
	 * @param id
	 * @param myparent
	 */
	public UserView(Long id, MCNetwork myparent) {
		parent=myparent;
		this.add(title);
		//Hämta användare och lagra i infoTable som läggs i leftPanel
		getUser(id);
		setMyself();

	}

	/**
	 * Kontruktor om man har tillgång till användaren i fråga
	 * @param mcuser
	 * @param myparent
	 */
	@SuppressWarnings("unchecked")
	public UserView(MCUser mcuser, final MCNetwork myparent) {
		viewUser=mcuser;
		parent=myparent;
		setMyself();	
	}

	/**
	 * Fastställer vilken användare som är inloggad (om någon)
	 */
	protected void setMyself() {
		System.out.println("SetMyself");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				System.out.println("SetMyself-->Failure");
			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("SetMyself-->Success");
				loginInfo = result;
				getDBuser(loginInfo.getUserID());
			}
			/**
			 * Anropas från setMyself
			 * Hämtar en användare (den som ska skicka ett meddelande) 
			 * från databasen mha GoogleID
			 * 
			 * @param userID
			 */
			private void getDBuser(String userID) {
				System.out.println("SetMyself-->GetDBUser");
				if (testService == null) {
					testService = GWT.create(DatabaseService.class);
				}
				// Set up the callback object.
				AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
					@Override
					public void onFailure(Throwable caught) {
						System.out.println("SetMyself-->GetDBUser-->Failure");
					}
					@Override
					public void onSuccess(MCUser result) {
						System.out.println("SetMyself-->GetDBUser-->Success");
						myself=result;
						SetUpGUI();
					}

				};
				testService.getUserByID(userID, callback);
			}
		});
	}
	/**
	 * Skapar den grafiska representationen av en MCUser
	 */
	private void SetUpGUI() {
		//Rensning
		this.clear();
		leftUpperPanel.clear();
		btnPanel.clear();
		topPanel.clear();
		msgPanel.clear();
		rightPanel.clear();
		bottomPanel.clear();
		scrollPnl=new ScrollPanel();
		//Från scratch
		this.add(title);
		//Lagra info om user i infoTable som läggs i leftPanel
		//Lägger till bild i leftPanel
		
		if(viewUser.getUserPicId()!=null){
			System.out.println("PicId IS NOT NULL: "+viewUser.getUserPicId());
			getPicture(viewUser.getUserPicId());
		}else{
			System.out.println("PicId IS NULL");
			img = new Image("images/question-mark-icon_21147438.jpg");
			img.setHeight("100px");
		}
		
		
		
		leftUpperPanel.add(img);
		topPanel.add(leftUpperPanel);
		setUserInfo(viewUser);
		setMCInfo(viewUser);
		leftUpperPanel.add(infoTable);
		//Skapa knapparna
		if(myself!=null){
			this.add(btnPanel);
			SimplePanel btn1 = new SimplePanel();
			HTML privMsgBtn = new HTML("Skicka meddelande", true);
			ClickHandler privMsgClickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Boolean priv=true;
					createMsgForm(priv);
				}
			};
			privMsgBtn.addClickHandler(privMsgClickHandler);
			btn1.addStyleName("GreenBtn");
			btn1.setWidth("120px");
			btn1.setHeight("20px");
			btn1.add(privMsgBtn);
			SimplePanel btn2 = new SimplePanel();
			HTML pubMsgBtn = new HTML("Skriv p&aring; v&auml;ggen", true);
			ClickHandler pubMsgClickHandler = new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Boolean priv=false;
					createMsgForm(priv);
				}
			};
			pubMsgBtn.addClickHandler(pubMsgClickHandler);
			btn2.addStyleName("GreenBtn");
			btn2.setWidth("100px");
			btn2.setHeight("20px");
			btn2.add(pubMsgBtn);

			btnPanel.add(btn1);
			btnPanel.add(btn2);

			SimplePanel btn3;
			Boolean friends=false;
			for( Long a : myself.getFriendsList()){
				System.out.println("myself.getFriendlist.length : "+ myself.getFriendsList().size());
				if(viewUser.getId().equals(a)){
					System.out.println("Friends: id: "+a);
					friends=true;
				}
			}
			//if(not friends)
			System.out.println("viewUser.getId(): "+ viewUser.getId()+"myself.getId(): "+ myself.getId());
			if(!friends && viewUser.getId()!=parent.getLoggedInUser().getId()){
				btn3 = new SimplePanel();
				HTML addFriendBtn = new HTML("Bli kompis", true);
				ClickHandler addFriendClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						addFriend(myself, viewUser);
					}
				};
				addFriendBtn.addClickHandler(addFriendClickHandler);
				btn3.add(addFriendBtn);
				btn3.setWidth("70px");
				btn3.addStyleName("GreenBtn");
				btn3.setHeight("20px");
				btnPanel.add(btn3);
			}else if(friends && viewUser.getId()!=parent.getLoggedInUser().getId()){ //if friends
				btn3 = new SimplePanel();
				HTML removeFriendBtn = new HTML("Ta bort kompis", true);
				ClickHandler removeFriendClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						removeFriend(myself, viewUser);
					}
				};
				removeFriendBtn.addClickHandler(removeFriendClickHandler);
				btn3.add(removeFriendBtn);
				btn3.setWidth("90px");
				btn3.addStyleName("GreenBtn");
				btn3.setHeight("20px");
				btnPanel.add(btn3);
			}
			else if(viewUser.getId()==parent.getLoggedInUser().getId()){
				btn3 = new SimplePanel();
				HTML updateBtn = new HTML("Uppdatera uppgifter", true);
				ClickHandler updateClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						parent.centerPanel.clear();
						parent.centerPanel.add(new EditUserView(parent));
					}
				};
				updateBtn.addClickHandler(updateClickHandler);
				btn3.add(updateBtn);
				btn3.setWidth("120px");
				btn3.addStyleName("GreenBtn");
				btn3.setHeight("20px");
				btnPanel.add(btn3);
			}

			btnPanel.addStyleName("btnPanel");
		}

		//Ladda meddelande i srollPanel i RightPanel
		HTML msgTitle = new HTML("<h2>Meddelande: </h2>", true);
		msgPanel.add(msgTitle);
		msgPanel.setWidth("250px");
		setMsgPanel();
		scrollPnl.setStyleName("UserViewScrollPnl");		
		scrollPnl.add(msgPanel);
		scrollPnl.setHeight("300px");
		scrollPnl.setWidth("275px");
		
		rightPanel.add(scrollPnl);
		topPanel.add(leftPanel);
		topPanel.add(rightPanel);
		this.add(topPanel);
		this.add(bottomPanel);
		leftLowerPanel.add(MCTable);
		leftPanel.add(leftUpperPanel);
		leftPanel.add(leftLowerPanel);
		
		topPanel.addStyleName("topPanel");
		bottomPanel.addStyleName("bottomPanel");
		leftUpperPanel.addStyleName("leftPanel");
		rightPanel.addStyleName("rightPanel");

		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("mcuser");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("mcuser");
		History.fireCurrentHistoryState();		
		//HISTORY

	}
	/**
	 * Hämtar meddelande från databasen och skriver ut dem i msgPaneln
	 */
	private void setMsgPanel() {
		if (testService == null) {
			testService = GWT.create(DatabaseService.class);
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
					MessageView msgview = new MessageView(a, parent);
					msgPanel.add(msgview);
				}

			}
		};
		Boolean priv=false;
		testService.getRecievedMessage(viewUser.getId(), priv, callback);

	}
	/**
	 * Metod som genererar ett MessageForm så man kan skicka
	 * meddelande, anropas från knapparna
	 * @param priv true=private meddelande, false=på väggen
	 */
	protected void createMsgForm(final Boolean priv) {
		System.out.println("steg 1");
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {

			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("Hittade en inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID(), priv);
			}
			/**
			 * Anropas från create messageform
			 * Hämtar en användare (den som ska skicka ett meddelande) 
			 * från databasen mha GoogleID
			 * 
			 * @param userID
			 * @param priv
			 */
			private void getDBUser(String userID, final Boolean priv) {
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
					public void onSuccess(MCUser result) {
						myself=result;
						MessageForm msgform = new MessageForm(result, viewUser, parent, priv, null);
						parent.centerPanel.clear();
						parent.centerPanel.add(msgform);
					}
				};
				testService.getUserByID(userID, callback);
			}
		});

	}

	/**
	 * Metoden skriver info om en MCUser till infoTable
	 * @param mcuser
	 */
	protected void setUserInfo(MCUser mcuser) {
		viewUser=mcuser;
		System.out.println("<userH1>"+mcuser.getFirstName()+" "+mcuser.getLastName()+"</userH1>");
		title.setHTML("<h1>"+mcuser.getFirstName()+" "+mcuser.getLastName()+"<h1>");
		infoTable.setWidget(2, 1, new HTML("<bold>E-mail: </bold>"+mcuser.geteMail(), true));
		infoTable.setWidget(3, 1, new HTML("<bold>Bostadsort: </bold>"+mcuser.getRegion() + ", " + mcuser.getCity(), true));
		infoTable.setWidget(4, 1, new HTML("<bold>Antal k&ouml;rda mil: </bold>"+Integer.toString(mcuser.getMilesDriven()), true));	
	}

	/**
	 * Metoden skriver info om en MC till MCTable
	 * @param mcuser
	 */
	protected void setMCInfo(MCUser theUser) {
		ArrayList<MC> MCList = theUser.getMcList();
		System.out.println("theUser.getId(): "+theUser.getId()+" MCLIST.size()"+theUser.getMcList().size());
		if(!MCList.isEmpty()){

			for(int i=0; i<theUser.getMcList().size(); i++){
				final MC myMC = MCList.get(i); 
				System.out.println("Url: "+myMC.getUrl());
				if(myMC.getUrl().length()<=3){
					HTML brandLink = new HTML("<H3>"+ myMC.getBrand() +" </H3>", true);
					MCTable.setWidget(2*i, 1, brandLink);
				}
				else{
					HTML brandLink = new HTML("<a href="+'"'+myMC.getUrl()+'"'+" target="+'"'+"_blank"+'"'+"><H3>"+ myMC.getBrand() +" </H3></a>", true);
					MCTable.setWidget(2*i, 1, brandLink);
				}
				
				MCTable.setWidget(2*i+1, 1, new HTML("<bold>"+ myMC.getModel() +" </bold>", true));
				MCTable.setWidget(2*i+1, 2, new HTML("<bold>"+ myMC.getYear() +" </bold>", true));
			}
		}
	}

	/**
	 * Metod som hämtar en MCUser med hjälp av ett MCUser id.
	 * Anropar sedan setUserInfo. Anropas från konstruktorn 
	 * när sidan genereras med bara ett id och man inte har användaren.
	 * @param id
	 * @return
	 */
	private void getUser(Long id){
		final Long userid = id;
		AsyncCallback<MCUser> callback = new AsyncCallback<MCUser>() {
			@Override
			public void onFailure(Throwable caught) {
				System.out.println("failure in getUser");
				try {
					throw caught;
				} catch (IncompatibleRemoteServiceException e) {
					// this client is not compatible with the server; cleanup and refresh the 
					// browser
				} catch (InvocationException e) {
					// the call didn't complete cleanly
				} catch (Throwable e) {
					// last resort -- a very unexpected exception
				}
			}

			@Override
			public void onSuccess(MCUser result) {
				System.out.println("succes in get user");
				if(!(result.getFirstName()==null)){
					System.out.println("McList: "+result.getMcList().size());
					viewUser=result;
				}
				else{
					setErrorMessage(userid);
				}
			}
		};
		testService.getUser(id, callback);
	}

	/**
	 * Metod för att lägga till en vän 
	 * @param myself
	 * @param viewUser
	 */
	protected void addFriend(MCUser myself, final MCUser viewUser) {
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
				parent.centerPanel.add(new UserView(viewUser, parent));
			}

		};
		testService.createFriendship(viewUser, myself, callback);

	}
	/**
	 * Metod för att ta bort en vän
	 * @param myself
	 * @param viewUser
	 */
	protected void removeFriend(MCUser myself, final MCUser viewUser) {
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
				parent.centerPanel.add(new UserView(viewUser, parent));
			}

		};
		testService.removeFriendship(viewUser, myself, callback);

	}
	
	/**
	 * Metod för att hämta användarens bild om den har nån.
	 * @param id
	 */
	public void getPicture(Long id) {

		if(id!=null){
			//Make another call to the Blob Service to retrieve the meta-data
			blobService.getPicture(id, new AsyncCallback<Picture>() {

				@Override
				public void onSuccess(Picture result) {
					if(result!=null){
						System.out.println("IMAGE URL IS NOT NULL: "+result.getImageUrl());
						img.setUrl(result.getImageUrl());
						img.setHeight("100px");
					}else{
						System.out.println("IMAGE URL IS NULL");
						img.setUrl("/MCNetwork/war/images/question-mark-icon_21147438.jpg");
						img.setHeight("100px");
					}
				}
				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		}else{
			System.out.println("picture.id==null");
		}


	}

	/**
	 * Metod, anropas om användaren inte kunde hämtas från databasen
	 * @param userid
	 */
	private void setErrorMessage(Long userid) {
		HTML errorLabel = new HTML("Ooops! N&aring;got gick fel i kontakten med databasen, f&ouml;rs&ouml;k igen senare.", true);
		add(errorLabel);
	}
	/**
	 * Hanterar historiken
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("mcuser")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}


	}
}