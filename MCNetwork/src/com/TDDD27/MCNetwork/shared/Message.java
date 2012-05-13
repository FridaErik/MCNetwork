package com.TDDD27.MCNetwork.shared;



import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;


@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Message implements Serializable{
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;

	@Persistent
	private Long senderid;

	@Persistent
	private Long resieverid;

	@Persistent
	private String message;

	@Persistent
	private Boolean offentlig;

	@Persistent
	private Date datum;



	public Message() {

		//Calendar rightNow = Calendar.getInstance();
		//datum = rightNow.getTime();
		
		//DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		//datum = new Date();

	}

	public Message(Long senderid, Long resieverid, String message, Boolean offentlig) {

		this.senderid = senderid;

		this.resieverid = resieverid;

		this.message = message;

		this.offentlig = offentlig;

		//Calendar rightNow = Calendar.getInstance();
		//datum = rightNow.getTime();
		datum = new Date();
	}

	public Long getsenderid() {
		return senderid;
	}

	public void setsenderid(Long senderid) {
		this.senderid = senderid;
	}

	public Long getresieverid() {
		return resieverid;
	}

	public void setresieverid(Long resieverid) {
		this.resieverid = resieverid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getOffentlig() {
		return offentlig;
	}

	public void setOffentlig(Boolean offentlig) {
		this.offentlig = offentlig;
	}

	public Date getDatum() {
		return datum;
	}



}