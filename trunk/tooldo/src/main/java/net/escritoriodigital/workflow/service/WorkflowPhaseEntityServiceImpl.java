/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;

import org.springframework.stereotype.Service;

/**
 * @author andre
 *
 */
@Service ("workflowPhaseEntityService")
public class WorkflowPhaseEntityServiceImpl extends GeneralService<PhaseEntity> implements WorkflowPhaseEntityService {

	private static final long serialVersionUID = 1326857622294426742L;

	@Override
	public boolean isFinished(Long workflowId, Integer order) throws ServiceException {
		
		Map<String, Object> fields = new HashMap<String, Object>();
		if(workflowId != null) fields.put("workflow.id", workflowId);
		if(order != null) fields.put("order", order);
		fields.put("finished", true);
		
		Collection<PhaseEntity> list = findByFields(fields, true);
		
		if(list.size() != 0) return true;
		
		return false;
	}

	
}
