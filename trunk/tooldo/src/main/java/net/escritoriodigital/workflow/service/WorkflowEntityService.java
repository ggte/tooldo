/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;

/**
 * @author andre
 *
 */
public interface WorkflowEntityService extends IGeneralService<WorkflowEntity> {
	
	public abstract void firstStepWorkflow(Theme theme) throws ServiceException;
	
}
