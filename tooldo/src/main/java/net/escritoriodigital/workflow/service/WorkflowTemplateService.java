/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.workflow.model.template.WorkflowTemplate;

/**
 * @author andre
 *
 */
public interface WorkflowTemplateService extends IGeneralService<WorkflowTemplate> {
	
	public abstract WorkflowTemplate getCurrent() throws ServiceException;

}
