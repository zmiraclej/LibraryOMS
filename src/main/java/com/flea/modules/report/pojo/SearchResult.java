/**  
* @Package com.flea.modules.report.pojo
* @Description: TODO
* @author bruce
* @date 2016年7月11日 下午5:11:50
* @version V1.0  
*/ 
package com.flea.modules.report.pojo;

import java.util.List;

/**
 * @author bruce
 * @2016年7月11日 下午5:11:50
 */
public class SearchResult<T> {

	protected ReportTotal  total;
	
	protected List<T> result;
	
	protected  int count;

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public ReportTotal getTotal() {
		return total;
	}

	public void setTotal(ReportTotal total) {
		this.total = total;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
}
