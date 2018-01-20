/**  
* @Package com.flea.modules.system.pojo.vo
* @Description: TODO
* @author bruce
* @date 2016年8月19日 上午11:18:57
* @version V1.0  
*/ 
package com.flea.modules.system.pojo.vo;

/**
 * @author bruce
 * @2016年8月19日 上午11:18:57
 */
public class LibraryCirculation {

	private Integer id;
	
	private String name;
	
	private String hallCode;
	
	private String libraryLevel;
	
	private String areaAddress;
	
	private String conperson;
	
	private String scopeString;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHallCode() {
		return hallCode;
	}

	public void setHallCode(String hallCode) {
		this.hallCode = hallCode;
	}

	public String getLibraryLevel() {
		return libraryLevel;
	}

	public void setLibraryLevel(String libraryLevel) {
		this.libraryLevel = libraryLevel;
	}

	public String getAreaAddress() {
		return areaAddress;
	}

	public void setAreaAddress(String areaAddress) {
		this.areaAddress = areaAddress;
	}

	public String getConperson() {
		return conperson;
	}

	public void setConperson(String conperson) {
		this.conperson = conperson;
	}

	public String getScopeString() {
		return scopeString;
	}

	public void setScopeString(String scopeString) {
		this.scopeString = scopeString;
	}
	
	
	
}
