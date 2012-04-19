package com.TDDD27.MCNetwork.client;

import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class UserView extends VerticalPanel{
	private static TestServiceAsync testService = GWT.create(TestService.class);

	public UserView(Long id) {

		getUser(id);

	}

	private User getUser(Long id){

		final User user = new User();
		final Long userid = id;

		AsyncCallback<User> callback = new AsyncCallback<User>() {
			public void onFailure(Throwable caught) {
				System.out.println("failure in get user");
			}

			@Override
			public void  onSuccess(User result) {
				System.out.println("succes in get user");
				if(!(result.getId()==null)){
					user = result;

				}
				else{
					setErrorMessage(userid);
				}
			}
		};
		
		testService.getUser(id, callback);
		
		return user;
	}
	private void setErrorMessage(Long userid) {
		HTML errorLabel = new HTML("Ooops! N&aring;got gick fel.", true);
		//errorLabel.setStyleName("errorLabel");
		add(errorLabel);


	}



}
