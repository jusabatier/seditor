package org.georchestra.seditor.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.georchestra.seditor.bean.Role;
import org.georchestra.seditor.configuration.SEditorPlaceHolder;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RolesDAO implements IRolesDAO {
	
	@Resource(name="ldapTemplate")
	protected LdapTemplate ldapTemplate;
	
	public List<Role> getAllRolesNames() {
		return ldapTemplate.search("", SEditorPlaceHolder.getProperty("rolesClassFilter"), new AttributesMapper<Role>() {
			public Role mapFromAttributes(Attributes attrs) throws NamingException {
				return new Role(attrs.get("cn").get().toString());
			}
		});
	}
}
