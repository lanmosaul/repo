package com.ccm.model.base;

/**
 * 
 */

import java.io.Serializable;
import java.util.Date;


/**
 * @author Jenny Liao the top class of all domain objects
 */
public abstract class BaseObject2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6093862859340752992L;

	private String createdBy;
	private String updatedBy;
	private Date lastModifyTime;
	private Boolean delFlag = Boolean.FALSE;

	private String sessionKey;

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Boolean getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Boolean delFlag) {
		this.delFlag = delFlag;
	}

	public String getSessionKey() {
		return sessionKey;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}

	/**
	 * Returns a multi-line String with key=value pairs.
	 * 
	 * @return a String representation of this class.
	 */

}
