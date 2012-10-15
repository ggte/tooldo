/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.page;

import java.util.Collection;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.page.Page;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Serviço
 */
public interface PageService extends IGeneralService<Page> {

	public abstract Collection<Page> getPageByTopic(Long topicId) throws ServiceException;
	
	public abstract Integer getLastPostionByTopic(Long topicId) throws ServiceException;
	
}
