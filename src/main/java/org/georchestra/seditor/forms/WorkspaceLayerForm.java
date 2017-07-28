package org.georchestra.seditor.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.georchestra.seditor.bean.WorkspaceLayer;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public class WorkspaceLayerForm {
	private Integer id;
	
	@NotNull
	private Integer workspace;
	
	private Boolean snappable;
	
	@NotEmpty
	@URL
	private String url;
	
	@NotEmpty
	@Pattern(regexp="^[:-_A-Za-z0-9]{0,255}$")
	private String typeName;
	
	private Boolean visible;
	
	public WorkspaceLayerForm() {
		this.visible = true;
	}

	public WorkspaceLayerForm(WorkspaceLayer wsl) {
		this.id = wsl.getId();
		this.workspace = wsl.getWorkspace().getId();
		this.snappable = wsl.getSnappable();
		this.url = wsl.getUrl();
		this.typeName = wsl.getTypeName();
		this.visible = wsl.getVisible();
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

	public Boolean getSnappable() {
		return snappable;
	}

	public void setSnappable(Boolean snappable) {
		this.snappable = snappable;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
}
