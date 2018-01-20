package com.flea.modules.news.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.flea.common.util.Config;
import com.flea.common.util.FileUtils;
import com.flea.common.util.OSSUtils;
import com.flea.modules.news.util.ImgEditor;
import com.flea.modules.news.util.StringUtil;
 

/**
 * 
 * @ClassName: NewsImagUploadController
 * @Description:咨询图片上传
 * @author QL
 * @date 2017年3月16日 下午7:51:07
 */
@Controller
@RequestMapping(value = "newsImage")
public class NewsImagUploadController {
	ImgEditor imgEditor = new ImgEditor();  
	Logger log = Logger.getLogger(NewsImagUploadController.class);

	/**
	 * @Description:百度编辑器的图片上传
	 * @param upfile 上传文件名
	 * @param request 请求的参数
	 * @param response  响应参数
	 * @return Map<String,Object>
	 */
	@ResponseBody
	@RequestMapping(value = "images")
	public Map<String, Object> images(MultipartFile upfile,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			String basePath = Config.getProperty("ueditor.uploadImage.url");
			String visitUrl = Config.getProperty("ueditor.visit.url");
			// 与properties文件中lyz.uploading.url相同，未读取到文件数据时为basePath赋默认值
			if (basePath == null || "".equals(basePath)) {
				basePath = Config.getProperty("ueditor.uploadImage.url");
			}
			// 与properties文件中lyz.visit.url相同，未读取到文件数据时为visitUrl赋默认值
			if (visitUrl == null || "".equals(visitUrl)) {
				visitUrl = Config.getProperty("ueditor.visit.url");
			}
			// 获取图片的扩展名字
			String image = StringUtil.getExt(upfile.getOriginalFilename());
			// 图片重名名
			String fileName = String.valueOf(System.currentTimeMillis()) + "." + image;
			visitUrl = visitUrl + fileName;
			basePath = basePath + fileName;
			File f = new File(basePath);
			if (!f.exists()) {
				f.getParentFile().mkdirs();
			}
			OutputStream out = new FileOutputStream(basePath);
			FileCopyUtils.copy(upfile.getInputStream(), out);
			boolean validateFileSize = FileUtils.validateImageSize(request, response);
			if(!validateFileSize){
				// 上传oss服务器
				OSSUtils ossUtils = new OSSUtils();
				try {
					FileInputStream inputStream = new FileInputStream(basePath);
					ossUtils.uploadNewImages(inputStream,fileName);
				FileUtils.deleteDirectory(Config.getProperty("ueditor.uploadImage.url"));
				} catch (Exception e1) {
					log.error(e1.getMessage(), e1);
					params.put("state", "服务器超时,请重新上传图片");
				}
			}
//			else{
//				imgEditor.scale(basePath, basePath, 400, 400, false);
//				// 上传oss服务器
//				OSSUtils ossUtils = new OSSUtils();
//				try {
//					FileInputStream inputStream = new FileInputStream(basePath);
//					ossUtils.uploadNewImages(inputStream,fileName);
//					FileUtils.deleteDirectory(Config.getProperty("ueditor.uploadImage.url"));
//				} catch (Exception e1) {
//					log.error(e1.getMessage(), e1);
//					params.put("state", "服务器超时,请重新上传图片");
//				}
//				
//			}
		    params.put("state", "SUCCESS");
			params.put("url", visitUrl);
//		    params.put("size", upfile.getSize());
//			params.put("original", fileName);
//			params.put("type", upfile.getContentType());

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			params.put("state", "上传文件失败");
		}
		return params;
	}
}
