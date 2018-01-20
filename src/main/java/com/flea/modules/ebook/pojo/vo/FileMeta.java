/**  
* @Package com.flea.modules.ebook.pojo.vo
* @Description: TODO
* @author bruce
* @date 2016年6月30日 下午2:19:02
* @version V1.0  
*/ 
package com.flea.modules.ebook.pojo.vo;

import com.flea.common.pojo.User;

/**
 * @author bruce
 * @2016年6月30日 下午2:19:02
 */
public class FileMeta {
	
	private String bookId;
	private String fileName;
	private String fileSize;
	private String fileType;
	private User user;
	private String remark;
	
	private byte[] bytes;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
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
	public byte[] getBytes() {
		return bytes;
	}
	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBookId() {
		return bookId;
	}
	public void setBookId(String bookId) {
		this.bookId = bookId;
	}
	
}
