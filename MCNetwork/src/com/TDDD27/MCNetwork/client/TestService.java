package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;
import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("network")
public interface TestService extends RemoteService {

	Long storeUser(MCUser tester);

	MCUser getUser(Long id);

	MC storeMC(MC mc);

	Long storeUserMC(MCUser mcuser, MC mc);

	ArrayList<MCUser> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname);

	ArrayList<MCUser> getUserByID(String userID);

	long updateUser(MCUser mcuser);



}
