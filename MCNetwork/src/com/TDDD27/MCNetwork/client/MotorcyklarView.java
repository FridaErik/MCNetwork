package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.LoginInfo;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MotorcyklarView extends VerticalPanel implements ValueChangeHandler{
	private static TestServiceAsync testService = GWT.create(TestService.class);
	
	private MCNetwork parent;
	private MCUser loggedInUser=null;
	private Button submit = new Button("Submit");
	private LoginInfo loginInfo = null;

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


		testService.getUserByID(userID, callback);
		return null;
	}

	protected void printMC(MCUser theUser) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onValueChange(ValueChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
