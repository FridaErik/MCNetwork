package com.TDDD27.MCNetwork.server;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.TDDD27.MCNetwork.client.DatabaseService;
import com.TDDD27.MCNetwork.shared.MC;
import com.TDDD27.MCNetwork.shared.MCUser;
import com.TDDD27.MCNetwork.shared.Message;
import com.TDDD27.MCNetwork.shared.Picture;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
/**
 * Serverklass för alla databashantering
 * @author Frida&Erik
 *
 */
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Metod för att lagra användare
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long storeUser(MCUser mcuser) {
		System.out.println("En användare lagras");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MCUser storedUser = pm.makePersistent(mcuser);
		MCUser tempUser= new MCUser();
		tempUser=storedUser;
		return tempUser.getId();//Test
	}
	/**
	 * Metod för att hämta en användare baserat på
	 * ett id
	 */
	@Override
	public MCUser getUser(Long id) {
		MCUser detachedUser=null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MCUser theUser = pm.getObjectById(MCUser.class, id); 
		detachedUser = pm.detachCopy(theUser);
		java.util.ArrayList<MC> templist = new ArrayList<MC>(theUser.getMcList());
		detachedUser.setMcList(templist);
		//System.out.println(theUser.getFirstName());
		pm.close();

		return detachedUser;
	}

	/**
	 * Metod för att lagra en MC
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Boolean storeMC(MC mc, MCUser user) {
		PersistenceManager pm1 = PMF.get().getPersistenceManager();
		try {
			MCUser e = pm1.getObjectById(MCUser.class, user.getId());
			mc.setOwner(e); 	//uppdatera mc
			e.getMcList().add(mc); //Uppdatera user
			System.out.println("e.getId(): "+e.getId()+" e.getMcList().size(): "+e.getMcList().size());
		}
		finally {
			pm1.close(); //Spara till databasen
		}
		return true;
	}
	/**
	 * Metod för att uppdatera användare
	 */
	public boolean updateMC(MC mc, MCUser user) {
		PersistenceManager pm1 = PMF.get().getPersistenceManager();

		try {
			MC dbMC = pm1.getObjectById(MC.class, mc.getId());
			dbMC.setBrand(mc.getBrand());
			dbMC.setImage(mc.getImage());
			dbMC.setModel(mc.getModel());
			dbMC.setUrl(mc.getUrl());
			dbMC.setYear(mc.getYear());
		}
		finally {
			pm1.close(); //Spara till databasen
		}

		return true;
	}
	/**
	 * Metod för att radera MC
	 */
	public boolean deleteMC(MC mc, MCUser user) {
		PersistenceManager pm1 = PMF.get().getPersistenceManager();

		try {
			MCUser thisuser = pm1.getObjectById(MCUser.class, user.getId());
			System.out.println("id: "+ thisuser.getId());
			MC dbMC = pm1.getObjectById(MC.class, mc.getId());
			thisuser.getMcList().remove(dbMC); //Ta bort MCn ur listan
			pm1.deletePersistent(dbMC);
		}
		finally {
			pm1.close(); //Spara till databasen
		}

		return true;
	}


	/**
	 * Filtermetoden som alltid kör två querys och slår ihop dem, 
	 * en intervall för birthyear och om det finns ett fast värde angivet för
	 * namn, län, stad osv. Och en med intervall för milesDriven
	 * Returnerar användare som uppfyller filterparametrarna
	 * 
	 * Vi valde att konstruerar filtret på följande sätt för att datastore inte stödjer queries med
	 * intervaller på två parametrar. Dvs en query med x<=milesDriven>=y OCH m<=birthyear<=n.
	 * 
	 * Istället körs en query med intervall för birthyear och eventuella likhetskrav för andra 
	 * parametrar så som namn och län samt en query för milesDriven. Dessa två jämförs sedan och 
	 * snittet av de två resultaten är det resultat som returneras.
	 * 
	 * Varje ifsats i koden är identisk förutom tillägg för olika likhetskrav.
	 */
	@Override
	public ArrayList<MCUser> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MCUser> result = new ArrayList<MCUser>();
		ArrayList<MCUser> toReturn = new ArrayList<MCUser>();
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
			System.out.println("Lastname to query: "+lname);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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
								MCUser temp = pm.detachCopy(a);
								result.add(temp);
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

	/**
	 * Metod som hämtar en användare med hjälp av ett Google ID
	 */
	@Override
	public MCUser getUserByID(String userID) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//ArrayList<MCUser> result = new ArrayList<MCUser>();
		MCUser result=null;
		MCUser detachedUser=null;
		Query q = pm.newQuery(MCUser.class);
		q.setFilter("userID == '"+ userID+"'");
		System.out.println(q.toString());
		try {

			@SuppressWarnings("unchecked")
			List<MCUser> results=null;
			results = (List<MCUser>) q.execute();
			if(results!=null && results.size()>0){
				result=results.get(0);
				//För att göra om datanucleus arraylist till java.util.arraylist så den kan skickas till klienten
				detachedUser = pm.detachCopy(result);
				java.util.ArrayList<MC> templist = new ArrayList<MC>(result.getMcList());
				detachedUser.setMcList(templist);
				System.out.println("user.getId(): "+detachedUser.getId()+" MClistan.size() "+detachedUser.getMcList().size());
			}

		} finally {
			q.closeAll();
		}

		return detachedUser;
	}
	/**
	 * Metod som hämtar en lisa med MCUSers baserat på en lista med Userid
	 */
	@Override
	public ArrayList<MCUser> getFriendsByID(ArrayList<Long> friendsID) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		//ArrayList<MCUser> result = new ArrayList<MCUser>();
		ArrayList<MCUser> result = new ArrayList<MCUser>();
		MCUser detachedUser=null;
		for(int i=0; i<friendsID.size(); i++){
			final Long thisFriendID = friendsID.get(i); 
			MCUser thisFriend = pm.getObjectById(MCUser.class, thisFriendID); 
			detachedUser = pm.detachCopy(thisFriend);
			java.util.ArrayList<MC> templist = new ArrayList<MC>(thisFriend.getMcList());
			detachedUser.setMcList(templist);
			result.add(detachedUser);
		}
		pm.close();
		return result;
	}
	/**
	 * Metod som uppdaterar en användare
	 */
	@Override
	public long updateUser(MCUser mcuser) {
		System.out.println("Försöker göra en update, id är :"+mcuser.getId());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			MCUser e = pm.getObjectById(MCUser.class, mcuser.getId());
			e.setBirthYear(mcuser.getBirthYear());
			e.setCity(mcuser.getCity());
			e.seteMail(mcuser.geteMail());
			e.setFirstName(mcuser.getFirstName());
			e.setLastName(mcuser.getLastName());
			e.setMilesDriven(mcuser.getMilesDriven());
			e.setRegion(mcuser.getRegion());
			e.setGender(mcuser.getGender());
			e.setUserID(mcuser.getUserID());

		} finally {
			pm.close();
		}
		return mcuser.getId();
	}
	/**
	 * Metod som lagrar ett meddelande
	 */
	@Override
	public boolean storeMsg(Message msg) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(msg);
		System.out.println("MSG stored");
		return true;
	}
	/**
	 * Metod som hämtare en lista med privata meddelande som har ett
	 * specifikt motagar id
	 */
	@Override
	public ArrayList<Message> getRecievedMessage(Long id, Boolean priv) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<Message> result = new ArrayList<Message>();
		Query q = pm.newQuery(Message.class);
		q.setFilter("resieverid == "+ id + " && priv == "+priv);
		q.setOrdering("datum desc");
		System.out.println(q.toString());
		try {
			@SuppressWarnings("unchecked")
			List<Message> results = (List<Message>) q.execute();
			if(results.size()==0){
				System.out.println("result==null, recieverid == "+ id);
				return null;
			}else{
				result = new java.util.ArrayList(results);
			}


		} finally {
			q.closeAll();
		}
		return result;
	}
	/**
	 * Metod för att radera meddelande
	 */
	@Override
	public boolean deleteMsg(String id) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Message msg = pm.getObjectById(Message.class, id); 
		try {
			pm.deletePersistent(msg);
		} catch (Exception e) {
			e.printStackTrace();
			pm.close();
			return false;
		}
		pm.close();
		return true;
	}
	/**
	 * Metod för att skapa en koppling (vänskap) mellan två användare
	 */
	@Override
	public boolean createFriendship(MCUser viewUser, MCUser myself) {
		if( viewUser!=null && myself!=null){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				//Lägger till i "myselfs" lista, dvs den som klickade på knappen.
				MCUser e1 = pm.getObjectById(MCUser.class, myself.getId());
				e1.getFriendsList().add(viewUser.getId());
				//Lägger till i "viewusers" lista, dvs den vars sida vi var inne på.
				MCUser e2 = pm.getObjectById(MCUser.class, viewUser.getId());
				e2.getFriendsList().add(myself.getId());

				Message notification = new Message(myself.getId(), viewUser.getId(), myself.getFirstName()+" "+myself.getLastName()+" har lagt till dig som vän. ", true);
				pm.makePersistent(notification);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally {
				pm.close();
			}
			return true;
		}
		System.out.println("Försöker skapa vänner men en av dem är null");
		return false;

	}
	/**
	 * Metod för att ta bort en koppling (vänskap) mellan två användare
	 */
	@Override
	public boolean removeFriendship(MCUser viewUser, MCUser myself) {
		if( viewUser!=null && myself!=null){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				//Lägger till i "myselfs" lista, dvs den som klickade på knappen.
				MCUser e1 = pm.getObjectById(MCUser.class, myself.getId());
				e1.getFriendsList().remove(viewUser.getId());
				//Lägger till i "viewusers" lista, dvs den vars sida vi var inne på.
				MCUser e2 = pm.getObjectById(MCUser.class, viewUser.getId());
				e2.getFriendsList().remove(myself.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} finally {
				pm.close();
			}
			return true;
		}
		System.out.println("Försöker skapa vänner men en av dem är null");
		return false;
	}
	/**
	 * Metod för att lagra en användares bild
	 * @param mcuserid
	 * @param picid
	 */
	public static void setUserPic(long mcuserid, long picid) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			MCUser e = pm.getObjectById(MCUser.class, mcuserid);
			e.setUserPicId(picid);
		} finally {
			pm.close();
		}

	}
	/**
	 * Tar bort alla bilder som är kopplad till en specifik användare (id)
	 * @param id
	 * @return lista med bilder som togs bort
	 */
	public static ArrayList<String> deleteUserPicKey(long id) {
		PersistenceManager pm0 = PMF.get().getPersistenceManager();
		MCUser theUser = pm0.getObjectById(MCUser.class, id); 
		//Ta bort kopplingen till Picture i user
		theUser.setUserPicId(null);
		pm0.close();
		//Start Blobstore
		BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<String> result = new ArrayList<String>();
		Query q = pm.newQuery(Picture.class);
		q.setFilter("userId == "+ id);
		System.out.println(q.toString());
		try {
			@SuppressWarnings("unchecked")
			List<Picture> results = (List<Picture>) q.execute();
			if(results.size()==0){
				return null;
			}else{
				Objectify ofy = ObjectifyService.begin();
				for(Picture a : results){
					int startindex = a.imageUrl.indexOf('=');
					//Delete Blob
					blobstoreService.delete(new BlobKey(a.getImageUrl().substring(startindex+1)));
					//Delete Picture
					ofy.delete(a);
				}	
			}
		} finally {
			q.closeAll();
		}
		pm.close();

		return result;
	}


}
