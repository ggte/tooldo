/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.secure;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Servi�o
 */
public interface RoleService extends IGeneralService<Role> {

	public abstract Role getByName(String name) throws ServiceException;

}
