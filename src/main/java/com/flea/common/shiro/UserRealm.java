package com.flea.common.shiro;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSONObject;
import com.flea.common.Constants;
import com.flea.common.action.LoginController;
import com.flea.common.pojo.Menu;
import com.flea.common.pojo.Role;
import com.flea.common.pojo.User;
import com.flea.common.service.RoleService;
import com.flea.common.service.UserService;

public class UserRealm extends AuthorizingRealm {
//	private Cache<String, Deque<Serializable>> cache;
//	private SessionManager sessionManager;
	@Resource
	private UserService userService; 
	@Resource
	private RoleService roleService;
	
//	public void setCacheManager(CacheManager cacheManager) {
//		this.cache = cacheManager.getCache("shiro-kickout-session");
//	}
	
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {  
		    principals.getPrimaryPrincipal();
	    User user = (User) SecurityUtils.getSubject().getSession().getAttribute(Constants.CURRENT_USER);
	    if(user==null){
			return null;
		}
		Set<String> roleNames = new HashSet<String>();
		Set<String> rules = new HashSet<String>();
		try {
			List<Role> roleList = roleService.getRoleById(user.getId());
			for(Role role:roleList){
				roleNames.add(role.getName());
				for(Menu rule:role.getMenus()){
					String permissionStr = rule.getPermission();
					if (StringUtils.isNotBlank(permissionStr)) {
						rules.add(permissionStr.trim());
					}
				}
			}
		
	    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames); 
		info.addStringPermissions(rules);
	    return info;  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}  

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordValidCodeToken login_token = (UsernamePasswordValidCodeToken)token;
		System.out.println("login -----");
        String username = (String) login_token.getUsername(); 
        String hallCode = (String) login_token.getHallCode().toUpperCase();
		//校验码判断逻辑  
        //取得用户输入的校验码  
		String userInputValidCode = login_token.getValidCode();  
	    JSONObject  json = new JSONObject();
        Boolean  validCode =false;
        //取得真实的正确校验码  
        String realRightValidCode = (String) SecurityUtils.getSubject().getSession().getAttribute("validcode");
    	  if (LoginController.isValidateCodeLogin(hallCode+username, false, false) &&!userInputValidCode.equalsIgnoreCase(realRightValidCode)) {  
    			json.put("data", "9");
            	json.put("validCode",true);
          	throw new AuthenticationException(json.toJSONString());
          }
        //以上校验码验证通过以后,查数据库  

        User user = userService.findByUserName$HallCode(username,hallCode);
    
        if (null == user){  
        	validCode = LoginController.isValidateCodeLogin(hallCode+username, true, false);
        	json.put("data", "2");
        	json.put("validCode",validCode);
        	throw new AuthenticationException(json.toJSONString());  
        }  
    	if(!StringUtils.equals(user.getPassword(),new String(login_token.getPassword()))){
    		validCode = LoginController.isValidateCodeLogin(hallCode+username, true, false);
    	  	json.put("data", "3");
        	json.put("validCode",validCode);
			throw new AuthenticationException(json.toJSONString());
		}
      	Session currentSession = SecurityUtils.getSubject().getSession();
    	currentSession.setAttribute(Constants.CURRENT_USER, user);
        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现  
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(  
                user.getUserName(), //用户名  
                user.getPassword(), //密码  
                getName()  //realm name  
        );
        currentSession.setAttribute(Constants.CURRENT_USER, user);
        return authenticationInfo;  
	}
	
	public void userLogout(Serializable sessionId){
		org.apache.shiro.mgt.SecurityManager securityManager = SecurityUtils.getSecurityManager();
		Subject.Builder builder = new Subject.Builder(securityManager);
		builder.sessionId(sessionId);
		Subject subject = builder.buildSubject();
		if (null != subject) {
			try {
				subject.logout();
			} catch (SessionException e) {
			}
		}
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}

	
	/**
	 * 清空所有关联认证
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
	
	/**
	 * 清空用户关联权限认证，待下次使用时重新加载
	 */
	@Override
	protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
//		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
//		clearCachedAuthorizationInfo(principals);
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	protected void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

//	public Cache<String, Deque<Serializable>> getCache() {
//		return cache;
//	}
//
//	public void setCache(Cache<String, Deque<Serializable>> cache) {
//		this.cache = cache;
//	}

//	public SessionManager getSessionManager() {
//		return sessionManager;
//	}
//
//	public void setSessionManager(SessionManager sessionManager) {
//		this.sessionManager = sessionManager;
//	}
	
	
	
}
