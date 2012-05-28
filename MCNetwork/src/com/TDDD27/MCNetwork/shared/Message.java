package com.TDDD27.MCNetwork.shared;



import java.io.Serializable;
import java.util.Date;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Klass som representerar ett meddelande
 * Innehåller mottagare, sändare, meddelandet osv.
 * @author Frida&Erik
 *
 */
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
	private Boolean priv;

	@Persistent
	private Date datum;

	public Message() {
		datum = new Date();
	}

	public Message(Long senderid, Long resieverid, String message, Boolean priv) {
		this.senderid = senderid;
		this.resieverid = resieverid;
		this.message = message;
		this.priv = priv;
		this.datum = new Date();
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

	public Boolean getPriv() {
		return priv;
	}

	public void setPriv(Boolean priv) {
		this.priv = priv;
	}

	public Date getDatum() {
		return datum;
	}

	public String getId() {
		return id;
	}

	public void setDatum(Date datum) {
		this.datum = datum;
	}



}