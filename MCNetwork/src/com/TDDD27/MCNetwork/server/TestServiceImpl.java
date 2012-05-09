package com.TDDD27.MCNetwork.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.TDDD27.MCNetwork.client.TestService;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class TestServiceImpl  extends RemoteServiceServlet implements TestService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public Long storeUser(MCUser mcuser) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MCUser storedUser = pm.makePersistent(mcuser);
		MCUser tempUser= new MCUser();
		tempUser=storedUser;

		return tempUser.getId();//Test
	}

	@Override

	public MCUser getUser(Long id) {


		PersistenceManager pm = PMF.get().getPersistenceManager();

		//User theUser = new User("Lars", "Larsson", 1977, "lars.larrson@gmail.com", "Larsstad", null, null, 0);

		MCUser theUser = pm.getObjectById(MCUser.class, id); 

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
	public Long storeUserMC(MCUser mcuser, MC mc) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MCUser storedUser = pm.makePersistent(mcuser);
		mc.setOwner(storedUser);
		MC storedMC = pm.makePersistent(mc);
		MCUser tempUser= new MCUser();
		tempUser=storedUser;

		return tempUser.getId();//Test
	}

	@Override
	public ArrayList<MCUser> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MCUser> result = new ArrayList<MCUser>();
		if(lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//Sök bara med födelseår och miles
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("lastName == '"+ lname + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("firstName == '"+ fname + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("lastName == '"+ lname + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("city == '"+ city + "' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("lastName == '"+ lname + "' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("city == '"+ city + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("lastName == '"+ lname + "' && firstName == '" +fname+"' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "'  && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("lastName == '"+ lname + "' && region == '" +lan+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			 
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && lastName == '" +lname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && city == '" +city+"'  && lastName == '" +lname+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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
			result=new ArrayList<MCUser>();
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("region == '"+ lan + "' && firstName == '" +fname+"'  && lastName == '" +lname+"' && city == '" +city+"' && birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de båda resultaten
				if (!results1.isEmpty() && !results2.isEmpty()) {
					for (MCUser a : results1){
						for(MCUser b : results2){
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

	
		return null;
	}

}
