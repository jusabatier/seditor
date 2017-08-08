package org.georchestra.seditor.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.validator.AttributeNameAvailable;
import org.hibernate.validator.constraints.NotEmpty;

@AttributeNameAvailable
public class WorkspaceAttributeForm {
	private Integer id;
	
	@NotNull
	private Integer workspace;
	
	@NotEmpty
	@Pattern(regexp="^[-_A-Za-z0-9]{1,255}$")
	private String name;
	
	@NotEmpty
	@Pattern(regexp="text|textarea|radio|checkbox|select|multi-select|radio|date")
	private String type;
	
	private String values;
	
	private String label;
	
	private Boolean required;
	
	public WorkspaceAttributeForm() {}

	public WorkspaceAttributeForm(WorkspaceAttribute wsa) {
		this.id = wsa.getId();
		this.label = wsa.getLabel();
		this.name = wsa.getName();
		this.type = wsa.getType();
		this.values = wsa.getValues();
		this.workspace = wsa.getWorkspace().getId();
		this.required = wsa.getRequired();
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
}
