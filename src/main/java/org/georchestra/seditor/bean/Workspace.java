package org.georchestra.seditor.bean;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="workspaces", uniqueConstraints = { 
	@UniqueConstraint(columnNames = "id"),
	@UniqueConstraint(columnNames = "key")
})
public class Workspace {
  
	@Id
	@SequenceGenerator(name="workspaces_id_seq", sequenceName="workspaces_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspaces_id_seq")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@Column(name="key", unique=true, nullable=false, length=255)
	private String key;
	
	@Column(name="wfsurl")
	private String wfsUrl;
	
	@Column(name="wfstypename", length=255)
	private String wfsTypeName;
	
	@Column(name="tablename")
	private String tableName;
	  
	@Column(name="featuretype", length=255)
	private String featureType;
	
	@OneToMany(mappedBy="workspace")
	private Set<WorkspacePermission> permissions;
	
	@OneToMany(mappedBy="workspace")
	private Set<WorkspaceLayer> layers;
	
	@OneToMany(mappedBy="workspace")
	private Set<WorkspaceAttribute> attributes;
	
	public Workspace() {};
	
	public Workspace(Integer id, String key, String wfsUrl, String wfsTypeName, String tableName, String featureType) {
		this.id = id;
		this.key = key;
		this.wfsUrl = wfsUrl;
		this.wfsTypeName = wfsTypeName;
		this.tableName = tableName;
		this.featureType = featureType;
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getWfsUrl() {
		return wfsUrl;
	}
	
	public void setWfsUrl(String wfsUrl) {
		this.wfsUrl = wfsUrl;
	}
	
	public String getWfsTypeName() {
		return wfsTypeName;
	}

	public void setWfsTypeName(String wfsTypeName) {
		this.wfsTypeName = wfsTypeName;
	}

	public String getTableName() {
		return tableName;
	}
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getFeatureType() {
		return featureType;
	}
	
	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}
	
	public Set<WorkspacePermission> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(Set<WorkspacePermission> permissions) {
		this.permissions = permissions;
	}

	public Set<WorkspaceLayer> getLayers() {
		return layers;
	}

	public void setLayers(Set<WorkspaceLayer> layers) {
		this.layers = layers;
	}
	
	public Set<WorkspaceAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Set<WorkspaceAttribute> attributes) {
		this.attributes = attributes;
	}
}
