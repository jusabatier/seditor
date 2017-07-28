package org.georchestra.seditor.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="workspaces_permissions", uniqueConstraints = {
	@UniqueConstraint(columnNames = "id")
})
public class WorkspacePermission {
	
	@Id
	@SequenceGenerator(name="workspaces_permissions_id_seq", sequenceName="workspaces_permissions_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspaces_permissions_id_seq")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="workspace", referencedColumnName="id")
	private Workspace workspace;
	
	@Column(name="role")
	private String role;
	
	@Column(name="access")
	private Integer access;
	
	public WorkspacePermission() {}
	
	public WorkspacePermission(Integer id, Workspace workspace, String role, Integer access) {
		this.id = id;
		this.workspace = workspace;
		this.role = role;
		this.access = access;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Workspace getWorkspace() {
		return workspace;
	}

	public void setWorkspace(Workspace workspace) {
		this.workspace = workspace;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getAccess() {
		return access;
	}

	public void setAccess(Integer access) {
		this.access = access;
	}
}
