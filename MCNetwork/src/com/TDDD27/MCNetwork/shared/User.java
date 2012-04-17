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
public class User implements Serializable
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 203986008069225496L;
    
    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

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
    private String gender;
    
    @Persistent
    private ArrayList<MC> mcList;
    
    @Persistent
    private int milesDriven;
    public User(){
    	
    }
    
    public User(String firstName, String lastName, int birthYear, 
    		String eMail, String city, String gender, ArrayList<MC> mcList,
    		int milesDriven){
    	
    	this.firstName=firstName;
    	this.lastName=lastName;
    	this.birthYear=birthYear;
    	this.eMail=eMail;
    	this.city=city;
    	this.gender=gender;
    	this.mcList=mcList;
    	this.milesDriven=milesDriven;
    	
    }
    
    //Tillägg 2012-04-17
    public User(String firstName, String lastName, int birthYear, 
    		String eMail, String city, String gender, int milesDriven){
    	
    	this.firstName=firstName;
    	this.lastName=lastName;
    	this.birthYear=birthYear;
    	this.eMail=eMail;
    	this.city=city;
    	this.gender=gender;
    	this.milesDriven=milesDriven;
    	
    }
    
    public String getfirstName(){
    	return firstName;
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
}
