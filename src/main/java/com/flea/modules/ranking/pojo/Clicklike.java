package com.flea.modules.ranking.pojo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

public class Clicklike implements Serializable{
	private static final long serialVersionUID = 7750815498060520400L;
	//名次
	private String ranking;
	//ISBN
	private String isbn;
	//书名
	private String bookName;
	//出版社
	private String press;
	//定价
	private double price;
	//著者
	private String author;
	//年份
	private String date;
	//分类号
	private String typeNumber;
	//赞数
	private BigInteger click;
	//集合总数
	private String sumNumber;	
	//赞数复
	private BigInteger clickt;
	
	
	public BigInteger getClickt() {
		return clickt;
	}
	public void setClickt(BigInteger clickt) {
		this.clickt = clickt;
	}
	public String getRanking() {
		return ranking;
	}
	public void setRanking(BigInteger ranking) {
		this.ranking = ranking.toString();
	}
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTypeNumber() {
		return typeNumber;
	}
	public void setTypeNumber(String typeNumber) {
		this.typeNumber = typeNumber;
	}
	public BigInteger getClick() {
		return click;
	}
	public void setClick(BigInteger click) {
		this.click = click;
	}
	public String getSumNumber() {
		return sumNumber;
	}
	public void setSumNumber(String sumNumber) {
		this.sumNumber = sumNumber;
	}
	@Override
	public String toString() {
		return "Clicklike [ranking=" + ranking + ", isbn=" + isbn + ", bookName=" + bookName + ", press=" + press
				+ ", price=" + price + ", author=" + author + ", date=" + date + ", typeNumber=" + typeNumber
				+ ", click=" + click + "]";
	}
	
}
