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
@Table(name="workspaces_layers", uniqueConstraints = {
	@UniqueConstraint(columnNames = "id")
})
public class WorkspaceLayer {
	
	@Id
	@SequenceGenerator(name="workspaces_layers_id_seq", sequenceName="workspaces_layers_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspaces_layers_id_seq")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="workspace", referencedColumnName="id")
	private Workspace workspace;
	
	@Column(name="snappable")
	private Boolean snappable;
	
	@Column(name="url")
	private String url;
	
	@Column(name="typename")
	private String typeName;
	
	@Column(name="visible")
	private Boolean visible;
	
	@Column(name="position")
	private Integer position;
	
	public WorkspaceLayer() {}
	
	public WorkspaceLayer(Integer id, Workspace workspace, Boolean snappable, String url, String typename, Boolean visible, Integer position) {
		this.id = id;
		this.workspace = workspace;
		this.snappable = snappable;
		this.url = url;
		this.typeName = typename;
		this.visible = visible;
		this.position = position;
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

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
