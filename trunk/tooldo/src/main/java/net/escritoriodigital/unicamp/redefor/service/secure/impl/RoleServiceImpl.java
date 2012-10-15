/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.service.secure.impl;

import javax.persistence.Query;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;
import net.escritoriodigital.unicamp.redefor.service.secure.RoleService;

import org.springframework.stereotype.Service;

/**
 * @author andrefabbro
 *
 */
@Service ("roleService")
public class RoleServiceImpl extends GeneralService<Role> implements RoleService{

	private static final long serialVersionUID = -5953804926089073901L;

	@Override
	public Role getByName(String name) throws ServiceException {
		
		Query q = em.createQuery("from Role r where r.name = :name");
		q.setParameter("name", name).setMaxResults(1);
		
		return (Role) q.getSingleResult();
	}
	

}
