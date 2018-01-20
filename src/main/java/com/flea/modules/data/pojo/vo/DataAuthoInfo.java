package com.flea.modules.data.pojo.vo;
/**
 * @author zhangjian
 * @2017年5月28日 下午2:19:02
 */
public class DataAuthoInfo {
	
	private String libcode;//图书馆码
	private String methodcode;//各个接口授权码
	private String methodname;//接口名称
	private String serverurl;//接口基地址
	private String unicode;//图书馆总授权码
	
	public String getLibcode() {
		return libcode;
	}
	public void setLibcode(String libcode) {
		this.libcode = libcode;
	}
	public String getMethodcode() {
		return methodcode;
	}
	public void setMethodcode(String methodcode) {
		this.methodcode = methodcode;
	}
	public String getMethodname() {
		return methodname;
	}
	public void setMethodname(String methodname) {
		this.methodname = methodname;
	}
	public String getServerurl() {
		return serverurl;
	}
	public void setServerurl(String serverurl) {
		this.serverurl = serverurl;
	}
	public String getUnicode() {
		return unicode;
	}
	public void setUnicode(String unicode) {
		this.unicode = unicode;
	}
	@Override
	public String toString() {
		return "DataAuthoInfo [libcode=" + libcode + ", methodcode="
				+ methodcode + ", methodname=" + methodname + ", serverurl="
				+ serverurl + ", unicode=" + unicode + "]";
	}
	
	
	
	
	
}
