<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ include file="../header.jsp" %>

<script type="text/javascript">
$(function() {
	$("#infob-attributes").popover({
		html: true,
		content: '<p><b><spring:message code="attributes.field.name"/></b> : <spring:message code="attributes.field.name.help"/><br/><b><spring:message code="attributes.field.values"/></b> : <spring:message code="attributes.field.values.help"/></p>',
		title: '<p><spring:message code="help.title"/></p>'
	});
	
	$("#infob-layers").popover({
		html: true,
		content: '<p><b><spring:message code="layers.field.snappable"/></b> : <spring:message code="layers.field.snappable.help"/><br/><b><spring:message code="layers.field.url"/></b> : <spring:message code="layers.field.url.help"/><br/><b><spring:message code="layers.field.typename"/></b> : <spring:message code="layers.field.typename.help"/></p>',
		title: '<p><spring:message code="help.title"/></p>'
	});
	
	$("#infob-permissions").popover({
		html: true,
		content: '<p><b><spring:message code="permissions.field.roles"/></b> : <spring:message code="permissions.field.roles.help"/><br/><b><spring:message code="permissions.field.access"/></b> : <spring:message code="permissions.field.access.help"/></p>',
		title: '<p><spring:message code="help.title"/></p>'
	});
	
	$("a.infobulle-complexe").mouseover(function() {
		$(this).popover("show");
	});
	
	$("a.infobulle-complexe").mouseout(function() {
		$(this).popover("hide");
	});
});
</script>
<div class="row">
	<div class="col-lg-2">
		
	</div>
	<div class="col-lg-10">
		<form:form method="post" modelAttribute="editworkspace" class="form-horizontal">
			<form:hidden path="id" value="${id}" />
			<form:hidden path="featureType" />
			<fieldset>
				<legend><spring:message code="workspaces.edit.title" arguments="${key}" /></legend>
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
				<form:errors path="wfsUrl" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="wfsUrl" class="col-md-4 control-label"><spring:message code="workspaces.fields.wfsUrl"/></form:label>  
					<div class="col-md-6">
						<form:input path="wfsUrl" class="form-control input-md"/>
						<span class="help-block"><spring:message code="workspaces.fields.wfsUrl.help"/></span>  
					</div>
				</div>
				<form:errors path="wfsTypeName" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<div class="form-group">
					<form:label path="wfsTypeName" class="col-md-4 control-label"><spring:message code="workspaces.fields.wfsTypeName"/></form:label>  
					<div class="col-md-6">
						<form:input path="wfsTypeName" class="form-control input-md"/>
						<span class="help-block"><spring:message code="workspaces.fields.wfsTypeName.help"/></span>  
					</div>
				</div>
			</fieldset>
			<div class="form-group">
				<div class="col-md-4">
					<a href="<c:url value="/workspaces"/>" class="btn btn-danger pull-right">
						<span class="glyphicon glyphicon-chevron-left"></span> <spring:message code="buttons.back"/>
					</a>
				</div>
				<div class="col-md-4">
					<button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.send"/></button>
				</div>
			</div>
		</form:form>
		
		<c:url value="/workspaces/attribute/add" var="addAttributeFormLink"></c:url>
		<form:form method="post" modelAttribute="createworkspaceattribute" action="${addAttributeFormLink}" class="form-horizontal">
			<form:hidden path="workspace" value="${id}" />
			<fieldset>
				<legend>
					<spring:message code="workspaces.edit.attributes.title"/> 
					<a href="#" id="infob-attributes" class="infobulle-complexe" data-placement="bottom" data-toggle="popover" role="button">
						<span class="glyphicon glyphicon-cog"></span>
					</a>
				</legend>
				<form:errors path="name" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<form:errors path="type" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<form:errors path="values" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<table class="table table-striped table-condensed">
					<thead>
						<tr>
							<th><spring:message code="attributes.field.name"/></th>
							<th><spring:message code="attributes.field.type"/></th>
							<th><spring:message code="attributes.field.values"/></th>
							<th><spring:message code="attributes.field.label"/></th>
							<th><spring:message code="attributes.field.required"/></th>
							<th><spring:message code="tablelist.columns.actions"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${listAttributes}" var="attribute" varStatus="status">
							<tr>
								<td><c:out value="${attribute.name}"/></td>
								<td><c:out value="${attribute.type}"/></td>
								<td><c:out value="${attribute.values}"/></td>
								<td><c:out value="${attribute.label}"/></td>
								<td>
									<c:if test="${attribute.required}">oui</c:if>
									<c:if test="${not attribute.required}">non</c:if>
								</td>
								<td>
									<c:if test="${not status.first}">
										<c:url value="/workspaces/attribute/up" var="upAttributeLink">
											<c:param name="id" value="${attribute.id}"/>
										</c:url>
										<a href="${upAttributeLink}" class="btn btn-success btn-xs">
											<span class="glyphicon glyphicon-arrow-up"></span>
										</a>
									</c:if>
									<c:if test="${not status.last}">
										<c:url value="/workspaces/attribute/down" var="downAttributeLink">
											<c:param name="id" value="${attribute.id}"/>
										</c:url>
										<a href="${downAttributeLink}" class="btn btn-success btn-xs">
											<span class="glyphicon glyphicon-arrow-down"></span>
										</a>
									</c:if>
									<c:url value="/workspaces/attribute/edit" var="editAttributeLink">
										<c:param name="id" value="${attribute.id}"/>
									</c:url>
									<a href="${editAttributeLink}" class="btn btn-info btn-xs">
										<span class="glyphicon glyphicon-pencil"></span> <spring:message code="tablelist.buttons.edit"/>
									</a>
									<c:url value="/workspaces/attribute/delete" var="deleteAttributeLink">
										<c:param name="id" value="${attribute.id}"/>
									</c:url>
									<a href="#" onclick='confirmDeleteWorkspace("<spring:message code="attributes.list.confirmdelete" arguments="${attribute.name}"/>","${deleteAttributeLink}");' class="btn btn-danger btn-xs">
										<span class="glyphicon glyphicon-remove"></span> <spring:message code="tablelist.buttons.delete"/>
									</a>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td>
								<form:input path="name" class="form-control input-md" />
							</td>
							<td>
								<form:select path="type" class="form-control">
									<form:option value="text" >Text</form:option>
									<form:option value="textarea" >Textarea</form:option>
									<form:option value="checkbox" >Checkbox</form:option>
									<form:option value="radio" >Radio</form:option>
									<form:option value="select" >Select</form:option>
									<form:option value="multi-select" >Multiselect</form:option>
									<form:option value="date" >Date</form:option>
								</form:select>
							</td>
							<td>
								<form:input path="values" class="form-control input-md" />
							</td>
							<td>
								<form:input path="label" class="form-control input-md" />
							</td>
							<td>
								<form:checkbox path="required" value="true" />
							</td>
							<td>
								<form:button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.add"/></form:button>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form:form>
		
		<c:url value="/workspaces/layer/add" var="addLayerFormLink"></c:url>
		<form:form method="post" modelAttribute="createworkspacelayer" action="${addLayerFormLink}" class="form-horizontal">
			<form:hidden path="workspace" value="${id}" />
			<fieldset>
				<legend>
					<spring:message code="workspaces.edit.layers.title"/> 
					<a href="#" id="infob-layers" class="infobulle-complexe" data-placement="bottom" data-toggle="popover" role="button">
						<span class="glyphicon glyphicon-cog"></span>
					</a>
				</legend>
				<form:errors path="snappable" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<form:errors path="url" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<form:errors path="typeName" cssClass="alert alert-danger" element="div" cssStyle="text-align: center;"/>
				<table class="table table-striped table-condensed">
					<thead>
						<tr>
							<th><spring:message code="layers.field.snappable"/></th>
							<th><spring:message code="layers.field.url"/></th>
							<th><spring:message code="layers.field.typename"/></th>
							<th><spring:message code="layers.field.visible"/></th>
							<th><spring:message code="tablelist.columns.actions"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${listLayers}" var="layer" varStatus="status">
							<tr>
								<td>
									<c:if test="${layer.snappable}">oui</c:if>
									<c:if test="${not layer.snappable}">non</c:if>
								</td>
								<td><c:out value="${layer.url}"/></td>
								<td><c:out value="${layer.typeName}"/></td>
								<td>
									<c:if test="${layer.visible}">oui</c:if>
									<c:if test="${not layer.visible}">non</c:if>
								</td>
								<td>
									<c:if test="${not status.first}">
										<c:url value="/workspaces/layer/up" var="upLayerLink">
											<c:param name="id" value="${layer.id}"/>
										</c:url>
										<a href="${upLayerLink}" class="btn btn-success btn-xs">
											<span class="glyphicon glyphicon-arrow-up"></span>
										</a>
									</c:if>
									<c:if test="${not status.last}">
										<c:url value="/workspaces/layer/down" var="downLayerLink">
											<c:param name="id" value="${layer.id}"/>
										</c:url>
										<a href="${downLayerLink}" class="btn btn-success btn-xs">
											<span class="glyphicon glyphicon-arrow-down"></span>
										</a>
									</c:if>
									<c:url value="/workspaces/layer/edit" var="editLayerLink">
										<c:param name="id" value="${layer.id}"/>
									</c:url>
									<a href="${editLayerLink}" class="btn btn-info btn-xs">
										<span class="glyphicon glyphicon-pencil"></span> <spring:message code="tablelist.buttons.edit"/>
									</a>
									<c:url value="/workspaces/layer/delete" var="deleteLayerLink">
										<c:param name="id" value="${layer.id}"/>
									</c:url>
									<a href="#" onclick='confirmDeleteWorkspace("<spring:message code="layers.list.confirmdelete" arguments="${layer.typeName}"/>","${deleteLayerLink}");' class="btn btn-danger btn-xs">
										<span class="glyphicon glyphicon-remove"></span> <spring:message code="tablelist.buttons.delete"/>
									</a>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td>
								<form:checkbox path="snappable" value="true" />
							</td>
							<td>
								<form:input path="url" class="form-control input-md" />
							</td>
							<td>
								<form:input path="typeName" class="form-control input-md" />
							</td>
							<td>
								<form:checkbox path="visible" value="true" />
							</td>
							<td>
								<form:button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.add"/></form:button>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form:form>
		
		<c:url value="/workspaces/permission/add" var="addPermissionFormLink"></c:url>
		<form:form method="post" modelAttribute="createworkspacepermission" action="${addPermissionFormLink}" class="form-horizontal">
			<form:hidden path="workspace" value="${id}" />
			<fieldset>
				<legend>
					<spring:message code="workspaces.edit.permissions.title"/> 
					<a href="#" id="infob-permissions" class="infobulle-complexe" data-placement="bottom" data-toggle="popover" role="button">
						<span class="glyphicon glyphicon-cog"></span>
					</a>
				</legend>
				<table class="table table-striped table-condensed">
					<thead>
						<tr>
							<th><spring:message code="permissions.field.roles"/></th>
							<th><spring:message code="permissions.field.access"/></th>
							<th><spring:message code="tablelist.columns.actions"/></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${listPermissions}" var="permission">
							<tr>
								<td><c:out value="${permission.role}"/></td>
								<td>
									<c:if test="${permission.access=='1'}">
										<spring:message code="permissions.field.access.1"/>
									</c:if>
									<c:if test="${permission.access=='2'}">
										<spring:message code="permissions.field.access.2"/>
									</c:if>
									<c:if test="${permission.access=='3'}">
										<spring:message code="permissions.field.access.3"/>
									</c:if>
								</td>
								<td>
									<c:url value="/workspaces/permission/edit" var="editPermissionLink">
										<c:param name="id" value="${permission.id}"/>
									</c:url>
									<a href="${editPermissionLink}" class="btn btn-info btn-xs">
										<span class="glyphicon glyphicon-pencil"></span> <spring:message code="tablelist.buttons.edit"/>
									</a>
									<c:url value="/workspaces/permission/delete" var="deletePermissionLink">
										<c:param name="id" value="${permission.id}"/>
									</c:url>
									<a href="#" onclick='confirmDeleteWorkspace("<spring:message code="permissions.list.confirmdelete" />","${deletePermissionLink}");' class="btn btn-danger btn-xs">
										<span class="glyphicon glyphicon-remove"></span> <spring:message code="tablelist.buttons.delete"/>
									</a>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td>
								<form:select multiple="true" path="roles" class="form-control">
									<form:option value="ALL"><spring:message code="permissions.field.roles.anyone"/></form:option>
									<form:option value="*"><spring:message code="permissions.field.roles.anyrole"/></form:option>
									<form:options items="${listRoles}" itemValue="name" itemLabel="name" />
								</form:select>
							</td>
							<td>
								<form:select path="access" class="form-control">
									<form:option value="1"><spring:message code="permissions.field.access.1"/></form:option>
									<form:option value="2"><spring:message code="permissions.field.access.2"/></form:option>
									<form:option value="3"><spring:message code="permissions.field.access.3"/></form:option>
								</form:select>
							</td>
							<td>
								<form:button id="sendform" name="save" class="btn btn-primary"><spring:message code="forms.buttons.add"/></form:button>
							</td>
						</tr>
					</tbody>
				</table>
			</fieldset>
		</form:form>
	</div>
</div>

<%@ include file="../footer.jsp" %>