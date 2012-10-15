/**
 *
 */
package net.escritoriodigital.workflow.service;

import net.escritoriodigital.unicamp.redefor.service.AbstractServiceTests;
import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;
import net.escritoriodigital.workflow.model.template.PhaseTemplate;
import net.escritoriodigital.workflow.model.template.StageTemplate;
import net.escritoriodigital.workflow.model.template.WorkflowTemplate;

/**
 * @author andre
 *
 */
public class AbstractWorkflowServiceTests extends AbstractServiceTests {

	public static final Integer QTDE_STAGES_IN_PHASES = 3;
	public static final Integer QTDE_PHASES_IN_WORKFLOW = 4;

	protected WorkflowEntity generateWorkflowEntity() {
		WorkflowEntity workflow = new WorkflowEntity();

		workflow.setName(StringUtils.randomString(90));
		workflow.setDescription(StringUtils.randomString(190));

		for (int i = 0; i < QTDE_PHASES_IN_WORKFLOW; i++)
			workflow.addPhase(generatePhaseEntity());

		return workflow;
	}

	protected PhaseEntity generatePhaseEntity() {
		PhaseEntity phase = new PhaseEntity();

		phase.setName(StringUtils.randomString(90));
		phase.setDescription(StringUtils.randomString(190));

		for (int i = 0; i < QTDE_STAGES_IN_PHASES; i++)
			phase.addStage(generateStageEntity());

		return phase;
	}

	protected StageEntity generateStageEntity() {
		StageEntity stage = new StageEntity();

		stage.setName(StringUtils.randomString(90));
		stage.setDescription(StringUtils.randomString(190));

		return stage;
	}

	protected WorkflowTemplate generateWorkflowTemplate() {
		WorkflowTemplate workflow = new WorkflowTemplate();

		workflow.setName(StringUtils.randomString(90));
		workflow.setDescription(StringUtils.randomString(190));

		for (int i = 0; i < QTDE_PHASES_IN_WORKFLOW; i++)
			workflow.addPhase(generatePhaseTemplate());

		return workflow;
	}

	protected PhaseTemplate generatePhaseTemplate() {
		PhaseTemplate phase = new PhaseTemplate();

		phase.setName(StringUtils.randomString(90));
		phase.setDescription(StringUtils.randomString(190));

		for (int i = 0; i < QTDE_STAGES_IN_PHASES; i++)
			phase.addStage(generateStageTemplate());

		return phase;
	}

	protected StageTemplate generateStageTemplate() {
		StageTemplate stage = new StageTemplate();

		stage.setName(StringUtils.randomString(90));
		stage.setDescription(StringUtils.randomString(190));

		return stage;
	}

}
