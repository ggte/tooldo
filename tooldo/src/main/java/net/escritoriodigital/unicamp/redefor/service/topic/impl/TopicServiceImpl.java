package net.escritoriodigital.unicamp.redefor.service.topic.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.topic.TopicService;

import org.springframework.stereotype.Service;

/**
 * @author Leandro Concon
 * @category Service
 * @created 18/06/2010
 * @description Servico Topico
 */
@Service("topicService")
public class TopicServiceImpl extends GeneralService<Topic> implements
		TopicService {
	
	private static final long serialVersionUID = -5931766646830578846L;

	@Override
	public Collection<Topic> getTopicByTheme(Long themeId)
			throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("theme.id", themeId);

		return findByFields(fields, false);
	}

	@Override
	public Integer getLastPostionByByTheme(Long themeId)
			throws ServiceException {

		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("theme.id", themeId);

		return getMaxByFields("position", false, fields);
	}

}
