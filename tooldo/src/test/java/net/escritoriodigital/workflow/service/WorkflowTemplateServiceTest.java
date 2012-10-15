/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;
import net.escritoriodigital.workflow.model.template.PhaseTemplate;
import net.escritoriodigital.workflow.model.template.WorkflowTemplate;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andre
 *
 */
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class WorkflowTemplateServiceTest extends AbstractWorkflowServiceTests {

	@Autowired
	private WorkflowTemplateService templateService;

	@Autowired
	private WorkflowEntityService entityService;

	@Test
	public void testSaveDeleteWorkflowEntity() throws Exception {
		WorkflowEntity wkf = generateWorkflowEntity();
		entityService.save(wkf);
		entityService.delete(wkf.getId());
	}

	@Test
	public void testSaveUpdateWorkflowEntity() throws Exception {
		WorkflowEntity wkf = generateWorkflowEntity();
		entityService.save(wkf);

		PhaseEntity delete = null;
		for (PhaseEntity phase : wkf.getPhases()) {
			delete = phase; break;
		}

		wkf.removePhase(delete);
		entityService.save(wkf);
	}

	@Test
	public void testSaveDeleteWorkflowTemplate() throws Exception {
		WorkflowTemplate wkf = generateWorkflowTemplate();
		templateService.save(wkf);
		templateService.delete(wkf.getId());
	}

	@Test
	public void testSaveUpdateWorkflowTemplate() throws Exception {
		WorkflowTemplate wkf = generateWorkflowTemplate();
		templateService.save(wkf);

		PhaseTemplate delete = null;
		for (PhaseTemplate phase : wkf.getPhases()) {
			delete = phase; break;
		}

		wkf.removePhase(delete);
		templateService.save(wkf);
	}

}
