<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../header.jsp" %>

<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<form:form method="post" modelAttribute="createworkspace" class="form-horizontal">
			<fieldset>
				<legend><spring:message code="workspaces.add.title"/></legend>
				<form:errors path="featureType" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="featureType" class="col-md-4 control-label"><spring:message code="workspaces.fields.featureType"/></form:label>
					<div class="col-md-4"> 
						<label class="radio-inline" for="featuretype-point">
							<form:radiobutton path="featureType" value="POINT"/> <spring:message code="workspaces.fields.featureType.point"/>
						</label>
						<label class="radio-inline" for="featuretype-line">
							<form:radiobutton path="featureType" value="LINESTRING"/> <spring:message code="workspaces.fields.featureType.line"/>
						</label>
						<label class="radio-inline" for="featuretype-polygon">
							<form:radiobutton path="featureType" value="POLYGON"/> <spring:message code="workspaces.fields.featureType.polygon"/>
						</label>
					</div>
				</div>
				<form:errors path="key" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="key" class="col-md-4 control-label"><spring:message code="workspaces.fields.key"/></form:label>
					<div class="col-md-4">
						<form:input path="key" class="form-control input-md"/>
						<span class="help-block"><spring:message code="workspaces.fields.key.help"/></span>  
					</div>
				</div>
				<form:errors path="tableName" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="tableName" class="col-md-4 control-label"><spring:message code="workspaces.fields.tableName"/></form:label>  
					<div class="col-md-4">
						<form:input path="tableName" class="form-control input-md"/>
						<span class="help-block"><spring:message code="workspaces.fields.tableName.help"/></span>  
					</div>
				</div>
			</fieldset>
			<div class="form-group">
				<label class="col-md-4 control-label" for="sendform"></label>
				<div class="col-md-4">
					<button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.send"/></button>
				</div>
			</div>
		</form:form>
	</div>
</div>

<%@ include file="../footer.jsp" %>