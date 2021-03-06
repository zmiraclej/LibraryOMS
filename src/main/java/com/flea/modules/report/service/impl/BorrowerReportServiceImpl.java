package com.flea.modules.report.service.impl;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flea.common.Common;
import com.flea.common.dao.AreaDao;
import com.flea.common.dao.CityDao;
import com.flea.common.dao.ProvinceDao;
import com.flea.common.pojo.Area;
import com.flea.common.pojo.City;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Province;
import com.flea.common.pojo.User;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.common.util.pdf.PdfHelper;
import com.flea.modules.report.dao.BorrowerReportDao;
import com.flea.modules.report.pojo.BorrowerReport;
import com.flea.modules.report.pojo.ReportTotal;
import com.flea.modules.report.pojo.SearchResult;
import com.flea.modules.report.pojo.VLibraryBook;
import com.flea.modules.report.pojo.vo.QueryCriteria;
import com.flea.modules.report.service.BorrowerReportService;
import com.flea.modules.report.service.impl.VLibraryBookServiceImpl.CustomCell;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 借书统计Service
 * @author bruce
 * @version 2016-07-14
 */
 @Service
public class BorrowerReportServiceImpl extends BaseServiceImpl<BorrowerReport,Integer> implements BorrowerReportService{

	@Autowired
	private BorrowerReportDao borrowerReportDao;
	@Autowired
	private ProvinceDao provinceDao;
	@Autowired
	private CityDao cityDao;
	@Autowired
	private AreaDao areaDao;
	
	
	@Autowired
	public  BorrowerReportServiceImpl(BorrowerReportDao borrowerReportDao) {
		super(borrowerReportDao);
		this.borrowerReportDao = borrowerReportDao;
	}
	
	@Override
	public Page<BorrowerReport> find(Page<BorrowerReport> page,QueryCriteria query) {
		User user =  ShiroUtils.getCurrentUser();
	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
	    if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
		   query.setCustomerId(user.getCustomer().getId());
	    }
		SearchResult<BorrowerReport>  result = borrowerReportDao.findBorrowerBookList(page, query);
		ReportTotal total = result.getTotal();
		page.setList(result.getResult());
		page.setCount(result.getCount());
		page.setTotal(total);
		return page;
	}
	
    //数据表字段数
    //表格的设置
     private final int spacing = 0;
    //表格的设置
     private  final int padding = 2;
    
	 private final int headColNumber = 4;

	/* (non-Javadoc)
	 * @see com.flea.modules.report.service.BorrowerReportService#exportLibraryBookPDF(com.flea.modules.report.pojo.vo.QueryCriteria, java.lang.String)
	 */
	@Override
	public void exportLibraryBookPDF(QueryCriteria query, String filePath) {
		Document document = new Document();
    	document.setPageSize(PageSize.A4);
    	 String[] tableHeader = { "馆号", "馆名", "日期","单号","册数", "总码洋", "操作员"};
    	//数据表字段数
    	int colNumber = 7;
        try {
        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
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
    	    
    		SearchResult<BorrowerReport>  result = borrowerReportDao.findBorrowerBookList(new Page<BorrowerReport>(),query);
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
            Paragraph cel = new Paragraph("借书统计清单",fontTitle);
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
            
            String areaString ="";
            if(StringUtils.isNotBlank(query.getProvince())){
            	Province province = provinceDao.findProvinceByCode(query.getProvince());
            	City  city = cityDao.findCityByCode(query.getCity());
            	Area area = areaDao.findAreaByCode(query.getArea());
            	if(province!=null){
            		areaString += province.getName();
            	}
            	if(city!=null){
            		areaString+="-"+city.getName();
            	}
            	if(area!=null){
            		areaString+="-"+area.getName();
            	}
            }else {
            	areaString = query.getArea();
			}
            cel = new Paragraph("地区:  "+areaString,fontHeader);
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
            
            String option ="",val="";
            if("1".equals(query.getOption())){
            	option = "馆名";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            } else if("2".equals(query.getOption())){
            	option = "日期";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("3".equals(query.getOption())){
            	option = "单号";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("4".equals(query.getOption())){
            	option = "册数";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("5".equals(query.getOption())){
            	option = "码洋";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("6".equals(query.getOption())){
            	option = "操作员";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            }else if("7".equals(query.getOption())){
            	option = "馆号";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }
            cel = new Paragraph(option+":  "+val,fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("合计种数:  "+result.getTotal().getSumCount(),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            cel = new Paragraph("合计册数:  "+result.getTotal().getSumBook(),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("合计码洋:  "+String.format("%.2f",result.getTotal().getSumPrice()),fontHeader);
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
            int[] cellsWidth = { 3,7,3,3,3,3,4};
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

            CustomCell border = new CustomCell();
            datatable.getDefaultCell().setCellEvent(border);//虚线设置
            
            datatable.getDefaultCell().setHorizontalAlignment(
                    Element.ALIGN_CENTER);
            
            // 添加表头元素
            for (int i = 0; i < colNumber; i++) {
            	cel = new Paragraph(tableHeader[i], fontChinese);
                cell=new PdfPCell(cel); 
                cell.setBorder(0);
                cell.setBorderWidthBottom(0.2f);
                if(i==4||i==5){
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
            for(BorrowerReport rs:result.getResult()){
            	Map<String, String> map = new HashMap<String,String>();
            	map.put("1",rs.getHallCode());
            	map.put("2", rs.getName());
            	map.put("3", String.valueOf(rs.getBorrowDate()));
            	map.put("4", String.valueOf(rs.getBorrowNumber()));
            	map.put("5", String.valueOf(rs.getTotalBook()));
            	map.put("6", String.format("%.2f",rs.getTotalPrice()));
            	map.put("7", String.valueOf(rs.getUserName()));
                if (rowIndex % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(0.9f);
                }
                for (int i = 1; i <= colNumber; i++){
            		if(i==5||i==6){
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
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.report.service.BorrowerReportService#exportCategoryDataPDF(com.flea.modules.report.pojo.vo.QueryCriteria, java.lang.String)
	 */
	@Override
	public void exportCategoryDataPDF(QueryCriteria query, String filePath) {
		Document document = new Document();
    	document.setPageSize(PageSize.A4);
    	String[] tableHeader = { "类别","册数", "码洋", "占比"};
    	//数据表字段数
    	 int colNumber = 4;
        try {
        	PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
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
    		SearchResult<BorrowerReport>  result = borrowerReportDao.findBorrowerDataList(query);
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
            Paragraph cel = new Paragraph("借书统计类别清单",fontTitle);
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
            
            String areaString ="";
            if(StringUtils.isNotBlank(query.getProvince())){
            	Province province = provinceDao.findProvinceByCode(query.getProvince());
            	City  city = cityDao.findCityByCode(query.getCity());
            	Area area = areaDao.findAreaByCode(query.getArea());
            	areaString = province.getName()+"-"+city.getName()+"-"+area.getName();
            }else {
            	areaString = query.getArea();
			}
            cel = new Paragraph("地区:  "+areaString,fontHeader);
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
            
            
            String option ="",val="";
            if("1".equals(query.getOption())){
            	option = "馆名";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            } else if("2".equals(query.getOption())){
            	option = "日期";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("3".equals(query.getOption())){
            	option = "单号";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("4".equals(query.getOption())){
            	option = "册数";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("5".equals(query.getOption())){
            	option = "码洋";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }else if("6".equals(query.getOption())){
            	option = "操作员";
            	val = query.getKeyWord()!=null?query.getKeyWord():"";
            }else if("7".equals(query.getOption())){
            	option = "馆号";
            	val = query.getSearchFrom()+" → "+query.getSearchTo();
            }
           
            cel = new Paragraph(option+":  "+val,fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(4);
            cell.setBorder(0);
            table.addCell(cell);
//            cel = new Paragraph("\n",fontHeader);
//            cell=new PdfPCell(cel); 
//            cell.setColspan(4);
//            cell.setBorder(0);
//            table.addCell(cell);
            cel = new Paragraph("合计数量:  "+result.getTotal().getSumBook(),fontHeader);
            cell=new PdfPCell(cel); 
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(0);
            table.addCell(cell);
            
            cel = new Paragraph("合计码洋:  "+String.format("%.2f",result.getTotal().getSumPrice()),fontHeader);
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
            int[] cellsWidth = {7,3,3,3};
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

            CustomCell border = new CustomCell();
            datatable.getDefaultCell().setCellEvent(border);//虚线设置
            datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            
            // 添加表头元素
            for (int i = 0; i < colNumber; i++) {
            	cel = new Paragraph(tableHeader[i], fontChinese);
                cell=new PdfPCell(cel); 
                cell.setBorder(0);
                cell.setBorderWidthBottom(0.2f);
                if(i==3){
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
            for(BorrowerReport rs:result.getResult()){
            	Map<String, String> map = new HashMap<String,String>();
            	map.put("1",rs.getCategory());
            	map.put("2", String.valueOf(rs.getTotalBook()));
            	map.put("3", String.format("%.2f",rs.getTotalPrice()));
            	map.put("4", String.format("%.2f",rs.getPercent())+"%");
                if (rowIndex % 2 == 1) {
                    datatable.getDefaultCell().setGrayFill(0.9f);
                }
                for (int i = 1; i <= colNumber; i++){
                		if(i==1){
                			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
                			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
                		}else if (i==4) {
                			datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                			datatable.addCell(new Paragraph(map.get(i+""),fontChinese));
						}  else {
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
		
	}

	/* (non-Javadoc)
	 * @see com.flea.modules.report.service.BorrowerReportService#findLibraryDataList(com.flea.modules.report.pojo.vo.QueryCriteria)
	 */
	@Override
	public SearchResult<BorrowerReport> findLibraryDataList(QueryCriteria query) {
		User user =  ShiroUtils.getCurrentUser();
	    String roleLevel =	ShiroUtils.getCurrentUserRoleLevel(user);
		 if(roleLevel.equals(Common.ROLE_SECOND_LEVLE)){
			   query.setCustomerId(user.getCustomer().getId());
		 }
		return 	borrowerReportDao.findBorrowerDataList(query);
	}
	
	class CustomCell implements PdfPCellEvent {
		@Override
		public void cellLayout(PdfPCell cell, Rectangle position,
				PdfContentByte[] canvases) {
		    PdfContentByte cb = canvases[PdfPTable.LINECANVAS];
		    cb.saveState();
		    cb.setLineWidth(0.5f);
		    cb.setLineDash(new float[] { 2.0f, 2.0f }, 0);
		    cb.moveTo(position.getLeft(), position.getBottom());
		    cb.lineTo(position.getRight(), position.getBottom());
		    cb.stroke();
		    cb.restoreState();
		}
	}
	
}
