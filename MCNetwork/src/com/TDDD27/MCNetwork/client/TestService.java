package com.TDDD27.MCNetwork.client;

import java.util.List;

import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("network")
public interface TestService extends RemoteService {

	List<User> storeUser(User tester);

}
