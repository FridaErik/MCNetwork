package com.TDDD27.MCNetwork.client;


import java.util.List;

import com.TDDD27.MCNetwork.shared.FieldVerifier;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class MCNetwork implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";


	
	private static TestServiceAsync testService = GWT.create(TestService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		//Test
		//Test från hämtat projekt
		
		/*User tester = new User("Lars", "Larsson", 1977, "lars.larrson@gmail.com", "Larsstad", null, null, 0);
		addTestUser(tester);*/
		
		Userform e = new Userform();
	    
	    RootPanel.get().add(e);	
		
		
	}
	
	
	
	

	private void addTestUser(User tester) {
		if (testService == null) {
			testService = GWT.create(TestService.class);
		}

		// Set up the callback object.
		AsyncCallback<List<User>> callback = new AsyncCallback<List<User>>() {
			public void onFailure(Throwable caught) {
				
			}

			@Override
			public void onSuccess(List<User> result) {
				for( User u : result){
					System.out.println(u.getfirstName());
				}
				
			}
		};


		// Make the call to server
		testService.storeUser(tester, callback);
		
	}
}
