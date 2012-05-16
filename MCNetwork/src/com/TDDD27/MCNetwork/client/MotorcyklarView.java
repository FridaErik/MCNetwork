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
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitHandler;

public class MotorcyklarView extends VerticalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private MC MC = null;
	private Button edit = new Button("Edit");
	private Button nymc = new Button("Ny MC");
	private LoginInfo loginInfo = null;
	private FlexTable MCTable = new FlexTable();


	public MotorcyklarView(MCNetwork myParent) {
		super();
		System.out.println("Skapar ny UserForm");
		parent=myParent;
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				//setWidget(new HTML("<H1>Det verkar inte finnas en inloggad användare men då ska inte fliken uppdatera" + " din uppgifter synas i menyn, nåt blir fel</H1>", true));
			}
			public void onSuccess(LoginInfo result) {
				System.out.println("Userform har registrerat att en användare är inloggad");
				loginInfo = result;
				getDBUser(loginInfo.getUserID());
			}
		});


	}

	@SuppressWarnings("unchecked")
	private MCUser getDBUser(String userID) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}
		// Set up the callback object.
		AsyncCallback<ArrayList<MCUser>> callback = new AsyncCallback<ArrayList<MCUser>>() {
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(ArrayList<MCUser> result) {
				System.out.println("Hämtat DBUSer med ID från Google");
				if(result==null){
					System.out.println("Använderan finns inte i databasen");
					//Hit ska vi aldrig komma fšr att man ser inte knappen i menyn om man inte Šr med i databasen
				}
				else if(result.size()>1){
					//Ajaj, nŒgon har klantat sig
					System.out.println("Dubble ID i Databasen!!!!!!");
				}
				else{
					loggedInUser=result.get(0);

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

	protected void printMC(MCUser theUser) {
		ArrayList<MC> MCList = loggedInUser.getMcList();

		if(!MCList.isEmpty()){
			for(int i=1; i<=MCList.size(); i++){

				MC = MCList.get(i); 

				MCTable.setWidget(i, 1, new HTML("<bold>Motorcykel </bold>"+ MC.getBrand(), true));
				edit.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						edit(MC);	
					}
				});
				MCTable.setWidget(i, 2, edit);
			}
		}
		
		nymc.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				edit(MC);	
			}
		});
		this.add(MCTable);
		this.add(nymc);

	}

	protected void edit(MC MC) {

		MCForm centerwidget = new MCForm(MC, parent); //Behšver kanske egentligen inte skicka med parent hŠr men jag gjorde det fšr att "den kan vara bra att ha"
		parent.centerPanel.clear();
		parent.centerPanel.add(centerwidget);


	}


	@Override
	public void onValueChange(ValueChangeEvent event) {
		if (event.getValue().equals("mcview")){
			parent.centerPanel.clear();
			parent.centerPanel.add(this);
		}

	}

}
