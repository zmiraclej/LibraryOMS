package com.flea.modules.report.dao;

import com.flea.common.dao.BaseDao;
import com.flea.modules.report.pojo.LightCirculation;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Function
 * @Author DaiBo
 * @DATE 2017/5/22 17:25
 */
@Repository
public interface LightCirculationDao extends BaseDao<LightCirculation,Integer> {


    /**
     * @method: getUpdateDate
     * @Description: 查询点亮热度数据更新的时间
     * @author: DaiBo
     * @date:2017年5月22日 15点19分
     * @param
     * @return Map
     */
    Map<String,Object> getUpdateLightColorDate();

    /**
     * @method: updateLightInfo
     * @Description: 更新点亮热度数据到数据表中
     * @author: DaiBo
     * @date:2017年5月22日 15点31分
     * @param
     * @return void
     */
    void updateLightInfo();
}
