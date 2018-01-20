package com.flea.modules.report.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.report.pojo.LightCirculation;
import com.flea.modules.report.pojo.Scattergram;
import com.flea.modules.report.pojo.vo.ScattergramQueryCriteria;

import java.util.List;
import java.util.Map;

/**
 * @Function
 * @Author DaiBo
 * @DATE 2017/5/17 14:19
 */
public interface ScattergramDao extends BaseDao<Scattergram,Integer> {

    /**
     * @method:list
     * @Description: 查询数据
     * @author: DaiBo
     * @date:2017年5月17日 11点26分
     * @param query  查询条件
     * @return List<Scattergram>
     */
    List<Scattergram> list(ScattergramQueryCriteria query);

    /**
     * @method:listLight
     * @Description: 查询点亮数据
     * @author: DaiBo
     * @date:2017年5月22日 10点19分
     * @param query  查询条件
     * @return List<Map>
     */
    List<LightCirculation> listLight(ScattergramQueryCriteria query);


}
