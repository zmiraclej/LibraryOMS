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

import com.flea.common.pojo.Dictionary;


@Entity
@Table(name="library_books")
public class LibraryBook implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1343891462779765796L;
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
	@Column(name="generalProcessingData")
	private String generalProcessingData;
	@Column(name="bodylanguage")
	private String bodylanguage;
	@Column(name="publishCountry")
	private String publishCountry;
	@Column(name="publishArea")
	private String publishArea;
	@Column(name="encodingData")
	private String encodingData;
	@Column(name="morphological")
	private String morphological;
	@Column(name="properTitle")
	private String properTitle;
	@Column(name="properTitlePY")
	private String properTitlePY;
	@Column(name="author")
	private String author;
	@Column(name="authorPY")
	private String authorPY;
	@Column(name="translate")
	private String translate;
	@Column(name="revision")
	private String revision;
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
	@Column(name="attachment")
	private String attachment;
	@Column(name="collectionTitle")
	private String collectionTitle;
	@Column(name="fascicleTitle")
	private String fascicleTitle;
	@Column(name="contentDescript")
	private String contentDescript;
	@Column(name="commonTheme")
	private String commonTheme;
	@Column(name="classificationNumber")
	private String classificationNumber;
	@Column(name="classificationEdition")
	private String classificationEdition;
	@Column(name="editorName")
	private String editorName;
	@Column(name="editor")
	private String editor;
	@Column(name="countryCode")
	private String countryCode;
	@Column(name="barCode")
	private String barCode;
	@Column(name="classificationCode")
	private String classificationCode;
	@Column(name="verietyCode")
	private String verietyCode;
	@Column(name="replicaRate")
	private String replicaRate;
	@Column(name="loginNumber")
	private String loginNumber;
	@Column(name="barCodeNumber")
	private String barCodeNumber;
	@Column(name="assignedAddress")
	private String assignedAddress;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookId")
	private transient Book book;
	
	@Column(name="deposit")
	private Integer deposit;
	@Column(name="catalogueCode")
	private String catalogueCode;
	@Column(name="catalogueDate")
	private Date catalogueDate;
	@Column(name="frameCode")
	private String frameCode;
	@Column(name="barNumber")
	private String barNumber;
	//编目日期string字段
	@Column(name="cataDate")
	private String cataDate;
	//出版日期string字段
	@Column(name="pubDate")
	private String pubDate;
	@Column(name="borrowNum")
	private Integer borrowNum;
	@Column(name="praiseNum")
	private Integer praiseNum;
	@Column(name="checkedDate")
	private Date checkedDate;
	
	@Column(name = "belongLibraryHallCode")
	private  String library1;
	@Column(name = "stayLibraryHallCode")
	private  String library2;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "operUser")
	private  LibraryUser libraryUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "bookState")
	private Dictionary dictionary_bookState;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "isChecked")
	private Dictionary dictionary_checked;
	
	
	
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryBook")
	private transient Set<LibraryBorrowerBookS> libraryBorrowerBookS = new HashSet<LibraryBorrowerBookS>(0);
	
	@OneToMany(cascade= CascadeType.ALL,fetch = FetchType.LAZY,mappedBy ="libraryBook")
	private transient Set<LibraryCompensateRecordMap> libraryCompensateRecordMap = new HashSet<LibraryCompensateRecordMap>(0);
	
	
	
	
	public String getFrameCode() {
		return frameCode;
	}
	public void setFrameCode(String frameCode) {
		this.frameCode = frameCode;
	}
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
	public String getGeneralProcessingData() {
		return generalProcessingData;
	}
	public void setGeneralProcessingData(String generalProcessingData) {
		this.generalProcessingData = generalProcessingData;
	}
	public String getBodylanguage() {
		return bodylanguage;
	}
	public void setBodylanguage(String bodylanguage) {
		this.bodylanguage = bodylanguage;
	}
	public String getPublishCountry() {
		return publishCountry;
	}
	public void setPublishCountry(String publishCountry) {
		this.publishCountry = publishCountry;
	}
	public String getPublishArea() {
		return publishArea;
	}
	public void setPublishArea(String publishArea) {
		this.publishArea = publishArea;
	}
	public String getEncodingData() {
		return encodingData;
	}
	public void setEncodingData(String encodingData) {
		this.encodingData = encodingData;
	}
	public String getMorphological() {
		return morphological;
	}
	public void setMorphological(String morphological) {
		this.morphological = morphological;
	}
	public String getProperTitle() {
		return properTitle;
	}
	public void setProperTitle(String properTitle) {
		this.properTitle = properTitle;
	}
	public String getProperTitlePY() {
		return properTitlePY;
	}
	public void setProperTitlePY(String properTitlePY) {
		this.properTitlePY = properTitlePY;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getAuthorPY() {
		return authorPY;
	}
	public void setAuthorPY(String authorPY) {
		this.authorPY = authorPY;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}
	public String getRevision() {
		return revision;
	}
	public void setRevision(String revision) {
		this.revision = revision;
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
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public String getCollectionTitle() {
		return collectionTitle;
	}
	public void setCollectionTitle(String collectionTitle) {
		this.collectionTitle = collectionTitle;
	}
	public String getFascicleTitle() {
		return fascicleTitle;
	}
	public void setFascicleTitle(String fascicleTitle) {
		this.fascicleTitle = fascicleTitle;
	}
	public String getContentDescript() {
		return contentDescript;
	}
	public void setContentDescript(String contentDescript) {
		this.contentDescript = contentDescript;
	}
	public String getCommonTheme() {
		return commonTheme;
	}
	public void setCommonTheme(String commonTheme) {
		this.commonTheme = commonTheme;
	}
	public String getClassificationNumber() {
		return classificationNumber;
	}
	public void setClassificationNumber(String classificationNumber) {
		this.classificationNumber = classificationNumber;
	}
	public String getClassificationEdition() {
		return classificationEdition;
	}
	public void setClassificationEdition(String classificationEdition) {
		this.classificationEdition = classificationEdition;
	}
	public String getEditorName() {
		return editorName;
	}
	public void setEditorName(String editorName) {
		this.editorName = editorName;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getClassificationCode() {
		return classificationCode;
	}
	public void setClassificationCode(String classificationCode) {
		this.classificationCode = classificationCode;
	}
	public String getVerietyCode() {
		return verietyCode;
	}
	public void setVerietyCode(String verietyCode) {
		this.verietyCode = verietyCode;
	}
	public String getReplicaRate() {
		return replicaRate;
	}
	public void setReplicaRate(String replicaRate) {
		this.replicaRate = replicaRate;
	}
	public String getLoginNumber() {
		return loginNumber;
	}
	public void setLoginNumber(String loginNumber) {
		this.loginNumber = loginNumber;
	}
	public String getBarCodeNumber() {
		return barCodeNumber;
	}
	public void setBarCodeNumber(String barCodeNumber) {
		this.barCodeNumber = barCodeNumber;
	}
	public String getAssignedAddress() {
		return assignedAddress;
	}
	public void setAssignedAddress(String assignedAddress) {
		this.assignedAddress = assignedAddress;
	}
	
	public String getCatalogueCode() {
		return catalogueCode;
	}
	public void setCatalogueCode(String catalogueCode) {
		this.catalogueCode = catalogueCode;
	}
	public Date getCatalogueDate() {
		return catalogueDate;
	}
	public void setCatalogueDate(Date catalogueDate) {
		this.catalogueDate = catalogueDate;
	}
	public String getBarNumber() {
		return barNumber;
	}
	public void setBarNumber(String barNumber) {
		this.barNumber = barNumber;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public LibraryUser getLibraryUser() {
		return libraryUser;
	}
	public void setLibraryUser(LibraryUser libraryUser) {
		this.libraryUser = libraryUser;
	}
	public String getCataDate() {
		return cataDate;
	}
	public void setCataDate(String cataDate) {
		this.cataDate = cataDate;
	}
	public Set<LibraryBorrowerBookS> getLibraryBorrowerBookS() {
		return libraryBorrowerBookS;
	}
	public void setLibraryBorrowerBookS(
			Set<LibraryBorrowerBookS> libraryBorrowerBookS) {
		this.libraryBorrowerBookS = libraryBorrowerBookS;
	}
	
	public Dictionary getDictionary_bookState() {
		return dictionary_bookState;
	}
	public void setDictionary_bookState(Dictionary dictionary_bookState) {
		this.dictionary_bookState = dictionary_bookState;
	}
	public Dictionary getDictionary_checked() {
		return dictionary_checked;
	}
	public void setDictionary_checked(Dictionary dictionary_checked) {
		this.dictionary_checked = dictionary_checked;
	}
	public Set<LibraryCompensateRecordMap> getLibraryCompensateRecordMap() {
		return libraryCompensateRecordMap;
	}
	public void setLibraryCompensateRecordMap(
			Set<LibraryCompensateRecordMap> libraryCompensateRecordMap) {
		this.libraryCompensateRecordMap = libraryCompensateRecordMap;
	}
	public Integer getBorrowNum() {
		return borrowNum;
	}
	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}
	public Integer getPraiseNum() {
		return praiseNum;
	}
	public void setPraiseNum(Integer praiseNum) {
		this.praiseNum = praiseNum;
	}
	public Date getCheckedDate() {
		return checkedDate;
	}
	public void setCheckedDate(Date checkedDate) {
		this.checkedDate = checkedDate;
	}
	public String getLibrary1() {
		return library1;
	}
	public void setLibrary1(String library1) {
		this.library1 = library1;
	}
	public String getLibrary2() {
		return library2;
	}
	public void setLibrary2(String library2) {
		this.library2 = library2;
	}
	public Integer getDeposit() {
		return deposit;
	}
	public void setDeposit(Integer deposit) {
		this.deposit = deposit;
	}
	
}
