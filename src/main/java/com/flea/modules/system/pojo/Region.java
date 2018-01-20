/**  
* @Package com.flea.common.pojo
* @Description: TODO
* @author bruce
* @date 2016年1月6日 上午9:25:49
* @version V1.0  
*/ 
package com.flea.modules.system.pojo;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.flea.common.pojo.IdEntity;


/**
 * @author bruce
 * @2016年1月6日 上午9:25:49
 */
@Entity
@Table(name="system_region")
public class Region  extends IdEntity {

	private static final long serialVersionUID = 1L;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parentId")
	private Region parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 区域编码
	private String name; 	// 区域名称
	private String type; 	// 区域类型（1：国家；2：省份、直辖市；3：地市；4：区县）
	
	public Region(){
		
	}
	
	public Region(String name, String type) {
		this.name = name;
		this.type = type;
	}
	public Region getParent() {
		return parent;
	}
	public void setParent(Region parent) {
		this.parent = parent;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
