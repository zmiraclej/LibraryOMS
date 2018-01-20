package com.flea.modules.ebook.pojo;


import java.io.Serializable;
import java.util.Date;

import org.apache.solr.client.solrj.beans.Field;



/**
 * 
 * @author bruce
 * @2016年6月18日 下午4:31:44
 */
public class EbookIndex implements  Serializable {

	private static final long serialVersionUID = 5030030057419269128L;

	@Field
	private Integer id;
	@Field
	private String bookName;
	@Field
	private String image;
	@Field
	private String isbn;
	@Field
	private String author;
	@Field
	private String summary;
	@Field
	private String publisher;
	@Field
	private String publishDate;
	@Field
	private String libCode;
	@Field
	private String categoryName;
	@Field
	private int categoryId;
	@Field
	private Date createDate;
	@Field
	private Date modifyDate;
	@Field
	private String file;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getLibCode() {
		return libCode;
	}
	public void setLibCode(String libCode) {
		this.libCode = libCode;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
}
