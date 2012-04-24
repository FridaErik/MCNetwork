package com.TDDD27.MCNetwork.client;

import java.util.List;

import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("network")
public interface TestService extends RemoteService {

	Long storeUser(User tester);

	User getUser(Long id);

	MC storeMC(MC mc);

	Long storeUserMC(User user, MC mc);

}
