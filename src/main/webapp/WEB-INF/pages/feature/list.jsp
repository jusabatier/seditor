<%@ page language="java" contentType="application/json; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
	"statut": "success",
	"type": "FeatureCollection",
	"crs": {
		"type": "name",
		"properties": {
			"name": "EPSG:${SRS}"
		}
	},
	"features": [
		<c:forEach items="${lsFeatures}" var="feature" varStatus="status">
		${feature}<c:if test="${not status.last}">,</c:if>
		</c:forEach>
	]
}