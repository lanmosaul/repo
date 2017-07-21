/**
 * 
 */
package com.ccm.model.base;

import java.io.Serializable;

/**
 * @author Jenny Liao
 *
 */
public class Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5929562781335675119L;
	
	private String province;
	private String city;
	private String area;//区
	
	private String address;//门牌号等等
	
	public String toString() {
		return province + "_" + city + "_" + area + "_" + address;
	}

	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	
}
