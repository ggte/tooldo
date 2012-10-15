package net.escritoriodigital.unicamp.redefor.service.page.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.service.page.PageService;

import org.springframework.stereotype.Service;

/**
 * @author Leandro Concon
 * @category Service
 * @created 18/06/2010
 * @description Servico Página
 */
@Service ("pageService")
public class PageServiceImpl extends GeneralService<Page> implements PageService {

	private static final long serialVersionUID = 2476287614824779822L;

	@Override
	public Collection<Page> getPageByTopic(Long topicId) throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("topic.id", topicId);		
		
		return findByFields(fields, false);
	}

	@Override
	public Integer getLastPostionByTopic(Long topicId) throws ServiceException {
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("topic.id", topicId);
		
		return getMaxByFields("position", false, fields);
	}

}
