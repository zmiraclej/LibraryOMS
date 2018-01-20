package com.flea.modules.data.util;
/**
 * @author zhangjian
 * 数据上报所需sql
 */
public class DataReportSQL {
	public static String getBorrowedDetailSQL(Integer times){
		String sqlString = "SELECT lbb.id, cast(1 as DECIMAL) as borrowtype,lbb.idCard,lbb.name,lbb.borrowDate,"
				+ "lbb.returnDate,lbb.operUser,lbb.returnUser,lbb.barNumber,lb.properTitle,lo.`name` outname,li.`name` inname,DATE_ADD(lbb.borrowDate,INTERVAL 10 DAY) theoryReturnDate FROM library_borrower_books lbb "
				+ "LEFT JOIN library_books lb ON lbb.libraryBookId = lb.id "
				+ "LEFT JOIN librarys lo ON lo.hallCode  = lbb.hallCode "
				+ "LEFT JOIN librarys li ON li.hallCode = lbb.returnHallCode "
				+ "WHERE lbb.hallCode in (select hallCode from librarys where customerId = 13 ) "
				+ "UNION "
				+ "SELECT lbb.id,cast(2 as DECIMAL) as borrowtype,lbb.idCard,lbb.name,lbb.borrowDate,"
				+ "lbb.returnDate,lbb.operUser,lbb.returnUser,lbb.barNumber,lb.properTitle,lo.`name` outname,li.`name` inname,DATE_ADD(lbb.borrowDate,INTERVAL 10 DAY) theoryReturnDate FROM library_borrower_books lbb "
				+ "LEFT JOIN library_books lb ON lbb.libraryBookId = lb.id "
				+ "LEFT JOIN librarys lo ON lo.hallCode  = lbb.hallCode "
				+ "LEFT JOIN librarys li ON li.hallCode = lbb.returnHallCode "
				+ "WHERE lbb.returnHallCode in (select hallCode from librarys where customerId = 13 ) "
				
//				+ "limit 0,1";
		+ "limit "+times*2+",2";
		return sqlString;
	}
	

	
	public static String getLiteratureDetailSQL(Integer times) {
		String sqlString = "SELECT lbs.id, lbs.isbn, lbs.properTitle, lbs.author, lbs.issuePlace, lbs.press,"
				+ "lbs.publishDate, lbs.collectionTitle, lbs.contentDescript, lbs.classificationNumber, lbs.replicaRate, "
				+ "lbs.barNumber, lbs.catalogueDate,b.imagePathL, ls.`name` "
				+ "FROM library_books lbs LEFT JOIN books b ON lbs.belongLibraryHallCode = b.id "
				+ "LEFT JOIN librarys ls ON lbs.belongLibraryHallCode = ls.hallCode "
				+ "WHERE belongLibraryHallCode in (select hallCode from librarys where customerId = 13 ) "
//				+ "( SELECT l.hallCode FROM librarys l WHERE l.id = ( SELECT sdr.library_id FROM system_data_report sdr WHERE sdr.library_code = ? ))"
				+ "limit "+times*2+",2";
		return sqlString;
	}
	
	public static String getFetchCollectionsSQL(Integer times){
		String sqlString = "SELECT barNumber,isbn,properTitle,replicaRate,barCode,"
				+ "CONCAT(classificationCode,'/',verietyCode) indexnumber,storeroom,"
				+ "frameCode,bookState,storageTime FROM library_books "
				+ "WHERE belongLibraryHallCode in (select hallCode from librarys where customerId = 13 ) "
				+ "limit "+times*2+",2";
		return sqlString;
	}
	
	public static String getUserTotalSQL(){
		String sqlString = "SELECT COUNT(lau.idcard) FROM (SELECT DISTINCT idcard FROM lib_activate_user WHERE library_code in (select hallCode from librarys where customerId = 13 )) lau";
		return sqlString;
	}
	
	public static String getLibVisitorsTotalSQL(){
		String sqlString = "SELECT uniontable.y,SUM(uniontable.num) FROM ("
				+ "(SELECT DATE_FORMAT(lbb.borrowDate,'%Y') y,COUNT(1) num FROM library_borrower_books lbb WHERE lbb.returnDate is NOT NULL AND "
				+ "(hallCode in (select hallCode from librarys where customerId = 13 ) OR returnHallCode in (select hallCode from librarys where customerId = 13 )) GROUP BY  y) "
				+ "UNION ALL "
				+ "(SELECT DATE_FORMAT(lbb.returnDate,'%Y') y,COUNT(1) num FROM library_borrower_books lbb WHERE lbb.returnDate is NOT NULL AND "
				+ "(hallCode in (select hallCode from librarys where customerId = 13 ) OR returnHallCode in (select hallCode from librarys where customerId = 13 )) GROUP BY  y) "
				+ "UNION ALL "
				+ "(SELECT DATE_FORMAT(recommend_time,'%Y') y,COUNT(1) num FROM borrower_recommend lr WHERE lr.recommend_time is not NULL AND "
				+ "hallCode in (select hallCode from librarys where customerId = 13 ) GROUP BY y)"
				+ ") uniontable GROUP BY uniontable.y";
		return sqlString;
	}
	
	public static String getborrowedTotalSQL(){
		String sqlString = "SELECT DATE_FORMAT(borrowDate,'%Y') y,COUNT(1) FROM library_borrower_books WHERE  hallCode in (select hallCode from librarys where customerId = 13 ) GROUP BY y";
		return sqlString;
	}
	
	public static String getactivityDetailSQL(Integer times){
		String sqlString = "SELECT sa.id,sa.title,sa.content,sa.sensitiveWord,sa.activeAddress,"
				+ "sa.startDate,sa.endDate,DATEDIFF(sa.endDate,sa.startDate) continuenum,su.realName contactman,su.phone contactphone,sa.source sponsor,su.phone sponsorphone,sa.source organizer,su.phone organizerphone "
				+ "FROM system_activity sa LEFT JOIN system_user su ON sa.customerId = su.customerId  "
				+ "WHERE su.remark=1 AND su.isEffective=1 AND (sa.customerId = (SELECT customerId FROM librarys WHERE hallCode = 'AAII') AND (sa.type = '单位活动'))"
				+ "UNION "
				+ "SELECT sa.id,sa.title,sa.content,sa.sensitiveWord,sa.activeAddress,"
				+ "sa.startDate,sa.endDate,DATEDIFF(sa.endDate,sa.startDate) continuenum,l.conperson contactman,l.phone contactphone,sa.source sponsor,l.phone sponsorphone,sa.source organizer,l.phone organizerphone "
				+ "FROM system_activity sa LEFT JOIN librarys l ON REPLACE(sa.source,' ','') = REPLACE(l.name,' ','') "
				+ "WHERE sa.type = '图书馆活动' AND l.hallCode = 'AAII'"
				+ "limit "+times*2+",2";
		return sqlString;
	}
	
	public static String getgenaralCollectionEbookSQL(){
		String sqlString = "SELECT COUNT(1) FROM r_customer_ebook WHERE customer_id = (SELECT customerId FROM librarys WHERE hallCode='AAAE')";
		return sqlString;
	}
	
	public static String getgenaralCollectionNormalbookSQL(){
		String sqlString = "SELECT COUNT(1) FROM library_books WHERE belongLibraryHallCode='AAAE'";
		return sqlString;
	}
	
	public static String getactivityTotalSQL(){
		String sqlString = "SELECT DATE_FORMAT(startDate,'%Y') y,COUNT(1) FROM system_activity WHERE (customerId = (SELECT customerId FROM librarys WHERE hallCode = 'AAAE') AND type = '单位活动') OR "
				+ "(type = '图书馆活动' AND REPLACE(source,' ','')= REPLACE((SELECT NAME FROM librarys WHERE hallCode = 'AAAE'),' ','')) GROUP BY y ORDER BY y DESC;";
		return sqlString;
	}
}
