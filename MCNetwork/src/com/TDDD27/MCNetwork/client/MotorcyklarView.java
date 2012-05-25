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
 * Klass för att visa en användares lista av 
 * motorcyklar samt lägga till nya
 * @author Frida
 *
 */
public class MotorcyklarView extends VerticalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private MC MC = null;
	private LoginInfo loginInfo = null;
	private FlexTable MCTable = new FlexTable();
	private Boolean editBoolean = false;


	public MotorcyklarView(MCNetwork myParent) {
		super();
		System.out.println("Skapar ny MotorcyklarView");
		parent=myParent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onFailure(Throwable error) {
				//setWidget(new HTML("<H1>Det verkar inte finnas en inloggad användare men då ska inte fliken uppdatera" + " din uppgifter synas i menyn, nåt blir fel</H1>", true));
			}
			@Override
			public void onSuccess(LoginInfo result) {
				System.out.println("MotorcyklarView har registrerat att en användare är inloggad");
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
					System.out.println("Hittade en!!!! (i MotorcyklarView) id: "+loggedInUser.getId()+ " med MClist.size(): "+result.getMcList());
					printMC(loggedInUser);
				}
			}
		};

		testService.getUserByID(userID, callback);
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

		return null;
	}

	/**
	 * Skriver ut användarens motorcyklar
	 * i en tabell
	 * @param theUser
	 */
	protected void printMC(MCUser theUser) {
		ArrayList<MC> MCList = theUser.getMcList();

		System.out.println("loggedInUser.getId(): "+theUser.getId()+" MCLIST.size()"+theUser.getMcList().size());
		if(!MCList.isEmpty()){

			for(int i=0; i<theUser.getMcList().size(); i++){

				final MC myMC = MCList.get(i); 
				//Button edit = new Button("Edit");
				//Button delete = new Button("Delete");
				MCTable.setWidget(i, 1, new HTML("<bold>Motorcykel  </bold>"+ myMC.getBrand() +"   "+ myMC.getModel() +"   "+ myMC.getYear(), true));
				//Knappar
				SimplePanel edit = new SimplePanel();
				HTML editBtn = new HTML("Redigera", true);
				ClickHandler editClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						editBoolean=true;
						edit(myMC);	
					}
				};
				editBtn.addClickHandler(editClickHandler);
				edit.add(editBtn);
				edit.setWidth("90px");
				edit.addStyleName("GreenBtn");
				edit.setHeight("20px");
				
				SimplePanel delete = new SimplePanel();
				HTML deleteBtn = new HTML("Ta bort", true);
				ClickHandler deleteClickHandler = new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						deleteMC(myMC, loggedInUser);
					}
				};
				deleteBtn.addClickHandler(deleteClickHandler);
				delete.add(deleteBtn);
				delete.setWidth("90px");
				delete.addStyleName("GreenBtn");
				delete.setHeight("20px");

				MCTable.setWidget(i, 2, edit);
				MCTable.setWidget(i, 3, delete);
			}
		}
		
		SimplePanel nymc = new SimplePanel();
		HTML nymcBtn = new HTML("L&auml;gg till MC", true);
		ClickHandler nymcClickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				editBoolean=false;
				edit(null);
			}
		};
		nymcBtn.addClickHandler(nymcClickHandler);
		nymc.add(nymcBtn);
		nymc.setWidth("90px");
		nymc.addStyleName("GreenBtn");
		nymc.setHeight("20px");


		this.add(MCTable);
		this.add(nymc);

	}
	/**
	 * Metod för att öppna ett formulär
	 * så att användaren kan ändra uppgifter de angett om en modell
	 * @param MC
	 */
	protected void edit(MC MC) {
		MCForm centerwidget = new MCForm(MC, loggedInUser, parent, editBoolean); //Behöver kanske egentligen inte skicka med parent här men jag gjorde det för att "den kan vara bra att ha"
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
