package com.TDDD27.MCNetwork.client;

import java.util.List;

import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TestServiceAsync {

	void storeUser(User tester, AsyncCallback<List<User>> asyncCallback);

	void getUser(Long id, AsyncCallback<User> callback);

}
