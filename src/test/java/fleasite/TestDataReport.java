package fleasite;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flea.modules.data.dao.BorrowedDetailDao;
import com.flea.modules.data.dao.LiteratureDetailDao;
import com.flea.modules.data.mapping.BorrowedDetail;
import com.flea.modules.data.mapping.LiteratureDetail;
import com.flea.modules.data.pojo.vo.DataAuthoInfo;
import com.flea.modules.data.service.DataReportingService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-context.xml")
public class TestDataReport {
	
	@Autowired
	DataReportingService dataReportingService;
	
	@Autowired
	LiteratureDetailDao libraryBookDao;
	
	@Autowired
	BorrowedDetailDao borrowedDetailDao;
	@Test
	public void testFindDeadLine() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0031566");
		dataAuthoInfo1.setLibcode("36EE579680105981");
		dataAuthoInfo1.setMethodcode("finddeadline");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo1);
		
		DataAuthoInfo dataAuthoInfo2 = new DataAuthoInfo();
		dataAuthoInfo2.setLibcode("AA0031566");
		dataAuthoInfo2.setLibcode("4D059F866502D318");
		dataAuthoInfo2.setMethodcode("updatedeadline");
		dataAuthoInfo2.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo2.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo2);
		
		DataAuthoInfo dataAuthoInfo4 = new DataAuthoInfo();
		dataAuthoInfo4.setLibcode("AA0031566");
		dataAuthoInfo4.setLibcode("390CDF9012727EB1");
		dataAuthoInfo4.setMethodcode("borrowedTotal");
		dataAuthoInfo4.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo4.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo4);
		
		DataAuthoInfo dataAuthoInfo5 = new DataAuthoInfo();
		dataAuthoInfo5.setLibcode("AA0031566");
		dataAuthoInfo5.setLibcode("BA0EB7723DE99FB4");
		dataAuthoInfo5.setMethodcode("borrowedDetail");
		dataAuthoInfo5.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo5.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo5);
		
		
		DataAuthoInfo dataAuthoInfo6 = new DataAuthoInfo();
		dataAuthoInfo6.setLibcode("AA0031566");
		dataAuthoInfo6.setLibcode("603EBB6589891B90");
		dataAuthoInfo6.setMethodcode("libVisitorsTotal");
		dataAuthoInfo6.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo6.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo6);
		
		DataAuthoInfo dataAuthoInfo7 = new DataAuthoInfo();
		dataAuthoInfo7.setLibcode("AA0031566");
		dataAuthoInfo7.setLibcode("9DFE39539C8E6AEF");
		dataAuthoInfo7.setMethodcode("activityTotal");
		dataAuthoInfo7.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo7.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo7);
		
		DataAuthoInfo dataAuthoInfo8 = new DataAuthoInfo();
		dataAuthoInfo8.setLibcode("AA0031566");
		dataAuthoInfo8.setLibcode("B17690A779B8377F");
		dataAuthoInfo8.setMethodcode("activityDetail");
		dataAuthoInfo8.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo8.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo8);
		
		DataAuthoInfo dataAuthoInfo9 = new DataAuthoInfo();
		dataAuthoInfo9.setLibcode("AA0031566");
		dataAuthoInfo9.setLibcode("C4914F78E43075DD");
		dataAuthoInfo9.setMethodcode("exhibitionTotal");
		dataAuthoInfo9.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo9.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo9);
		
		DataAuthoInfo dataAuthoInfo10 = new DataAuthoInfo();
		dataAuthoInfo10.setLibcode("AA0031566");
		dataAuthoInfo10.setLibcode("3F8B4D56A8941C28");
		dataAuthoInfo10.setMethodcode("exhibitionDetail");
		dataAuthoInfo10.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo10.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo10);
		
		DataAuthoInfo dataAuthoInfo11 = new DataAuthoInfo();
		dataAuthoInfo11.setLibcode("AA0031566");
		dataAuthoInfo11.setLibcode("C68D10BCF8FFB292");
		dataAuthoInfo11.setMethodcode("annualWebsiteVisits");
		dataAuthoInfo11.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo11.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo11);
		
		DataAuthoInfo dataAuthoInfo12 = new DataAuthoInfo();
		dataAuthoInfo12.setLibcode("AA0031566");
		dataAuthoInfo12.setLibcode("65BB66958723ACEA");
		dataAuthoInfo12.setMethodcode("genaralCollection");
		dataAuthoInfo12.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo12.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo12);
		
		DataAuthoInfo dataAuthoInfo13 = new DataAuthoInfo();
		dataAuthoInfo13.setLibcode("AA0031566");
		dataAuthoInfo13.setLibcode("BCDC27E8B1CD1263");
		dataAuthoInfo13.setMethodcode("literatureDetail");
		dataAuthoInfo13.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo13.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo13);
		
		DataAuthoInfo dataAuthoInfo14 = new DataAuthoInfo();
		dataAuthoInfo14.setLibcode("AA0031566");
		dataAuthoInfo14.setLibcode("2720396CF3380481");
		dataAuthoInfo14.setMethodcode("fetchCollections");
		dataAuthoInfo14.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo14.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo14);
		
		DataAuthoInfo dataAuthoInfo15 = new DataAuthoInfo();
		dataAuthoInfo15.setLibcode("AA0031566");
		dataAuthoInfo15.setLibcode("939A7B97403C2E01");
		dataAuthoInfo15.setMethodcode("addliterature");
		dataAuthoInfo15.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo15.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo15);
		
		DataAuthoInfo dataAuthoInfo3 = new DataAuthoInfo();
		dataAuthoInfo3.setLibcode("AA0031566");
		dataAuthoInfo3.setLibcode("52DFBECA21E0728D");
		dataAuthoInfo3.setMethodcode("userTotal");
		dataAuthoInfo3.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo3.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo3);
		
		DataAuthoInfo dataAuthoInfo16 = new DataAuthoInfo();
		dataAuthoInfo16.setLibcode("AA0031566");
		dataAuthoInfo16.setLibcode("E7D2E0B4D5B3BC04");
		dataAuthoInfo16.setMethodcode("userDetail");
		dataAuthoInfo16.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo16.setUnicode("5D7D13470CE89B728B7CF251E5458191");
		dataAuthoInfos.add(dataAuthoInfo16);
		String result = dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testHQL(){
		String sqlString = "SELECT lbs.isbn,lbs.properTitle,lbs.author,lbs.issuePlace,lbs.press,lbs.publishDate,"
				+ "lbs.collectionTitle,lbs.contentDescript,lbs.classificationNumber,lbs.replicaRate,"
				+ "lbs.catalogueCode,lbs.catalogueDate,b.imagePathL from "
				+ "(SELECT * FROM library_books lb WHERE lb.belongLibraryHallCode = "
				+ "(SELECT l.hallCode FROM librarys l WHERE l.id ="
				+ "(SELECT sdr.library_id from system_data_report sdr WHERE sdr.library_code = ? ))) lbs"
				+ " LEFT JOIN books b ON lbs.belongLibraryHallCode=b.id";
				List<Object> values = new ArrayList<Object>();
				values.add("31223132");
				List<LiteratureDetail> listBySQL = libraryBookDao.getLiteratureDetailsBySql(sqlString.toString(), values);
				for (Object object : listBySQL) {
					System.out.println(object.getClass());
					
				}
	}

	@Test
	public void testBorrowedDetailDaoImpl(){
		// 获得请求体所需数据
				String sqlString = "SELECT cast(1 as DECIMAL) as borrowtype,lbb.idCard,lbb.name,lbb.borrowNumber,"
						+ "lbb.hallCode,lbb.returnHallCode,lbb.borrowDate,"
						+ "lbb.returnDate,lbb.operUser,lbb.returnUser FROM library_borrower_books lbb  WHERE hallCode ="
						+ "(SELECT l.hallCode FROM librarys l WHERE l.id ="
						+ "(SELECT sdr.library_id from system_data_report sdr WHERE sdr.library_code = ? ))"
						+ "UNION ALL "
						+ "SELECT cast(2 as DECIMAL) as borrowtype,lbb.idCard,lbb.name,lbb.borrowNumber,"
						+ "lbb.hallCode,lbb.returnHallCode,lbb.borrowDate,"
						+ "lbb.returnDate,lbb.operUser,lbb.returnUser FROM library_borrower_books lbb  WHERE returnHallCode ="
						+ "(SELECT l.hallCode FROM librarys l WHERE l.id ="
						+ "(SELECT sdr.library_id from system_data_report sdr WHERE sdr.library_code = ? ))";
		List<Object> values = new ArrayList<Object>();
		values.add("AA0031566");
		values.add("AA0031566");
		List<BorrowedDetail> listBySQL = borrowedDetailDao.getBorrowedDetailsBySql(sqlString.toString(), values);
		for (BorrowedDetail object : listBySQL) {
			System.out.println(object.getId());
			
		}
	}
	
	
	@Test
	public void testFetchCollections() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("fetchCollections");
		dataAuthoInfo1.setMethodcode("DD4377E82E1F1111");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
		
	}
	
	@Test
	public void testborrowedDetail() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("borrowedDetail");
		dataAuthoInfo1.setMethodcode("365E48A9B67F5BCB");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testliteratureDetail() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("literatureDetail");
		dataAuthoInfo1.setMethodcode("11E7FF43ACC47C0A");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testUserTotal() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("userTotal");
		dataAuthoInfo1.setMethodcode("4716E2FC747A9955");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testLibVisitorsTotal() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("libVisitorsTotal");
		dataAuthoInfo1.setMethodcode("1E41D9BB65AB5022");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testborrowedTotal() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("borrowedTotal");
		dataAuthoInfo1.setMethodcode("951717C84CD24AEF");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testactivityDetaill() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("activityDetail");
		dataAuthoInfo1.setMethodcode("FAFD8FE242EE6AFA");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testfinddeadline() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("finddeadline");
		dataAuthoInfo1.setMethodcode("100C07046BF88D5C");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testgenaralCollection() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("genaralCollection");
		dataAuthoInfo1.setMethodcode("B021EB3FFB20EC93");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
	
	@Test
	public void testactivityTotal() throws Exception{
		List<DataAuthoInfo> dataAuthoInfos = new ArrayList<DataAuthoInfo>();
		DataAuthoInfo dataAuthoInfo1 = new DataAuthoInfo();
		dataAuthoInfo1.setLibcode("AA0070338");
		dataAuthoInfo1.setMethodname("activityTotal");
		dataAuthoInfo1.setMethodcode("BA06E1554FC546ED");
		dataAuthoInfo1.setServerurl("http://218.22.58.75:18081/evaReport/rest/dataRepService/");
		dataAuthoInfo1.setUnicode("008E9DED35D2C40E0EFA7D2DB7278D70");
		dataAuthoInfos.add(dataAuthoInfo1);
		dataReportingService.entrance(dataAuthoInfos);
	}
}
