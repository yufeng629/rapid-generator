<#include "/java_copyright.include">
<#include "/java_imports.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>
<#assign managerClass = className+"Manager">
<#assign managerName = table.classNameFirstLower+"Manager">

package ${basepackage}.business.${moduleName}.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import com.o2o.common.bean.BaseResponse;
import ${basepackage}.${moduleName}.service.${className}Service;
import ${basepackage}.${moduleName}.bean.${className};
import ${basepackage}.business.${moduleName}.bll.${className}Manager;

import com.o2o.business.util.CommonFunction;

@Service
@Path("${className}Service")
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class ${className}ServiceImpl implements ${className}Service{
	@Autowired
	private ${managerClass} ${managerName};

	@Path("add${className}")
	public BaseResponse<${className}> add${className}(${className} ${table.classNameFirstLower}){
		//↓↓↓ business codes ↓↓↓

		return ${managerName}.insert${className}(${table.classNameFirstLower});
	}

	@Path("batchAdd${className}")
	public BaseResponse<List<${className}>> batchAdd${className}(List<${className}> ${table.classNameFirstLower}List){
		//↓↓↓ business codes ↓↓↓


		return ${managerName}.batchInsert${className}(${table.classNameFirstLower}List);
	}

	@Path("edit${className}")
	public BaseResponse edit${className}(${className} ${table.classNameFirstLower}){
		//↓↓↓ business codes ↓↓↓


		return ${managerName}.update${className}(${table.classNameFirstLower});
	}

	@Path("batchEdit${className}")
	public BaseResponse batchEdit${className}(List<${className}> ${table.classNameFirstLower}List){
		//↓↓↓ business codes ↓↓↓


		return ${managerName}.batchUpdate${className}(${table.classNameFirstLower}List);
	}

	@Path("find${className}ById")
	public ${className} find${className}ById(Long ${table.pkColumn.columnNameFirstLower}){
		if(${table.pkColumn.columnNameFirstLower} == null) return null;
		return ${managerName}.find${className}ById(${table.pkColumn.columnNameFirstLower});
	}

	@Path("find${className}ByIdList")
	public List<${className}> find${className}ByIdList(List<Long> ${table.pkColumn.columnNameFirstLower}List){
		if(${table.pkColumn.columnNameFirstLower}List == null || ${table.pkColumn.columnNameFirstLower}List.isEmpty()) return null;
		return ${managerName}.find${className}ByIdList(${table.pkColumn.columnNameFirstLower}List);
	}

	@Path("find${className}ByFieldMap")
	public BaseResponse<List<${className}>> find${className}ByFieldMap(Map<String, Object> filter, Integer pageCurrent, Integer pageSize, String sortColumns){//accurately query have pagination
		String validParamString = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		String validOrderBy = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		if(sortColumns == null || sortColumns == "") sortColumns = "${table.pkColumn.columnNameFirstLower} DESC";

		if(! CommonFunction.isValidQueryParam(filter, validParamString)){
            return BaseResponse.fail("存在未允许的查询条件！");
        }else if((sortColumns = CommonFunction.getRealOrderByField(sortColumns, validOrderBy)) == null){
            return BaseResponse.fail("存在未允许的排序字段！");
        }
		
		if(pageCurrent == null) pageCurrent = 1;
		if(pageSize == null) pageSize = 1;

        List<${className}> ${table.classNameFirstLower}List = null;
        Integer totalRecord = ${managerName}.count${className}ByDynamic(filter);
        if(totalRecord > 0){
            Integer offset = (pageCurrent - 1) * pageSize;
            ${table.classNameFirstLower}List = ${managerName}.find${className}ByDynamic(filter, offset, pageSize, sortColumns);
        }

        return BaseResponse.success(${table.classNameFirstLower}List);
	}

	@Path("find${className}ByFieldMapNoPage")
	public BaseResponse<List<${className}>> find${className}ByFieldMapNoPage(Map<String, Object> filter, String sortColumns){//accurately query without pagination
		String validParamString = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		String validOrderBy = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		if(sortColumns == null || sortColumns == "") sortColumns = "${table.pkColumn.columnNameFirstLower} DESC";

		if(filter == null || filter.isEmpty()){
			return BaseResponse.fail("请添加查询条件");
		}else if(! CommonFunction.isValidQueryParam(filter, validParamString)){
            return BaseResponse.fail("存在未允许的查询条件！");
        }else if((sortColumns = CommonFunction.getRealOrderByField(sortColumns, validOrderBy)) == null){
            return BaseResponse.fail("存在未允许的排序字段！");
        }

		List<${className}> ${table.classNameFirstLower}List = ${managerName}.find${className}ByDynamic(filter, sortColumns);
        return BaseResponse.success(${table.classNameFirstLower}List);
	}

	@Path("find${className}ByRange")
	public BaseResponse<List<${className}>> find${className}ByRange(Map<String, Object> filter, Integer pageCurrent, Integer pageSize, String sortColumns){//range query have pagination
		String validParamString = "<#list table.columns as column><#if column.javaType == 'java.lang.Integer' || column.javaType == 'java.lang.Long'>${column.columnNameFirstLower}List<#elseif column.javaType == 'java.lang.String'>${column.columnNameFirstLower}<#elseif column.javaType == 'java.util.Date'>startOf${column.columnName},endOf${column.columnName}</#if><#if column_has_next>,</#if></#list>";
		String validOrderBy = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		if(sortColumns == null || sortColumns == "") sortColumns = "${table.pkColumn.columnNameFirstLower} DESC";

		if(! CommonFunction.isValidQueryParam(filter, validParamString)){
            return BaseResponse.fail("存在未允许的查询条件！");
        }else if((sortColumns = CommonFunction.getRealOrderByField(sortColumns, validOrderBy)) == null){
            return BaseResponse.fail("存在未允许的排序字段！");
        }

		if(pageCurrent == null) pageCurrent = 1;
		if(pageSize == null) pageSize = 1;

        List<${className}> ${table.classNameFirstLower}List = null;
        Integer totalRecord = ${managerName}.count${className}ByRange(filter);
        if(totalRecord > 0){
            Integer offset = (pageCurrent - 1) * pageSize;
            ${table.classNameFirstLower}List = ${managerName}.find${className}ByRange(filter, offset, pageSize, sortColumns);
        }

        return BaseResponse.success(${table.classNameFirstLower}List);
	}

	@Path("find${className}ByRangeNoPage")
	public BaseResponse<List<${className}>> find${className}ByRangeNoPage(Map<String, Object> filter, String sortColumns){//range query without pagination
		String validParamString = "<#list table.columns as column><#if column.javaType == 'java.lang.Integer' || column.javaType == 'java.lang.Long'>${column.columnNameFirstLower}List<#elseif column.javaType == 'java.lang.String'>${column.columnNameFirstLower}<#elseif column.javaType == 'java.util.Date'>startOf${column.columnName},endOf${column.columnName}</#if><#if column_has_next>,</#if></#list>";
		String validOrderBy = "<#list table.columns as column>${column.columnNameFirstLower}<#if column_has_next>,</#if></#list>";
		if(sortColumns == null || sortColumns == "") sortColumns = "${table.pkColumn.columnNameFirstLower} DESC";

		if(filter == null || filter.isEmpty()){
			return BaseResponse.fail("查询条件不能为空");
		}else if(! CommonFunction.isValidQueryParam(filter, validParamString)){
            return BaseResponse.fail("存在未允许的查询条件！");
        }else if((sortColumns = CommonFunction.getRealOrderByField(sortColumns, validOrderBy)) == null){
            return BaseResponse.fail("存在未允许的排序字段！");
        }

		List<${className}> ${table.classNameFirstLower}List = ${managerName}.find${className}ByRange(filter, sortColumns);
        return BaseResponse.success(${table.classNameFirstLower}List);
	}
}
