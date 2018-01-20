package com.flea.modules.report.dao;
 
 
import org.springframework.stereotype.Repository;
 
import com.flea.common.dao.BaseDao;
import com.flea.common.pojo.Page;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
 
/**
 * 赔书统计DAO接口
 * @author bruce
 * @version 2016-07-13
 */
@Repository
public interface CompensateBookDao extends BaseDao<CatalogueReport,Integer> {
 
   /**
    * 
    * @method:find
    * @Description:find 获取当前默认列表
    * @author: HeTao
    * @date:2016年7月13日 上午9:43:53
    * @param:@param page
    * @param:@param string
    * @param:@param string2
    * @param:@return
    * @return:Page<CatalogueReport>
    */
   Page<CatalogueReport> find(Page<CatalogueReport> page, String string, String string2);
 
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
   Page<CatalogueReport> find(Page<CatalogueReport> page, QueryCriteria qc);
 
   /**
    * 
    * @method:getSum
    * @Description:getSum    获取默认统计数值
    * @author: HeTao
    * @date:2016年7月13日 下午2:49:30
    * @param:@param string
    * @param:@param string2
    * @param:@return
    * @return:String
    */
   String getSum(String string, String string2);
   
}