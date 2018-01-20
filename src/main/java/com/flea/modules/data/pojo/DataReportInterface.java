package com.flea.modules.data.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

public class DataReportInterface {

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name ="finddeadline_code")
	private String finddeadlineCode; // 上次上传截止时间授权码
	
	@Column(name ="updatedeadline_code")
	private String updatedeadlineCode; //本次上传截止时间授权码
	
	@Column(name ="borrowed_total_code")
	private String borrowedTotalCode; //公共图书馆年流通数据量（万册次）授权码
	
	@Column(name ="borrowed_detail_code")
	private String borrowedDetailCode; //公共图书馆年流通册次明细信息授权码
	
	@Column(name ="lib_visitors_total_code")
	private String libVisitorsTotalCode; //接收年读者到馆量（人次） 授权码
	
	@Column(name ="activity_total_code")
	private String activityTotalCode; //活动总数授权码
	
	@Column(name ="activity_detail_code")
	private String activityDetailCode; //活动详情授权码
	
	@Column(name ="exhibition_total_code")
	private String exhibition_total_code; //展览总次数授权码
	
	@Column(name ="exhibition_detail_code")
	private String exhibitionDetailCode; //展览详情授权码
	
	@Column(name ="annual_website_visits_code")
	private String annualWebsiteVisitsCode; //公共图书馆年网站访问量
	
	@Column(name ="genaral_collection_code")
	private String genaralCollectionCode; //普通文献的馆藏量(册件)
	
	@Column(name ="literature_detail_code")
	private String literatureDetailCode; //接收普通文献编目明细数据
	
	@Column(name ="fetch_collections_code")
	private String fetchCollectionsCode; //接收公共图书馆馆藏数据
	
	@Column(name ="addliterature_code")
	private String addliteratureCode; //接收新增文献入（册件）藏量
	
	@Column(name ="user_total_code")
	private String userTotalCode; //公共图书馆用户总数
	
	@Column(name ="user_detail_code")
	private String userDetailCode; //接收公共图书馆读者办证数据授权码
	
	@Column(name ="library_id")
	private Integer libraryId; //图书馆ID

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFinddeadlineCode() {
		return finddeadlineCode;
	}

	public void setFinddeadlineCode(String finddeadlineCode) {
		this.finddeadlineCode = finddeadlineCode;
	}

	public String getUpdatedeadlineCode() {
		return updatedeadlineCode;
	}

	public void setUpdatedeadlineCode(String updatedeadlineCode) {
		this.updatedeadlineCode = updatedeadlineCode;
	}

	public String getBorrowedTotalCode() {
		return borrowedTotalCode;
	}

	public void setBorrowedTotalCode(String borrowedTotalCode) {
		this.borrowedTotalCode = borrowedTotalCode;
	}

	public String getBorrowedDetailCode() {
		return borrowedDetailCode;
	}

	public void setBorrowedDetailCode(String borrowedDetailCode) {
		this.borrowedDetailCode = borrowedDetailCode;
	}

	public String getLibVisitorsTotalCode() {
		return libVisitorsTotalCode;
	}

	public void setLibVisitorsTotalCode(String libVisitorsTotalCode) {
		this.libVisitorsTotalCode = libVisitorsTotalCode;
	}

	public String getActivityTotalCode() {
		return activityTotalCode;
	}

	public void setActivityTotalCode(String activityTotalCode) {
		this.activityTotalCode = activityTotalCode;
	}

	public String getActivityDetailCode() {
		return activityDetailCode;
	}

	public void setActivityDetailCode(String activityDetailCode) {
		this.activityDetailCode = activityDetailCode;
	}

	public String getExhibition_total_code() {
		return exhibition_total_code;
	}

	public void setExhibition_total_code(String exhibition_total_code) {
		this.exhibition_total_code = exhibition_total_code;
	}

	public String getExhibitionDetailCode() {
		return exhibitionDetailCode;
	}

	public void setExhibitionDetailCode(String exhibitionDetailCode) {
		this.exhibitionDetailCode = exhibitionDetailCode;
	}

	public String getAnnualWebsiteVisitsCode() {
		return annualWebsiteVisitsCode;
	}

	public void setAnnualWebsiteVisitsCode(String annualWebsiteVisitsCode) {
		this.annualWebsiteVisitsCode = annualWebsiteVisitsCode;
	}

	public String getGenaralCollectionCode() {
		return genaralCollectionCode;
	}

	public void setGenaralCollectionCode(String genaralCollectionCode) {
		this.genaralCollectionCode = genaralCollectionCode;
	}

	public String getLiteratureDetailCode() {
		return literatureDetailCode;
	}

	public void setLiteratureDetailCode(String literatureDetailCode) {
		this.literatureDetailCode = literatureDetailCode;
	}

	public String getFetchCollectionsCode() {
		return fetchCollectionsCode;
	}

	public void setFetchCollectionsCode(String fetchCollectionsCode) {
		this.fetchCollectionsCode = fetchCollectionsCode;
	}

	public String getAddliteratureCode() {
		return addliteratureCode;
	}

	public void setAddliteratureCode(String addliteratureCode) {
		this.addliteratureCode = addliteratureCode;
	}

	public String getUserTotalCode() {
		return userTotalCode;
	}

	public void setUserTotalCode(String userTotalCode) {
		this.userTotalCode = userTotalCode;
	}

	public String getUserDetailCode() {
		return userDetailCode;
	}

	public void setUserDetailCode(String userDetailCode) {
		this.userDetailCode = userDetailCode;
	}

	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer libraryId) {
		this.libraryId = libraryId;
	}
	
	
}
