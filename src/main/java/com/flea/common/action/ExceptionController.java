/**  
* @Package com.flea.common.action
* @Description: TODO
* @author bruce
* @date 2016年7月21日 下午5:01:06
* @version V1.0  
*/ 
package com.flea.common.action;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * @author bruce
 * @2016年7月21日 下午5:01:06
 */
@Controller
@RequestMapping("/error")
public class ExceptionController {

	
	@RequestMapping(value="/maxUploadExceeded")
	public ModelAndView maxSize(Model model){
		model.addAttribute("error", "上传文件大小不超过200M");
		 return new ModelAndView("/error");
	}
	
	
}
