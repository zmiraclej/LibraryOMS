package com.flea.modules.data.mapping;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiteratureDetail implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String isbn;
	private String properTitle;
	private String author;
	private String issuePlace;
	private String press;
	private Date publishDate;
	private String collectionTitle;
	private String contentDescript;
	private String classificationNumber;
	private String replicaRate;
	private String barNumber;
	private Date catalogueDate;
	private String imagePathL;
	private String name;//文献来源馆名
	private String authormodifier = "1";
	private String topic = "";
	private String publishingperiod = "";
	private String searchtype = "1";
	private String mediatype = "1";
	private String isdigit = "1";
	private String fileformat = "99";
	private String arttype = "1";
	private String location = "";
	private String language = "1";
	private String remark = "";
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getProperTitle() {
		if (properTitle!=null) {
			return properTitle.replaceAll(":", "\\:").replaceAll("'", "\\'");
		}
		return null;
	}
	public void setProperTitle(String properTitle) {
		this.properTitle = properTitle;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getIssuePlace() {
		return issuePlace;
	}
	public void setIssuePlace(String issuePlace) {
		this.issuePlace = issuePlace;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getPublishDate() {
		if (publishDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(publishDate);
		}
		return "";
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getCollectionTitle() {
		if (collectionTitle!=null) {
			return collectionTitle.replaceAll(":", "\\:").replaceAll("'", "\\'");
		}
		return "";
	}
	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}
	public String getContentDescript() {
		if (contentDescript!=null) {
			return contentDescript.replaceAll(":", "\\:").replaceAll("'", "\\'");
		}
		return "";
	}
	public void setContentDescript(String contentDescript) {
		this.contentDescript = contentDescript;
	}
	public String getClassificationNumber() {
		return classificationNumber;
	}
	public void setClassificationNumber(String classificationNumber) {
		this.classificationNumber = classificationNumber;
	}
	public String getReplicaRate() {
		return replicaRate;
	}
	public void setReplicaRate(String replicaRate) {
		this.replicaRate = replicaRate;
	}
	public String getBarNumber() {
		return barNumber;
	}
	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}
	public String getCatalogueDate() {
		if (catalogueDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(catalogueDate);
		}
		return "";
	}
	public void setCatalogueDate(Date catalogueDate) {
		this.catalogueDate = catalogueDate;
	}
	public String getImagePathL() {
		return imagePathL;
	}
	public void setImagePathL(String imagePathL) {
		this.imagePathL = imagePathL;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAuthormodifier() {
		return authormodifier;
	}
	public void setAuthormodifier(String authormodifier) {
		this.authormodifier = authormodifier;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPublishingperiod() {
		return publishingperiod;
	}
	public void setPublishingperiod(String publishingperiod) {
		this.publishingperiod = publishingperiod;
	}
	public String getSearchtype() {
		return searchtype;
	}
	public void setSearchtype(String searchtype) {
		this.searchtype = searchtype;
	}
	public String getMediatype() {
		return mediatype;
	}
	public void setMediatype(String mediatype) {
		this.mediatype = mediatype;
	}
	public String getIsdigit() {
		return isdigit;
	}
	public void setIsdigit(String isdigit) {
		this.isdigit = isdigit;
	}
	public String getFileformat() {
		return fileformat;
	}
	public void setFileformat(String fileformat) {
		this.fileformat = fileformat;
	}
	public String getArttype() {
		return arttype;
	}
	public void setArttype(String arttype) {
		this.arttype = arttype;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


}
