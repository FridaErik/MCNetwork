/**
 * 
 */
package com.TDDD27.MCNetwork.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



/**
 * @author Frida
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MCUser implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 203986008069225496L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;

	//För att lagra id för com.google.appengine.api.users.User (getUserId())
	@Persistent
	private String userID;

	@Persistent
	private String firstName;

	@Persistent
	private String lastName;

	@Persistent
	private int birthYear;

	@Persistent
	private String eMail;

	@Persistent
	private String city;

	@Persistent
	private String region;

	@Persistent
	private String gender;

	@Persistent(mappedBy = "owner")
	private ArrayList<MC> mcList = new ArrayList<MC>();

	//There is currently a bug preventing owned one-to-many relationships where the parent and the 
	//child are the same class, making it difficult to model tree structures. This will be fixed in 
	//a future release. You can work around this by storing explicit Key values for either the parent 
	//or children.
	
	@Persistent
	private ArrayList<Long> friendsList = new ArrayList<Long>();


	@Persistent
	private int milesDriven;

	public MCUser(){

	}

	public MCUser(String firstName, String lastName, int birthYear, 
			String eMail, String city, String region, String gender, ArrayList<MC> mcList,
			int milesDriven, String userid){

		this.firstName=firstName;
		this.lastName=lastName;
		this.birthYear=birthYear;
		this.eMail=eMail;
		this.city=city;
		this.region=region;
		this.gender=gender;
		this.mcList=mcList;
		this.milesDriven=milesDriven;
		this.userID=userid;

	}

	//Tillägg 2012-04-17
	public MCUser(String firstName, String lastName, int birthYear, 
			String eMail, String city, String region, String gender, int milesDriven, String userid){

		this.firstName=firstName;
		this.lastName=lastName;
		this.birthYear=birthYear;
		this.eMail=eMail;
		this.city=city;
		this.region=region;
		this.gender=gender;
		this.milesDriven=milesDriven;
		this.userID=userid;

	}


	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getBirthYear() {
		return birthYear;
	}

	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ArrayList<MC> getMcList() {
		return mcList;
	}

	public void setMcList(ArrayList<MC> mcList) {
		this.mcList = mcList;
	}

	public int getMilesDriven() {
		return milesDriven;
	}

	public void setMilesDriven(int milesDriven) {
		this.milesDriven = milesDriven;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public ArrayList<Long> getFriendsList() {
		return friendsList;
	}

	public void setFriendsList(ArrayList<Long> friendsList) {
		this.friendsList = friendsList;
	}


}
