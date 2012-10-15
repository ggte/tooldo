/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.secure;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Serviço
 */
public interface ProfileService extends IGeneralService<Profile> {

	public abstract Profile getProfileByName(String name) throws ServiceException;
	
}
