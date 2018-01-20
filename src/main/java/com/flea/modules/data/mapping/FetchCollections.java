package com.flea.modules.data.mapping;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FetchCollections {
//	"catalogcode":"20110217003",
//	"isocode":"isbn",
//	"bookname":"书名",
//	"copier":"复本数",
//    "volumes":"卷数",n
//    "barcode":"图书条码号",n
//    "indexnumber":"索书号",n
//	"roomlocation":"馆藏地点",n
//	"holdlocation":"排架位置",n
//	"libstatus":"馆藏状态",n
//	"appenddate":” 添加日期”n
	
	private String barNumber;
	private String isbn;
	private String properTitle;
	private String replicaRate;
	private Integer volumes;
	private String barCode;
	private String indexnumber;
	private String storeroom;
	private String frameCode;
	private Character bookState;
	private Date storageTime;
	
public String getBarNumber() {
		return barNumber;
	}


	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public String getProperTitle() {
		if (properTitle!=null) {
			properTitle.replaceAll(":", "\"").replaceAll("'", "\'");
		}
		return properTitle;
	}


	public void setProperTitle(String properTitle) {
		this.properTitle = properTitle;
	}


	public String getReplicaRate() {
		return replicaRate;
	}


	public void setReplicaRate(String replicaRate) {
		this.replicaRate = replicaRate;
	}


	public Integer getVolumes() {
		return volumes;
	}


	public void setVolumes(Integer volumes) {
		this.volumes = volumes;
	}


	public String getBarCode() {
		return barCode;
	}


	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}


	public String getIndexnumber() {
		return indexnumber;
	}


	public void setIndexnumber(String indexnumber) {
		this.indexnumber = indexnumber;
	}


	public String getStoreroom() {
		return storeroom;
	}


	public void setStoreroom(String storeroom) {
		this.storeroom = storeroom;
	}


	public String getFrameCode() {
		return frameCode;
	}


	public void setFrameCode(String frameCode) {
		this.frameCode = frameCode;
	}


	public Character getBookState() {
		return bookState;
	}


	public void setBookState(Character bookState) {
		this.bookState = bookState;
	}


	public String getStorageTime() {
		if (storageTime!=null) {
			return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(storageTime);
		}
		return "";
	}


	public void setStorageTime(Date storageTime) {
		this.storageTime = storageTime;
	}

	@Override
	public String toString() {
		return "FetchCollections [catalogcode=" + barNumber + ", isocode=" + isbn
				+ ", bookname=" + properTitle + ", copier="
				+ replicaRate + ", volumes=" + volumes + ", barcode=" + barCode
				+ ", indexnumber=" + indexnumber + ", roomlocation=" + storeroom
				+ ", holdlocation=" + frameCode + ", libstatus=" + bookState
				+ ", appenddate=" + storageTime + "]";
	}
	
	
	
}
