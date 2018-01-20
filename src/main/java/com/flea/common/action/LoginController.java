package com.flea.common.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.pojo.User;
import com.flea.common.service.UserService;
import com.flea.common.shiro.MySessionContext;
import com.flea.common.shiro.UsernamePasswordValidCodeToken;
import com.flea.common.util.CacheUtils;
import com.flea.common.util.EncryptUtils;
import com.flea.common.util.ShiroUtils;
import com.flea.modules.system.service.LibraryService;
import com.google.common.collect.Maps;

@Controller
@RequestMapping(value = "/common")
public class LoginController {
	
	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login.html", method = RequestMethod.GET)
	public ModelAndView loginPage(Model model) {
		return new ModelAndView("login");
	}

	@ResponseBody
	@RequestMapping(value = "/loginUser", method = RequestMethod.POST)
	public String checkLogin(User user, String redirectPage, HttpServletRequest request,HttpSession httpSession,Model model) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean rememberMe = WebUtils.isTrue(request, FormAuthenticationFilter.DEFAULT_REMEMBER_ME_PARAM);
		String host = request.getRemoteHost();

		UsernamePasswordValidCodeToken token = new UsernamePasswordValidCodeToken(user.getHallCode(), user.getUserName(),EncryptUtils.encryptMD5(user.getPassword()), user.getValidCode(), rememberMe, host);
		JSONObject  json = new JSONObject();
		try {
			currentUser.login(token);
//		 if(!currentUser.isAuthenticated()){
//			 model.addAttribute("isValidateCodeLogin", isValidateCodeLogin(user.getHallCode()+user.getUserName(), true, false));
//			 return "5";
//		 }
			String userSessionId = ShiroUtils.getConextMapSessionId(httpSession);
			if(StringUtils.isNotBlank(userSessionId)){
			 	json.put("data", "8");
			 	return json.toJSONString();
			}
			ShiroUtils.setConextMapSessionId(httpSession);
		 	json.put("data", "0");
			return json.toJSONString();
		} catch (AuthenticationException e) {
			logger.error(e.getMessage(), e);
			System.out.println(e.getMessage());
			return e.getMessage();
		}
		
	}
	
	@RequestMapping(value = "/relogin", method = RequestMethod.POST)
	public @ResponseBody String reLogin(User user,HttpServletRequest request,HttpSession httpSession) {
		Subject currentUser = SecurityUtils.getSubject();
		boolean rememberMe = false;
		String host = request.getRemoteHost();
		UsernamePasswordValidCodeToken token = new UsernamePasswordValidCodeToken(user.getHallCode(), user.getUserName(),EncryptUtils.encryptMD5(user.getPassword()),
				user.getValidCode(), rememberMe, host);
		try {
//			currentUser.login(token);
			String sessionId = ShiroUtils.getConextMapSessionId(httpSession);
			if(httpSession.getId().equals(sessionId)){//同一IP重复登录
				return "0";
			}
			if(StringUtils.isNotBlank(sessionId)){//不同IP重复登录
				 HttpSession  session =MySessionContext.getSession(sessionId.toString());
				 if(session!=null){
					 session.invalidate();
				 }
			}
			ShiroUtils.setConextMapSessionId(httpSession);
			return "0";
		} catch (AuthenticationException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			return e.getMessage();
		}
	}

	@Resource
	private LibraryService deptService;

	@ResponseBody
	@RequestMapping(value = "/checkDeptcode", method = RequestMethod.POST)
	public boolean checkDeptCode(String hallCode) {
		return userService.checkByHallCode(hallCode);
	}

	/**
	 * 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @param clean
	 *            计数清零
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static boolean isValidateCodeLogin(String useruame, boolean isFail,
			boolean clean) {
		Map<String, Integer> loginFailMap = (Map<String, Integer>) CacheUtils
				.get("loginFailMap");
		if (loginFailMap == null) {
			loginFailMap = Maps.newHashMap();
			CacheUtils.put("loginFailMap", loginFailMap);
		}
		Integer loginFailNum = loginFailMap.get(useruame);
		if (loginFailNum == null) {
			loginFailNum = 0;
		}
		if (isFail) {
			loginFailNum++;
			loginFailMap.put(useruame, loginFailNum);
		}
		if (clean) {
			SecurityUtils.getSubject().getSession().setAttribute("validcode",null);
			loginFailMap.remove(useruame);
		}
		return loginFailNum >= 3;
	}

	@RequestMapping(value = "/index")
	public ModelAndView loginSuccess(Model model,HttpSession httpSession,Integer reLogin) {
		 User user = ShiroUtils.getCurrentUser();
		if (user == null) {
			model.addAttribute("message", "login");
			return new ModelAndView("login");
		}
		isValidateCodeLogin(user.getHallCode()+user.getUserName(), false, true);
		//登录后查看用户级别（客户还是平台用户）
		ModelAndView modelView = new ModelAndView("index");
		String level = ShiroUtils.getCurrentUserRoleLevel(user);
		modelView.addObject("level", level);
		return modelView;
	}

	@RequestMapping(value = "/welcome", method = RequestMethod.GET)
	public ModelAndView welcomePage() {
		return new ModelAndView("welcome");
	}

	@RequestMapping(value = "/building", method = RequestMethod.GET)
	public String building() {
		return  "405";
	}
	
	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession httpSession) {
		Subject currentUser = SecurityUtils.getSubject();
		if (SecurityUtils.getSubject().getSession() != null) {
			currentUser.logout();
		}
		return new ModelAndView("login");
	}
	
	 @RequestMapping("/download")
	 public String download(String fileName, HttpServletRequest request,
	            HttpServletResponse response) throws UnsupportedEncodingException {
	        response.setCharacterEncoding("utf-8");
	        response.setContentType("multipart/form-data");
	        System.out.println(fileName);
	        String downFileName = "云图一键安装包.rar";
	        response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(downFileName, "UTF-8"));
	        try {
	            String path = "/home/myyzx/Downloads/apache-tomcat-7.0.68/webapps/soft/";
	            fileName =path+ File.separator +URLDecoder.decode(fileName, "UTF-8");;
	            System.out.println("download--"+fileName);
	            File downFile = new File(fileName);
	            InputStream inputStream = new FileInputStream(downFile);
	            response.getOutputStream();
	            OutputStream os = response.getOutputStream();
	            byte[] b = new byte[2048];
	            int length;
	            while ((length = inputStream.read(b)) > 0) {
	            	os.write(b, 0, length);
	            }
	             // 这里主要关闭。
	            os.close();
	            inputStream.close();
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	            //  返回值要注意，要不然就出现下面这句错误！
	        return null;
	    }
}
