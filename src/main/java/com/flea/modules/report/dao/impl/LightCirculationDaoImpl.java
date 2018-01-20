package com.flea.modules.report.dao.impl;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.report.dao.LightCirculationDao;
import com.flea.modules.report.pojo.LightCirculation;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @Function
 * @Author DaiBo
 * @DATE 2017/5/22 17:49
 */
@Repository
public class LightCirculationDaoImpl extends BaseDaoImpl<LightCirculation, Integer> implements LightCirculationDao {

    @Override
    public Map<String, Object> getUpdateLightColorDate() {
        String sql = " select updateDate from lib_circulation limit 1";
        SQLQuery query = getSession().createSQLQuery(sql);
        Map resultMap = new HashMap();
        resultMap.put("updateDate",query.uniqueResult());
        return resultMap;
    }

    @Override
    public void updateLightInfo() {
        String sql = " delete from lib_circulation ";
        SQLQuery query = getSession().createSQLQuery(sql);
        query.executeUpdate();

        String allInfoSql = " SELECT a.hallCode AS libraryCode, " +
                " CASE WHEN a.totalBook IS NULL THEN 0 ELSE a.totalBook END AS totalBook , " +
                " CASE WHEN b.totalBookBelong IS NULL THEN 0 ELSE b.totalBookBelong END AS totalBookBelong , " +
                " CASE WHEN a.longitude IS NULL THEN 0 ELSE a.longitude END AS longitude ," +
                " CASE WHEN a.latitude IS NULL THEN 0 ELSE a.latitude END AS latitude , " +
                " CURRENT_DATE() AS updateDate " +
                " FROM (SELECT  l.id AS id, COUNT(lb.id) AS totalBook, hallCode, " +
                " SUBSTRING(lnglat, 1, POSITION(',' IN lnglat) - 1) longitude, SUBSTRING(lnglat, POSITION(',' IN lnglat) + 1) latitude " +
                " FROM librarys l LEFT JOIN library_books lb ON lb.stayLibraryHallCode = l.hallCode WHERE l.isEffective = 1     " +
                " GROUP BY hallCode) AS a LEFT JOIN (SELECT l.id AS id, COUNT(lb.id) AS totalBookBelong " +
                " FROM library_books lb LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode " +
                " WHERE l.isEffective = 1 GROUP BY belongLibraryHallCode) AS b ON a.id = b.id ";
        query = getSession().createSQLQuery(allInfoSql);
        query.setResultTransformer(new AliasToBeanResultTransformer(LightCirculation.class));
        query.addScalar("totalBook", StandardBasicTypes.INTEGER);
        query.addScalar("totalBookBelong", StandardBasicTypes.INTEGER);
        query.addScalar("libraryCode",StandardBasicTypes.STRING);
        query.addScalar("longitude",StandardBasicTypes.STRING);
        query.addScalar("latitude",StandardBasicTypes.STRING);
        query.addScalar("updateDate",StandardBasicTypes.DATE);

        Collection<LightCirculation> collection = query.list();
        this.batchSave(collection);
    }
}
