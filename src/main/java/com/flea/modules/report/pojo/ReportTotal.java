/**  
* @Package com.flea.modules.report.pojo
* @Description: TODO
* @author bruce
* @date 2016年7月11日 下午5:20:54
* @version V1.0  
*/ 
package com.flea.modules.report.pojo;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author bruce
 * @2016年7月11日 下午5:20:54
 */
public class ReportTotal {

	private BigDecimal sumIsbn =new BigDecimal(0);
	
	private BigDecimal sumBook  =new BigDecimal(0);
	
	private Double sumPrice =0.00;
	//所属总数
	private BigDecimal sumTotalBelong = new BigDecimal(0);
	//所属总价
	private Double sumPriceBelong = 0.00;
	
	private BigInteger  sumCount;

	
	
	
	public BigDecimal getSumTotalBelong() {
		return sumTotalBelong;
	}

	public void setSumTotalBelong(BigDecimal sumTotalBelong) {
		this.sumTotalBelong = sumTotalBelong;
	}

	public Double getSumPriceBelong() {
		return sumPriceBelong;
	}

	public void setSumPriceBelong(Double sumPriceBelong) {
		this.sumPriceBelong = sumPriceBelong;
	}

	public Double getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(Double sumPrice) {
		this.sumPrice = sumPrice;
	}

	public BigDecimal getSumIsbn() {
		return sumIsbn;
	}

	public void setSumIsbn(BigDecimal sumIsbn) {
		this.sumIsbn = sumIsbn;
	}

	public BigDecimal getSumBook() {
		return sumBook;
	}

	public void setSumBook(BigDecimal sumBook) {
		this.sumBook = sumBook;
	}

	public BigInteger getSumCount() {
		return sumCount;
	}

	public void setSumCount(BigInteger sumCount) {
		this.sumCount = sumCount;
	}
	
}
