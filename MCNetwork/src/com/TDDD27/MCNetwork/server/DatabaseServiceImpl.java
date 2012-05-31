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
 * Serverklass f�r alla databashantering
 * @author Frida&Erik
 *
 */
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Metod f�r att lagra anv�ndare
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Long storeUser(MCUser mcuser) {
		System.out.println("En anv�ndare lagras");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		MCUser storedUser = pm.makePersistent(mcuser);
		MCUser tempUser= new MCUser();
		tempUser=storedUser;
		return tempUser.getId();//Test
	}
	/**
	 * Metod f�r att h�mta en anv�ndare baserat p�
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
	 * Metod f�r att lagra en MC
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
	 * Metod f�r att uppdatera anv�ndare
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
	 * Metod f�r att radera MC
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
	 * Filtermetoden som alltid k�r tv� querys och sl�r ihop dem, 
	 * en intervall f�r birthyear och om det finns ett fast v�rde angivet f�r
	 * namn, l�n, stad osv. Och en med intervall f�r milesDriven
	 * Returnerar anv�ndare som uppfyller filterparametrarna
	 * 
	 * Vi valde att konstruerar filtret p� f�ljande s�tt f�r att datastore inte st�djer queries med
	 * intervaller p� tv� parametrar. Dvs en query med x<=milesDriven>=y OCH m<=birthyear<=n.
	 * 
	 * Ist�llet k�rs en query med intervall f�r birthyear och eventuella likhetskrav f�r andra 
	 * parametrar s� som namn och l�n samt en query f�r milesDriven. Dessa tv� j�mf�rs sedan och 
	 * snittet av de tv� resultaten �r det resultat som returneras.
	 * 
	 * Varje ifsats i koden �r identisk f�rutom till�gg f�r olika likhetskrav.
	 */
	@Override
	public ArrayList<MCUser> searchUsers(int yearup, int yeardown, int milesup,
			int milesdown, String lan, String city, String fname, String lname) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		ArrayList<MCUser> result = new ArrayList<MCUser>();
		ArrayList<MCUser> toReturn = new ArrayList<MCUser>();
		if(lan.equals("Alla") && city.equals("") && fname.equals("") && lname.equals("")){
			//S�k bara med f�delse�r och miles
			Query q1 = pm.newQuery(MCUser.class);
			q1.setFilter("birthYear >= "+ yeardown + " && birthYear <= "+ yearup);
			Query q2 = pm.newQuery(MCUser.class);
			q2.setFilter("milesDriven >= "+ milesdown + " && milesDriven <= "+ milesup);
			try {
				@SuppressWarnings("unchecked")
				List<MCUser> results1 = (List<MCUser>) q1.execute();
				@SuppressWarnings("unchecked")
				List<MCUser> results2 = (List<MCUser>) q2.execute();
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles och lname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, fname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lname och fname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, fname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lname, fname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, lname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles,lan, fname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, lname och fname
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, lname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, fname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//S�k med f�delse�r, miles, lan, lname, fname, city
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
				//Plocka ut snittet av de b�da resultaten
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
			//OM man kommer hit har vi missat n�t fall eller n�t funkar inte s� det �r t�nkte. 
			//Alla m�jliga utfall ska vara t�ckta.

			System.out.println("Vi har missat ett s�k fall i filter funktionen");

		}


		return null;
	}

	/**
	 * Metod som h�mtar en anv�ndare med hj�lp av ett Google ID
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
				//F�r att g�ra om datanucleus arraylist till java.util.arraylist s� den kan skickas till klienten
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
	 * Metod som h�mtar en lisa med MCUSers baserat p� en lista med Userid
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
	 * Metod som uppdaterar en anv�ndare
	 */
	@Override
	public long updateUser(MCUser mcuser) {
		System.out.println("F�rs�ker g�ra en update, id �r :"+mcuser.getId());
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
	 * Metod som h�mtare en lista med privata meddelande som har ett
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
	 * Metod f�r att radera meddelande
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
	 * Metod f�r att skapa en koppling (v�nskap) mellan tv� anv�ndare
	 */
	@Override
	public boolean createFriendship(MCUser viewUser, MCUser myself) {
		if( viewUser!=null && myself!=null){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				//L�gger till i "myselfs" lista, dvs den som klickade p� knappen.
				MCUser e1 = pm.getObjectById(MCUser.class, myself.getId());
				e1.getFriendsList().add(viewUser.getId());
				//L�gger till i "viewusers" lista, dvs den vars sida vi var inne p�.
				MCUser e2 = pm.getObjectById(MCUser.class, viewUser.getId());
				e2.getFriendsList().add(myself.getId());

				Message notification = new Message(myself.getId(), viewUser.getId(), myself.getFirstName()+" "+myself.getLastName()+" har lagt till dig som v�n. ", true);
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
		System.out.println("F�rs�ker skapa v�nner men en av dem �r null");
		return false;

	}
	/**
	 * Metod f�r att ta bort en koppling (v�nskap) mellan tv� anv�ndare
	 */
	@Override
	public boolean removeFriendship(MCUser viewUser, MCUser myself) {
		if( viewUser!=null && myself!=null){
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				//L�gger till i "myselfs" lista, dvs den som klickade p� knappen.
				MCUser e1 = pm.getObjectById(MCUser.class, myself.getId());
				e1.getFriendsList().remove(viewUser.getId());
				//L�gger till i "viewusers" lista, dvs den vars sida vi var inne p�.
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
		System.out.println("F�rs�ker skapa v�nner men en av dem �r null");
		return false;
	}
	/**
	 * Metod f�r att lagra en anv�ndares bild
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
	 * Tar bort alla bilder som �r kopplad till en specifik anv�ndare (id)
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
