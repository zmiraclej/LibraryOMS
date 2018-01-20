/**  
* @Package com.flea.modules.customer.pojo
* @Description: TODO
* @author bruce
* @date 2016年4月15日 下午3:47:44
* @version V1.0  
*/ 
package com.flea.modules.ebook.pojo;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.User;


/**
 * 
 * @author bruce
 * @2016年6月18日 下午4:31:44
 */
@Entity
@Table(name="system_ebook")
public class Ebook implements  Serializable {

	private static final long serialVersionUID = 5030030057419269128L;

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	
	@Column(name="bookName")
	private String bookName;
	
	@Column(name="image")
	private String image;
	
	@Column(name="isbn")
	private String isbn;
	
	@Column(name="author")
	private String author;
	
	@Column(name="summary")
	private String summary;
	
	@Column(name="publisher")
	private String publisher;
	
	@Column(name="publishDate")
	private String publishDate;
	
	@Column(name="libCode")
	private String libCode;
	
	@Column(name="categoryName")
	private String categoryName;
	
	@Column(name="categoryId")
	private Integer categoryId;
	
	@Column(name="price")
	private String price;
	
	@Column(name="createDate")
	private Date createDate;
	
	@Column(name="modifyDate")
	private Date modifyDate;
	
	@Column(name="file")
	private String file;
	
	@Column(name="fileSize")
	private String fileSize;
	
	@Column(name="excelFile")
	private String excelFile;
	
	@ManyToOne
	@JoinColumn(name ="systemUser")
	private User user;
	
	@Transient
	private String filename;
	
	@Transient
	private String excelname;
	
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
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
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
	public String getExcelFile() {
		return excelFile;
	}
	public void setExcelFile(String excelFile) {
		this.excelFile = excelFile;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getExcelname() {
		return excelname;
	}
	public void setExcelname(String excelname) {
		this.excelname = excelname;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
