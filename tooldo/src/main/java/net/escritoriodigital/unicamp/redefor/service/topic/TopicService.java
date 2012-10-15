/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.topic;

import java.util.Collection;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Servi�o
 */
public interface TopicService extends IGeneralService<Topic> {

	/**
	 * Busca os t�picos pelo Theme
	 * @param themeId
	 * @return Collection<Topic>
	 * @throws ServiceException
	 */
	public abstract Collection<Topic> getTopicByTheme(Long themeId) throws ServiceException;
	
	/**
	 * Retorna o maior position dos t�picos de um theme
	 * @param themeId
	 * @return Integer
	 * @throws ServiceException
	 */
	public abstract Integer getLastPostionByByTheme(Long themeId) throws ServiceException;
}
