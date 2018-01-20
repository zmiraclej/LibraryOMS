/**  
* @Package com.flea.modules.customer.pojo
* @Description: TODO
* @author bruce
* @date 2016年4月15日 下午3:47:44
* @version V1.0  
*/ 
package com.flea.modules.news.pojo.vo;


import java.util.Date;


import org.apache.solr.client.solrj.beans.Field;


/**
 * 
 * @author bruce
 * @2016年6月12日 下午5:40:04
 */
public class NewsIndex  {

	private static final long serialVersionUID = 5030030057419269128L;

	@Field
	private Integer id;
	
	@Field
	private String type;
	
	@Field
	private String source;
	
	@Field
	private String title;
	
	@Field
	private String image;
	
	@Field
	private String content;
	
	@Field
	private String sensitive;
	
	@Field
	private Integer status;//1待审,2审核通过,3驳回
	
	@Field
	private Integer top;//1置顶
	
	@Field
	private Date createDate;
	
	@Field
	private Date modifyDate;
	
	@Field
	private String remark;
	
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
	
}
