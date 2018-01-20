package com.flea.modules.system.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;



/**
 * The persistent class for the WAREHOUSE database table.
 * 
 */
@Entity
@Table(name="WAREHOUSE")
public class Warehouse implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;//排架号
	private String startClassNumber;//分类号
	private String startTag;
	private String endClassNumber;//分类号
	private String maxStockNum;//每格最大存放数
	private String layerNumber;//货格层数
	private String  additional;//备用
	private Library library;//所属图书馆
	
	public Warehouse(){}

	public Warehouse(Warehouse warehouse) {
		this.code = warehouse.getCode();
		this.startClassNumber = warehouse.getStartClassNumber();
		this.endClassNumber = warehouse.getEndClassNumber();
		this.startTag = warehouse.getStartTag();
		this.maxStockNum = warehouse.getMaxStockNum();
		this.layerNumber = warehouse.getLayerNumber();
		this.additional = warehouse.getAdditional();
	}

	@Id
	@GenericGenerator(name="generator",strategy="identity")
	@GeneratedValue(generator="generator")
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(length=40)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="libraryId")
	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	@Column(length=10)
	public String getStartClassNumber() {
		return startClassNumber;
	}

	public void setStartClassNumber(String startClassNumber) {
		this.startClassNumber = startClassNumber;
	}

	@Column(length=10)
	public String getStartTag() {
		return startTag;
	}

	public void setStartTag(String startTag) {
		this.startTag = startTag;
	}

	@Column(length=10)
	public String getEndClassNumber() {
		return endClassNumber;
	}

	public void setEndClassNumber(String endClassNumber) {
		this.endClassNumber = endClassNumber;
	}

	@Column(length=10)
	public String getMaxStockNum() {
		return maxStockNum;
	}

	public void setMaxStockNum(String maxStockNum) {
		this.maxStockNum = maxStockNum;
	}

	@Column(length=10)
	public String getLayerNumber() {
		return layerNumber;
	}

	public void setLayerNumber(String layerNumber) {
		this.layerNumber = layerNumber;
	}


	@Column(length=10)
	public String getAdditional() {
		return additional;
	}

	public void setAdditional(String additional) {
		this.additional = additional;
	}

	
	
}