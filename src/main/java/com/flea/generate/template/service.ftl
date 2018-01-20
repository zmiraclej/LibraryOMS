package ${packageName}.${moduleName}.service${subModuleName};


import com.flea.common.pojo.Page;
import com.flea.common.service.BaseService;
import ${packageName}.${moduleName}.pojo${subModuleName}.${ClassName};

/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */

public interface ${ClassName}Service extends BaseService<${ClassName},Integer> {

   	Page<${ClassName}> find(Page<${ClassName}> page,${ClassName} ${className});
	
}
