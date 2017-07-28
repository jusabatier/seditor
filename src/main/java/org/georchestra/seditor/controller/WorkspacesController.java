package org.georchestra.seditor.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.georchestra.seditor.bean.Role;
import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.bean.WorkspaceLayer;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.georchestra.seditor.forms.WorkspaceAttributeForm;
import org.georchestra.seditor.forms.WorkspaceForm;
import org.georchestra.seditor.forms.WorkspaceLayerForm;
import org.georchestra.seditor.forms.WorkspacePermissionForm;
import org.georchestra.seditor.permissions.PermissionsHandler;
import org.georchestra.seditor.services.IServiceRoles;
import org.georchestra.seditor.services.IServiceWorkspaces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WorkspacesController {
	
	final static Logger logger = Logger.getLogger(WorkspacesController.class);
	
	@Autowired
	private IServiceWorkspaces service;
	
	@Autowired
	private IServiceRoles rolesService;
	
	@Resource(name="dataSource")
	protected DataSource dataSource;
	
	@RequestMapping(value = "/workspaces", method = RequestMethod.GET)
	public String listWorkspaces(final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles ) {
		
		PermissionsHandler.setRoles(roles);
		
		final List<Workspace> lListeWorkspaces = service.getWorkspacesList();
		pModel.addAttribute("listeWorkspaces",lListeWorkspaces);
		pModel.addAttribute("roles",roles);
		
		/** TODO
		 * Filtrer les workspaces retournés par rapport aux permissions du rôle.
		 * => A verifier avec des permissions renseignées.
		 */
		
		logger.info("sec-roles : "+roles);
		
		return "workspaces/list";
	}
	
	@RequestMapping(value = "/workspaces/add", method = RequestMethod.GET)
	public String createWorkspaceForm(final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( pModel.get("createworkspace") == null ) {
			pModel.addAttribute("createworkspace", new WorkspaceForm());
		}
		return "workspaces/add";
	}
	
	@RequestMapping(value = "/workspaces/edit", method = RequestMethod.GET)
	public String editWorkspaceForm(@RequestParam(value="id") final Integer pIdWorkspace, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
				
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		Workspace ws = service.getFullWorkspaceById(pIdWorkspace);
		pModel.addAttribute("id", ws.getId());
		pModel.addAttribute("key",ws.getKey());
		
		if( pModel.get("editworkspace") == null ) {
			WorkspaceForm wsForm = new WorkspaceForm(ws);
			pModel.addAttribute("editworkspace", wsForm);
		}
		
		if( pModel.get("createworkspaceattribute") == null ) {
			pModel.addAttribute("createworkspaceattribute", new WorkspaceAttributeForm());
		}
		
		if( pModel.get("createworkspacelayer") == null ) {
			pModel.addAttribute("createworkspacelayer", new WorkspaceLayerForm());
		}
		
		if( pModel.get("createworkspacepermission") == null ) {
			pModel.addAttribute("createworkspacepermission", new WorkspacePermissionForm());
		}
		
		List<WorkspaceAttribute> lsAttributes = new ArrayList<WorkspaceAttribute>(ws.getAttributes());
		Collections.sort(lsAttributes, new Comparator<WorkspaceAttribute>() {
			public int compare(final WorkspaceAttribute wsa1, final WorkspaceAttribute wsa2) {
				return wsa1.getPosition().compareTo(wsa2.getPosition());
			}
		});
		pModel.addAttribute("listAttributes", lsAttributes);
		
		List<WorkspaceLayer> lsLayers = new ArrayList<WorkspaceLayer>(ws.getLayers());
		Collections.sort(lsLayers, new Comparator<WorkspaceLayer>() {
			public int compare(final WorkspaceLayer wsl1, final WorkspaceLayer wsl2) {
				return wsl1.getPosition().compareTo(wsl2.getPosition());
			}
		});
		pModel.addAttribute("listLayers", lsLayers);
		
		pModel.addAttribute("listPermissions", ws.getPermissions());
		
		List<Role> lsRoles = rolesService.getAllRolesNames();
		Collections.sort(lsRoles, new Comparator<Role>() {
			public int compare(final Role object1, final Role object2) {
				return object1.getName().compareTo(object2.getName());
			}
		});
		pModel.addAttribute("listRoles", lsRoles);
		
		return "workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/attribute/edit", method = RequestMethod.GET)
	public String editWorkspaceAttributeForm(@RequestParam(value="id") final Integer pIdWorkspaceAttribute, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
				
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceAttribute wsa = service.getAttributeById(pIdWorkspaceAttribute);
		pModel.addAttribute("id", pIdWorkspaceAttribute);
		pModel.addAttribute("workspace", wsa.getWorkspace().getId());
		pModel.addAttribute("name",wsa.getName());
		
		if( pModel.get("editworkspaceattribute") == null ) {
			WorkspaceAttributeForm wsaForm = new WorkspaceAttributeForm(wsa);
			pModel.addAttribute("editworkspaceattribute", wsaForm);
		}
		
		return "/workspaces/attribute/edit";
	}
	
	@RequestMapping(value = "/workspaces/layer/edit", method = RequestMethod.GET)
	public String editWorkspaceLayerForm(@RequestParam(value="id") final Integer pIdWorkspaceLayer, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
				
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceLayer wsl = service.getLayerById(pIdWorkspaceLayer);
		pModel.addAttribute("id", pIdWorkspaceLayer);
		pModel.addAttribute("workspace", wsl.getWorkspace().getId());
		pModel.addAttribute("typeName", wsl.getTypeName());
		
		if( pModel.get("editworkspacelayer") == null ) {
			WorkspaceLayerForm wslForm = new WorkspaceLayerForm(wsl);
			pModel.addAttribute("editworkspacelayer", wslForm);
		}
		
		return "/workspaces/layer/edit";
	}
	
	@RequestMapping(value = "/workspaces/permission/edit", method = RequestMethod.GET)
	public String editWorkspacePermissionForm(@RequestParam(value="id") final Integer pIdWorkspacePermission, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
				
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspacePermission wsp = service.getPermissionById(pIdWorkspacePermission);
		pModel.addAttribute("id", pIdWorkspacePermission);
		pModel.addAttribute("workspace", wsp.getWorkspace().getId());
		List<Role> lsRoles = rolesService.getAllRolesNames();
		Collections.sort(lsRoles, new Comparator<Role>() {
			public int compare(final Role object1, final Role object2) {
				return object1.getName().compareTo(object2.getName());
			}
		});
		pModel.addAttribute("listRoles", lsRoles);
		
		if( pModel.get("editworkspacepermission") == null ) {
			WorkspacePermissionForm wspForm = new WorkspacePermissionForm(wsp);
			pModel.addAttribute("editworkspacepermission", wspForm);
		}
		
		return "/workspaces/permission/edit";
	}
	
	@RequestMapping(value = "/workspaces/add", method = RequestMethod.POST)
	public String createWorkspaceFormHandle(@Valid @ModelAttribute(value="createworkspace") final WorkspaceForm pWorkspaceForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			Workspace created = service.createWorkspace(pWorkspaceForm.getKey(), pWorkspaceForm.getWfsUrl(), pWorkspaceForm.getWfsTypeName(), pWorkspaceForm.getTableName(), pWorkspaceForm.getFeatureType());
			
			pModel.addAttribute("id", created.getId());
			
			return "redirect:/workspaces/edit";
		}
		
		return createWorkspaceForm(pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/attribute/add", method = RequestMethod.POST)
	public String createWorkspaceAttributeFormHandle(@Valid @ModelAttribute(value="createworkspaceattribute") final WorkspaceAttributeForm pWorkspaceAttributeForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			service.addAttributeToWorkspace(pWorkspaceAttributeForm.getWorkspace(), pWorkspaceAttributeForm.getName(), pWorkspaceAttributeForm.getType(), pWorkspaceAttributeForm.getValues(), pWorkspaceAttributeForm.getLabel(), pWorkspaceAttributeForm.getRequired());
			
			pModel.addAttribute("id", pWorkspaceAttributeForm.getWorkspace());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceForm(pWorkspaceAttributeForm.getWorkspace(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/layer/add", method = RequestMethod.POST)
	public String createWorkspaceLayerFormHandle(@Valid @ModelAttribute(value="createworkspacelayer") final WorkspaceLayerForm pWorkspaceLayerForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			service.addLayerToWorkspace(pWorkspaceLayerForm.getWorkspace(),pWorkspaceLayerForm.getSnappable(),pWorkspaceLayerForm.getUrl(),pWorkspaceLayerForm.getTypeName(), pWorkspaceLayerForm.getVisible());
			
			pModel.addAttribute("id", pWorkspaceLayerForm.getWorkspace());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceForm(pWorkspaceLayerForm.getWorkspace(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/permission/add", method = RequestMethod.POST)
	public String createWorkspacePermissionFormHandle(@Valid @ModelAttribute(value="createworkspacepermission") final WorkspacePermissionForm pWorkspacePermissionForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			service.createWorkspacePermission(pWorkspacePermissionForm.getWorkspace(),pWorkspacePermissionForm.getRoles(),pWorkspacePermissionForm.getAccess());
			
			pModel.addAttribute("id", pWorkspacePermissionForm.getWorkspace());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceForm(pWorkspacePermissionForm.getWorkspace(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/edit", method = RequestMethod.POST)
	public String editWorkspaceFormHandle(@Valid @ModelAttribute(value="editworkspace") final WorkspaceForm pWorkspaceForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			Workspace updated = service.updateWorkspace(pWorkspaceForm.getId(), pWorkspaceForm.getKey(), pWorkspaceForm.getWfsUrl(), pWorkspaceForm.getWfsTypeName(), pWorkspaceForm.getTableName(), pWorkspaceForm.getFeatureType());
			
			pModel.addAttribute("id", updated.getId());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceForm(pWorkspaceForm.getId(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/attribute/edit", method = RequestMethod.POST)
	public String editWorkspaceAttributeFormHandle(@Valid @ModelAttribute(value="editworkspaceattribute") final WorkspaceAttributeForm pWorkspaceAttributeForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			WorkspaceAttribute updated = service.updateWorkspaceAttribute(pWorkspaceAttributeForm.getId(),pWorkspaceAttributeForm.getWorkspace() ,pWorkspaceAttributeForm.getName(),pWorkspaceAttributeForm.getType(),pWorkspaceAttributeForm.getValues(),pWorkspaceAttributeForm.getLabel(),pWorkspaceAttributeForm.getRequired());
			
			pModel.addAttribute("id", updated.getWorkspace().getId());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceAttributeForm(pWorkspaceAttributeForm.getId(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/layer/edit", method = RequestMethod.POST)
	public String editWorkspaceLayerFormHandle(@Valid @ModelAttribute(value="editworkspacelayer") final WorkspaceLayerForm pWorkspaceLayerForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			WorkspaceLayer updated = service.updateWorkspaceLayer(pWorkspaceLayerForm.getId(), pWorkspaceLayerForm.getWorkspace(), pWorkspaceLayerForm.getSnappable(), pWorkspaceLayerForm.getUrl(), pWorkspaceLayerForm.getTypeName(), pWorkspaceLayerForm.getVisible());
			
			pModel.addAttribute("id", updated.getWorkspace().getId());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspaceLayerForm(pWorkspaceLayerForm.getId(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/permission/edit", method = RequestMethod.POST)
	public String editWorkspacePermissionFormHandle(@Valid @ModelAttribute(value="editworkspacepermission") final WorkspacePermissionForm pWorkspacePermissionForm,
			final BindingResult pBindingResult, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		if( !pBindingResult.hasErrors() ) {
			WorkspacePermission updated = service.updateWorkspacePermission(pWorkspacePermissionForm.getId(),pWorkspacePermissionForm.getWorkspace(),pWorkspacePermissionForm.getRoles(),pWorkspacePermissionForm.getAccess());
			
			pModel.addAttribute("id", updated.getWorkspace().getId());
			
			return "redirect:/workspaces/edit";
		}
		
		return editWorkspacePermissionForm(pWorkspacePermissionForm.getId(), pModel, roles);
	}
	
	@RequestMapping(value = "/workspaces/delete", method = RequestMethod.GET)
	public String deleteWorkspace(@RequestParam(value="id") final Integer pIdWorkspace, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		service.deleteWorkspace(pIdWorkspace);
		
		return "redirect:/workspaces";
	}
	
	@RequestMapping(value = "/workspaces/attribute/up", method = RequestMethod.GET)
	public String positionUpWorkspaceAttribute(@RequestParam(value="id") final Integer pIdWorkspaceAttribute, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceAttribute wsa = service.getAttributeById(pIdWorkspaceAttribute);
		pModel.addAttribute("id", wsa.getWorkspace().getId());
		
		service.positionUpWorkspaceAttribute(pIdWorkspaceAttribute);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/attribute/down", method = RequestMethod.GET)
	public String positionDownWorkspaceAttribute(@RequestParam(value="id") final Integer pIdWorkspaceAttribute, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceAttribute wsa = service.getAttributeById(pIdWorkspaceAttribute);
		pModel.addAttribute("id", wsa.getWorkspace().getId());
		
		service.positionDownWorkspaceAttribute(pIdWorkspaceAttribute);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/attribute/delete", method = RequestMethod.GET)
	public String deleteWorkspaceAttribute(@RequestParam(value="id") final Integer pIdWorkspaceAttribute, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceAttribute wsa = service.getAttributeById(pIdWorkspaceAttribute);
		pModel.addAttribute("id", wsa.getWorkspace().getId());
		
		service.deleteWorkspaceAttribute(pIdWorkspaceAttribute);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/layer/up", method = RequestMethod.GET)
	public String positionUpWorkspaceLayer(@RequestParam(value="id") final Integer pIdWorkspaceLayer, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceLayer wsl = service.getLayerById(pIdWorkspaceLayer);
		pModel.addAttribute("id", wsl.getWorkspace().getId());
		
		service.positionUpWorkspaceLayer(pIdWorkspaceLayer);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/layer/down", method = RequestMethod.GET)
	public String positionDownWorkspaceLayer(@RequestParam(value="id") final Integer pIdWorkspaceLayer, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceLayer wsl = service.getLayerById(pIdWorkspaceLayer);
		pModel.addAttribute("id", wsl.getWorkspace().getId());
		
		service.positionDownWorkspaceLayer(pIdWorkspaceLayer);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/layer/delete", method = RequestMethod.GET)
	public String deleteWorkspaceLayer(@RequestParam(value="id") final Integer pIdWorkspaceLayer, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspaceLayer wsl = service.getLayerById(pIdWorkspaceLayer);
		pModel.addAttribute("id", wsl.getWorkspace().getId());
		
		service.deleteWorkspaceLayer(pIdWorkspaceLayer);
		
		return "redirect:/workspaces/edit";
	}
	
	@RequestMapping(value = "/workspaces/permission/delete", method = RequestMethod.GET)
	public String deleteWorkspacePermission(@RequestParam(value="id") final Integer pIdWorkspacePermission, final ModelMap pModel, @RequestHeader(value="sec-roles", defaultValue="") String roles) {
		
		PermissionsHandler.setRoles(roles);
		if( !PermissionsHandler.getInstance().isAdmin() ) {
			return "restricted";
		}
		
		WorkspacePermission wsp = service.getPermissionById(pIdWorkspacePermission);
		pModel.addAttribute("id", wsp.getWorkspace().getId());
		
		service.deleteWorkspacePermission(pIdWorkspacePermission);
		
		return "redirect:/workspaces/edit";
	}
}
