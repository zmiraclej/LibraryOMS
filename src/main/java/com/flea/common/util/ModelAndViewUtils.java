package com.flea.common.util;

import org.springframework.web.servlet.ModelAndView;

import com.flea.common.pojo.Page;


public class ModelAndViewUtils {
	public enum ActionType{
		EDIT,ADD;
		@Override
		public String toString() {
			switch (this) {
			case EDIT:
				return "edit";
			case ADD:
				return "add";
				default:
					return "";
			}
		}
	};
	public static ModelAndView createModelAndView(String viewName){
		return new ModelAndView(viewName);
	}
	public static ModelAndView createFormModelAndView(String viewName,ActionType action){
		ModelAndView mv = createModelAndView(viewName);
		if (action==ActionType.ADD) {
			mv.addObject("title","添加");
		}else{
			mv.addObject("title","编辑");
		}
		mv.addObject("action",action);
		return mv;
	}
	public static ModelAndView createFormModelAndView(String viewName,ActionType action,String title){
		ModelAndView mv = createFormModelAndView(viewName, action);
		mv.addObject("title", title);
		return mv;
	}
	public static ModelAndView createFormModelAndView(String viewName,ActionType action,String title,String msg){
		ModelAndView mv = createFormModelAndView(viewName, action, title);
		mv.addObject("msg",msg);
		return mv;
	}
	public static ModelAndView createFormPageAndView(String viewName,Page<?> page){
		ModelAndView mv = createModelAndView(viewName);
		mv.addObject("page",page);
		return mv;
	}
	
	public static ModelAndView createAddFormModelAndView(String viewName){
		ModelAndView mv = createFormModelAndView(viewName,ActionType.ADD);
		return mv;
	}
	
	public static ModelAndView createEditFormModelAndView(String viewName){
		ModelAndView mv = createFormModelAndView(viewName,ActionType.EDIT);
		return mv;
	}
}
