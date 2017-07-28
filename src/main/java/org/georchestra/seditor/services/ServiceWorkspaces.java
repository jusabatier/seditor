package org.georchestra.seditor.services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.georchestra.seditor.dao.IWorkspacesDAO;
import org.georchestra.seditor.permissions.PermissionsHandler;
import org.hibernate.Hibernate;
import org.json.JSONObject;

@Service
public class ServiceWorkspaces implements IServiceWorkspaces {
	
	final static Logger logger = Logger.getLogger(ServiceWorkspaces.class);
	
	@Autowired
	private IWorkspacesDAO dao;
	
	@Transactional(readOnly=true)
	public List<Workspace> getWorkspacesList() {
		return dao.getWorkspacesList();
	}
	
	@Transactional(readOnly=true)
	public Workspace  getWorkspaceById(Integer id) {
		Workspace lWorkspace = new Workspace();
		lWorkspace.setId(id);
		
		return dao.getWorkspaceById(lWorkspace);
	}
	
	@Transactional
	public Workspace createWorkspace(String pKey, String pWfsUrl, String pWfsTypeName, String pTableName, String pFeatureType) {
		Workspace lWorkspace = new Workspace(null,pKey,pWfsUrl,pWfsTypeName,pTableName,pFeatureType);
		return dao.createWorkspace(lWorkspace);
	}
	
	@Transactional
	public void deleteWorkspace(Integer pIdWorkspace) {
		Workspace lWorkspace = new Workspace(pIdWorkspace,null,null,null,null,null);
		dao.deleteWorkspace(lWorkspace);
	}
	
	@Transactional
	public Workspace updateWorkspace(Integer id, String key, String wfsUrl, String wfsTypeName, String tableName, String featureType) {
		Workspace update = new Workspace(id,key,wfsUrl,wfsTypeName,tableName,featureType);
		return dao.updateWorkspace(update);
	}
	
	@Transactional(readOnly=true)
	public Workspace getFullWorkspaceById(Integer id) {
		Workspace lWorkspace = new Workspace(id,null,null,null,null,null);
		lWorkspace = dao.getWorkspaceById(lWorkspace);
		
		Hibernate.initialize(lWorkspace.getAttributes());
		Hibernate.initialize(lWorkspace.getLayers());
		Hibernate.initialize(lWorkspace.getPermissions());
		
		return lWorkspace;
	}
	
	@Transactional(readOnly=true)
	public WorkspaceAttribute  getAttributeById(Integer id) {
		WorkspaceAttribute wsa = new WorkspaceAttribute(id,null,null,null,null,null,null,null);
		return dao.getAttributeById(wsa);
	}
	
	@Transactional
	public WorkspaceAttribute addAttributeToWorkspace(Integer wsId, String name, String type, String values, String label, Boolean required) {
		WorkspaceAttribute wsa = new WorkspaceAttribute(null,getWorkspaceById(wsId),name,type,values,label,required,null);
		return dao.createWorkspaceAttribute(wsa);
	}
	
	@Transactional
	public void deleteWorkspaceAttribute(Integer pIdWorkspaceAttribute) {
		WorkspaceAttribute wsa = new WorkspaceAttribute(pIdWorkspaceAttribute,null,null,null,null,null,null,null);
		dao.deleteWorkspaceAttribute(wsa);
	}
	
	@Transactional
	public WorkspaceAttribute updateWorkspaceAttribute(Integer id, Integer wsId, String name, String type, String values, String label, Boolean required) {
		WorkspaceAttribute was = new WorkspaceAttribute(id,getWorkspaceById(wsId),name,type,values,label,required,null);
		return dao.updateWorkspaceAttribute(was);
	}
	
	@Transactional
	public WorkspaceLayer addLayerToWorkspace(Integer wsId, Boolean snappable, String url, String typename, Boolean visible) {
		WorkspaceLayer wsl = new WorkspaceLayer(null,getWorkspaceById(wsId),snappable,url,typename,visible,null);
		return dao.createWorkspaceLayer(wsl);
	}
	
	@Transactional(readOnly=true)
	public WorkspaceLayer getLayerById(Integer id) {
		WorkspaceLayer wsl = new WorkspaceLayer(id,null,null,null,null,null,null);
		return dao.getLayerById(wsl);
	}
	
	@Transactional
	public void deleteWorkspaceLayer(Integer id) {
		WorkspaceLayer wsl = new WorkspaceLayer(id,null,null,null,null,null,null);
		dao.deleteWorkspaceLayer(wsl);
	}
	
	@Transactional
	public WorkspaceLayer updateWorkspaceLayer(Integer id, Integer wsId, Boolean snappable, String url, String typeName, Boolean visible) {
		WorkspaceLayer wsl = new WorkspaceLayer(id,getWorkspaceById(wsId),snappable,url,typeName, visible,null);
		return dao.updateWorkspaceLayer(wsl);
	}
	
	@Transactional
	public WorkspacePermission createWorkspacePermission(Integer wsId, Set<String> roles, Integer access) {
		String rolesString = null;
		
		if( roles.contains("*") ) rolesString = "*";
		if( roles.contains("ALL") ) rolesString = "ALL";
		
		if( rolesString == null ) rolesString = PermissionsHandler.getRolesStringFromSet(roles);
		
		WorkspacePermission wsp = new WorkspacePermission(null,getWorkspaceById(wsId),rolesString,access);
		return dao.createWorkspacePermission(wsp);
	}
	
	@Transactional
	public WorkspacePermission getPermissionById(Integer id) {
		WorkspacePermission wsp = new WorkspacePermission(id,null,null,null);
		return dao.getPermissionById(wsp);
	}
	
	@Transactional
	public void deleteWorkspacePermission(Integer id) {
		WorkspacePermission wsp = new WorkspacePermission(id,null,null,null);
		dao.deleteWorkspacePermission(wsp);
	}
	
	@Transactional
	public WorkspacePermission updateWorkspacePermission(Integer id, Integer workspace, Set<String> roles, Integer access) {
		String rolesString = null;
		
		if( roles.contains("*") ) rolesString = "*";
		if( roles.contains("ALL") ) rolesString = "ALL";
		
		if( rolesString == null ) rolesString = PermissionsHandler.getRolesStringFromSet(roles);
		
		WorkspacePermission wsp = new WorkspacePermission(id,getWorkspaceById(workspace),rolesString,access);
		return dao.updateWorkspacePermission(wsp);
	}
	
	@Transactional
	public Workspace getWorkspaceByKey(String pWorkspaceKey) {
		Workspace lWorkspace = new Workspace(null,pWorkspaceKey,null,null,null,null);
		lWorkspace = dao.getWorkspaceByKey(lWorkspace);
		return lWorkspace;
	}
	
	@Transactional
	public Workspace getFullWorkspaceByKey(String pWorkspaceKey) {
		Workspace lWorkspace = new Workspace(null,pWorkspaceKey,null,null,null,null);
		lWorkspace = dao.getWorkspaceByKey(lWorkspace);
		
		Hibernate.initialize(lWorkspace.getAttributes());
		Hibernate.initialize(lWorkspace.getLayers());
		Hibernate.initialize(lWorkspace.getPermissions());
		
		return lWorkspace;
	}
	
	@Transactional
	public Integer getWorkspaceAccessLevel(Integer wsId) {
		Set<String> roleList = new HashSet<String>();
		if( PermissionsHandler.getInstance().roleList != null ) {
			roleList.addAll(Arrays.asList(PermissionsHandler.getInstance().roleList));
		}
		Workspace ws = getFullWorkspaceById(wsId);
		Integer accessLevel = 0; 
		
		if( PermissionsHandler.getInstance().isAdmin() ) {
			return 3;
		}
		
		for( WorkspacePermission wsp : ws.getPermissions() ) {
			if( wsp.getRole().equals("ALL") || 
					(wsp.getRole().equals("*") && roleList != null && !roleList.isEmpty()) || 
					roleList.contains("ROLE_"+wsp.getRole()) ) {
				accessLevel = Integer.max(accessLevel, wsp.getAccess());
			}
		}
		
		return accessLevel;
	}
	
	@Transactional
	public void positionUpWorkspaceLayer(Integer pIdWorkspaceLayer) {
		WorkspaceLayer wsl = new WorkspaceLayer(pIdWorkspaceLayer,null,null,null,null,null,null);
		dao.positionUpWorkspaceLayer(wsl);
	}
	
	@Transactional
	public void positionDownWorkspaceLayer(Integer pIdWorkspaceLayer) {
		WorkspaceLayer wsl = new WorkspaceLayer(pIdWorkspaceLayer,null,null,null,null,null,null);
		dao.positionDownWorkspaceLayer(wsl);
	}
	
	@Transactional
	public void positionUpWorkspaceAttribute(Integer pIdWorkspaceAttribute) {
		WorkspaceAttribute wsa = new WorkspaceAttribute(pIdWorkspaceAttribute,null,null,null,null,null,null,null);
		dao.positionUpWorkspaceAttribute(wsa);
	}
	
	@Transactional
	public void positionDownWorkspaceAttribute(Integer pIdWorkspaceAttribute) {
		WorkspaceAttribute wsa = new WorkspaceAttribute(pIdWorkspaceAttribute,null,null,null,null,null,null,null);
		dao.positionDownWorkspaceAttribute(wsa);
	}
	
	@Transactional
	public List<String> getJSONFeaturesByWorkspaceKey(String wsKey) {
		return dao.getJSONFeaturesByWorkspaceKey(getFullWorkspaceByKey(wsKey));
	}
	
	@Transactional
	public String getFeatureAuthorByIdAndWorkspaceKey(int id, String wsKey) {
		return dao.getFeatureAuthorByIdAndWorkspace(id, getFullWorkspaceByKey(wsKey));
	}
	
	@Transactional
	public JSONObject persistFeatureToWorkspace(String wsKey, JSONObject feature, String author) {
		String state = feature.getJSONObject("properties").getString("state");
		Workspace ws = getFullWorkspaceByKey(wsKey);
		Integer accessLevel = getWorkspaceAccessLevel(ws.getId());
		JSONObject reponse = new JSONObject();
		reponse.put("element", feature);
		
		switch(state) {
			case "created": 
				reponse.put("action", "create");
				if( PermissionsHandler.getInstance().isAdmin() || accessLevel > 1 ) {
					JSONObject result = dao.addFeatureToWorkspace(ws, feature.getJSONObject("properties"), feature.getJSONObject("geometry"), author);
					reponse.put("result", result);
				} else {
					JSONObject result = new JSONObject();
					result.put("statut", "restricted");
					reponse.put("result", result);
				}
				break;
				
			case "modified":
				reponse.put("action", "modify");
				String updatedFeatureAuthor = getFeatureAuthorByIdAndWorkspaceKey(feature.getJSONObject("properties").getInt("feat_id"), wsKey);
				if( PermissionsHandler.getInstance().isAdmin() || accessLevel > 2 || (accessLevel > 1 && updatedFeatureAuthor.equals(author) && !updatedFeatureAuthor.isEmpty()) ) {
					JSONObject result = dao.updateWorkspaceFeature(ws, feature.getJSONObject("properties"), feature.getJSONObject("geometry"), author);
					reponse.put("result", result);
				} else {
					JSONObject result = new JSONObject();
					result.put("statut", "restricted");
					reponse.put("result", result);
				}
				break;
			
			case "deleted":
				reponse.put("action", "delete");
				String deletedFeatureAuthor = getFeatureAuthorByIdAndWorkspaceKey(feature.getJSONObject("properties").getInt("feat_id"), wsKey);
				if( PermissionsHandler.getInstance().isAdmin() || accessLevel > 2 || (accessLevel > 1 && deletedFeatureAuthor.equals(author) && !deletedFeatureAuthor.isEmpty()) ) {
					JSONObject result = dao.deleteWorkspaceFeature(ws, feature.getJSONObject("properties").getInt("feat_id"));
					reponse.put("result", result);
				} else {
					JSONObject result = new JSONObject();
					result.put("statut", "restricted");
					reponse.put("result", result);
				}
		}
		
		return reponse;
	}
}
