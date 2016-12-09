<#include "/java_copyright.include">
<#include "/java_imports.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first>

package ${basepackage}.${moduleName}.bll;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ${basepackage}.${moduleName}.bean.${className};
import ${basepackage}.business.${moduleName}.dao.${className}Dao;

import com.o2o.common.bean.BaseResponse;

@Component
public class ${className}Manager{
	@Autowired
	private ${className}Dao ${table.classNameFirstLower}Dao;

	public BaseResponse<${className}> insert${className}(${className} ${table.classNameFirstLower}){
		//set default value if the column had defined
<#list table.notPkColumns as column>
	<#if column.columnNameFirstLower == "createTime" || column.columnNameFirstLower == "updateTime">
		${table.classNameFirstLower}.set${column.columnName}(new Date());
	<#elseif column.defaultValue??>
		if(${table.classNameFirstLower}.get${column.columnName}() == null) ${table.classNameFirstLower}.set${column.columnName}(${column.defaultValue});
	</#if>
</#list>

		String validateMessage = ${table.classNameFirstLower}.getValidateMessage();
		if(! "".equals(validateMessage)) return BaseResponse.fail(validateMessage);
		
		boolean insertResult = ${table.classNameFirstLower}Dao.insert(${table.classNameFirstLower}) > 0;
		if(insertResult){
			return BaseResponse.success(${table.classNameFirstLower});
		}else{
			return BaseResponse.fail("新增记录失败！");
		}
	}

	public BaseResponse<List<${className}>> batchInsert${className}(List<${className}> ${table.classNameFirstLower}List){
		//set default value if the column had defined
		for(${className} obj : ${table.classNameFirstLower}List){
	<#list table.notPkColumns as column>
		<#if column.columnNameFirstLower == "createTime" || column.columnNameFirstLower == "updateTime">
			obj.set${column.columnName}(new Date());
		<#elseif column.defaultValue??>
			if(obj.get${column.columnName}() == null) obj.set${column.columnName}(${column.defaultValue});
		</#if>
	</#list>

			String validateMessage = obj.getValidateMessage();
			if(! "".equals(validateMessage)) return BaseResponse.fail(validateMessage);
		}
		
		boolean insertResult = ${table.classNameFirstLower}Dao.insert(${table.classNameFirstLower}List) > 0;
		if(insertResult){
			return BaseResponse.success(${table.classNameFirstLower}List);
		}else{
			return BaseResponse.fail("批量新增记录失败！");
		}
	}

	public BaseResponse update${className}(${className} ${table.classNameFirstLower}){
		${className} objOld = find${className}ById(${table.classNameFirstLower}.get${table.pkColumn.columnName}());
		if(objOld == null) BaseResponse.fail("要更新的记录不存在！");
		//set default value if the column had defined
<#list table.notPkColumns as column>
	<#if column.columnNameFirstLower == "createTime">
		${table.classNameFirstLower}.set${column.columnName}(objOld.get${column.columnName}());
	<#elseif column.columnNameFirstLower == "updateTime">
		${table.classNameFirstLower}.set${column.columnName}(new Date());
	</#if>
		if(${table.classNameFirstLower}.get${column.columnName}() == null) ${table.classNameFirstLower}.set${column.columnName}(objOld.get${column.columnName}());
</#list>

		String validateMessage = ${table.classNameFirstLower}.getValidateMessage();
		if(! "".equals(validateMessage)) return BaseResponse.fail(validateMessage);

		boolean updateResult = ${table.classNameFirstLower}Dao.update(${table.classNameFirstLower}) > 0;
		if(updateResult){
			return BaseResponse.success();
		}else{
			return BaseResponse.fail("更新记录失败！");
		}
	}

	public BaseResponse batchUpdate${className}(List<${className}> ${table.classNameFirstLower}List){
		List<Long> idList = new ArrayList();
		for(${className} obj : ${table.classNameFirstLower}List){
			idList.add(obj.get${table.pkColumn.columnName}());
		}
		
		Map<Long, ${className}> ${className}IdMap = new HashMap<>();
		List<${className}> oldObjList = find${className}ByIdList(idList);
		if(oldObjList != null && ! oldObjList.isEmpty()){
			for(${className} obj : ${table.classNameFirstLower}List){
				${className}IdMap.put(obj.get${table.pkColumn.columnName}(), obj);
			}
		}

		//set default value if the column had defined
		for(${className} objNew : ${table.classNameFirstLower}List){
			if(! ${className}IdMap.containsKey(objNew.get${table.pkColumn.columnName}())) return BaseResponse.fail("id为"+objNew.get${table.pkColumn.columnName}()+"的记录不存在!");
			${className} objOld = ${className}IdMap.get(objNew.get${table.pkColumn.columnName}());
	<#list table.notPkColumns as column>
		<#if column.columnNameFirstLower == "createTime">
			objNew.set${column.columnName}(objOld.get${column.columnName}());
		<#elseif column.columnNameFirstLower == "updateTime">
			objNew.set${column.columnName}(new Date());
		<#else>
			if(objNew.get${column.columnName}() == null) objNew.set${column.columnName}(objOld.get${column.columnName}());
		</#if>
	</#list>

			String validateMessage = objNew.getValidateMessage();
			if(! "".equals(validateMessage)) return BaseResponse.fail("id为"+objNew.get${table.pkColumn.columnName}()+"的数据不合法，"+validateMessage);
		}

		boolean updateResult = ${table.classNameFirstLower}Dao.updateList(${table.classNameFirstLower}List) > 0;
		if(updateResult){
			return BaseResponse.success();
		}else{
			return BaseResponse.fail("批量更新记录失败！");
		}
	}

	public ${className} find${className}ById(Long ${table.pkColumn.columnNameFirstLower}){
		return ${table.classNameFirstLower}Dao.getById(${table.pkColumn.columnNameFirstLower});
	}

	public List<${className}> find${className}ByIdList(List<Long> ${table.pkColumn.columnNameFirstLower}List){
		return ${table.classNameFirstLower}Dao.getByIdList(${table.pkColumn.columnNameFirstLower}List);
	}

	public Integer count${className}ByDynamic(Map<String, Object> filter){
		return ${table.classNameFirstLower}Dao.getCounts(filter);
	}

	public List<${className}> find${className}ByDynamic(Map<String, Object> filter, Integer offset, Integer limit, String sortColumns){
		return ${table.classNameFirstLower}Dao.getList(filter, offset, limit, sortColumns);
	}

	public List<${className}> find${className}ByDynamic(Map<String, Object> filter, String sortColumns){//set sortColumns = null if no need to order by
		return ${table.classNameFirstLower}Dao.getList(filter, sortColumns);
	}

	public Integer count${className}ByRange(Map<String, Object> filter){
		return ${table.classNameFirstLower}Dao.countByRange(filter);
	}

	public List<${className}> find${className}ByRange(Map<String, Object> filter, Integer offset, Integer limit, String sortColumns){
		return ${table.classNameFirstLower}Dao.getList(filter, offset, limit, sortColumns);
	}

	public List<${className}> find${className}ByRange(Map<String, Object> filter, String sortColumns){//set sortColumns = null if no need to order by
		return ${table.classNameFirstLower}Dao.findByRange(filter, sortColumns);
	}
}
