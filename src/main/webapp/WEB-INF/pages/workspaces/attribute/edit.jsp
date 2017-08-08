<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../../header.jsp" %>

<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<form:form method="post" modelAttribute="editworkspaceattribute" class="form-horizontal">
			<form:hidden path="id" value="${id}" />
			<form:hidden path="workspace" value="${workspace}" />
			<fieldset>
				<legend><spring:message code="attributes.edit.title" arguments="${name}" /></legend>
				<form:errors path="name" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="name" class="col-md-4 control-label"><spring:message code="attributes.field.name"/></form:label>
					<div class="col-md-4">
						<form:input path="name" class="form-control input-md"/>
						<span class="help-block"><spring:message code="attributes.field.name.help"/></span>  
					</div>
				</div>
				<form:errors path="type" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="type" class="col-md-4 control-label"><spring:message code="attributes.field.type"/></form:label>
					<div class="col-md-4">
						<form:select path="type" class="form-control input-md">
							<form:option value="text" >Text</form:option>
							<form:option value="textarea" >Textarea</form:option>
							<form:option value="checkbox" >Checkbox</form:option>
							<form:option value="select" >Select</form:option>
							<form:option value="multi-select" >Multiselect</form:option>
							<form:option value="radio" >Radio</form:option>
							<form:option value="date" >Date</form:option>
						</form:select> 
					</div>
				</div>
				<form:errors path="values" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="values" class="col-md-4 control-label"><spring:message code="attributes.field.values"/></form:label>
					<div class="col-md-4">
						<form:textarea path="values" class="form-control" />
						<span class="help-block"><spring:message code="attributes.field.values.help"/></span>  
					</div>
				</div>
				<form:errors path="label" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="label" class="col-md-4 control-label"><spring:message code="attributes.field.label"/></form:label>
					<div class="col-md-4">
						<form:input path="label" class="form-control input-md"/> 
					</div>
				</div>
				<form:errors path="required" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="required" class="col-md-4 control-label"><spring:message code="attributes.field.required"/></form:label>
					<div class="col-md-4">
						<form:checkbox path="required" value="true" class="form-control" />
					</div>
				</div>
			</fieldset>
			<div class="form-group">
				<div class="col-md-4">
					<c:url value="/workspaces/edit" var="backLink">
						<c:param name="id" value="${workspace}"/>
					</c:url>
					<a href="${backLink}" class="btn btn-danger pull-right">
						<span class="glyphicon glyphicon-chevron-left"></span> <spring:message code="buttons.back"/>
					</a>
				</div>
				<div class="col-md-4">
					<button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.send"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>

<%@ include file="../../footer.jsp" %>