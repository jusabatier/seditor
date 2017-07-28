package org.georchestra.seditor.dao;

import java.util.List;

import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.json.JSONObject;

public interface IWorkspacesDAO {
	List<Workspace> getWorkspacesList();
	Workspace getWorkspaceById(Workspace pWorkspace);
	Workspace createWorkspace(Workspace pWorkspace);
	void deleteWorkspace(Workspace pWorkspace);
	Workspace updateWorkspace(Workspace update);
	WorkspaceAttribute getAttributeById(WorkspaceAttribute wsa);
	WorkspaceAttribute createWorkspaceAttribute(WorkspaceAttribute wsa);
	void deleteWorkspaceAttribute(WorkspaceAttribute wsa);
	WorkspaceAttribute updateWorkspaceAttribute(WorkspaceAttribute wsa);
	WorkspaceLayer getLayerById(WorkspaceLayer wsl);
	WorkspaceLayer createWorkspaceLayer(WorkspaceLayer wsl);
	void deleteWorkspaceLayer(WorkspaceLayer wsl);
	WorkspaceLayer updateWorkspaceLayer(WorkspaceLayer wsl);
	WorkspacePermission createWorkspacePermission(WorkspacePermission wsp);
	WorkspacePermission getPermissionById(WorkspacePermission wsp);
	void deleteWorkspacePermission(WorkspacePermission wsp);
	WorkspacePermission updateWorkspacePermission(WorkspacePermission wsp);
	Workspace getWorkspaceByKey(Workspace lWorkspace);
	void positionUpWorkspaceLayer(WorkspaceLayer wsl);
	void positionDownWorkspaceLayer(WorkspaceLayer wsl);
	void positionUpWorkspaceAttribute(WorkspaceAttribute wsa);
	void positionDownWorkspaceAttribute(WorkspaceAttribute wsa);
	
	String getFeatureAuthorByIdAndWorkspace(int fid, Workspace ws);
	// Create
	JSONObject addFeatureToWorkspace(Workspace workspace, JSONObject properties, JSONObject geometry, String author);
	// Research
	List<String> getJSONFeaturesByWorkspaceKey(Workspace ws);
	//Update
	JSONObject updateWorkspaceFeature(Workspace ws, JSONObject properties, JSONObject geometry, String author);
	//Delete
	JSONObject deleteWorkspaceFeature(Workspace fullWorkspaceByKey, int fid);
}