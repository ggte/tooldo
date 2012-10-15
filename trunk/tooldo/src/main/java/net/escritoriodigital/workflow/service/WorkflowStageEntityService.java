/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.workflow.model.entity.StageEntity;

/**
 * @author andre
 *
 */
public interface WorkflowStageEntityService extends IGeneralService<StageEntity> {
	
	public abstract StageEntity getStage(Long phaseId, Integer order, boolean finished) throws ServiceException;
	
}
