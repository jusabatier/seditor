package org.georchestra.seditor.validator;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.bean.WorkspaceAttribute;
import org.georchestra.seditor.configuration.SEditorPlaceHolder;
import org.georchestra.seditor.forms.WorkspaceAttributeForm;
import org.georchestra.seditor.services.IServiceWorkspaces;
import org.springframework.beans.factory.annotation.Autowired;

public class AttributeNameAvailableValidator implements ConstraintValidator<AttributeNameAvailable, WorkspaceAttributeForm> {
	
	@Resource(name="dataSource")
	protected DataSource dataSource;
	
	@Autowired
	protected IServiceWorkspaces service;
	
	public void initialize(AttributeNameAvailable constraintAnnotation) {}

	public boolean isValid(WorkspaceAttributeForm was, ConstraintValidatorContext context) {
		Workspace ws = service.getWorkspaceById(was.getWorkspace()); 
		
		if( was.getId() != null ) {
			WorkspaceAttribute current = service.getAttributeById(was.getId());
			
			if( current.getName().equals(was.getName()) ) {
				return true;
			}
		}
		
		try {
			DatabaseMetaData dbmd = dataSource.getConnection().getMetaData();
			ResultSet rs = dbmd.getColumns(null, SEditorPlaceHolder.getProperty("schema.name"), ws.getTableName(), was.getName());
			if(!rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("{org.georchestra.seditor.validator.AttributeNameAvailable}").addPropertyNode("name").addConstraintViolation();
		
		return false;
	}

}
