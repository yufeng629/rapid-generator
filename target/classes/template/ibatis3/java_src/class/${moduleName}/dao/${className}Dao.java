<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>   
package ${basepackage}.${moduleName}.dao;

<#include "/java_imports.include">

import org.springframework.stereotype.Repository;
import com.o2o.base.dao.BaseMybatisDao;
import ${basepackage}.${moduleName}.bean.${className};


@Repository
public class ${className}Dao extends BaseMybatisDao<${className},${table.idColumn.javaType}>{
	

	
	
	<#list table.columns as column>
	<#if column.unique && !column.pk>
	public ${className} getBy${column.columnName}(${column.javaType} v) {
		return this.getSqlSession().selectOne("${className}.getBy${column.columnName}",v);
	}	
	
	</#if>
	</#list>

}
