package net.escritoriodigital.unicamp.redefor.service.profile;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;
import net.escritoriodigital.unicamp.redefor.service.AbstractServiceTests;
import net.escritoriodigital.unicamp.redefor.service.secure.ProfileService;
import net.escritoriodigital.unicamp.redefor.service.secure.RoleService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class ProfileServiceTest extends AbstractServiceTests
{

	@Autowired
	private ProfileService service;

	@Autowired
	private RoleService roleService;

	@Test
	public void testInsertRole() {

		Profile p1 = new Profile();



		try {

			Role r1 = roleService.getById(new Long(1));

			p1.setName("ADMINISTRATOR");
			p1.addRole(r1);
			p1.setFixed(true);
			service.save(p1);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
