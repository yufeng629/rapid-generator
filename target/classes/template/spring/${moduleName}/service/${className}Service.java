<#include "/java_copyright.include">
<#include "/java_imports.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>

package ${basepackage}.${moduleName}.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.o2o.common.bean.BaseResponse;
import ${basepackage}.${moduleName}.bean.${className};

public interface ${className}Service{
	BaseResponse<${className}> add${className}(${className} ${table.classNameFirstLower});

	BaseResponse<${className}> batchAdd${className}(List<${className}> ${table.classNameFirstLower}List);

	BaseResponse edit${className}(${className} ${table.classNameFirstLower});

	BaseResponse batchEdit${className}(List<${className}> ${table.classNameFirstLower}List);

	${className} find${className}ById(Long ${table.pkColumn.columnNameFirstLower}Id);

	List<${className}> find${className}ByIdList(List<Long> ${table.pkColumn.columnNameFirstLower}List);

	BaseResponse<List<${className}>> find${className}ByFieldMapNoPage(Map<String, Object> filter, String sortColumns);//accurately query without pagination

	BaseResponse<List<${className}>> find${className}ByFieldMap(Map<String, Object> filter, Integer offset, Integer limit, String sortColumns);//accurately query have pagination

	BaseResponse<List<${className}>> find${className}ByRangeNoPage(Map<String, Object> filter, String sortColumns);//range query without pagination

	BaseResponse<List<${className}>> find${className}ByRange(Map<String, Object> filter, Integer offset, Integer limit, String sortColumns);//range query have pagination
}
