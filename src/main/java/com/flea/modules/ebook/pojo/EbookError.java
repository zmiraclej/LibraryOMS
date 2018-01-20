package com.flea.modules.ebook.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.flea.common.pojo.User;

@Entity
@Table(name="system_ebook_errordata")
public class EbookError implements Serializable{

	/**
	 * @Fields serialVersionUID : 序列号
	 */ 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GenericGenerator(name="generator", strategy="identity")
	@GeneratedValue(generator="generator")
	@Column(name ="id", unique=true, nullable= false)
	private Integer id;
	
	//文件名称
	@Column(name ="fileName")
	private String fileName;
	//文件大小
	@Column(name="fileSize")
	private String fileSize;
	//文件类型
	@Column(name="fileType")
	private String fileType; 
	//备注
	@Column(name="remark")
	private String remark;
	
	@Column(name="uploadTime")
	private String uploadTime;
	
	@ManyToOne
	@JoinColumn(name ="systemUser")
	private User user;

	public User getUser() {
		return user;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileSize() {
		return fileSize;
	}

	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
