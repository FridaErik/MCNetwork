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
 * Klass f�r att visa en anv�ndares lista av 
 * motorcyklar samt l�gga till nya
 * @author Frida
 *
 */
public class MotorcyklarView extends VerticalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private MC MC = null;
	//private Button edit;
	private Button nymc = new Button("Ny MC");
	private LoginInfo loginInfo = null;
	private FlexTable MCTable = new FlexTable();
	private Boolean editBoolean = false;


	public MotorcyklarView(MCNetwork myParent) {
		super();
		System.out.println("Skapar ny UserForm");
		parent=myParent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				//setWidget(new HTML("<H1>Det verkar inte finnas en inloggad anv�ndare men d� ska inte fliken uppdatera" + " din uppgifter synas i menyn, n�t blir fel</H1>", true));
			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("Userform har registrerat att en anv�ndare �r inloggad");
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
			testService = GWT.create(TestService.class);
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
					System.out.println("Hittade en!!!! (i MotorcyklarView)");
					printMC(loggedInUser);
				}
			}
		};

		//HISTORY
		History.addValueChangeHandler(this);
		String initToken = History.getToken();
		if(initToken.length()==0){
			History.newItem("mcview");
			System.out.println("HistoryToken = 0");
		}
		History.newItem("mcview");
		History.fireCurrentHistoryState();		
		//HISTORY

		testService.getUserByID(userID, callback);
		return null;
	}

	/**
	 * Skriver ut anv�ndarens motorcyklar
	 * i en tabell
	 * @param theUser
	 */
	protected void printMC(MCUser theUser) {
		ArrayList<MC> MCList = loggedInUser.getMcList();

		System.out.println("loggedInUser.getId(): "+loggedInUser.getId()+" Listan.size()"+MCList.size());
		if(!MCList.isEmpty()){

			for(int i=0; i<MCList.size(); i++){

				final MC myMC = MCList.get(i); 
				Button edit = new Button("Edit");
				Button delete = new Button("Delete");
				MCTable.setWidget(i, 1, new HTML("<bold>Motorcykel </bold>"+ myMC.getBrand(), true));
				edit.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						editBoolean=true;
						edit(myMC);	
					}
				});
				delete.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						deleteMC(myMC, loggedInUser);	
					}
				});
				MCTable.setWidget(i, 2, edit);
				MCTable.setWidget(i, 3, delete);
			}
		}

		nymc.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editBoolean=false;
				edit(null);	
			}
		});
		this.add(MCTable);
		this.add(nymc);

	}
	/**
	 * Metod f�r att �ppna ett formul�r
	 * s� att anv�ndaren kan �ndra uppgifter de angett om en modell
	 * @param MC
	 */
	protected void edit(MC MC) {
		MCForm centerwidget = new MCForm(MC, loggedInUser, parent, editBoolean); //Beh�ver kanske egentligen inte skicka med parent h�r men jag gjorde det f�r att "den kan vara bra att ha"
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);
	}
	
	private void deleteMC(MC mc, MCUser loggedInUser) {
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
				parent.centerPanel.add(new MotorcyklarView(parent));
			}
		};

		testService.deleteMC(mc, loggedInUser, callback);
	}

	/**Hanterar historiken
	 * 
	 */
	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("mcview")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}

}
