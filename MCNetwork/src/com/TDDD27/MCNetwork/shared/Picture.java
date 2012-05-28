package com.TDDD27.MCNetwork.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;
/**
 * Representerar en bild, innehåller
 * info så som URL till bilden i blobstore m.m.
 * @author Frida&Erik
 *
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@SuppressWarnings("serial")
public class Picture implements Serializable {

	@Id
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	public Long id;
	@Persistent
	public String title;
	@Persistent
	public String description;
	@Persistent
	public String imageUrl;
	@Persistent
	public Long userId;
	
	//SETTERS & GETTERS

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;

	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
