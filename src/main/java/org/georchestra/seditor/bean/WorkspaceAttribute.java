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
@Table(name="workspaces_attributes", uniqueConstraints = {
	@UniqueConstraint(columnNames = "id")
})
public class WorkspaceAttribute {
	
	@Id
	@SequenceGenerator(name="workspaces_attributes_id_seq", sequenceName="workspaces_attributes_id_seq")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="workspaces_attributes_id_seq")
	@Column(name="id", unique=true, nullable=false)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="workspace", referencedColumnName="id")
	private Workspace workspace;
	
	@Column(name="name", nullable=false, length=255)
	private String name;
	
	@Column(name="type", nullable=false, length=255)
	private String type;
	
	@Column(name="values")
	private String values;
	
	@Column(name="label")
	private String label;
	
	@Column(name="required")
	private Boolean required;
	
	@Column(name="position")
	private Integer position;
	
	public WorkspaceAttribute() {};

	public WorkspaceAttribute(Integer id, Workspace workspace, String name, String type, String values, String label, Boolean required, Integer position) {
		this.id = id;
		this.workspace = workspace;
		this.name = name;
		this.type = type;
		this.values = values;
		this.label = label;
		this.required = required;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getRequired() {
		return required;
	}

	public void setRequired(Boolean required) {
		this.required = required;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}
}
