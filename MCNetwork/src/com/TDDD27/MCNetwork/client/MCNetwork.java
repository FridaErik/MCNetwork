package com.TDDD27.MCNetwork.client;


import java.util.List;

import com.TDDD27.MCNetwork.shared.FieldVerifier;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
		
		/*User tester = new User("Lars");
		
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
		testService.storeUser(tester, callback);*/
		
		
	}
}
