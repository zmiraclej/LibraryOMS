package com.flea.modules.data.mapping;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityDetail {
	private Integer id;//"code":"活动编码"
	private String title;//"title":"活动标题"
	private String content;//"abstracts":"活动简介
	private String sensitiveWord;//"keywords":"活动关键字"
	private String activeAddress;//"address":"活动举办地"
	private Date startDate;//"startdate":"活动举办日"
	private Date endDate;//"enddate":"活动结束日"
	private BigInteger continuenum;//"continuenum":活动持续日
	private String contactman;//"contactman":"活动联系人"
	private String contactphone;//"contactphone":"活动联系人电话"
	private String sponsor;//"sponsor":"活动主办方"
	private String sponsorphone;//"sponsorphone":"主办方联系电话"
	private String organizer;//"organizer":"活动承办方"
	private String organizerphone;//"organizerphone":"承办方联系电话"
	//以下字段不是数据库查出
	private String url;//宣传地址
	private String periodnum;//活动周期性 0 代表1次，其余数字代表周期，例如7代表每隔7天进行一次
	private String status;//活动状态 如期举行为1，提前取消为2
	private String portable;//活动流动性 1为固定场所，2为巡演。
	private String partingnum;//参加人数
	private String partednum;//实际参加人数
	private String commentsnum;//评论量
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		if (title!=null) {
			return title.replaceAll(":", "\\:").replaceAll("'", "\'");
		}
		return "";
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		if (content!=null) {
			return content.replaceAll(":", "\\:").replaceAll("'", "\'");
		}
		return "";
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSensitiveWord() {
		return sensitiveWord;
	}
	public void setSensitiveWord(String sensitiveWord) {
		this.sensitiveWord = sensitiveWord;
	}
	public String getActiveAddress() {
		return activeAddress;
	}
	public void setActiveAddress(String activeAddress) {
		this.activeAddress = activeAddress;
	}
	public String getStartDate() {
		if (startDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(startDate);
		}
		return "";
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		if (endDate!=null) {
			return new SimpleDateFormat("yyyy-MM-dd").format(endDate);
		}
		return "";
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public BigInteger getContinuenum() {
		return continuenum;
	}
	public void setContinuenum(BigInteger continuenum) {
		this.continuenum = continuenum;
	}
	public String getContactman() {
		return contactman;
	}
	public void setContactman(String contactman) {
		this.contactman = contactman;
	}
	public String getContactphone() {
		return contactphone;
	}
	public void setContactphone(String contactphone) {
		this.contactphone = contactphone;
	}
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getSponsorphone() {
		return sponsorphone;
	}
	public void setSponsorphone(String sponsorphone) {
		this.sponsorphone = sponsorphone;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public String getOrganizerphone() {
		return organizerphone;
	}
	public void setOrganizerphone(String organizerphone) {
		this.organizerphone = organizerphone;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPeriodnum() {
		return periodnum;
	}
	public void setPeriodnum(String periodnum) {
		this.periodnum = periodnum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPortable() {
		return portable;
	}
	public void setPortable(String portable) {
		this.portable = portable;
	}
	public String getPartingnum() {
		return partingnum;
	}
	public void setPartingnum(String partingnum) {
		this.partingnum = partingnum;
	}
	public String getPartednum() {
		return partednum;
	}
	public void setPartednum(String partednum) {
		this.partednum = partednum;
	}
	public String getCommentsnum() {
		return commentsnum;
	}
	public void setCommentsnum(String commentsnum) {
		this.commentsnum = commentsnum;
	}
	
	
}
