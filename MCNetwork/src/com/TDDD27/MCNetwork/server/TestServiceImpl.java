package com.TDDD27.MCNetwork.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.TDDD27.MCNetwork.client.TestService;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TestServiceImpl  extends RemoteServiceServlet implements TestService {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public Long storeUser(User user) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User storedUser = pm.makePersistent(user);
		User tempUser= new User();
		tempUser=storedUser;
		
		return tempUser.getId();//Test
	}

	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MC storeMC(MC mc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MC storedMC = pm.makePersistent(mc);

		 return storedMC;
	}

	@Override
	public Long storeUserMC(User user, MC mc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User storedUser = pm.makePersistent(user);
		mc.setOwner(storedUser);
		MC storedMC = pm.makePersistent(mc);
		User tempUser= new User();
		tempUser=storedUser;
		
		return tempUser.getId();//Test
	}

}
