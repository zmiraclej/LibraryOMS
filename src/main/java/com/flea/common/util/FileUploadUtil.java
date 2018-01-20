package com.flea.common.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * @ClassName: FileUploadUtil
 * @Description:上传文件
 * @author QL
 * @date 2017年1月16日 下午8:30:50
 */
public class FileUploadUtil {
	 public static Boolean uploadFile(HttpServletRequest request, MultipartFile file) {  
         System.out.println("开始");  
         String path = request.getSession().getServletContext().getRealPath("upload");  
         String fileName = file.getOriginalFilename();  
         System.out.println(path);  
         File targetFile = new File(path, fileName);  
         if (!targetFile.exists()) {  
             targetFile.mkdirs();  
         }  
         // 保存  
         try {  
             file.transferTo(targetFile);  
             return true;  
         } catch (Exception e) {  
             e.printStackTrace();  
             return false;  
         }  
  
    }  
}
