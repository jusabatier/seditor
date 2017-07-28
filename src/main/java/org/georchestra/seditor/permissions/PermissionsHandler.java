package org.georchestra.seditor.permissions;

import java.util.Arrays;

import org.georchestra.seditor.configuration.SEditorPlaceHolder;

public class PermissionsHandler {
	
	private static PermissionsHandler instance = null;
	
	public static void setRoles(String roles) {
		if( roles != null && !roles.trim().isEmpty() ) {
			getInstance().roleList = roles.split(SEditorPlaceHolder.getProperty("roleSeparator"));
		}
	}
	
	public static PermissionsHandler getInstance() {
		if( instance == null ) instance = new PermissionsHandler();
		return instance;
	}
	
	public String[] roleList = null;
	
	private PermissionsHandler() {}
	
	public boolean isAdmin() {
		
		String[] adminRolesList = null;
		
		String adminRoles = SEditorPlaceHolder.getProperty("admin.roles");
		adminRolesList = adminRoles.split(";");
			
		if( roleList != null && adminRolesList != null && havePermission(roleList, adminRolesList) ) {
			return true;
		}
		
		return false;
	}
	
	private boolean havePermission(String[] roles, String[] allowed) {
		for( int i = 0 ; i < roles.length ; i++ ) {
			if( Arrays.asList(allowed).contains(roles[i]) ) return true;
		}
		
		return false;
	}
}
