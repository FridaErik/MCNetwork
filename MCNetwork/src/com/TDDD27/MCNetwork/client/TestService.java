package com.TDDD27.MCNetwork.client;

import java.util.ArrayList;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("network")
public interface TestService extends RemoteService {

	Long storeUser(MCUser tester);

	MCUser getUser(Long id);

	Boolean storeMC(MC mc, MCUser loggedInUser);

	Long storeUserMC(MCUser mcuser, MC mc);

	ArrayList<MCUser> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname);

	MCUser getUserByID(String userID);

	long updateUser(MCUser mcuser);

	boolean storeMsg(Message msg);

	ArrayList<Message> getRecievedMessage(Long id, Boolean priv);

	boolean deleteMsg(String id);

	boolean createFriendship(MCUser viewUser, MCUser myself);

	boolean updateMC(MC mc, MCUser loggedInUser);

	boolean deleteMC(MC mc, MCUser loggedInUser);

	boolean removeFriendship(MCUser viewUser, MCUser myself);

	ArrayList<MCUser> getFriendsByID(ArrayList<Long> friendsID);


	







}
