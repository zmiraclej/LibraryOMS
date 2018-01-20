package com.flea.modules.report.dao.impl;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.report.dao.ScattergramDao;
import com.flea.modules.report.pojo.LightCirculation;
import com.flea.modules.report.pojo.Scattergram;
import com.flea.modules.report.pojo.vo.ScattergramQueryCriteria;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Function 全景分布图Dao实现类
 * @Author DaiBo
 * @DATE 2017/5/17 14:20
 */
@Repository
public class ScattergramDaoImpl extends BaseDaoImpl<Scattergram, Integer> implements ScattergramDao {

    @Override
    public List<Scattergram> list(ScattergramQueryCriteria params) {
        // 经纬度条件暂未使用.
        SQLQuery query;
        String outerSql = "SELECT a.libName, a.stayLibraryHallCode as hallCode, a.totalBook, b.totalBookBelong, " +
                "c.readership, c.longitude, c.latitude FROM ";

        StringBuffer totalBookSql = new StringBuffer();  // 所在馆的图书数量查询
        StringBuffer totalBookBelongSql = new StringBuffer();  // 所属馆的图书数量查询
        StringBuffer readerShipSql = new StringBuffer(); // 读者数量查询
        //StringBuffer positionSql = new StringBuffer(); //定位信息查询
        totalBookSql.append(" (SELECT l.id AS id, COUNT(lb.id) AS totalBook, stayLibraryHallCode , l.name AS libName FROM " +
                "library_books lb LEFT JOIN librarys l ON lb.stayLibraryHallCode=l.hallCode where l.isEffective =1 ");
        totalBookBelongSql.append(" (SELECT l.id AS id,COUNT(lb.id) AS totalBookBelong FROM library_books lb " +
                "LEFT JOIN librarys l ON lb.belongLibraryHallCode = l.hallCode WHERE l.isEffective = 1 ");
        readerShipSql.append(" (SELECT l.id AS id,COUNT(lau.id) AS readership, lau.library_code ," +
                "SUBSTRING(lnglat, 1, POSITION(',' IN lnglat) - 1) longitude, SUBSTRING(lnglat, POSITION(',' IN lnglat) + 1) latitude " +
                "FROM lib_activate_user lau LEFT JOIN librarys l ON lau.library_code = l.hallCode WHERE l.isEffective = 1  ");

        if (params.getCustomerId() != null) {
            totalBookSql.append(" and  l.customerId=" + params.getCustomerId());
            totalBookBelongSql.append(" and  l.customerId=" + params.getCustomerId());
            readerShipSql.append(" and  l.customerId=" + params.getCustomerId());
        }
        if (StringUtils.isNotBlank(params.getLib())) {
            totalBookSql.append(" and  l.libraryLevel='" + params.getLib() + "'");
            totalBookBelongSql.append(" and  l.libraryLevel='" + params.getLib() + "'");
            readerShipSql.append(" and  l.libraryLevel='" + params.getLib() + "'");
        }
        if (StringUtils.isNotBlank(params.getProvince())) {
            totalBookSql.append(" and l.provinceCode='" + params.getProvince() + "' ");
            totalBookBelongSql.append(" and l.provinceCode='" + params.getProvince() + "' ");
            readerShipSql.append(" and l.provinceCode='" + params.getProvince() + "' ");
        }
        if (StringUtils.isNotBlank(params.getCity())) {
            totalBookSql.append(" and l.cityCode='" + params.getCity() + "' ");
            totalBookBelongSql.append(" and l.cityCode='" + params.getCity() + "' ");
            readerShipSql.append(" and l.cityCode='" + params.getCity() + "' ");
        }
        if (StringUtils.isNotBlank(params.getArea())) {
            if (params.getCustomerId() == null) { // 一级用户
                totalBookSql.append(" and l.areaCode='" + params.getArea() + "' ");
                totalBookBelongSql.append(" and l.areaCode='" + params.getArea() + "' ");
                readerShipSql.append(" and l.areaCode='" + params.getArea() + "' ");
            } else { // 二级
                totalBookSql.append(" and l.areaAddress='" + params.getArea() + "' ");
                totalBookBelongSql.append(" and l.areaAddress='" + params.getArea() + "' ");
                readerShipSql.append(" and l.areaAddress='" + params.getArea() + "' ");
            }
        }
        /*if ("6".equals(params.getOption())) {
            if (StringUtils.isNotBlank(params.getSearchFrom())) {
                totalBookSql.append(" and l.hallCode>='" + params.getSearchFrom() + "' ");
                totalBookBelongSql.append(" and l.hallCode>='" + params.getSearchFrom() + "' ");
                readerShipSql.append(" and l.hallCode<='" + params.getSearchTo() + "' ");
            }
            if (StringUtils.isNotBlank(params.getSearchTo())) {
                totalBookSql.append(" and l.hallCode<='" + params.getSearchTo() + "' ");
                totalBookBelongSql.append(" and l.hallCode<='" + params.getSearchTo() + "' ");
                readerShipSql.append(" and l.hallCode<='" + params.getSearchTo() + "' ");
            }
        }*/
        totalBookSql.append(" GROUP BY stayLibraryHallCode) as a ");
        totalBookBelongSql.append(" GROUP BY belongLibraryHallCode) as b ");
        readerShipSql.append(" GROUP BY library_code) as c ");

        /*//外层条件.
        String whereString = " where 1=1";
        String fieldString = " and ";
        if ("1".equals(params.getOption()) && StringUtils.isNotBlank(params.getKeyWord())) {
            whereString += " and a.libName like '%" + params.getKeyWord() + "%'";
        }
        System.out.println("fieldString: " + fieldString);
        switch (params.getOption()){  // jdk 1.7支持String型
            case "2":
                fieldString += " a.totalIsbn";
                break;
            case "3":
                fieldString += " a.totalBook";
                break;
            case "4":
                fieldString += " a.totalPrice";
                break;
            case "5":
                fieldString += " a.totalIn";
                break;
        }
        System.out.println("fieldString: " + fieldString);
        //
        if (!"6".equals(params.getOption()) && StringUtils.isNotBlank(params.getSearchFrom())) {
            whereString += fieldString + ">=" + params.getSearchFrom();
        }
        if (!"6".equals(params.getOption()) && StringUtils.isNotBlank(params.getSearchTo())) {
            whereString += fieldString + "<=" + params.getSearchTo();
        }*/

        //拼接查询sql
        String str= outerSql+ totalBookSql.toString()
                + " left join "+ totalBookBelongSql.toString() + " on a.id = b.id "
                + " left join "+ readerShipSql.toString() + " on a.id = c.id " ;
        query = getSession().createSQLQuery(str);
        // System.out.println("sql: " + str);
        query.setResultTransformer(new AliasToBeanResultTransformer(Scattergram.class));

        List<Scattergram> scattergram = query.list(); // 记录
        return scattergram;
    }

    @Override
    public List<LightCirculation> listLight(ScattergramQueryCriteria params) {
        // 经纬度条件暂未使用.
        SQLQuery query;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT lc.id, lc.latitude, lc.longitude, lc.updateDate, lc.totalBook, lc.totalBookBelong, " +
                " ll.light_option AS lightOption FROM librarys l LEFT JOIN lib_light ll ON l.hallCode = ll.library_code " +
                " LEFT JOIN lib_circulation lc ON l.hallCode = lc.libraryCode where l.isEffective =1 ");

        if (params.getCustomerId() != null) {
            sql.append(" and  l.customerId=" + params.getCustomerId());
        }
        if (StringUtils.isNotBlank(params.getLib())) {
            sql.append(" and  l.libraryLevel='" + params.getLib() + "'");
        }
        if (StringUtils.isNotBlank(params.getProvince())) {
            sql.append(" and l.provinceCode='" + params.getProvince() + "' ");
        }
        if (StringUtils.isNotBlank(params.getCity())) {
            sql.append(" and l.cityCode='" + params.getCity() + "' ");
        }
        if (StringUtils.isNotBlank(params.getArea())) {
            if (params.getCustomerId() == null) { // 一级用户
                sql.append(" and l.areaCode='" + params.getArea() + "' ");
            } else { // 二级
                sql.append(" and l.areaAddress='" + params.getArea() + "' ");
            }
        }
        /*if ("6".equals(params.getOption())) {
            if (StringUtils.isNotBlank(params.getSearchFrom())) {
                sql.append(" and l.hallCode>='" + params.getSearchFrom() + "' ");
            }
            if (StringUtils.isNotBlank(params.getSearchTo())) {
                sql.append(" and l.hallCode<='" + params.getSearchTo() + "' ");
            }
        }
        //外层条件.
        String fieldString = " and ";
        if ("1".equals(params.getOption()) && StringUtils.isNotBlank(params.getKeyWord())) {
            sql.append(" and l.name like '%" + params.getKeyWord() + "%'");
        }
        if("3".equals(params.getOption())){
            fieldString += " a.totalBook";
        }
        System.out.println("fieldString: " + fieldString);
        //
        if (!"6".equals(params.getOption()) && StringUtils.isNotBlank(params.getSearchFrom())) {
            sql.append(fieldString + ">=" + params.getSearchFrom());
        }
        if (!"6".equals(params.getOption()) && StringUtils.isNotBlank(params.getSearchTo())) {
            sql.append( fieldString + "<=" + params.getSearchTo() );
        }*/

        //拼接查询sql
        query = getSession().createSQLQuery(sql.toString());
        //System.out.println(sql.toString());
        query.setResultTransformer(new AliasToBeanResultTransformer(LightCirculation.class));
        List<LightCirculation> lightList = query.list(); // 记录
        return lightList;
    }

}
