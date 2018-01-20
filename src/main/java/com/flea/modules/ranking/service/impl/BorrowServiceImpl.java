package com.flea.modules.ranking.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.ranking.pojo.Clicklike;
import com.flea.modules.ranking.service.BorrowService;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.util.ExprotPDF;
import com.flea.modules.ranking.dao.BorrowDao;

/**
 * 点赞排行Service
 * @author bruce
 * @version 2016-07-21
 */
 @Service
public class BorrowServiceImpl extends BaseServiceImpl<Clicklike,Integer> implements BorrowService{

	@Autowired
	private BorrowDao borrowDao;
	
	
	@Autowired
	public  BorrowServiceImpl(BorrowDao borrowDao) {
		super(borrowDao);
		this.borrowDao = borrowDao;
	}
	/**
	 * 
	 * @method:find
	 * @Description:find	获取当前默认列表
	 * @author: HeTao
	 * @date:2016年7月13日 上午9:41:43
	 * @param:@param page
	 * @param:@param list
	 * @param:@param list2
	 * @param:@return
	 * @return:Page<CatalogueReport>
	 */
	@Override
	public Page<Clicklike> find(Page<Clicklike> page, List<String> list, List<String> list2) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			 return page;
		}else{
			return borrowDao.find(page,list.get(0),list2.get(0));
		}
	}

	/**
	 * 
	 * @method:getQueryInfo
	 * @Description:getQueryInfo 根据查询条件查询
	 * @author: HeTao
	 * @date:2016年7月13日 上午11:23:12
	 * @param:@param qc
	 * @param:@param req
	 * @param:@param resp
	 * @param:@return
	 * @return:String
	 */
	@Override
	public Page<Clicklike> find(Page<Clicklike> page, QueryCriteria qc) {
		// TODO Auto-generated method stub
		return borrowDao.find(page,qc);
	}

	/**
	 * 
	 * @method:getSum
	 * @Description:getSum	获取默认统计数值
	 * @author: HeTao
	 * @date:2016年7月13日 下午2:49:30
	 * @param:@param string
	 * @param:@param string2
	 * @param:@return
	 * @return:String
	 */
	@Override
	public String getSum(String string, String string2) {
		// TODO Auto-generated method stub
		return borrowDao.getSum(string, string2);
	}
	/**
	 * 
	 * @method:exprotListPDF	导出清单
	 * @Description:exprotListPDF
	 * @author: HeTao
	 * @date:2016年7月25日 下午3:57:39
	 * @param:@param qc
	 * @param:@param request
	 * @param:@param response
	 * @return:void
	 */
	@Override
	public String exprotListPDF(QueryCriteria qc, HttpServletRequest request) {
		Page<Clicklike> result = borrowDao.find(null,qc);
		if(result.getList().size() == 0 ){
    		return "";
    	}
    	return ExprotPDF.getInstance().exportRanking(qc, request, result.getList(),"借阅排行榜统计","Borrow");
	}
}
