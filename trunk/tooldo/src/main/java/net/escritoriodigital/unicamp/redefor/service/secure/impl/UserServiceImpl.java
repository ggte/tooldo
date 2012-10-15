/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.service.secure.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.components.utils.secure.PasswordEncoder;
import net.escritoriodigital.unicamp.redefor.constant.ApplicationConstants;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;

import org.springframework.stereotype.Service;

/**
 * @author andrefabbro
 *
 */
@Service ("userService")
public class UserServiceImpl extends GeneralService<User> implements UserService {

	private static final long serialVersionUID = 7724439345453824783L;

	@Override
	public User getUserByEmail(String email) throws ServiceException {

		User user = null;

		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("email", email);
		Collection<User> result = findByFields(fields, false);

		Iterator<User> it = result.iterator();

		while(it.hasNext()) {
			user = it.next();
			break;
		}

		return user;
	}

	@Override
	public void resetPassword(String password, String hash) throws ServiceException {

		User entity = null;
		String username = "";

		// Decrypt hash
		try {
			String decrypted = TripleDESUtils.newInstance().decrypt(hash);
			username = decrypted.replace(ApplicationConstants.SALT_VALUE, "");
		} catch (Exception e) {
			new  RuntimeException("Senha não pode ser resetada o hash está inválido", e);
		}

		// Get user by hash
		try {
			entity = getUserByUsername(username);
			if(entity == null) throw new ServiceException("Senha não pode ser resetada o usuário não encontrado");
		} catch (Exception e) {
			throw new ServiceException("Senha não pode ser resetada ocorreu um erro ao recuperar o usuário", e);
		}

		// Enconder
		password = PasswordEncoder.getMd5PasswordEncoder(password);
		entity.setPassword(password);
		
		if(entity.getChangePassword()) entity.setChangePassword(false);

		// Merge
		save(entity);
	}

	@Override
	public User getUserByUsername(String username) throws ServiceException {
		User user = null;
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("username", username);
		Collection<User> result = findByFields(fields, false);

		Iterator<User> it = result.iterator();

		while(it.hasNext()) {
			user = it.next();
			break;
		}

		return user;
	}

	@Override
	public Collection<User> getUserByProfile(Long profile) throws ServiceException {

		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("profile.id", profile);

		return findByFields(fields, false, "name");
	}

	@Override
	public Collection<User> getUserByProfileAndDiscipline(Long profileId, Discipline discipline) throws ServiceException {

		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("profile.id", profileId);

		Map<String, Object> memeberOf = new HashMap<String, Object>();
		memeberOf.put("disciplines", discipline);

		return findByFieldsMemberOf(fields, memeberOf, true);
	}

}
