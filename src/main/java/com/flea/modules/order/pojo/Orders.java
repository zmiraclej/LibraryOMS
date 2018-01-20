package com.flea.modules.order.pojo;
/**  
* @Package com.flea.modules.order.pojo
* @Description: 配发订单POJO
* @author Wuhua
* @date 2017年1月16日 
* @version V1.0
*/ 
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import com.flea.modules.customer.pojo.Customer;
import com.flea.modules.customer.pojo.CustomerContact;

@Entity
@Table(name = "ebook_orders")
public class Orders {
	
	@Id
	@GenericGenerator(name = "generator", strategy = "identity")
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	@Column(name = "order_number",nullable=false)
	private Long orderNumber;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="customer_id", nullable=false)
	private Customer customer;
//	@OneToOne(fetch=FetchType.EAGER)
//	@JoinColumn(name = "contacts_id",nullable=false)
//	@inverseJoinColumns()
//	private CustomerContact customerContact;
	
	@Formula("(select scc.contactPerson from system_customer_contact scc where scc.customerId = customer_id)")  
	private String contactPerson;
	
	@Formula("(select scc.phone from system_customer_contact scc where scc.customerId = customer_id)")  
	private String phone;
	
	@Formula("(select scc.tel from system_customer_contact scc where scc.customerId = customer_id)")  
	private String tel;
	
	@Formula("(select scc.chat from system_customer_contact scc where scc.customerId = customer_id)")  
	private String chat;
	
	@Column(name = "create_date",nullable=false)
	private Date createDate;
	@Column(name = "modify_date",nullable=true)
	private Date modifyDate;
	@Column(name = "submit_date",nullable=true)
	private Date submitDate;
	@Column(name = "total_price",nullable=true)
	private Double totalPrice;
	//@Column(name = "kinds",nullable=false)
	@Formula("(select count(1) from ebook_orders_detail eod where eod.order_id = id)") 
	private Integer kinds;
	@Column(name = "status")
	private Integer status;
	@Formula("(select scc.userName from system_user scc where scc.id = operator_id)")
	private String operatorName;
	
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
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}


	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
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
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Integer getKinds() {
		return kinds;
	}
	public void setKinds(Integer kinds) {
		this.kinds = kinds;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getOperatorName() {
		return operatorName;
	}
	public void setOperatorName(String operatorName) {
		operatorName = operatorName;
	}
	public String getChat() {
		return chat;
	}
	public void setChat(String chat) {
		this.chat = chat;
	}

	
}
