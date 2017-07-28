package org.georchestra.seditor.services;

import java.util.List;

import org.georchestra.seditor.bean.Role;
import org.georchestra.seditor.dao.IRolesDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRoles implements IServiceRoles {
	
	@Autowired
	private IRolesDAO dao;
	
	public List<Role> getAllRolesNames() {
		return dao.getAllRolesNames();
	}

}
