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
	public User storeUser(User tester) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		User storedUser = pm.makePersistent(tester);
		User TempUser= new User();
		return TempUser;
		
		/*String query = "select from "+User.class.getName();
		List<User> users = (List<User>) pm.newQuery(query).execute();
		List<User> returnList = new ArrayList<User>();

		 for(User user: users)
		 {
		     returnList.add(user);
		 }
		 return returnList;*/
	}

	@Override
	public User getUser(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MC> storeMC(MC mc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MC storedMC = pm.makePersistent(mc);
		
		String query = "select from "+User.class.getName();
		List<MC> mcs = (List<MC>) pm.newQuery(query).execute();
		List<MC> returnList = new ArrayList<MC>();

		 for(MC tempMc: mcs)
		 {
		     returnList.add(mc);
		 }
		 return returnList;
	}

}
