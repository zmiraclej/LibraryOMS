/**  
* @Package com.flea.modules.customer.pojo
* @Description: TODO
* @author bruce
* @date 2016年4月15日 下午3:47:44
* @version V1.0  
*/ 
package com.flea.modules.news.pojo;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.User;


/**
 * 资讯
 * @author bruce
 * @2016年6月8日 上午10:20:06
 */
@Entity
@Table(name="system_news")
public class News implements  Serializable {

	private static final long serialVersionUID = 5030030057419269128L;

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id",unique=true,nullable= false)
	@Field("id")
	private Integer id;
	
	@Column(name="type")
	@Field("type")
	private String type;
	
	@Column(name="source")
	@Field("source")
	private String source;
	
	@Column(name="title")
	@Field("title")
	private String title;
	
	@Column(name="image")
	@Field("image")
	private String image;
	
	@Column(name="content")
	@Field("content")
	private String content;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="userId")
	private User author;
	
	@Column(name="sensitiveWord")
	private String sensitive;
	
	@Column(name="provinceCode",length=8)
	@Field("provinceCode")
	private String provinceCode;
	
	@Column(name="cityCode",length=8)
	@Field("cityCode")
	private String cityCode;
	
	@Column(name="areaCode",length=8)
	@Field("areaCode")
	private String areaCode;
	
	@Column(name="objKey",length=4)
	@Field("objKey")
	private String objKey;
	
	@Column(name="objVal")
	@Field("objVal")
	private String objVal;
	
	@Column(name="status")
	@Field("status")
	private Integer status=1;//1待审,2审核通过,3驳回,4弃审,5未提交
	
	@Column(name="top",length=2)
	@Field("top")
	private Integer top=0;//1置顶
	
	@Column(name="createDate")
	@Field("createDate")
	private Date createDate;
	
	@Column(name="modifyDate")
	@Field("modifyDate")
	private Date modifyDate;
	
	@Column(name="modifyUser",length =30)
	private String modifyUser;
	
	//驳回理由
	@Column(name="rejectReason")
	private String rejectReason;
	
	@Column(name="startDate")
	@Field("startDate")
	private Date startDate;
	
	@Column(name="endDate")
	@Field("endDate")
	private Date endDate;
	
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="customerId")
	private Integer customerId;
	
	@Column(name="area")
	private String area;
	
	
	
	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSensitive() {
		return sensitive;
	}
	public void setSensitive(String sensitive) {
		this.sensitive = sensitive;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public Integer getTop() {
		return top;
	}
	public void setTop(Integer top) {
		this.top = top;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getObjKey() {
		return objKey;
	}

	public void setObjKey(String objKey) {
		this.objKey = objKey;
	}

	public String getObjVal() {
		return objVal;
	}

	public void setObjVal(String objVal) {
		this.objVal = objVal;
	}
	
	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	@Override
	public String toString() {
		return "News [id=" + id + ", type=" + type + ", source=" + source + ", title=" + title + ", image=" + image
				+ ", content=" + content + ", author=" + author + ", sensitive=" + sensitive + ", status=" + status
				+ ", top=" + top + ", createDate=" + createDate + ", modifyDate=" + modifyDate + ", remark=" + remark
				+ ", area=" + area + "]";
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	
}
