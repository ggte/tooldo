/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.secure;

import java.util.Collection;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.secure.User;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Serviço
 */
public interface UserService extends IGeneralService<User> {

	public abstract User getUserByEmail(String email) throws ServiceException;
	
	public abstract User getUserByUsername(String username) throws ServiceException;
	
	public abstract Collection<User> getUserByProfile(Long profile) throws ServiceException;
	
	public abstract void resetPassword(String password, String hash) throws ServiceException;
	
	public abstract Collection<User> getUserByProfileAndDiscipline(Long profileId, Discipline discipline) throws ServiceException;

}
