package org.georchestra.seditor.services;

import java.util.List;
import java.util.Set;

import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.json.JSONObject;

public interface IServiceWorkspaces {
	List<Workspace> getWorkspacesList();
	Workspace  getWorkspaceById(Integer id);
	Workspace getFullWorkspaceById(Integer id);
	Workspace createWorkspace(String pKey, String pWfsUrl, String pFeatureType);
	void deleteWorkspace(Integer pIdWorkspace);
	Workspace updateWorkspace(Integer id, String key, String tableName, String featureType);
	WorkspaceAttribute  getAttributeById(Integer id);
	WorkspaceAttribute addAttributeToWorkspace(Integer wsId, String name, String type, String values, String label, Boolean required);
	void deleteWorkspaceAttribute(Integer pIdWorkspaceAttribute);
	WorkspaceAttribute updateWorkspaceAttribute(Integer id, Integer wsId, String name, String type, String values, String label, Boolean required);
	WorkspaceLayer addLayerToWorkspace(Integer wsId, Boolean snappable, String url, String typename, Boolean visible);
	WorkspaceLayer  getLayerById(Integer id);
	void deleteWorkspaceLayer(Integer id);
	WorkspaceLayer updateWorkspaceLayer(Integer id, Integer wsId, Boolean snappable, String url, String typeName, Boolean visible);
	WorkspacePermission createWorkspacePermission(Integer wsId, Set<String> roles, Integer access);
	WorkspacePermission getPermissionById(Integer id);
	void deleteWorkspacePermission(Integer id);
	WorkspacePermission updateWorkspacePermission(Integer id, Integer workspace, Set<String> roles, Integer access);
	Workspace getWorkspaceByKey(String pWorkspaceKey);
	Workspace getFullWorkspaceByKey(String pWorkspaceKey);
	Integer getWorkspaceAccessLevel(Integer wsId);
	void positionUpWorkspaceLayer(Integer pIdWorkspaceLayer);
	void positionDownWorkspaceLayer(Integer pIdWorkspaceLayer);
	void positionUpWorkspaceAttribute(Integer pIdWorkspaceAttribute);
	void positionDownWorkspaceAttribute(Integer pIdWorkspaceAttribute);
	
	List<String> getJSONFeaturesByWorkspaceKey(String wsKey);
	String getFeatureAuthorByIdAndWorkspaceKey(int id, String wsKey);
	JSONObject persistFeatureToWorkspace(String wsKey, JSONObject jsonObject, String author);
}
