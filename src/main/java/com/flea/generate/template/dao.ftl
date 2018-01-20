package ${packageName}.${moduleName}.dao${subModuleName};

import org.springframework.stereotype.Repository;

import com.flea.common.dao.BaseDao;
import ${packageName}.${moduleName}.pojo${subModuleName}.${ClassName};

/**
 * ${functionName}DAO接口
 * @author ${classAuthor}
 * @version ${classVersion}
 */
@Repository
public interface ${ClassName}Dao extends BaseDao<${ClassName},Integer> {
	
}
