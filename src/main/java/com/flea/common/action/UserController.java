package com.flea.common.action;




import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.Common;
import com.flea.common.pojo.Page;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.pojo.UserOperation;
import com.flea.common.pojo.UserRoleMap;
import com.flea.common.service.RoleService;
import com.flea.common.service.UserOperationService;
import com.flea.common.service.UserRoleMapService;
import com.flea.common.service.UserService;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.JsonUtil;
import com.flea.common.util.ModelAndViewUtils;
import com.flea.common.util.PasswordHelper;
import com.flea.common.util.ResponseUtils;
import com.flea.common.util.SendMessages;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.pojo.Library;
import com.flea.modules.system.service.LibraryService;
import com.taobao.api.ApiException;

@Controller
@RequestMapping(value="/cms/user")
public class UserController {
	@Resource
	private UserService userService;
	@Resource
	private RoleService roleService;
	@Resource
	private UserOperationService userOperationService;
	
	
	@RequestMapping(value="list")
	public ModelAndView list(Integer pageNum,String search,Integer deptid,String deptname,HttpServletRequest request,HttpServletResponse response) {
		User user = ShiroUtils.getCurrentUser();
		String hallCode = user.getHallCode();
		if(hallCode.toUpperCase().equals(Common.TZPT_OCCUPY_DEPCODE)){
			hallCode = "";
		}
		Page<User> page=userService.findPagingList(new Page<User>(request,response), search, hallCode);		
		ModelAndView mv = new ModelAndView("common/user_list");
		mv.addObject("page", page);
		mv.addObject("deptname", deptname);
		mv.addObject("search", search);
		return mv;
	}
	@ResponseBody
	@RequestMapping(value="del/{id}",method=RequestMethod.POST)
	public JSONObject del(@PathVariable Integer id, HttpServletRequest request) {
		boolean bool = userService.deleteById(id);
		/**
		 * 删除用户成功，将操作信息保存到数据仓库
		 */
		if (bool) {
			UserOperation userOperation = new UserOperation();
			userOperation.setModifyUser(ShiroUtils.getCurrentUser());
			userOperation.setModifyDate(new Date());
			userOperation.setIpAddr(request.getRemoteAddr());
			userOperation.setOperation("del");
			userOperation.setUserId(id);
			userOperationService.saveOne(userOperation);
		}
		return JsonUtil.createSuccessJson(bool);
	}
	
	/**
	 * 启用用户
	 * @param id
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="startUse/{id}",method=RequestMethod.POST)
	public JSONObject startUse(@PathVariable Integer id, HttpServletRequest request) {
		boolean bool = userService.startUseById(id);
		
		return JsonUtil.createSuccessJson(bool);
	}
	
	@ResponseBody
	@RequestMapping(value="stop/{id}",method=RequestMethod.POST)
	public JSONObject sotp(@PathVariable String id){
		return JsonUtil.createSuccessJson(userService.sotp(id));
	}
	@ResponseBody
	@RequestMapping(value="start/{id}",method=RequestMethod.POST)
	public JSONObject start(@PathVariable String id){
		return JsonUtil.createSuccessJson(userService.start(id));
	}
	@RequestMapping(value="/form",method=RequestMethod.GET)
	public ModelAndView add(String save){
		ModelAndView mav = ModelAndViewUtils.createAddFormModelAndView("common/user_add");
		setCDeptAndRole(mav);
		mav.addObject("action", "add");
		mav.addObject("save", save);
		return mav;
	}
	
	public void setCDeptAndRole(ModelAndView mav){
		User loginUser = ShiroUtils.getCurrentUser();
		String level = ShiroUtils.getCurrentUserRoleLevel(loginUser);
		if(level.equals(Common.ROLE_FIRST_LEVLE)){
			mav.addObject("roles",roleService.findRolesByLevel(Common.ROLE_FIRST_LEVLE));
		}else {
			List<Role> roles  = roleService.getRoleByOwnerId(loginUser.getId());
			mav.addObject("roles",roles);
		}
	}
	
	@Resource
	private UserRoleMapService userRoleService;
	@RequestMapping(value="/add",method=RequestMethod.POST)//客户新增用户remark为1
	public void add(User newUser,HttpServletRequest request,HttpServletResponse response) throws IOException{
		String [] roleIds = newUser.getRoleIds().trim().split(",");
		userService.saveOne(newUser);
		for(String roleId:roleIds){
			UserRoleMap userRoleMap = new UserRoleMap();
			userRoleMap.setRoleId(Integer.parseInt(roleId));
			userRoleMap.setUserId(newUser.getId());
			userRoleService.saveOne(userRoleMap);
		}
		ResponseUtils.renderText(response, "add");
	}
	@RequestMapping(value="/valiUserName")
	@ResponseBody
	public boolean valiUserName(String userName,Integer userid,String hallCode) {
		if(StringUtils.isBlank(hallCode)){
			User user = ShiroUtils.getCurrentUser();
			hallCode = user.getHallCode();
		}
		return userService.valiUserName(userName,userid,hallCode);
	}

	@RequestMapping(value="/edit/{id}",method=RequestMethod.GET)
	public ModelAndView edit(@PathVariable Integer id,HttpSession session, HttpServletRequest request){
		ModelAndView mav = null;
		User user = userService.getOne(id);
		System.out.println(user.getRoles().get(0).getId()+"========]");
		Integer roleId = user.getRoles().get(0).getId();
		if (1 == roleId || 3 == roleId) {
			//超级权限跳转
			mav = ModelAndViewUtils.createEditFormModelAndView("common/user_edit");
		} else {
			//普通权限跳转
			mav = ModelAndViewUtils.createEditFormModelAndView("common/user_add");
		}
		/**
		 * 修改记录修改人信息
		 */
		UserOperation userOperation = new UserOperation();
		userOperation.setModifyUser(ShiroUtils.getCurrentUser());
		userOperation.setModifyDate(new Date());
		userOperation.setIpAddr(request.getRemoteAddr());
		userOperation.setOperation("update");
		userOperation.setUserId(id);
		userOperationService.saveOne(userOperation);
		setCDeptAndRole(mav);
		mav.addObject("action", "edit");
		mav.addObject("euser", user);
		mav.addObject("roleId", roleId);
		return mav;
	}
	
	@RequestMapping(value="/edit",method=RequestMethod.POST)
	public void edit(User user,HttpSession session,HttpServletResponse response){
		String [] roleIds = user.getRoleIds().trim().split(",");
		User loginUser = ShiroUtils.getCurrentUser();
		user.setLibrary(loginUser.getLibrary());
		
		userService.update(user);
		userRoleService.deleteByUserId(user.getId());
		for(String roleId:roleIds){
			UserRoleMap userRoleMap = new UserRoleMap();
			userRoleMap.setRoleId(Integer.parseInt(roleId));
			userRoleMap.setUserId(user.getId());
			userRoleService.saveOne(userRoleMap);
		}
		ResponseUtils.renderText(response, "edit");
	}
	
//	@ResponseBody
//	@RequestMapping(value="/checkIdentificationCardNotExist")
//	public boolean checkIdentificationCardNotExist(String identificationCard){
//		return userService.checkIdentificationCardNotExist(identificationCard);
//	}
	
	/**
	 * 客户首次发送密码
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/sendPwd")
	private JSONObject validPaswd(String hallCode,String name,String phone,Integer userId) {
			String passwd = PasswordHelper.getStringRandom(6);//随机生成密码
			String content ="{hallCode:'"+hallCode+"',name:'"+name+"',passwd:'"+passwd+"'}";
			boolean falg = false;
			try {
				falg = userService.setPaswd(userId, passwd);
				SendMessages.sendMessages(phone,content,"SMS_16810379");//SMS_13221457
			} catch (ApiException e) {
				e.printStackTrace();
			}
			return JsonUtil.createSuccessJson(falg);
	}
	
	/**
	 * 客户再次发送密码
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/logininfo/{pwd}")
	private JSONObject loginInfo(@PathVariable String pwd,String hallCode,String name,String phone,Integer userId) {
		User user = ShiroUtils.getCurrentUser();
		pwd =Base64.decodeToString(pwd);
		pwd =EncryptUtils.encryptMD5(pwd);
		boolean falg = false;
		if(pwd.equals(user.getPassword())){
			String passwd = PasswordHelper.getStringRandom(6);//随机生成密码
			String content ="{hallCode:'"+hallCode+"',name:'"+name+"',passwd:'"+passwd+"'}";
			try {
				falg = userService.setPaswd(userId, passwd);
				SendMessages.sendMessages(phone,content,"SMS_16810379");
			} catch (ApiException e) {
				e.printStackTrace();
			}
		
			return JsonUtil.createSuccessJson(falg);
		}else {
			return JsonUtil.createSuccessJson(false);
		}
	}
	
	
	/**
	 * 密码验证
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/validate/{pwd}",method=RequestMethod.POST)
	private JSONObject validatePwd(@PathVariable String pwd) {
		User user = ShiroUtils.getCurrentUser();
		pwd =Base64.decodeToString(pwd);
		pwd =EncryptUtils.encryptMD5(pwd);
		if(pwd.equals(user.getPassword())){
			return JsonUtil.createSuccessJson(true);
		}else {
			return JsonUtil.createSuccessJson(false);
		}
	}
	
	@Autowired
	private LibraryService libraryService;
	/**
	 * 图书馆发送密码
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/valid/{pwd}",method=RequestMethod.POST)
	private JSONObject validPwd(@PathVariable String pwd,String hallCode,String phone) {
		if(StringUtils.isBlank(pwd)){
			sendPassword(hallCode,phone);
			return JsonUtil.createSuccessJson(true);
		}else {
			User user = ShiroUtils.getCurrentUser();
			pwd =Base64.decodeToString(pwd);
			pwd =EncryptUtils.encryptMD5(pwd);
			if(pwd.equals(user.getPassword())){
				Library library = libraryService.findByHallCode(hallCode);
				Boolean save = libraryService.updateLibraryUserPassword(hallCode,library.getIsEffective());
			    if(save) {
			    	org.apache.shiro.session.Session currentSession = SecurityUtils.getSubject().getSession();
					String passwd =(String)currentSession.getAttribute(hallCode+"userPassword");
			 		PasswordHelper.sendPassword(hallCode,phone, passwd);
			 		return JsonUtil.createSuccessJson(true);
			    } else {
			    	return JsonUtil.createSuccessJson(false);
			    }
			}else {
				return JsonUtil.createSuccessJson(false);
			}
		}
	}
	
	
	public void sendPassword(String hallCode,String phone) {
		String passwd = PasswordHelper.getStringRandom(6);//随机生成密码
		String content ="{hallCode:'"+hallCode+"',name:'admin',passwd:'"+passwd+"'}";
		try {
			SendMessages.sendMessages(phone,content,"SMS_13191460");
		} catch (ApiException e) {
			e.printStackTrace();
		}
	 	Session currentSession = SecurityUtils.getSubject().getSession();
	 	String md5 = EncryptUtils.encryptMD5(passwd);
	 	currentSession.setAttribute("userPasswrd",md5);
	}
	
	/**
	 * 重置密码
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/resetPaswd/{id}")
	private JSONObject resetPaswd(@PathVariable String id) {
		return JsonUtil.createSuccessJson(true,userService.resetPaswd(id));
	}
	
	/**
	 * 
	 * @method:changePswPage	跳转更改密码页面
	 * @Description:changePswPage
	 * @author: HeTao
	 * @date:2016年8月12日 下午2:48:12
	 * @param:@return
	 * @return:ModelAndView
	 */
	@ResponseBody
	@RequestMapping(value="changePswPage")
	private ModelAndView changePswPage(){
		return new ModelAndView("editpsw");
	}
	
	/**
	 * 
	 * @method:changePsw	更改新密码
	 * @Description:changePsw
	 * @author: HeTao
	 * @date:2016年8月12日 下午2:55:14
	 * @param:@param oldpsw
	 * @param:@param newpsw
	 * @param:@return
	 * @return:ModelAndView
	 */
	@ResponseBody
	@RequestMapping(value="changePsw",method=RequestMethod.POST)
	private String changePsw(String oldpsw,String newpsw){
		if(oldpsw.trim().length() == 0 || newpsw.trim().length() == 0){
			return "false";
		}
		boolean flag = userService.changePsw(oldpsw,newpsw);
		//修改成功返回true    修改不成功返回false
		if (flag) {
			return "true";
		}else{
			return "false";
		}
	}
	
	
}
