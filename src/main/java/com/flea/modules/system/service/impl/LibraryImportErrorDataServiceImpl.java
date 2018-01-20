package com.flea.modules.system.service.impl;

import java.io.File;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.dao.LibraryImportErrorDataDao;
import com.flea.modules.system.pojo.LibraryImportErrorData;
import com.flea.modules.system.service.LibraryImportErrorDataService;

/**
 * 导入图书馆错误信息Service
 * @author bruce
 * @version 2016-12-26
 */
 @Service
public class LibraryImportErrorDataServiceImpl extends BaseServiceImpl<LibraryImportErrorData,Integer> implements LibraryImportErrorDataService{

	@Autowired
	private LibraryImportErrorDataDao libraryImportErrorDataDao;
	
	
	@Autowired
	public  LibraryImportErrorDataServiceImpl(LibraryImportErrorDataDao libraryImportErrorDataDao) {
		super(libraryImportErrorDataDao);
		this.libraryImportErrorDataDao = libraryImportErrorDataDao;
	}
	
	@Override
	public Page<LibraryImportErrorData> find(Page<LibraryImportErrorData> page,LibraryImportErrorData libraryImportErrorData) {
		DetachedCriteria dc = libraryImportErrorDataDao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(libraryImportErrorData.getUser().getId().toString())){
			dc.add(Restrictions.eq("user.id",libraryImportErrorData.getUser().getId()));
		}
		return libraryImportErrorDataDao.find(page, dc);
	}
	
	@Override
	public void exportErrorData(String fileName) {
		// TODO Auto-generated method stub
		try {
			WritableWorkbook wwb = null;
            
            // 创建可写入的Excel工作簿
            File file=new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            //以fileName为文件名来创建一个Workbook
            wwb = Workbook.createWorkbook(file);

            // 创建工作表
            WritableSheet ws = wwb.createSheet("sheet1",0);
            List<LibraryImportErrorData> list = listImportErrorData();
            Label labelId= new Label(0, 0, "序号");
            Label labelName= new Label(1, 0, "馆名");
            Label labelAddress= new Label(2, 0, "地址");
            Label labelAgreementAccount= new Label(3, 0, "协议账号");
            Label labelAcountName= new Label(4, 0, "户名");
            Label labelCreditLines= new Label(5, 0, "授信额度");
            Label labelConperson= new Label(6, 0, "联系人");
            Label labelTel= new Label(7, 0, "电话");
            Label labelPhone= new Label(8, 0, "手机");
            Label labelEmail= new Label(9, 0, "邮箱");
            Label labelMsg= new Label(10, 0, "错误提示");
            ws.addCell(labelId);
            ws.addCell(labelName);
            ws.addCell(labelAddress);
            ws.addCell(labelAgreementAccount);
            ws.addCell(labelAcountName);
            ws.addCell(labelCreditLines);
            ws.addCell(labelConperson);
            ws.addCell(labelTel);
            ws.addCell(labelPhone);
            ws.addCell(labelEmail);
            ws.addCell(labelMsg);
            for (int i = 0; i < list.size(); i++) {
            	Label labelName_id= new Label(0, i+1, (i+1) + "");
                Label labelName_i= new Label(1, i+1, list.get(i).getName() + "");
                Label labelAddress_i= new Label(2, i+1, list.get(i).getAddress() + "");
                Label labelAgreementAccount_i= new Label(3, i+1, list.get(i).getAgreementAccount() + "");
                Label labelAcountName_i= new Label(4, i+1, list.get(i).getAcountName() + "");
                Label labelCreditLines_i= new Label(5, i+1, String.valueOf(list.get(i).getCreditLines()));
                Label labelConperson_i= new Label(6, i+1, list.get(i).getConperson() + "");
                Label labelTel_i= new Label(7, i+1, list.get(i).getTel() + "");
                Label labelPhone_i= new Label(8, i+1, list.get(i).getPhone() + "");
                Label labelEmail_i= new Label(9, i+1, list.get(i).getEmail() + "");
                Label labelMsg_i= new Label(10, i+1, list.get(i).getMsg() + "");
                ws.addCell(labelName_id);
                ws.addCell(labelName_i);
                ws.addCell(labelAddress_i);
                ws.addCell(labelAgreementAccount_i);
                ws.addCell(labelAcountName_i);
                ws.addCell(labelCreditLines_i);
                ws.addCell(labelConperson_i);
                ws.addCell(labelTel_i);
                ws.addCell(labelPhone_i);
                ws.addCell(labelEmail_i);
                ws.addCell(labelMsg_i);
			}
            //写进文档
            wwb.write();
           // 关闭Excel工作簿对象
            wwb.close();
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
		}
	}
	
	@Override
	public void detele(){
		//获取当前用户id去删除
		libraryImportErrorDataDao.deleteById(ShiroUtils.getCurrentUser().getId());
	}
	
	/**
	 * @return
	 * 错误信息列表
	 */
	@Override
	public List<LibraryImportErrorData> listImportErrorData(){
		StringBuffer buffer = new StringBuffer();
		buffer.append(" from LibraryImportErrorData where user.id = '"+ShiroUtils.getCurrentUser().getId()+"'");
		List<LibraryImportErrorData> list = libraryImportErrorDataDao.getListByHQL(buffer.toString(), null);
		return list;
	}

}
