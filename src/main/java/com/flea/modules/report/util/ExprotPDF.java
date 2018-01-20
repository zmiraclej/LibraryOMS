package com.flea.modules.report.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.flea.common.Common;
import com.flea.common.pojo.User;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.pdf.PdfHelper;
import com.flea.modules.ranking.pojo.Clicklike;
import com.flea.modules.report.pojo.CatalogueReport;
import com.flea.modules.report.pojo.Circulated;
import com.flea.modules.report.pojo.LightReport;
import com.flea.modules.report.pojo.ReaderReport;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.impl.CustomCellTwo;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ExprotPDF {
	private final int headColNumber = 4;
	//表格的设置
    private final int spacing = 0;
    //表格的设置
    private  final int padding = 2;
    //日期
    private static Date date;
    //生成格式
	private static SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
	//随机
	private static Random rd = new Random();
	//防止外部创建对象
	private ExprotPDF() {}
	private static ExprotPDF ex;
	public static ExprotPDF getInstance() {
		if(ex == null){
			ex = new ExprotPDF();
		}
		return ex;
	}
	
	public void exprotPDFList(String realPath, HttpServletResponse response, String string) {
		File file = new File(realPath);
        // 取得文件名。
        try {
			String filename = file.getName();
			//生成下载文件名
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			date = new Date();
			String aa = sdf.format(date);
			string = aa+string+".pdf";
			
			InputStream fis = new BufferedInputStream(new FileInputStream(realPath));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename=" +parseGBK(string));
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/pdf");
			toClient.write(buffer);
			toClient.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			file.delete();
		}
	}
	
	
	//在本地生成PDF文件-------
	public String exportLibraryBookPDF(QueryCriteria query,HttpServletRequest rquest,List<CatalogueReport> result,String title) {
		//获取当前运行环境路径
		String realPath = rquest.getSession().getServletContext().getRealPath("/");
		realPath = realPath.replace("library\\", "");
		File fileDir = new File(realPath+"/pdf");
		if(!fileDir.isDirectory()){
			fileDir.mkdirs();
		}
		realPath =realPath +"/pdf/";
		//生成唯一文件名
		realPath = createFileName(realPath);
		Document document = new Document();
    	document.setPageSize(PageSize.A4);
    	String[] tableHeader = { "馆号", "馆名", "日期","单号", "册数", "码洋","操作员"};
    	//数据表字段数
    	int colNumber = 7;
        try {
        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
        	writer.setPageEvent(new PdfHelper());
            document.open();
            // 中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
            
        	User user =  ShiroUtils.getCurrentUser();
    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
    		   query.setCustomerId(user.getCustomer().getId());
    	    }
            PdfPTable table = new PdfPTable(headColNumber);
            int[] cellWidth = {14,14,14,2};
            table.setWidths(cellWidth);
            table.setWidthPercentage(100);
            table.getDefaultCell().setPadding(padding);
            table.getDefaultCell().setBorderWidth(spacing);
            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            table.getDefaultCell().setBorder(0);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            
            PdfPCell cell = new PdfPCell(); 
            Paragraph cel = new Paragraph(title,fontTitle);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("\n",fontHeader);
            cell=new PdfPCell(cel); 
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("地区:  "+query.getArea(),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("馆别:  "+query.getLib(),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            String dateFrom = changeString(query.getDateFrom());
            String dateTo = changeString(query.getDateTo());
            String scFrom = query.getSearchFrom();
            String scTo = query.getSearchTo();
            
            
            String option ="",val="";
            if("1".equals(query.getOption())){
            	option = "馆名";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            } else if("2".equals(query.getOption())){
            	option = "日期";
            	if(dateFrom.trim().length()>0&&dateTo.trim().length()>0){
            		val = dateFrom+" → "+dateTo;
            	}else if(dateFrom.trim().length()>0&&dateTo.trim().length()==0){
            		val = dateFrom+" → ";
            	}else if(dateFrom.trim().length()==0&&dateTo.trim().length()>0){
            		val = " → "+dateTo;
            	}
            	//val = changeString(query.getDateFrom())+" →  "+changeString(query.getDateTo());
            }else if("3".equals(query.getOption())){
            	option = "单号";
            	val = query.getSearchFrom()+" →  "+query.getSearchTo();
            }else if("4".equals(query.getOption())){
            	option = "册数";
            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
            		val = scFrom+" → "+scTo;
            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
            		val = scFrom+" → ";
            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
            		val = " → "+scTo;
            	}
            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
            }else if("5".equals(query.getOption())){
            	option = "码洋";
            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
            		val = scFrom+" → "+scTo;
            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
            		val = scFrom+" → ";
            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
            		val = " → "+scTo;
            	}
            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
            }else if("6".equals(query.getOption())){
            	option = "操作员";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            }
            
            if("0".equals(query.getOption())){
            	cel = new Paragraph("馆号:  "+query.getSearchFrom()+"→"+query.getSearchTo(),fontHeader);
            }else{
            	cel = new Paragraph(option+":  "+val,fontHeader);
            }
            cell=new PdfPCell(cel);  
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            
            cel = new Paragraph("",fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            int size = result.size();
            CatalogueReport ct = result.get(size-1);
            String sum = ct.getSumNumber();
            String[] arr = sum.split(",");
            
            cel = new Paragraph("合计册数:  "+arr[0],fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            
            cel = new Paragraph("合计码洋:  "+String.format("%.2f",Double.parseDouble(arr[1])),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("\n",fontHeader);
            cell=new PdfPCell(cel); 
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("\n",fontHeader);
            cell=new PdfPCell(cel); 
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            document.add(table);
            
            //表格内容
            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
            // 创建有colNumber(10)列的表格
            PdfPTable datatable = new PdfPTable(colNumber);
//            int[] cellsWidth = { 3,7,3,3,3,4};
            int[] cellsWidth = {3,7,3,3,3,4,4};
            datatable.setWidths(cellsWidth);
            datatable.setWidthPercentage(100); // 表格的宽度百分比
            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
            datatable.getDefaultCell().setBorderWidthLeft(0);
            datatable.getDefaultCell().setBorderWidthRight(0);
            datatable.getDefaultCell().setBorderWidthTop(0);
            datatable.getDefaultCell().setPaddingTop(6f);
            datatable.getDefaultCell().setPaddingBottom(6f);
            datatable.getDefaultCell().setBorderWidth(6f);
            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            CustomCellTwo border = new CustomCellTwo();
            datatable.getDefaultCell().setCellEvent(border);//虚线设置
            
            datatable.getDefaultCell().setHorizontalAlignment(
                    Element.ALIGN_CENTER);
            
            // 添加表头元素
            for (int i = 0; i < colNumber; i++) {
            	cel = new Paragraph(tableHeader[i], fontChinese);
                cell=new PdfPCell(cel); 
                cell.setBorder(0);
                cell.setBorderWidthBottom(0.2f);
                if(i==4){
                	cell.setHorizontalAlignment( Element.ALIGN_RIGHT);
                }else {
                	cell.setHorizontalAlignment( Element.ALIGN_CENTER);
				}
                cell.setMinimumHeight(20);
                cell.setCellEvent(border);//虚线设置
            	datatable.addCell(cell);
          	}
            datatable.setHeaderRows(1); // 每页都输出表头
            datatable.getDefaultCell().setBorderWidth(0.2f);
           
            int rowIndex = 1;
            for(CatalogueReport rs:result){
            	Map<String, String> map = new HashMap<String,String>();
            	map.put("1",rs.getHallCode());
            	map.put("2", String.valueOf(rs.getName()));
            	map.put("3", String.format("%tF%n",rs.getCatalogueDate()));
            	map.put("4", String.format(rs.getCatalogueCode()));
            	map.put("5", String.valueOf(rs.getCount().toString()));
            	map.put("6", String.format("%.2f",Double.parseDouble(String.valueOf(rs.getPrice()))));
//            	map.put("6", String.valueOf(rs.getPrice()));
//            	String.format("%.2f",Double.parseDouble(arr[1]));
            	map.put("7", String.format(rs.getUserName()));
                if (rowIndex % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(0.9f);
                }
                //判断列靠右、居中显示
                for (int i = 1; i <= colNumber; i++){
            		if(i==5 || i== 6){
            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
            		}else {
            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
					}
                }
                if (rowIndex % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(1.0f);
                }
                rowIndex++;
            }
            document.add(datatable);
            document.close();
        }catch(Exception e){
        	e.printStackTrace();
        }
        return realPath;
	}

	
	private String createFileName(String realPath) {
		date = new Date();
		String str = df.format(date);
		String str1 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKMNOPQRSTUVWXYZ";
		char[] chars = new char[5];
		for (int i = 0; i < 5; i++) {
			char cha = str1.charAt(rd.nextInt(40));
			chars[i] = cha;
		}
		int num1 = rd.nextInt(1000)+100;
		realPath =realPath+str+String.valueOf(chars)+num1;
		return realPath+".pdf";
	}
	
	// 将GBK字符转化为ISO码
	public static String parseGBK(String sIn) {
		if (sIn == null || "".equals(sIn))
			return sIn;
		try {
			return new String(sIn.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException usex) {
			return sIn;
		}
	}
	
	public String changeString(String str){
		if(str.trim().length()==0){
			return "";
		}
		String[] arr = str.split("-");
		String a = "";
		for(int i=0;i<arr.length;i++){
			if(arr[i].length()>1){
				a+=arr[i];
			}else{
				a+="0"+arr[i];
			}
		}
		return a;
	}
	
	
	
	
	public String exportRederPDF(QueryCriteria query,HttpServletRequest rquest,List<ReaderReport> result,String title) {
		//获取当前运行环境路径
				String realPath = rquest.getSession().getServletContext().getRealPath("/");
				realPath = realPath.replace("library\\", "");
				File fileDir = new File(realPath+"/pdf");
				if(!fileDir.isDirectory()){
					fileDir.mkdirs();
				}
				realPath =realPath +"/pdf/";
				//生成唯一文件名
				realPath = createFileName(realPath);
				Document document = new Document();
		    	document.setPageSize(PageSize.A4);
		    	String[] tableHeader = {"建档馆号","建档日期","姓名","身份证号","读者级别","借阅次数","押金金额"};
		    	//数据表字段数
		    	int colNumber = 7;
		        try {
		        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
		        	writer.setPageEvent(new PdfHelper());
		            document.open();
		            // 中文字体
		            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
		                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
		            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
		            
		        	User user =  ShiroUtils.getCurrentUser();
		    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
		    		   query.setCustomerId(user.getCustomer().getId());
		    	    }
		            PdfPTable table = new PdfPTable(headColNumber);
		            int[] cellWidth = {12,10,12,12};
		            table.setWidths(cellWidth);
		            table.setWidthPercentage(100);
		            table.getDefaultCell().setPadding(padding);
		            table.getDefaultCell().setBorderWidth(spacing);
		            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            table.getDefaultCell().setBorder(0);
		            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            
		            PdfPCell cell = new PdfPCell(); 
		            Paragraph cel = new Paragraph(title,fontTitle);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("地区:"+query.getArea(),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("馆别:  "+query.getLib(),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            
		            String dateFrom = changeString(query.getDateFrom());
		            String dateTo = changeString(query.getDateTo());
		            String scFrom = query.getSearchFrom();
		            String scTo = query.getSearchTo();
		            
		            //馆号	建档日期	姓名	借阅证号	身份证号	读者级别	借阅次数	押金金额
		            String option ="",val="";
		            if("0".equals(query.getOption())){
		            	option = "建档馆号";
		            	if(scFrom.trim().length() > 0 && scTo.trim().length() > 0){
		            		val = scFrom+" → "+scTo;
		            	}else if(scFrom.trim().length() > 0 && scTo.trim().length() == 0){
		            		val = scFrom+" → ";
		            	}else if(scFrom.trim().length() == 0 && scTo.trim().length() > 0){
		            		val = " → "+scTo;
		            	}
		            } 
		            if("1".equals(query.getOption())){
		            	option = "建档日期";
		            	if(dateFrom.trim().length() > 0 && dateTo.trim().length() > 0){
		            		val = dateFrom+" → "+dateTo;
		            	}else if(dateFrom.trim().length()>0&&dateTo.trim().length()==0){
		            		val = dateFrom+" → ";
		            	}else if(dateFrom.trim().length()==0&&dateTo.trim().length()>0){
		            		val = " → "+dateTo;
		            	}
		            	//val = changeString(query.getDateFrom())+" →  "+changeString(query.getDateTo());
		            } 
		            if("2".equals(query.getOption())){
		            	option = "姓名";
		            	val = query.getKeyWord()!=null?query.getKeyWord():"";
		            }
		            if("4".equals(query.getOption())){
		            	option = "身份证号";
		            	val = query.getKeyWord()!=null?query.getKeyWord():"";
		            }
		            if("6".equals(query.getOption())){
		            	option = "借阅次数";
		            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
		            		val = scFrom+" → "+scTo;
		            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
		            		val = scFrom+" → ";
		            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
		            		val = " → "+scTo;
		            	}
		            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
		            }
		            if("7".equals(query.getOption())){
		            	option = "押金金额";
		            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
		            		val = scFrom+" → "+scTo;
		            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
		            		val = scFrom+" → ";
		            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
		            		val = " → "+scTo;
		            	}
		            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
		            }
		            
		            cel = new Paragraph(option+":  "+val,fontHeader);
		            cell=new PdfPCell(cel);  
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("  ",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            int size = result.size();
		            ReaderReport ct = result.get(size-1);
		            String sum = ct.getSumNumber();
		            String[] arr = sum.split(",");
		            
		            cel = new Paragraph("合计数量:  "+arr[0],fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            
		            cel = new Paragraph("合计押金:  "+String.format("%.2f",Double.parseDouble(arr[1])),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            document.add(table);
		            
		            //表格内容
		            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
		            // 创建有colNumber(10)列的表格
		            PdfPTable datatable = new PdfPTable(colNumber);
		            int[] cellsWidth = {3,3,7,7,3,3,3};
		            datatable.setWidths(cellsWidth);
		            datatable.setWidthPercentage(100); // 表格的宽度百分比
		            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            datatable.getDefaultCell().setBorderWidthLeft(0);
		            datatable.getDefaultCell().setBorderWidthRight(0);
		            datatable.getDefaultCell().setBorderWidthTop(0);
		            datatable.getDefaultCell().setPaddingTop(6f);
		            datatable.getDefaultCell().setPaddingBottom(6f);
		            datatable.getDefaultCell().setBorderWidth(6f);
		            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		            CustomCellTwo border = new CustomCellTwo();
		            datatable.getDefaultCell().setCellEvent(border);//虚线设置
		            
		            datatable.getDefaultCell().setHorizontalAlignment(
		                    Element.ALIGN_CENTER);
		            
		            // 添加表头元素
		            for (int i = 0; i < colNumber; i++) {
		            	cel = new Paragraph(tableHeader[i], fontChinese);
		                cell=new PdfPCell(cel); 
		                cell.setBorder(0);
		                cell.setBorderWidthBottom(0.2f);
		                cell.setHorizontalAlignment( Element.ALIGN_CENTER);
		                cell.setMinimumHeight(20);
		                cell.setCellEvent(border);//虚线设置
		            	datatable.addCell(cell);
		          	}
		            datatable.setHeaderRows(1); // 每页都输出表头
		            datatable.getDefaultCell().setBorderWidth(0.2f);
		           
		            int rowIndex = 1;
		            for(ReaderReport rs:result){
		            	Map<String, String> map = new HashMap<String,String>();
		            	map.put("1", rs.getHallCode());
		            	map.put("2", String.valueOf(new SimpleDateFormat("yyyyMMdd").format(rs.getCreateDate())));
		            	map.put("3", String.valueOf(rs.getCardName()));
		            	map.put("4", String.valueOf(rs.getIdCard()));
		            	map.put("5", "默认");
		            	map.put("6", String.valueOf(rs.getTotalBorrowSum()));
		            	map.put("7", String.format("%.2f", rs.getDeposit()));
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(0.9f);
		                }
		                for (int i = 1; i <= colNumber; i++){
	            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
		                }
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(1.0f);
		                }
		                rowIndex++;
		            }
		            document.add(datatable);
		            document.close();
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        return realPath;
	}
	
	public String exportLightPDF(BigInteger days, QueryCriteria query,HttpServletRequest rquest,List<LightReport> result,String title) {
		//获取当前运行环境路径
				String realPath = rquest.getSession().getServletContext().getRealPath("/");
				realPath = realPath.replace("library\\", "");
				File fileDir = new File(realPath+"/pdf");
				if(!fileDir.isDirectory()){
					fileDir.mkdirs();
				}
				realPath =realPath +"/pdf/";
				//生成唯一文件名
				realPath = createFileName(realPath);
				Document document = new Document();
		    	document.setPageSize(PageSize.A4);
		    	String[] tableHeader = {"馆号","馆名","馆别","开放时长","负责人","电话"};
		    	//数据表字段数
		    	int colNumber = 6;
		        try {
		        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
		        	writer.setPageEvent(new PdfHelper());
		            document.open();
		            // 中文字体
		            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
		                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
		            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
		            
		        	User user =  ShiroUtils.getCurrentUser();
		    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
		    		   query.setCustomerId(user.getCustomer().getId());
		    	    }
		            PdfPTable table = new PdfPTable(headColNumber);
		            int[] cellWidth = {14, 14, 14, 2};
		            table.setWidths(cellWidth);
		            table.setWidthPercentage(100);
		            table.getDefaultCell().setPadding(padding);
		            table.getDefaultCell().setBorderWidth(spacing);
		            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            table.getDefaultCell().setBorder(0);
		            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            
		            PdfPCell cell = new PdfPCell(); 
		            Paragraph cel = new Paragraph(title,fontTitle);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("地区:  "+query.getArea(),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("馆别:  "+query.getLib(), fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            String dateFrom = changeString(query.getDateFrom());
		            String dateTo = changeString(query.getDateTo());
		            String scFrom = query.getSearchFrom();
		            String scTo = query.getSearchTo();
		            //馆号
		            String option = "",val= "";
		    		if("0".equals(query.getOption())){
		    			option = "馆号";
		    			if(scFrom.trim().length() > 0 && scTo.trim().length() > 0){
		            		val = scFrom + " → "+scTo;
		            	}else if(scFrom.trim().length() > 0 && scTo.trim().length() == 0){
		            		val = scFrom + " ";
		            	}else if(scFrom.trim().length() == 0 && scTo.trim().length() > 0){
		            		val = " " + scTo;
		            	}
		    		}
		    		if ("2".equals(query.getOption())) {
		    			option = "日期";
		            	if(dateFrom.trim().length() > 0 && dateTo.trim().length() > 0){
		            		val = dateFrom + " → " + dateTo;
		            	}else if(dateFrom.trim().length()>0 && dateTo.trim().length()==0){
		            		val = dateFrom + " ";
		            	}else if(dateFrom.trim().length()==0 && dateTo.trim().length()>0){
		            		val = " " + dateTo;
		            	}
		    		} 
		    		cel = new Paragraph(option+":  "+val, fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("天数:  "+days, fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            int size = result.size();
		            LightReport ct = result.get(size-1);
		            String sum = ct.getSumNumber();
		            String[] arr = sum.split(",");
		            //Integer.parseInt(arr[1])/60 +": "+ Integer.parseInt(arr[1])%60
		            cel = new Paragraph("时长:  "+(int)Double.parseDouble(arr[1])/60+": "+(int)Double.parseDouble(arr[1])%60, fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n", fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n", fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            document.add(table);
		            
		            //表格内容
		            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
		            // 创建有colNumber(6)列的表格
		            PdfPTable datatable = new PdfPTable(colNumber);
		            int[] cellsWidth = {3, 7, 7, 3, 3, 3};
		            datatable.setWidths(cellsWidth);
		            datatable.setWidthPercentage(100); // 表格的宽度百分比
		            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            datatable.getDefaultCell().setBorderWidthLeft(0);
		            datatable.getDefaultCell().setBorderWidthRight(0);
		            datatable.getDefaultCell().setBorderWidthTop(0);
		            datatable.getDefaultCell().setPaddingTop(6f);
		            datatable.getDefaultCell().setPaddingBottom(6f);
		            datatable.getDefaultCell().setBorderWidth(6f);
		            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		            CustomCellTwo border = new CustomCellTwo();
		            datatable.getDefaultCell().setCellEvent(border);//虚线设置
		            
		            datatable.getDefaultCell().setHorizontalAlignment(
		                    Element.ALIGN_CENTER);
		            
		            // 添加表头元素
		            for (int i = 0; i < colNumber; i++) {
		            	cel = new Paragraph(tableHeader[i], fontChinese);
		                cell=new PdfPCell(cel); 
		                cell.setBorder(0);
		                cell.setBorderWidthBottom(0.2f);
		                cell.setHorizontalAlignment( Element.ALIGN_CENTER);
		                cell.setMinimumHeight(20);
		                cell.setCellEvent(border);//虚线设置
		            	datatable.addCell(cell);
		          	}
		            datatable.setHeaderRows(1); // 每页都输出表头
		            datatable.getDefaultCell().setBorderWidth(0.2f);
		           
		            int rowIndex = 1;
		            for(LightReport rs:result){
		            	Map<String, String> map = new HashMap<String,String>();
		            	map.put("1",rs.getHallCode());
		            	map.put("2", String.valueOf(rs.getName()));
		            	map.put("3", String.valueOf(rs.getLib()));
		            	map.put("4", String.valueOf(rs.getTotalTime().intValue()/60+": "+rs.getTotalTime().intValue()%60));
		            	map.put("5", String.valueOf(rs.getOperName()));
		            	map.put("6", String.valueOf(rs.getPhone()));
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(0.9f);
		                }
		                for (int i = 1; i <= colNumber; i++){
	            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
		                }
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(1.0f);
		                }
		                rowIndex++;
		            }
		            document.add(datatable);
		            document.close();
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        return realPath;
	}
	
	
	//支付统计
	public String exportPayPDF(QueryCriteria query,HttpServletRequest rquest,List<CatalogueReport> result,String title) {
		//获取当前运行环境路径
				String realPath = rquest.getSession().getServletContext().getRealPath("/");
				realPath = realPath.replace("library\\", "");
				File fileDir = new File(realPath+"/pdf");
				if(!fileDir.isDirectory()){
					fileDir.mkdirs();
				}
				realPath =realPath +"/pdf/";
				//生成唯一文件名
				realPath = createFileName(realPath);
				Document document = new Document();
		    	document.setPageSize(PageSize.A4);
		    	String[] tableHeader = {"馆号", "馆名", "日期","金额","操作员"};
		    	//数据表字段数
		    	int colNumber = 5;
		        try {
		        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
		        	writer.setPageEvent(new PdfHelper());
		            document.open();
		            // 中文字体
		            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
		                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
		            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
		            
		        	User user =  ShiroUtils.getCurrentUser();
		    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
		    		   query.setCustomerId(user.getCustomer().getId());
		    	    }
		            PdfPTable table = new PdfPTable(headColNumber);
		            int[] cellWidth = {12,10,12,12};
		            table.setWidths(cellWidth);
		            table.setWidthPercentage(100);
		            table.getDefaultCell().setPadding(padding);
		            table.getDefaultCell().setBorderWidth(spacing);
		            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            table.getDefaultCell().setBorder(0);
		            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		            
		            PdfPCell cell = new PdfPCell(); 
		            Paragraph cel = new Paragraph(title,fontTitle);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("地区:"+query.getArea(),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("馆别:  "+query.getLib(),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            
		            String dateFrom = changeString(query.getDateFrom());
	                String dateTo = changeString(query.getDateTo());
	                String scFrom = query.getSearchFrom();
	                String scTo = query.getSearchTo();
	            	/*if(dateFrom.trim().length()>0&&dateTo.trim().length()>0){
	            		val = dateFrom+" → "+dateTo;
	            	}else if(dateFrom.trim().length()>0&&dateTo.trim().length()==0){
	            		val = dateFrom+" → 至今";
	            	}else{
	            		val = "开始 → "+dateTo;
	            	}
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → 以上";
	            	}else{
	            		val = scTo+" → 以下";
	            	}*/
		            
		            String option ="",val="";
		            if("1".equals(query.getOption())){
		            	option = "馆名";
		            	val = query.getKeyWord()!=null?query.getKeyWord():"";
		            } else if("2".equals(query.getOption())){
		            	option = "日期";
		            	if(dateFrom.trim().length()>0&&dateTo.trim().length()>0){
		            		val = dateFrom+" → "+dateTo;
		            	}else if(dateFrom.trim().length()>0&&dateTo.trim().length()==0){
		            		val = dateFrom+" → ";
		            	}else if(dateFrom.trim().length()==0&&dateTo.trim().length()>0){
		            		val = " → "+dateTo;
		            	}
		            	//val = changeString(query.getDateFrom())+" →  "+changeString(query.getDateTo());
		            }else if("5".equals(query.getOption())){
		            	option = "金额";
		            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
		            		val = scFrom+" → "+scTo;
		            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
		            		val = scFrom+" → ";
		            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
		            		val = " → "+scTo;
		            	}
		            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
		            }else if("6".equals(query.getOption())){
		            	option = "操作员";
		            	val = query.getKeyWord()!=null?query.getKeyWord():"";
		            }
		            
		            if("0".equals(query.getOption())){
		            	cel = new Paragraph("馆号:  "+query.getSearchFrom()+"→"+query.getSearchTo(),fontHeader);
		            }else{
		            	cel = new Paragraph(option+":  "+val,fontHeader);
		            }
		            cell=new PdfPCell(cel);  
		            cell.setHorizontalAlignment(Element.CREATOR);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("  ",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            
		            int size = result.size();
		            CatalogueReport ct = result.get(size-1);
		            String sum = ct.getSumNumber();
		            String[] arr = sum.split(",");
		            
		            
		            cel = new Paragraph("合计册数:  "+arr[0],fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            
		            cel = new Paragraph("合计码洋:  "+String.format("%.2f",Double.parseDouble(arr[1])),fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            cel = new Paragraph("\n",fontHeader);
		            cell=new PdfPCell(cel); 
		            cell.setColspan(4);
		            cell.setBorder(0);
		            table.addCell(cell);
		            
		            document.add(table);
		            
		            //表格内容
		            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
		            // 创建有colNumber(10)列的表格
		            PdfPTable datatable = new PdfPTable(colNumber);
		            int[] cellsWidth = {3,7,3,3,3};
		            datatable.setWidths(cellsWidth);
		            datatable.setWidthPercentage(100); // 表格的宽度百分比
		            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
		            datatable.getDefaultCell().setBorderWidthLeft(0);
		            datatable.getDefaultCell().setBorderWidthRight(0);
		            datatable.getDefaultCell().setBorderWidthTop(0);
		            datatable.getDefaultCell().setPaddingTop(6f);
		            datatable.getDefaultCell().setPaddingBottom(6f);
		            datatable.getDefaultCell().setBorderWidth(6f);
		            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

		            CustomCellTwo border = new CustomCellTwo();
		            datatable.getDefaultCell().setCellEvent(border);//虚线设置
		            
		            datatable.getDefaultCell().setHorizontalAlignment(
		                    Element.ALIGN_CENTER);
		            
		            // 添加表头元素
		            for (int i = 0; i < colNumber; i++) {
		            	cel = new Paragraph(tableHeader[i], fontChinese);
		                cell=new PdfPCell(cel); 
		                cell.setBorder(0);
		                cell.setBorderWidthBottom(0.2f);
		                cell.setHorizontalAlignment( Element.ALIGN_CENTER);
		                cell.setMinimumHeight(20);
		                cell.setCellEvent(border);//虚线设置
		            	datatable.addCell(cell);
		          	}
		            datatable.setHeaderRows(1); // 每页都输出表头
		            datatable.getDefaultCell().setBorderWidth(0.2f);
		           
		            int rowIndex = 1;
		            for(CatalogueReport rs:result){
		            	Map<String, String> map = new HashMap<String,String>();
		            	map.put("1",rs.getHallCode());
		            	map.put("2", String.valueOf(rs.getName()));
		            	map.put("3", String.valueOf(rs.getCatalogueDate()));
		            	map.put("4", String.format("%.2f",rs.getPrice()));
		            	map.put("5", String.format(rs.getUserName()));
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(0.9f);
		                }
		                for (int i = 1; i <= colNumber; i++){
	            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
		                }
		                if (rowIndex % 2 == 1) {
		                    datatable.getDefaultCell().setGrayFill(1.0f);
		                }
		                rowIndex++;
		            }
		            document.add(datatable);
		            document.close();
		        }catch(Exception e){
		        	e.printStackTrace();
		        }
		        return realPath;
	}
	
	
		
		public String exportCirulatePDF(QueryCriteria query,HttpServletRequest rquest,List<Circulated> result,String title) {
			//获取当前运行环境路径
					String realPath = rquest.getSession().getServletContext().getRealPath("/");
					realPath = realPath.replace("library\\", "");
					File fileDir = new File(realPath+"/pdf");
					if(!fileDir.isDirectory()){
						fileDir.mkdirs();
					}
					realPath =realPath +"/pdf/";
					//生成唯一文件名
					realPath = createFileName(realPath);
					Document document = new Document();
			    	document.setPageSize(PageSize.A4);
			    	//馆号	馆名	日期	当前流出	当前流入	累积流出	累积流入
			    	String[] tableHeader = {"馆号", "馆名", "日期","当前流出","当前流入","累积流出","累积流入"};
			    	//数据表字段数
			    	int colNumber = 7;
			        try {
			        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
			        	writer.setPageEvent(new PdfHelper());
			            document.open();
			            // 中文字体
			            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
			                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
			            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
			            
			        	User user =  ShiroUtils.getCurrentUser();
			    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
			    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
			    		   query.setCustomerId(user.getCustomer().getId());
			    	    }
			            PdfPTable table = new PdfPTable(headColNumber);
			            int[] cellWidth = {12,10,12,12};
			            table.setWidths(cellWidth);
			            table.setWidthPercentage(100);
			            table.getDefaultCell().setPadding(padding);
			            table.getDefaultCell().setBorderWidth(spacing);
			            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
			            table.getDefaultCell().setBorder(0);
			            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
			            
			            PdfPCell cell = new PdfPCell(); 
			            Paragraph cel = new Paragraph(title,fontTitle);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setColspan(4);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("\n",fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setColspan(4);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("地区:"+query.getArea(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("馆别:  "+query.getLib(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            
			            String dateFrom = changeString(query.getDateFrom());
		                String dateTo = changeString(query.getDateTo());
		                String scFrom = query.getSearchFrom();
		                String scTo = query.getSearchTo();
			            
			            //馆名	日期	当前流出	当前流入	累积流出	累积流入
			            String option ="",val="";
			            if("1".equals(query.getOption())){
			            	option = "馆名";
			            	val = query.getKeyWord()!=null?query.getKeyWord():"";
			            } else if("2".equals(query.getOption())){
			            	option = "日期";
			            	if(dateFrom.trim().length()>0&&dateTo.trim().length()>0){
			            		val = dateFrom+" → "+dateTo;
			            	}else if(dateFrom.trim().length()>0&&dateTo.trim().length()==0){
			            		val = dateFrom+" → ";
			            	}else if(dateFrom.trim().length()==0&&dateTo.trim().length()>0){
			            		val = " → "+dateTo;
			            	}
			            	//val = changeString(query.getDateFrom())+" →  "+changeString(query.getDateTo());
			            }else if("3".equals(query.getOption())){
			            	option = "当前流出";
			            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
			            		val = scFrom+" → "+scTo;
			            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
			            		val = scFrom+" → ";
			            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
			            		val = " → "+scTo;
			            	}
			            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
			            }else if("4".equals(query.getOption())){
			            	option = "当前流入";
			            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
			            		val = scFrom+" → "+scTo;
			            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
			            		val = scFrom+" → ";
			            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
			            		val = " → "+scTo;
			            	}
			            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
			            }else if("5".equals(query.getOption())){
			            	option = "累积流出";
			            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
			            		val = scFrom+" → "+scTo;
			            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
			            		val = scFrom+" → ";
			            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
			            		val = " → "+scTo;
			            	}
			            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
			            }else if("6".equals(query.getOption())){
			            	option = "累积流入";
			            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
			            		val = scFrom+" → "+scTo;
			            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
			            		val = scFrom+" → ";
			            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
			            		val = " → "+scTo;
			            	}
			            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
			            }
			            
			            if("0".equals(query.getOption())){
			            	cel = new Paragraph("馆号:  "+query.getSearchFrom()+"→"+query.getSearchTo(),fontHeader);
			            }else{
			            	cel = new Paragraph(option+":  "+val,fontHeader);
			            }
			            cell=new PdfPCell(cel);  
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("  ",fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setColspan(4);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            int size = result.size();
			            Circulated ct = result.get(size-1);
			            
			            cel = new Paragraph("当前流出:  "+ct.getReportNowOut()+"/"+ct.getReportOutMoney(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            
			            cel = new Paragraph("当前流入:  "+ct.getReportNowIn()+"/"+ct.getReportNowInMoney(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("累积流出:  "+ct.getReportOldOut()+"/"+ct.getReportOldMoney(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("累积流入:  "+ct.getReportOldIn()+"/"+ct.getReportOldInMoney(),fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
			            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("\n",fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setColspan(4);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            cel = new Paragraph("\n",fontHeader);
			            cell=new PdfPCell(cel); 
			            cell.setColspan(4);
			            cell.setBorder(0);
			            table.addCell(cell);
			            
			            document.add(table);
			            
			            //表格内容
			            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
			            // 创建有colNumber(10)列的表格
			            PdfPTable datatable = new PdfPTable(colNumber);
			            int[] cellsWidth = {3,6,3,3,3,3,3};
			            datatable.setWidths(cellsWidth);
			            datatable.setWidthPercentage(100); // 表格的宽度百分比
			            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
			            datatable.getDefaultCell().setBorderWidthLeft(0);
			            datatable.getDefaultCell().setBorderWidthRight(0);
			            datatable.getDefaultCell().setBorderWidthTop(0);
			            datatable.getDefaultCell().setPaddingTop(6f);
			            datatable.getDefaultCell().setPaddingBottom(6f);
			            datatable.getDefaultCell().setBorderWidth(6f);
			            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

			            CustomCellTwo border = new CustomCellTwo();
			            datatable.getDefaultCell().setCellEvent(border);//虚线设置
			            
			            datatable.getDefaultCell().setHorizontalAlignment(
			                    Element.ALIGN_CENTER);
			            
			            // 添加表头元素
			            for (int i = 0; i < colNumber; i++) {
			            	cel = new Paragraph(tableHeader[i], fontChinese);
			                cell=new PdfPCell(cel); 
			                cell.setBorder(0);
			                cell.setBorderWidthBottom(0.2f);
			                cell.setHorizontalAlignment( Element.ALIGN_CENTER);
			                cell.setMinimumHeight(20);
			                cell.setCellEvent(border);//虚线设置
			            	datatable.addCell(cell);
			          	}
			            datatable.setHeaderRows(1); // 每页都输出表头
			            datatable.getDefaultCell().setBorderWidth(0.2f);
			           
			            int rowIndex = 1;
			            for(Circulated rs:result){
			            	Map<String, String> map = new HashMap<String,String>();
			            	map.put("1",rs.getHallCode());
			            	map.put("2", String.valueOf(rs.getName()));
			            	if(query.getDateFrom().length()>0&&query.getDateTo().length()>0){
			            		map.put("3", changeString(query.getDateFrom())+"-"+changeString(query.getDateTo()));
			            	}else if (query.getDateFrom().length()==0&&query.getDateTo().length()>0) {
			            		map.put("3",changeString(query.getDateTo()));
							}else if(query.getDateFrom().length()>0&&query.getDateTo().length()==0){
								map.put("3",changeString(query.getDateFrom()));
							}else{
								map.put("3","");
							}
			            	map.put("4", String.valueOf(rs.getNowOut())+"/"+String.valueOf(rs.getNowOutMoney()));
			            	map.put("5", String.valueOf(rs.getNowIn())+"/"+String.valueOf(rs.getNowInMoney()));
			            	map.put("6", String.valueOf(rs.getTotalNowOut())+"/"+String.valueOf(rs.getTotalOutMoney()));
			            	map.put("7", String.valueOf(rs.getTotalNowin())+"/"+String.valueOf(rs.getTotalNowInMoney()));
			                if (rowIndex % 2 == 1) {
			                    datatable.getDefaultCell().setGrayFill(0.9f);
			                }
			                for (int i = 1; i <= colNumber; i++){
		            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
			                }
			                if (rowIndex % 2 == 1) {
			                    datatable.getDefaultCell().setGrayFill(1.0f);
			                }
			                rowIndex++;
			            }
			            document.add(datatable);
			            document.close();
			        }catch(Exception e){
			        	e.printStackTrace();
			        }
			        return realPath;
		}
		
		//在本地生成PDF文件-------
		public String exportRanking(QueryCriteria query,HttpServletRequest rquest,List<Clicklike> result,String title,String some) {
			//获取当前运行环境路径
			String realPath = rquest.getSession().getServletContext().getRealPath("/");
			realPath = realPath.replace("library\\", "");
			File fileDir = new File(realPath+"/pdf");
			if(!fileDir.isDirectory()){
				fileDir.mkdirs();
			}
			realPath =realPath +"/pdf/";
			//生成唯一文件名
			realPath = createFileName(realPath);
			Document document = new Document();
	    	document.setPageSize(PageSize.A4);
	    	//名次	ISBN	书名	出版社	定价	著者	年份	分类号	借次
	    	String str1 = "";
	    	//区分赞还是借
	    	if("Borrow".equals(some)){
	    		str1="借次";
	    	}else{
	    		str1="赞次";
	    	}
	    	String[] tableHeader = { "名次", "ISBN", "书名","出版社", "定价", "著者","年份","分类号",str1};
	    	//数据表字段数
	    	int colNumber = 9;
	        try {
	        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(realPath));
	        	writer.setPageEvent(new PdfHelper());
	            document.open();
	            // 中文字体
	            BaseFont bfChinese = BaseFont.createFont("STSong-Light",
	                    "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
	            Font fontTitle = new Font(bfChinese, 14, Font.NORMAL);
	            Font fontHeader = new Font(bfChinese, 11, Font.NORMAL);
	            
	        	User user =  ShiroUtils.getCurrentUser();
	    	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
	    	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
	    		   query.setCustomerId(user.getCustomer().getId());
	    	    }
	            PdfPTable table = new PdfPTable(headColNumber);
	            int[] cellWidth = {12,10,12,12};
	            table.setWidths(cellWidth);
	            table.setWidthPercentage(100);
	            table.getDefaultCell().setPadding(padding);
	            table.getDefaultCell().setBorderWidth(spacing);
	            table.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
	            table.getDefaultCell().setBorder(0);
	            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
	            
	            PdfPCell cell = new PdfPCell(); 
	            Paragraph cel = new Paragraph(title,fontTitle);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setColspan(4);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            cel = new Paragraph("\n",fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setColspan(4);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            cel = new Paragraph("地区:"+query.getArea(),fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            cel = new Paragraph("馆别:  "+query.getLib(),fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            if("0".equals(query.getOption())){
	            	cel = new Paragraph("馆号:  "+query.getSearchFrom()+"→"+query.getSearchTo(),fontHeader);
	            }else{
	            	cel = new Paragraph("馆号:  "+query.getHallCode(),fontHeader);
	            }
	            cell=new PdfPCell(cel);  
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            String dateFrom = changeString(query.getDateFrom());
                String dateTo = changeString(query.getDateTo());
                String scFrom = query.getSearchFrom();
                String scTo = query.getSearchTo();
                
	            //名次	ISBN	书名	出版社	定价	著者	年份	分类号	借次
	            String option ="",val="";
	            if("0".equals(query.getOption())){
	            	option = "名次";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            } else if("1".equals(query.getOption())){
	            	option = "ISBN";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            }else if("2".equals(query.getOption())){
	            	option = "书名";
	            	val = query.getKeyWord()!=null?query.getKeyWord():"";
	            }else if("3".equals(query.getOption())){
	            	option = "出版社";
	            	val = query.getKeyWord()!=null?query.getKeyWord():"";
	            }else if("4".equals(query.getOption())){
	            	option = "定价";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            }else if("5".equals(query.getOption())){
	            	option = "著者";
	            	val = query.getKeyWord()!=null?query.getKeyWord():"";
	            }else if("6".equals(query.getOption())){
	            	option = "年份";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            }else if("7".equals(query.getOption())){
	            	option = "分类号";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            }else if("8".equals(query.getOption())){
	            	option = "借次";
	            	if(scFrom.trim().length()>0&&scTo.trim().length()>0){
	            		val = scFrom+" → "+scTo;
	            	}else if(scFrom.trim().length()>0&&scTo.trim().length()==0){
	            		val = scFrom+" → ";
	            	}else if(scFrom.trim().length()==0&&scTo.trim().length()>0){
	            		val = " → "+scTo;
	            	}
	            	//val = query.getSearchFrom()+" →  "+query.getSearchTo();
	            }
	            
	            if("0".equals(query.getOption())){
	            	cel = new Paragraph("馆号:  "+query.getSearchFrom()+"→"+query.getSearchTo(),fontHeader);
	            }else{
	            	cel = new Paragraph(option+":  "+val,fontHeader);
	            }
	            cell=new PdfPCell(cel);  
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            

	            cel = new Paragraph("  ",fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setColspan(4);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            int size = result.size();
	            Clicklike ct = result.get(size-1);
	            String sum = ct.getSumNumber();
	            String[] arr = sum.split(",");
	            
	            cel = new Paragraph("合计册数:  "+arr[0],fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            
	            cel = new Paragraph("合计码洋:  "+String.format("%.2f",Double.parseDouble(arr[1])),fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
	            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            cel = new Paragraph("\n",fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setColspan(4);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            cel = new Paragraph("\n",fontHeader);
	            cell=new PdfPCell(cel); 
	            cell.setColspan(4);
	            cell.setBorder(0);
	            table.addCell(cell);
	            
	            document.add(table);
	            
	            //表格内容
	            Font fontChinese = new Font(bfChinese, 10, Font.NORMAL);
	            // 创建有colNumber(10)列的表格
	            PdfPTable datatable = new PdfPTable(colNumber);
	            int[] cellsWidth = {3,3,4,4,2,6,2,2,2};
	            datatable.setWidths(cellsWidth);
	            datatable.setWidthPercentage(100); // 表格的宽度百分比
	            datatable.getDefaultCell().setBackgroundColor(BaseColor.WHITE);
	            datatable.getDefaultCell().setBorderWidthLeft(0);
	            datatable.getDefaultCell().setBorderWidthRight(0);
	            datatable.getDefaultCell().setBorderWidthTop(0);
	            datatable.getDefaultCell().setPaddingTop(6f);
	            datatable.getDefaultCell().setPaddingBottom(6f);
	            datatable.getDefaultCell().setBorderWidth(6f);
	            datatable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

	            CustomCellTwo border = new CustomCellTwo();
	            datatable.getDefaultCell().setCellEvent(border);//虚线设置
	            
	            datatable.getDefaultCell().setHorizontalAlignment(
	                    Element.ALIGN_CENTER);
	            
	            // 添加表头元素
	            for (int i = 0; i < colNumber; i++) {
	            	cel = new Paragraph(tableHeader[i], fontChinese);
	                cell=new PdfPCell(cel); 
	                cell.setBorder(0);
	                cell.setBorderWidthBottom(0.2f);
	                cell.setHorizontalAlignment( Element.ALIGN_CENTER);
	                cell.setMinimumHeight(20);
	                cell.setCellEvent(border);//虚线设置
	            	datatable.addCell(cell);
	          	}
	            datatable.setHeaderRows(1); // 每页都输出表头
	            datatable.getDefaultCell().setBorderWidth(0.2f);
	           //名次	ISBN	书名	出版社	定价	著者	年份	分类号	借次
	            int rowIndex = 1;
	            for(Clicklike rs:result){
	            	Map<String, String> map = new HashMap<String,String>();
	            	map.put("1",rs.getRanking());
	            	map.put("2",rs.getIsbn());
	            	map.put("3",rs.getBookName());
	            	map.put("4",rs.getPress());
	            	map.put("5", String.format("%.2f",rs.getPrice()));
	            	map.put("6", rs.getAuthor());
	            	map.put("7", rs.getDate());
	            	map.put("8",rs.getTypeNumber());
	            	map.put("9", String.valueOf(rs.getClick()));
	                if (rowIndex % 2 == 1) {
	                    datatable.getDefaultCell().setGrayFill(0.9f);
	                }
	                for (int i = 1; i <= colNumber; i++){
	            		if(i==5){
	            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
	            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
	            		}else {
	            			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
	            			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
						}
	                }
	                if (rowIndex % 2 == 1) {
	                    datatable.getDefaultCell().setGrayFill(1.0f);
	                }
	                rowIndex++;
	            }
	            document.add(datatable);
	            document.close();
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
	        return realPath;
		}
		
	
}
