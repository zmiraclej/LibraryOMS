package ${packageName}.${moduleName}.service${subModuleName}.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.flea.common.pojo.Page;
import com.flea.common.service.impl.BaseServiceImpl;
import ${packageName}.${moduleName}.pojo${subModuleName}.${ClassName};
import ${packageName}.${moduleName}.service${subModuleName}.${ClassName}Service;
import ${packageName}.${moduleName}.dao${subModuleName}.${ClassName}Dao;

/**
 * ${functionName}Service
 * @author ${classAuthor}
 * @version ${classVersion}
 */
 @Service
public class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName},Integer> implements ${ClassName}Service{

	@Autowired
	private ${ClassName}Dao ${className}Dao;
	
	
	@Autowired
	public  ${ClassName}ServiceImpl(${ClassName}Dao ${className}Dao) {
		super(${className}Dao);
		this.${className}Dao = ${className}Dao;
	}
	
	@Override
	public Page<${ClassName}> find(Page<${ClassName}> page,${ClassName} ${className}) {
		DetachedCriteria dc = ${className}Dao.createDetachedCriteria();
		if(StringUtils.isNotEmpty(${className}.getName())){
			dc.add(Restrictions.eq("name", ${className}.getName()));
		}
		return ${className}Dao.find(page, dc);
	}
	
}
