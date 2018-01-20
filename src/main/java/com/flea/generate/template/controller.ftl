package ${packageName}.${moduleName}.action${subModuleName};

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import com.flea.common.util.JsonUtil;
import com.flea.common.pojo.Page;
import ${packageName}.${moduleName}.pojo${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.service${subModuleName}.${ClassName}Service;

/**
 * ${functionName}Controller
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Controller
@RequestMapping(value = "${urlPrefix}")
public class ${ClassName}Controller  {

	@Autowired
	private ${ClassName}Service ${className}Service;
	
	
	
	@RequestMapping(value = {"list", ""})
	public ModelAndView list(${ClassName} ${className}, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<${ClassName}> page=${className}Service.find(new Page<${ClassName}>(request,response), ${className});
        model.addAttribute("page",page);
        model.addAttribute("${className}",${className});
		return new ModelAndView("system/providerList");
	}

	@RequestMapping(value = "form")
	public String form(${ClassName} ${className}, Model model) {
		model.addAttribute("${className}", ${className});
		return "${viewPrefix}Form";
	}

	@RequestMapping(value = "save")
	public ModelAndView save(${ClassName} ${className}, Model model) {
		${className}Service.saveOne(${className});
		return new ModelAndView("redirect:list.html");
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public JSONObject delete(Integer id, RedirectAttributes redirectAttributes) {
	return JsonUtil.createSuccessJson(${className}Service.deleteById(id));
	}

}
