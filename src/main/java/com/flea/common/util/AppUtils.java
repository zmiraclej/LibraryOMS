/**  
* @Package com.flea.common.util
* @Description: TODO
* @author bruce
* @date 2016年1月8日 上午10:27:31
* @version V1.0  
*/ 
package com.flea.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.flea.common.pojo.Menu;
import com.flea.modules.system.pojo.Region;


/**
 * @author bruce
 * @2016年1月8日 上午10:27:31
 */
public class AppUtils implements ApplicationContextAware{

	private static ThreadLocal<HttpServletRequest> requestLocal= new ThreadLocal<HttpServletRequest>(); 
	private static ApplicationContext appContext;
	public static void setRequest(HttpServletRequest request){
		requestLocal.set(request);
	}
	public static HttpServletRequest getRequest(){
		return requestLocal.get();
	}
	
	public static String RegionAndParentName(Region area,String cut){
		if (area!=null) {
			Region pArea = area.getParent();
			String str = RegionAndParentName(pArea,cut);
			if (str.length()!=0) {
				str +=cut; 
			}
			str += area.getName();
			return str;
		}
		return "";
	}
	
	public static String MenuAndParentName(Menu menu,String cut){
		if (menu!=null) {
			Menu pArea = menu.getParent();
			String str = MenuAndParentName(pArea,cut);
			if (str.length()!=0) {
				str +=cut; 
			}
			str += menu.getName();
			return str;
		}
		return "";
	}
	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		AppUtils.appContext = applicationContext;
	}
}
