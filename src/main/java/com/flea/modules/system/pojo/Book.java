package com.flea.modules.system.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.City;
import com.flea.common.pojo.Dictionary;
@Entity
@Table(name="books")
public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -47564261320540447L;
	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	private Integer id;
	@Column(name="isbn")
	private String isbn;
	@Column(name="bindingMode")
	private String bindingMode;
	@Column(name="price")
	private double price;
	@Column(name="title")
	private String title;
	@Column(name="author")
	private String author;
	@Column(name="translate")
	private String translate;
	@Column(name="issuePlace")
	private String issuePlace;
	@Column(name="press")
	private String press;
	@Column(name="publishDate")
	private Date publishDate;
	@Column(name="pageNumber")
	private String pageNumber;
	@Column(name="pageSize")
	private String pageSize;
	@Column(name="contentDescript")
	private String contentDescript;
	@Column(name="classificationNumber")
	private String classificationNumber;
	@Column(name="imagePathS")
	private String imagePathS;
	@Column(name="imagePathL")
	private String imagePathL;
	@Column(name="imagePathM")
	private String imagePathM;
	@Column(name="authorIntro")
	private String authorIntro;
	@Column(name="catalog")
	private String catalog;
	@Column(name="tags")
	private String tags;
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="book")
	private  transient Set<LibraryBook> libraryBook = new HashSet<LibraryBook>(0);//
	
	
	//临时发布日期
	private String pubDate;
	
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
	public String getBindingMode() {
		return bindingMode;
	}
	public void setBindingMode(String bindingMode) {
		this.bindingMode = bindingMode;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
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
	
	public Date getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}
	public String getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(String pageNumber) {
		this.pageNumber = pageNumber;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getContentDescript() {
		return contentDescript;
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
	public String getImagePathS() {
		return imagePathS;
	}
	public void setImagePathS(String imagePathS) {
		this.imagePathS = imagePathS;
	}
	public String getImagePathL() {
		return imagePathL;
	}
	public void setImagePathL(String imagePathL) {
		this.imagePathL = imagePathL;
	}
	public String getImagePathM() {
		return imagePathM;
	}
	public void setImagePathM(String imagePathM) {
		this.imagePathM = imagePathM;
	}
	public String getAuthorIntro() {
		return authorIntro;
	}
	public void setAuthorIntro(String authorIntro) {
		this.authorIntro = authorIntro;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public Set<LibraryBook> getLibraryBook() {
		return libraryBook;
	}
	public void setLibraryBook(Set<LibraryBook> libraryBook) {
		this.libraryBook = libraryBook;
	}
	
	
}
