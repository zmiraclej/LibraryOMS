package com.flea.modules.system.dao.impl;

 
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.flea.common.dao.impl.BaseDaoImpl;
import com.flea.modules.system.dao.LibrarysUpdateStatusDao;
import com.flea.modules.system.pojo.LibrarysUpdateStatus;
/**
 * 
 * @ClassName: LibrarysUpdateStatusDaoImpl
 * @Description:当用户的信息状态为修改待审时，平台给驳回了，则用户显示的信息为原来的数据状态为修改驳回，而不是更新之后的数据，
 * @author QL
 * @date 2017年5月10日 下午7:50:54
 */
@Repository
public class LibrarysUpdateStatusDaoImpl extends BaseDaoImpl<LibrarysUpdateStatus, Integer> implements  LibrarysUpdateStatusDao{
	@Override
	public boolean updateLibrarys(Integer libraryId) {
		StringBuffer sql =new StringBuffer();
		sql.append("UPDATE librarys a LEFT JOIN librarys_update_status b ON a.id = b.libraryId SET a.name = b.name, a.address = b.address, ");
		sql.append("a.lngLat = b.lngLat, a.agreementAccount = b.agreementAccount, a.acountName = b.acountName, a.creditLines = b.creditLines, "); 
        sql.append("a.conperson = b.conperson, a.phone = b.phone, a.fixphone = b.fixphone, a.email = b.email  WHERE a.id = ? ");
		SQLQuery query = this.getSession().createSQLQuery(sql.toString());
		query.setInteger(0, libraryId);
		return query.executeUpdate()>0?true:false;
	}
}
