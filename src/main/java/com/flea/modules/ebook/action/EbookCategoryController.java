package com.flea.modules.ebook.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.Page;
import com.flea.common.util.JsonUtil;
import com.flea.modules.ebook.pojo.EbookCategory;
import com.flea.modules.ebook.service.EbookCategoryService;

/**
 * 电子书分类Controller
 * @author bruce
 * @version 2016-07-04
 */
@Controller
@RequestMapping(value = "ebook/ebookCategory")
public class EbookCategoryController  {

	@Autowired
	private EbookCategoryService ebookCategoryService;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(EbookCategory ebookCategory, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<EbookCategory> page=ebookCategoryService.find(new Page<EbookCategory>(request,response), ebookCategory);
        model.addAttribute("page",page);
        model.addAttribute("ebookCategory",ebookCategory);
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(EbookCategory ebookCategory, Model model) {
		model.addAttribute("ebookCategory", ebookCategory);
		return "ebook/ebookCategoryForm";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(EbookCategory ebookCategory, Model model) {
		ebookCategoryService.saveOne(ebookCategory);
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(ebookCategoryService.deleteById(id));
	}

}
