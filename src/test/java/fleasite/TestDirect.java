package fleasite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

import com.flea.common.util.Config;
import com.flea.common.util.DateUtils;
import com.flea.common.util.ImportExcel;
import com.flea.common.util.PasswordHelper;
import com.flea.common.util.SendMessages;
import com.flea.modules.system.util.PublicDataUtils;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;


/**
 * @author bruce
 * @2016年7月6日 上午10:13:46
 */
public class TestDirect {

	@Test
	public void testExcel(){
		try {
			int a=398; 
			int b=1024; 
			double c; 
			
			String fString = "abcde";
			System.out.println(fString.substring(0, 4));
			String abc = String.format("%.2f",Double.valueOf(fString));
			System.out.println(abc);
			double d=4.0;
			double e=5.0;
			System.out.println((double)3/5);
			
			c=(double)(Math.round(d/e));//这样为保持2位 
			
			System.out.println(c);
			
			BigDecimal n = new BigDecimal(398);
			BigDecimal m = new BigDecimal(398);
			
			String fileString = "'Twixt Land & Sea.epub";
			fileString = fileString.replaceAll("'", "").substring(0, fileString.lastIndexOf(".")-1);
			System.out.println(fileString);
			
			ImportExcel  excel = new ImportExcel("C:\\四原科技-需调取样书的书目-已补齐信息.xls",0);
			Row rows =excel.getRow(excel.getLastDataRowNum());
			Map<String, String> cellMap = excel.getCell(0);
			String row = cellMap.get("自然：消灭的自然灾难");
			
			Row bookRow = excel.getRow(Integer.parseInt(row));
			Double isbn = bookRow.getCell(1).getNumericCellValue();
			String author = bookRow.getCell(2).getStringCellValue();
			String publisher = bookRow.getCell(3).getStringCellValue();
			Double publishDate = bookRow.getCell(4).getNumericCellValue();
			String type = bookRow.getCell(5).getStringCellValue();
			String summary = bookRow.getCell(6).getStringCellValue();
			String price = bookRow.getCell(6).getStringCellValue();
			System.out.println(String.format("%.0f",isbn));
//			for(String key:cellMap.keySet()){
//				Integer row1 = cellMap.get(key);
//				System.out.println(row1);
//			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		}
	}
	
	
 
	
	@Test
	public void testLinkedList() {
		List<Map<String,String>> typeMap = PublicDataUtils.getNewsType();
		for(Map<String,String> map:typeMap){
			String key = map.get("key");
			if(key.equals("4"))
			System.out.println(map.get("val"));
		}
	}
@Test	
public void	 testArrayList(){
		  String fileName ="8月考勤汇总表_20160913152653.xlsx";
		  String  prefix = fileName.substring(0, fileName.lastIndexOf("_"));
		  String suffix = fileName.substring(fileName.lastIndexOf("."));
		  System.out.println(prefix);
		  System.out.println(suffix);
		  
}

@Test
public void testPwd() throws ApiException {
	SendMessages.sendMessages("18080241296", "{code:'654321'}","");
}

@Test
public void testNPwd() throws ApiException {
	String url ="http://gw.api.taobao.com/router/rest";
	String appkey ="23431204";
	String secret ="9c79e70414d680c1c3ed1fb6a018b228";
	TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
	AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
	req.setExtend("");
	req.setSmsType("normal");
	req.setSmsFreeSignName("云图书馆");
	req.setSmsParamString( "{name:'1242256'}" ); 
	req.setRecNum("18080241296");
	req.setSmsTemplateCode("SMS_13002096");
	AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
	System.out.println(rsp.getBody());
}

@Test
public void testISBN(){
  String isbn =	getISBN13("7010000085");
  System.out.println(isbn);
}

private static String getISBN13(String isbn) {
	if (isbn.length() != 10) {
		return isbn;
	}
	isbn = isbn.substring(0, isbn.length() - 1);
	isbn = "978" + isbn;
	int a = 0;
	int b = 0;
	int c = 0;
	int d = 0;
	for (int i = 0; i < isbn.length(); i++) {
		int x = Integer.parseInt(isbn.substring(i, i+1));
		if (i % 2 == 0) {
			a += x;
		} else {
			b += x;
		}
	}
	c = a + 3 * b;
	d = 10 - c % 10;
	isbn = isbn + d;
	return isbn;
}

  public static void main(String[] args) {
//	  String sql ="2016-12-16";
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//	try {
//		System.out.println(String.format("%tF%n",sdf.parse(sql).toString()));
//	} catch (ParseException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
	
	  		
	 String sqlString =  DateUtils.formatDate("2016-12-16", "yyyyMMdd");
	 System.out.println(sqlString);
  }
  
	
}
 
