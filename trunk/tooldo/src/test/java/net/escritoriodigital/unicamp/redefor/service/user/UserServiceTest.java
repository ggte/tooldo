package net.escritoriodigital.unicamp.redefor.service.user;

import java.util.Date;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.utils.secure.PasswordEncoder;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;
import net.escritoriodigital.unicamp.redefor.service.secure.ProfileService;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class UserServiceTest extends AbstractToolDoServiceTests
{

	@Autowired
	private UserService service;
	@Autowired
	private ProfileService profileService;


	@Test
	public void testPersistAdministrador() throws ServiceException {


		User user = new User();
		user.setName("Adminstrador");
		user.setEmail("felipe@escritoriodigital.net");
		user.setPhonenumber("1978104391");
		user.setEnabled(true);
		user.setCreated(new Date());
		user.setUsername("administrador");
		String password = PasswordEncoder.getMd5PasswordEncoder("admin01");
		user.setPassword(password);
		System.out.println("Senha: " + password);

		Profile p1 = profileService.getById(new Long(1));
		user.setProfile(p1);

		try {
			service.save(user);
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}



	@Test
	public void testAuthenticates() throws Exception {
		authenticates(service);
	}


}
