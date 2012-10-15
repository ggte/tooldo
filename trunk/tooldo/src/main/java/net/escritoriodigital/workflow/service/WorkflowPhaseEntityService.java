/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;

/**
 * @author andre
 *
 */
public interface WorkflowPhaseEntityService extends IGeneralService<PhaseEntity> {
	
	public abstract boolean isFinished(Long workflowId, Integer order) throws ServiceException;
	
}
