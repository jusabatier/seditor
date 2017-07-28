package org.georchestra.seditor.forms;

import javax.validation.constraints.Pattern;

import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.validator.TableNameAvailable;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

@TableNameAvailable
public class WorkspaceForm {
	
	private Integer id;
	
	@NotEmpty
	@Pattern(regexp="^[-_A-Za-z0-9]{1,255}$")
	private String key;
	
	@URL
	private String wfsUrl;
	
	@Pattern(regexp="^[-:_A-Za-z0-9]{0,255}$")
	private String wfsTypeName;
	
	@NotEmpty
	@Pattern(regexp="^[-_A-Za-z0-9]{1,63}$")
	private String tableName;
	
	@NotEmpty
	@Pattern(regexp="POINT|LINESTRING|POLYGON")
	private String featureType;
	
	public WorkspaceForm() {}
	
	public WorkspaceForm(Workspace ws) {
		this.featureType = ws.getFeatureType();
		this.key = ws.getKey();
		this.tableName = ws.getTableName();
		this.wfsUrl = ws.getWfsUrl();
		this.wfsTypeName = ws.getWfsTypeName();
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
}
