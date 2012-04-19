/**
 * 
 */
package com.TDDD27.MCNetwork.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * @author Frida
 * @param <E>
 *
 */

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class MCList<MC> extends ArrayList<MC> implements Serializable {
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String id;
	
	 
	
	public MCList(){
		
	}

}
