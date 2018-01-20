package com.flea.common.util.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfHelper extends PdfPageEventHelper{
	
	public PdfTemplate tpl; 
    public BaseFont bf;  
	
    public void onOpenDocument(PdfWriter writer, Document document) {  
       try {  
            tpl = writer.getDirectContent().createTemplate(100, 100);  
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);     
       }  
        catch(Exception e) {  
            throw new ExceptionConverter(e);  
        } 
    }  
    
    
    public void onEndPage(PdfWriter writer, Document document) {  
       //在每页结束的时候把“第x页”信息写道模版指定位置  
    	PdfContentByte cb = writer.getDirectContent();  
        cb.saveState();  
        String text = writer.getPageNumber()+" / "; 
        int width = String.valueOf(writer.getPageNumber()).length();
        cb.beginText(); 
        cb.setFontAndSize(bf, 10);  
        Rectangle pageSize = document.getPageSize();
        cb.setTextMatrix(pageSize.getWidth()/2-(width*2)+2, 20);//定位“第x页,共” 在具体的页面调试时候需要更改这xy的坐标  
        cb.showText(text);  
        cb.endText();  
        cb.addTemplate(tpl, (pageSize.getWidth()/2)+14, 20);//定位“y页” 在具体的页面调试时候需要更改这xy的坐标  
        cb.restoreState();         
    	/*
    	 ColumnText.showTextAligned(writer.getDirectContent(),
                 Element.ALIGN_CENTER, new Phrase(String.format(" %d", writer.getPageNumber())),
                 300, 15, 0);
                 */
    }  
    public void onCloseDocument(PdfWriter writer, Document document) {  
       //关闭document的时候获取总页数，并把总页数按模版写道之前预留的位置  
    	
       tpl.beginText();  
       tpl.setFontAndSize(bf, 10);  
       tpl.setTextMatrix(0,0); 
       tpl.showText(String.valueOf(writer.getPageNumber()-1));  
       tpl.endText();  
    }  
}
