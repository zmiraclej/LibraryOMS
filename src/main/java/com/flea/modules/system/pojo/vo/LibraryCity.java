/**  
* @Package com.flea.modules.system.pojo.vo
* @Description: TODO
* @author bruce
* @date 2016年1月20日 下午3:17:39
* @version V1.0  
*/ 
package com.flea.modules.system.pojo.vo;

/**
 * @author bruce
 * @2016年1月20日 下午3:17:39
 */
public class LibraryCity {
	
	private Integer libraryId;
	
	private String libraryName;
	
	private String cityCode;
	
	private String cityName;

	public LibraryCity(Integer libraryId, String libraryName, String cityCode,
			String cityName) {
		this.libraryId = libraryId;
		this.libraryName = libraryName;
		this.cityCode = cityCode;
		this.cityName = cityName;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}

	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
}
