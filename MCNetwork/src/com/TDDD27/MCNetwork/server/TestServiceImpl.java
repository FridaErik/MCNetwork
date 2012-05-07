package com.TDDD27.MCNetwork.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

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


		PersistenceManager pm = PMF.get().getPersistenceManager();

		//User theUser = new User("Lars", "Larsson", 1977, "lars.larrson@gmail.com", "Larsstad", null, null, 0);

		User theUser = pm.getObjectById(User.class, id); 

		System.out.println(theUser.getFirstName());

		pm.close();


		return theUser;

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
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<User> result = new ArrayList<User>();
		if(lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//Sök bara med födelseår och miles
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && city.equals("") && fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles och lname
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("lastName == '"+ lname + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && city.equals("") && !fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, fname
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("firstName == '"+ fname + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
			
		}
		else if(lan.equals("Alla") && city.equals("") && !fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lname och fname
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("lastName == '"+ lname + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && !city.equals("") && fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, city
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("city == '"+ city + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && !city.equals("") && fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lname, city
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("lastName == '"+ lname + "' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && !city.equals("") && !fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, fname, city
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("city == '"+ city + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(lan.equals("Alla") && !city.equals("") && !fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lname, fname, city
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("lastName == '"+ lname + "' && firstName == '" +fname+"' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, lan
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "'  && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}///////////////////////
		else if(!lan.equals("Alla") && city.equals("") && fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lan, lname
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("lastName == '"+ lname + "' && region == '" +lan+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && city.equals("") && !fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles,lan, fname
			result=new ArrayList<User>();
			 
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && city.equals("") && !fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lan, lname och fname
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && lastName == '" +lname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && !city.equals("") && fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, lan, city
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
			
		}
		else if(!lan.equals("Alla") && !city.equals("") && fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lan, lname, city
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && city == '" +city+"'  && lastName == '" +lname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && !city.equals("") && !fname.equals("") && lname.equals("")){
			//Sök med födelseår, miles, lan, fname, city
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else if(!lan.equals("Alla") && !city.equals("") && !fname.equals("") && !lname.equals("")){
			//Sök med födelseår, miles, lan, lname, fname, city
			result=new ArrayList<User>();
			Query q1 = pm.newQuery(User.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && lastName == '" +lname+"' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(User.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<User> results1 = (List<User>) q1.execute();
				@SuppressWarnings("unchecked")
				List<User> results2 = (List<User>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (User a : results1){
						for(User b : results2){
							if(a.equals(b)){
								result.add(a);
							}
						}
					}
				}
			} finally {
				q1.closeAll();
				q2.closeAll();
			}
			return result;
		}
		else{
			//OM man kommer hit har vi missat nåt fall eller nåt funkar inte så det är tänkte. 
			//Alla möjliga utfall ska vara täckta.

			System.out.println("Vi har missat ett sök fall i filter funktionen");

		}


		/*//Test kod, ta bort!
		User tester = new User("Leif", "Lomsson", 1987, "leif.lomsson@gmail.com", "Husbo", "Blekinge", "Kvinna", 234);
		result.add(tester);

		User theUser = pm.getObjectById(User.class, 44);
		//User theUser = (User) pm.getObjectById(44);
		result.add(theUser);
		//Fram till hit
		 */		
		return null;
	}

}
