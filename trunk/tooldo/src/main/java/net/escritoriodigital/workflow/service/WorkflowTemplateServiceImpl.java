/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.workflow.model.template.WorkflowTemplate;

import org.springframework.stereotype.Service;

/**
 * @author andre
 *
 */
@Service ("workflowTemplateService")
public class WorkflowTemplateServiceImpl extends GeneralService<WorkflowTemplate> implements WorkflowTemplateService {

	private static final long serialVersionUID = 3503098223537995480L;

	@Override
	public WorkflowTemplate getCurrent() throws ServiceException {
		
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("enabled", true);	
		
		Iterator<WorkflowTemplate> it = findByFields(fields, false).iterator();
				
		return it.next();
	}


}
