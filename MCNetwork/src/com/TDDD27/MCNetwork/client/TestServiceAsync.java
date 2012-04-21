package com.TDDD27.MCNetwork.client;

import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TestServiceAsync {

	void storeUser(User tester, AsyncCallback<User> asyncCallback);

	void getUser(Long id, AsyncCallback<User> callback);

	void storeMC(MC mc, AsyncCallback<List<MC>> callback);

}
