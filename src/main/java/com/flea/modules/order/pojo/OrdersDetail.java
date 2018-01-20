package com.flea.modules.order.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "ebook_orders_detail")
public class OrdersDetail {
	@Id
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "order_number",nullable=false)
	private Long orderNumber;
	@Column(name = "order_id",nullable=false)
	private Integer ordersID;
	@Column(name = "ebook_id",nullable=false)
	private Integer ebookID;
	@Formula("(select se.categoryName from system_ebook se where se.id = ebook_id)")  
	private String categoryName;
	
	@Formula("(select se.bookName from system_ebook se where se.id = ebook_id)")  
	private String bookName;
	
	@Formula("(select se.isbn from system_ebook se where se.id = ebook_id)")  
	private String isbn;
	@Formula("(select se.publisher from system_ebook se where se.id = ebook_id)")  
	private String publisher; //出版社
	
	@Formula("(select se.author from system_ebook se where se.id = ebook_id)")  
	private String author;  //著者
	
	@Formula("(select se.publishDate from system_ebook se where se.id = ebook_id)")  
	private String publishDate; //年份
	
	@Formula("(select scc.userName from system_user scc where scc.id =(select eo.operator_id from ebook_orders eo where eo.id = order_id))")  
	private String operatorName; //操作员
	
	@Formula("(select se.createDate from system_ebook se where se.id = ebook_id)")  
	private Date createDate; //上传日期
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Long getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getOrdersID() {
		return ordersID;
	}
	public void setOrdersID(Integer ordersID) {
		this.ordersID = ordersID;
	}
	public Integer getEbookID() {
		return ebookID;
	}
	public void setEbookID(Integer ebookID) {
		this.ebookID = ebookID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getPublishDate() {
		return publishDate;
	}
	public void setPublishDate(String publishDate) {
		this.publishDate = publishDate;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
