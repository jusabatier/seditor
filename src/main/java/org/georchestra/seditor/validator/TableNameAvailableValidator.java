package org.georchestra.seditor.validator;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.georchestra.seditor.bean.Workspace;
import org.georchestra.seditor.configuration.SEditorPlaceHolder;
import org.georchestra.seditor.forms.WorkspaceForm;
import org.georchestra.seditor.services.IServiceWorkspaces;
import org.springframework.beans.factory.annotation.Autowired;

public class TableNameAvailableValidator implements ConstraintValidator<TableNameAvailable, WorkspaceForm> {
	
	@Resource(name="dataSource")
	protected DataSource dataSource;
	
	@Autowired
	protected IServiceWorkspaces service;
	
	public void initialize(TableNameAvailable constraintAnnotation) {}

	public boolean isValid(WorkspaceForm w, ConstraintValidatorContext context) {
		
		if( w.getTableName() == null || w.getTableName().isEmpty() ) return false;
		
		if( w.getId() != null ) {
			Workspace current = service.getWorkspaceById(w.getId());
			
			if( current.getTableName().equals(w.getTableName()) ) {
				return true;
			}
		}
		try {
			DatabaseMetaData dbmd = dataSource.getConnection().getMetaData();
			ResultSet rs = dbmd.getTables(null, SEditorPlaceHolder.getProperty("schema.name"), w.getTableName(), null);
			if(!rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		context.disableDefaultConstraintViolation();
		context.buildConstraintViolationWithTemplate("{org.georchestra.seditor.validator.TableNameAvailable}").addPropertyNode("tableName").addConstraintViolation();
		return false;
	}
	
}
