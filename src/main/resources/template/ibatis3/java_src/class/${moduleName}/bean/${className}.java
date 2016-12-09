<#include "/macro.include"/>
<#include "/java_copyright.include">
<#assign className = table.className>   
<#assign classNameLower = className?uncap_first> 
package ${basepackage}.${moduleName}.bean;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.apache.commons.lang3.StringUtils;

<#include "/java_imports.include">
/**
 * ${table.remarks}
 */
public class ${className} implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	

	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	<#list table.columns as column>
	//${column.columnAlias}
	private ${column.javaType} ${column.columnNameLower};
	</#list>
	//columns END

<@generateConstructor className/>
<@generateJavaColumns/>
<@generateJavaOneToMany/>
<@generateJavaManyToOne/>

	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
		<#list table.columns as column>
			.append("${column.columnName}",get${column.columnName}())
		</#list>
			.toString();
	}
	
	public int hashCode() {
		return new HashCodeBuilder()
		<#list table.pkColumns as column>
			.append(get${column.columnName}())
		</#list>
			.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof ${className} == false) return false;
		if(this == obj) return true;
		${className} other = (${className})obj;
		return new EqualsBuilder()
			<#list table.pkColumns as column>
			.append(get${column.columnName}(),other.get${column.columnName}())
			</#list>
			.isEquals();
	}

	/**
	* 验证数据是否合法,不合法时会直接抛出错误！
	*/
	public void validate(){
		<#list table.columns as column>
			<#if !column.nullable >
				<#if column.simpleJavaType == "String" >
					if (StringUtils.isBlank(this.${column.columnNameLower})) {
						throw new RuntimeException("${column.columnAlias}不能为空.");
					}
				<#else>
					if (null == this.${column.columnNameLower}) {
						throw new RuntimeException("${column.columnAlias}不能为空.");
					}
				</#if>
			</#if>
			<#if column.simpleJavaType == "String" && column.sqlTypeName != "text">
					if(null != this.${column.columnNameLower} && this.${column.columnNameLower}.length() > ${column.size}){
						throw new RuntimeException("${column.columnAlias}不能超过${column.size}个字.");
					}
			</#if>

	</#list>
	}

	/**
	* 验证数据是否合法,合法时返回空字符串，不合法时返回错误信息
	*/
	public String getValidateMessage(){
		String message = "";
		<#list table.columns as column>
			<#if !column.nullable >
				<#if column.simpleJavaType == "String" >
					if (StringUtils.isBlank(this.${column.columnNameLower})) {
						message = "${column.columnAlias}不能为空.";
					}
				<#else>
					if (null == this.${column.columnNameLower}) {
						message = "${column.columnAlias}不能为空.";
					}
				</#if>
			</#if>
			<#if column.simpleJavaType == "String" && column.sqlTypeName != "text">
					if(null != this.${column.columnNameLower} && this.${column.columnNameLower}.length() > ${column.size}){
						message = "${column.columnAlias}不能超过${column.size}个字.";
					}
			</#if>
		</#list>
		return message;
	}
}

<#macro generateJavaColumns>
	<#list table.columns as column>
	/**
	*${column.columnAlias}
	*/
	public void set${column.columnName}(${column.javaType} value) {
		this.${column.columnNameLower} = value;
	}
	
	/**
	*${column.columnAlias}
	*/
	public ${column.javaType} get${column.columnName}() {
		return this.${column.columnNameLower};
	}
	</#list>
</#macro>

<#macro generateJavaOneToMany>
	<#list table.exportedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	private Set ${fkPojoClassVar}s = new HashSet(0);
	public void set${fkPojoClass}s(Set<${fkPojoClass}> ${fkPojoClassVar}){
		this.${fkPojoClassVar}s = ${fkPojoClassVar};
	}
	
	public Set<${fkPojoClass}> get${fkPojoClass}s() {
		return ${fkPojoClassVar}s;
	}
	</#list>
</#macro>

<#macro generateJavaManyToOne>
	<#list table.importedKeys.associatedTables?values as foreignKey>
	<#assign fkSqlTable = foreignKey.sqlTable>
	<#assign fkTable    = fkSqlTable.className>
	<#assign fkPojoClass = fkSqlTable.className>
	<#assign fkPojoClassVar = fkPojoClass?uncap_first>
	
	private ${fkPojoClass} ${fkPojoClassVar};
	
	public void set${fkPojoClass}(${fkPojoClass} ${fkPojoClassVar}){
		this.${fkPojoClassVar} = ${fkPojoClassVar};
	}
	
	public ${fkPojoClass} get${fkPojoClass}() {
		return ${fkPojoClassVar};
	}
	</#list>
</#macro>
