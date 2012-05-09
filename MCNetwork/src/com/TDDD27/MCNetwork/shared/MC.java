package com.TDDD27.MCNetwork.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;



@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class MC implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8513511776596417083L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;

	@Persistent
	private String brand;

	@Persistent
	private String model;

	@Persistent
	private int year;

	@Persistent
	private String url;

	@Persistent
	private String image;

	@Persistent
	private MCUser owner;

	public MC(){

	}

	public MC(String b, String m, int y, String u, MCUser us){
		brand=b;
		model=m;
		year=y;
		url=u;
		setOwner(us);
	}
	public MC(String b, String m, int y, String u){
		brand=b;
		model=m;
		year=y;
		url=u;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public MCUser getOwner() {
		return owner;
	}

	public void setOwner(MCUser owner) {
		this.owner = owner;
	}



}
