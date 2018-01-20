package com.flea.modules.news.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.util.FileUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.news.pojo.Activity;
import com.flea.modules.news.service.ActivityService;
 

@Controller
@RequestMapping("activity")
public class ActivityController {
	
	private ActivityService activityService;
	@Autowired
	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	/**
	 * 
	 * @method:form
	 * @Description:form 进入添加活动页面
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:13:10
	 * @param:@param news
	 * @param:@param model
	 * @param:@return
	 * @return:String
	 */
	@RequestMapping(value = "form")
	public ModelAndView form( Model model,String status,String action) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","false");
		}else{
			model.addAttribute("isCustomer","true");
		}
		model.addAttribute("action", action);
		model.addAttribute("status", status);
		return new ModelAndView("activity/activity_add");
	}
	
	/**
	 * 
	 * @method:list
	 * @Description:list	分别进入审核页面和维护页面
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:13:33
	 * @param:@param news
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@param status
	 * @param:@param title
	 * @param:@param testState
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(Activity activity, HttpServletRequest request, HttpServletResponse response, Model model,
			String status,String title,Integer testState) {
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if (Common.ROLE_FIRST_LEVLE.equals(le)) {
			model.addAttribute("isCustomer", le);
		} else {
			model.addAttribute("isCustomer", le);
			return new ModelAndView("redirect:/activity/customerList.do?status=" + activity.getStatus() + "&action="
							+ activity.getStatus());
		}
		status = testState==null?status:String.valueOf(testState);
		Page<Activity> page=activityService.find(new Page<Activity>(request,response), activity,status);
        model.addAttribute("page",page);
        model.addAttribute("activity",activity);
        ModelAndView  mv = new ModelAndView();
        if("0".equals(status) || status == null){
        	mv.setViewName("activity/activity_list");
        }else{
        	mv.setViewName("activity/activity_examine_list");
        }
		boolean reslut = activityService.isExtistNewsAddRoleAndAddPlatForm(user.getId());
	    mv.addObject("reslut", reslut);
		return mv;
	}
	/**
	 * 
	 * @Description:用户的读友会维护列表
	 * @param activity
	 * @param request
	 * @param response
	 * @param model
	 * @param status
	 * @param title
	 * @param testState
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月10日 下午4:14:00
	 */
	@RequestMapping(value = {"customerList", ""})
	public ModelAndView customerList(Activity activity, HttpServletRequest request, HttpServletResponse response, Model model,
			String status,String title,Integer testState) {
		status = testState == null ? status : String.valueOf(testState);
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer",le);
		}else{
			model.addAttribute("isCustomer",le);
		}
		Page<Activity> page=activityService.findCustomer(new Page<Activity>(request,response), activity,status);
		model.addAttribute("page", page);
		model.addAttribute("activity", activity);
		ModelAndView  mv = new ModelAndView();
		if("0".equals(status) || status == null){
			mv.setViewName("activity/activity_customer_list");
		}else{
			mv.setViewName("activity/activity_examine_customer_list");
		}
		boolean reslut = activityService.isExtistNewsAddRoleAndAddPlatForm(user.getId());
	    mv.addObject("reslut", reslut);
		return mv;
	}
	
	/**
	 * 
	 * @method:save
	 * @Description:save	保存活动
	 * @author: HeTao
	 * @date:2016年6月24日 上午11:12:56
	 * @param:@param news
	 * @param:@param model
	 * @param:@param request
	 * @param:@param response
	 * @param:@return
	 * @param:@throws IOException
	 * @return:ModelAndView
	 */
	@RequestMapping("save")
	public ModelAndView save(Activity activity, Model model,HttpServletRequest request,
			HttpServletResponse response)throws IOException {
		String fileName = FileUtils.uploadImages(request, response,"newsImages");
		activity.setImage(fileName);
		activity = activityService.setActivityArea(activity,request);
		if ("2".equals(activity.getType())) {
			activity.setType("单位活动");
		}
		if ("3".equals(activity.getType())) {
			activity.setType("图书馆活动");
		}
		User user = ShiroUtils.getCurrentUser();
		if(null != user.getCustomer()) {
			activity.setCustomerId(user.getCustomer().getId());
		} else {
			activity.setCustomerId(0);
		}
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			if(activity.getStatus() == 1){
				activity.setStatus(1);
			}else{
				activity.setStatus(5);
			}
		}else{
             if(activity.getStatus() == 1){
            	activity.setStatus(6);
			}else{
				activity.setStatus(10);
			}
		}
		activityService.saveOne(activity);
		return new ModelAndView("redirect:/activity/form.do?status=" + activity.getStatus() + "&action=" + activity.getStatus());
	}
	
	/**
	 * 
	 * @method:updateView
	 * @Description:updateView	进入修改界面
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:38:59
	 * @param:@param id
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("updateView")
	public ModelAndView updateView(Integer id,Model model,String status,String action) {
		Activity activity = activityService.queryActivityInfo(id);
		activity.setId(id);
		model.addAttribute("action", action);
		model.addAttribute("status", status);
		model.addAttribute("newsInfo",activity);
		return new ModelAndView("activity/activity_update");
	}
	
	/**
	 * 
	 * @method:updateActivityInfo
	 * @Description:updateActivityInfo 更新一条资讯
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:46:46
	 * @param:@param id
	 * @param:@param activity
	 * @param:@param request
	 * @param:@param response
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("updateinfo")
	public ModelAndView updateActivityInfo(Integer id,Activity activity,HttpServletRequest request,
			HttpServletResponse response) {
		String leve = ShiroUtils.getCurrentUserRoleLevel(ShiroUtils.getCurrentUser());
		if(Common.ROLE_FIRST_LEVLE.equals(leve)){
			if(activity.getStatus() == 1){
			activity.setStatus(1);
			}else{
				activity.setStatus(5);
			}
		}else{
             if(activity.getStatus() == 1){
            	activity.setStatus(6);
			}else{
				activity.setStatus(10);
			}
		}
		//得到上传图片的名字
		String fileName;
		fileName = FileUtils.uploadImages(request, response,"newsImages");
		activity.setImage(fileName);
		activity = activityService.setActivityArea(activity,request);
		activityService.updateActivityInfo(id, activity);
		return new ModelAndView("redirect:/activity/updateView.do?status="+activity.getStatus()+"&id="+id+"&action="+activity.getStatus());
	}
	
	/**
	 * 
	 * @method:deleteActivity
	 * @Description:deleteActivity
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:53:32
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("del")
	@ResponseBody
	public ModelAndView deleteActivity(Integer id) {
		activityService.deleteActivity(id);
		return new ModelAndView("redirect:/activity/list.do?status=0");
	}
	
	/**
	 * 
	 * @method:examine
	 * @Description:examine 审核界面
	 * @author: HeTao
	 * @date:2016年6月24日 下午2:57:57
	 * @param:@param id
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("examine")
	public ModelAndView examine(Integer id,Model model,Integer status) {
		Activity activity = activityService.getOne(id);
		User user = ShiroUtils.getCurrentUser();
		Role role = user.getRoles().get(0);
		String le = role.getLevel();
		if(Common.ROLE_FIRST_LEVLE.equals(le)){
			model.addAttribute("isCustomer","0");
		}else{
			model.addAttribute("isCustomer","1");
			return new ModelAndView("redirect:/activity/customerExamine.do?&id=" + id );
		}
		if(status != null){
			//1 是查看界面
			model.addAttribute("status","1");
		}else{
			//0 是审核界面
			model.addAttribute("status","0");
		}
	    model.addAttribute("activity",activity);
		return new ModelAndView("activity/activity_examine");
	}
	/**
	 * 
	 * @Description:用户读友会审核详情页面
	 * @param id
	 * @param model
	 * @param status
	 * @return    
	 * ModelAndView    返回类型
	 * @throws
	 * @author QL 
	 * @date 2017年4月10日 下午5:01:59
	 */
	@RequestMapping("customerExamine")
	public ModelAndView customerExamine(Integer id,Model model,Integer status) {
		Activity activity = activityService.getOne(id);
		if(status != null){
			//1 是查看界面
			model.addAttribute("status","1");
		}else{
			//0 是审核界面
			model.addAttribute("status","0");
		}
		model.addAttribute("activity",activity);
		return new ModelAndView("activity/activity_examine_customer");
	}
	
	/**
	 * 
	 * @method:audit
	 * @Description:audit	审核通过或者不通过或者其他状态
	 * @author: HeTao
	 * @date:2016年6月24日 下午3:35:37
	 * @param:@param id
	 * @param:@param status
	 * @param:@param request
	 * @param:@param response
	 * @param:@param model
	 * @param:@return
	 * @return:ModelAndView
	 */
	@RequestMapping("audit")
	@ResponseBody
	public ModelAndView audit(Integer id,Integer status, String reject, Model model) {
		//驳回消息，存入驳回理由，否则不存
		String leve = ShiroUtils.getCurrentUserRoleLevel(ShiroUtils.getCurrentUser());
		if(Common.ROLE_FIRST_LEVLE.equals(leve)){
			//点击审核驳回显示当前的审核列表页面
			if (status==3 ) {
				activityService.audit(id, status, reject);
				return  new ModelAndView("redirect:/activity/list.do?&status=1");
			} else {
				if(status == 2){
					//审核通过显示当前的审核列表页面
					activityService.audit(id, status);
					return  new ModelAndView("redirect:/activity/list.do?&status=1");
				}else{
					//弃审显示当前列表页面
					activityService.audit(id, status);
					return  new ModelAndView("redirect:/activity/list.do");	
				}
			}
		}else{
			//点击审核驳回显示当前的审核列表页面
			if (status==3 ) {
				activityService.audit(id, status, reject);
				return  new ModelAndView("redirect:/activity/customerList.do?&status=1");
			} else {
				//审核通过显示当前的审核列表页面
				if(status == 2){
				activityService.audit(id, status);
				return  new ModelAndView("redirect:/activity/customerList.do?&status=1");
				}else{
				//弃审显示当前列表页面
				activityService.audit(id, status);
				return  new ModelAndView("redirect:/activity/customerList.do");
				}
			}
		}
		
	}
	
	/**
	 * 
	 * @method:top
	 * @Description:top	更新置顶
	 * @author: HeTao
	 * @date:2016年6月30日 下午4:19:56
	 * @param:@param id
	 * @param:@param redirectAttributes
	 * @param:@param state
	 * @param:@return
	 * @return:JSONObject
	 */
	@RequestMapping("top")
	@ResponseBody
	public ModelAndView top(Integer id, RedirectAttributes redirectAttributes,Integer state) {
		activityService.updateTopById(id,state);
		return new ModelAndView("redirect:/activity/list.do?status=0");
	}
	
}
