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

	@Override
	public ArrayList<User> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname) {

		ArrayList<User> result = new ArrayList<User>();
		if(lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//S�k bara med f�delse�r och miles
		}
		else if(lan.equals("Alla") && city.equals("") && fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles och lname
		}
		else if(lan.equals("Alla") && city.equals("") && !fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, fname
		}
		else if(lan.equals("Alla") && city.equals("") && !fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lname och fname
		}
		else if(lan.equals("Alla") && !city.equals("") && fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, city
		}
		else if(lan.equals("Alla") && !city.equals("") && fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lname, city
		}
		else if(lan.equals("Alla") && !city.equals("") && !fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, fname, city
		}
		else if(lan.equals("Alla") && !city.equals("") && !fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lname, fname, city
		}
		else if(!lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, lan
		}///////////////////////
		else if(!lan.equals("Alla") && city.equals("") && fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lan, lname
		}
		else if(!lan.equals("Alla") && city.equals("") && !fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles,lan, fname
		}
		else if(!lan.equals("Alla") && city.equals("") && !fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lan, lname och fname
		}
		else if(!lan.equals("Alla") && !city.equals("") && fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, lan, city
		}
		else if(!lan.equals("Alla") && !city.equals("") && fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lan, lname, city
		}
		else if(!lan.equals("Alla") && !city.equals("") && !fname.equals("") && lname.equals("")){
			//S�k med f�delse�r, miles, lan, fname, city
		}
		else if(!lan.equals("Alla") && !city.equals("") && !fname.equals("") && !lname.equals("")){
			//S�k med f�delse�r, miles, lan, lname, fname, city
		}
		else{
			//OM man kommer hit har vi missat n�t fall eller n�t funkar inte s� det �r t�nkte. 
			//Alla m�jliga utfall ska vara t�ckta.
			
			System.out.println("Vi har missat ett s�k fall i filter funktionen");
		}
		
		
		
		return result;
	}

}
