package org.georchestra.seditor.forms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.georchestra.seditor.bean.WorkspacePermission;
import org.hibernate.validator.constraints.NotEmpty;

public class WorkspacePermissionForm {
	private Integer id;
	
	@NotNull
	private Integer workspace;
	
	@NotEmpty
	private Set<String> roles;
	
	@NotNull
	@Min(1)
	@Max(3)
	private Integer access;
	
	public WorkspacePermissionForm() {}
	
	public WorkspacePermissionForm(WorkspacePermission wsp) {
		this.id = wsp.getId();
		this.workspace = wsp.getWorkspace().getId();
		this.roles = new HashSet<String>(Arrays.asList(wsp.getRole().split(",")));
		this.access = wsp.getAccess();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Integer workspace) {
		this.workspace = workspace;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}
}
