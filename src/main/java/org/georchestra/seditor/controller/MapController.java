package org.georchestra.seditor.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.configuration.SEditorPlaceHolder;
import org.georchestra.seditor.permissions.PermissionsHandler;
import org.georchestra.seditor.services.IServiceWorkspaces;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for frontend map.
 * @author jusabatier
 * @version 1.0
 *
 */
@Controller
public class MapController {
	
	@Autowired
	private IServiceWorkspaces service;
	
	/**
	 * Redirect to /resources/index.html
	 * @return The redirect string
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String handleMapRequest() {
		return "redirect:/resources/index.html";
	}
	
	/**
	 * Handle configuration request
	 * @param pModel The request ModelMap
	 * @param pWorkspaceKey The workspace key for which configuration is requested
	 * @param roles The user roles in request headers (sec-roles)
	 * @param username The user name in request header (sec-username)
	 * @return The JSP template to load
	 */
	@RequestMapping(value = "/api/configuration", method = RequestMethod.GET)
	public String handleConfigurationRequest(final ModelMap pModel, @RequestParam(value="workspace") final String pWorkspaceKey, @RequestHeader(value="sec-roles", defaultValue="") String roles, @RequestHeader(value="sec-username", defaultValue="") String username) {
		PermissionsHandler.setRoles(roles);
		
		pModel.addAttribute("wsKey", pWorkspaceKey);
		pModel.addAttribute("SRS", SEditorPlaceHolder.getProperty("srsCode"));
		pModel.addAttribute("SRSdef", SEditorPlaceHolder.getProperty("srsDef"));
		pModel.addAttribute("CenterX", SEditorPlaceHolder.getProperty("center.x"));
		pModel.addAttribute("CenterY", SEditorPlaceHolder.getProperty("center.y"));
		pModel.addAttribute("CenterZ", SEditorPlaceHolder.getProperty("zoom"));
		pModel.addAttribute("MExtentXmin", SEditorPlaceHolder.getProperty("maxExtent.xmin"));
		pModel.addAttribute("MExtentYmin", SEditorPlaceHolder.getProperty("maxExtent.ymin"));
		pModel.addAttribute("MExtentXmax", SEditorPlaceHolder.getProperty("maxExtent.xmax"));
		pModel.addAttribute("MExtentYmax", SEditorPlaceHolder.getProperty("maxExtent.ymax"));
		
		Workspace ws = service.getFullWorkspaceByKey(pWorkspaceKey);
		
		pModel.addAttribute("featureType", ws.getFeatureType());
		
		List<WorkspaceLayer> lsLayers = new ArrayList<WorkspaceLayer>(ws.getLayers());
		Collections.sort(lsLayers, new Comparator<WorkspaceLayer>() {
			public int compare(final WorkspaceLayer wsl1, final WorkspaceLayer wsl2) {
				return wsl1.getPosition().compareTo(wsl2.getPosition());
			}
		});
		pModel.addAttribute("layers", lsLayers);
		
		List<WorkspaceAttribute> lsAttributes = new ArrayList<WorkspaceAttribute>(ws.getAttributes());
		Collections.sort(lsAttributes, new Comparator<WorkspaceAttribute>() {
			public int compare(final WorkspaceAttribute wsa1, final WorkspaceAttribute wsa2) {
				return wsa1.getPosition().compareTo(wsa2.getPosition());
			}
		});
		pModel.addAttribute("attributes", lsAttributes);
		
		Integer accessLevel = service.getWorkspaceAccessLevel(service.getWorkspaceByKey(pWorkspaceKey).getId());
		pModel.addAttribute("accessLevel", accessLevel);
		
		pModel.addAttribute("username", username);
		
		return "configuration";
	}
	 
	/**
	 * Handle features list request
	 * @param pModel The request ModelMap
	 * @param wsKey The workspace key for which fetures list is requested
	 * @param roles The user roles in request headers (sec-roles)
	 * @return The JSP template to load
	 */
	@RequestMapping(value = "/api/list", method = RequestMethod.GET)
	public String workspaceLayerRequest(final ModelMap pModel, @RequestParam(value="workspace") final String wsKey, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		PermissionsHandler.setRoles(roles);
		
		Integer accessLevel = service.getWorkspaceAccessLevel(service.getWorkspaceByKey(wsKey).getId());
		
		if( accessLevel < 1 && !PermissionsHandler.getInstance().isAdmin() ) return "feature/restricted";
		
		List<String> lsJSONFeatures = service.getJSONFeaturesByWorkspaceKey(wsKey);
		pModel.addAttribute("lsFeatures", lsJSONFeatures);
		pModel.addAttribute("SRS", SEditorPlaceHolder.getProperty("srsCode"));
		
		return "feature/list";
	}
	
	/**
	 * Handle a features list persist request (create, edit, delete)
	 * @param pModel The request ModelMap
	 * @param wsKey The workspace key where persist features
	 * @param data The list of features to persist (GeoJSON)
	 * @param roles The user roles in request headers (sec-roles)
	 * @param username The user name in request header (sec-username)
	 * @return The JSP template to load
	 */
	@RequestMapping(value = "/api/persist", method = RequestMethod.POST)
	public String persistFeatures(final ModelMap pModel, @RequestParam(value="workspace") final String wsKey, @RequestParam(value="data") final String data,
			@RequestHeader(value="sec-roles", defaultValue="") String roles, @RequestHeader(value="sec-username", defaultValue="") String username) {
		PermissionsHandler.setRoles(roles);
		
		JSONObject persistData = new JSONObject(data);
		JSONArray features = persistData.getJSONArray("features");
		JSONArray reponses = new JSONArray();
		for( int i = 0 ; i < features.length() ; i++ ) {
			reponses.put(service.persistFeatureToWorkspace(wsKey, features.getJSONObject(i), username));
		}
		
		pModel.addAttribute("reponses", reponses.toString());
		pModel.addAttribute("statut", "success");
		
		return "feature/persist";
	}
}
