/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.workflow.model.entity.StageEntity;

import org.springframework.stereotype.Service;

/**
 * @author andre
 *
 */
@Service ("workflowStageEntityService")
public class WorkflowStageEntityServiceImpl extends GeneralService<StageEntity> implements WorkflowStageEntityService {

	private static final long serialVersionUID = 4520308297759195054L;

	@Override
	public StageEntity getStage(Long phaseId, Integer order, boolean finished) throws ServiceException {
		
		StageEntity stageEntity = null;
		
		Map<String, Object> fields = new HashMap<String, Object>();
		if(phaseId != null) fields.put("phase.id", phaseId);
		if(order != null) fields.put("phase.order", order);
		fields.put("finished", finished);
		
		Collection<StageEntity> list = findByFields(fields, true);
		
		if(list.size() != 0) stageEntity = list.iterator().next();
		
		return stageEntity;
	}

	
}
