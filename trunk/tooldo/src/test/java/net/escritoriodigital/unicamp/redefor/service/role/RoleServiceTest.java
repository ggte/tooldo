package net.escritoriodigital.unicamp.redefor.service.role;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;
import net.escritoriodigital.unicamp.redefor.service.AbstractServiceTests;
import net.escritoriodigital.unicamp.redefor.service.secure.RoleService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RoleServiceTest extends AbstractServiceTests
{

	@Autowired
	private RoleService service;

	@Test
	public void testPersistRole() {

		Role role = new Role();
		Role role2 = new Role();

		try {
			role.setName("ROLE_ADMINISTRATOR");
			role.setDescription("Administrador");
			service.save(role);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
