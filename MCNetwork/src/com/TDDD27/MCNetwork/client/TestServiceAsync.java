package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;
import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface TestServiceAsync {

	void storeUser(MCUser tester, AsyncCallback<Long> asyncCallback);

	void getUser(Long id, AsyncCallback<MCUser> callback);

	void storeMC(MC mc, MCUser loggedInUser, AsyncCallback<Boolean> callback);

	void storeUserMC(MCUser mcuser, MC mc, AsyncCallback<Long> callback);

	void searchUsers(int yearup, int yeardown, int milesup, int milesdown,
			String lan, String city, String fname, String lname,
			AsyncCallback<ArrayList<MCUser>> callback);

	void getUserByID(String userID, AsyncCallback<MCUser> callback);

	void updateUser(MCUser mcuser, AsyncCallback<Long> callback);

	void storeMsg(Message msg, AsyncCallback<Boolean> callback);

	void getRecievedMessage(Long id, Boolean priv, AsyncCallback<ArrayList<Message>> callback);

	void deleteMsg(String id, AsyncCallback<Boolean> callback);

}
