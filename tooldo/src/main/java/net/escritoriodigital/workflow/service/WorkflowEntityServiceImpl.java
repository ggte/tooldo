/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.Iterator;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;
import net.escritoriodigital.workflow.model.template.PhaseTemplate;
import net.escritoriodigital.workflow.model.template.StageTemplate;
import net.escritoriodigital.workflow.model.template.WorkflowTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andre
 *
 */
@Service ("workflowEntityService")
public class WorkflowEntityServiceImpl extends GeneralService<WorkflowEntity> implements WorkflowEntityService {

	private static final long serialVersionUID = -7373821320050302594L;

	@Autowired
	private WorkflowTemplateService templateService;
	
	@Autowired
	private ThemeService themeService;
	
	@Override
	public void firstStepWorkflow(Theme theme) throws ServiceException {

		// recarregar o thema
		theme = themeService.getById(theme.getId());
		
		// verifica se ja nao existe um workflow para o theme
		if(theme.getWorkflow() != null) {
			throw new ServiceException("Workflow já foi iniciado para esse Tema.");
		}
		
		// Cria workflow através do template atual
		WorkflowTemplate template = templateService.getCurrent();
		
		WorkflowEntity entity = new WorkflowEntity();
		entity.setName(template.getName());
		entity.setDescription(template.getDescription());
		
		Iterator<PhaseTemplate> itPhaseTemplate = template.getPhases().iterator();
		PhaseEntity phase = null;
		PhaseTemplate tPhase = null;
		StageEntity stage = null;
		StageTemplate tStage = null;
		
		while(itPhaseTemplate.hasNext()) {
			
			phase = new PhaseEntity();
			tPhase = itPhaseTemplate.next();
			phase.setName(tPhase.getName());
			phase.setDescription(tPhase.getDescription());			
			phase.setOrder(tPhase.getOrder());
			phase.setWorkflow(entity);
			
			if(phase.getOrder().equals(new Integer(1))) phase.setFinished(true);
			else phase.setFinished(false);
			
			Iterator<StageTemplate> itStageTemplate = tPhase.getStages().iterator();
			
			while(itStageTemplate.hasNext()) {
				tStage = itStageTemplate.next();
				stage = new StageEntity();
				
				stage.setDescription(tStage.getDescription());
				
				if(phase.getOrder().equals(new Integer(1))) stage.setFinished(true);
				else stage.setFinished(false);
				
				stage.setName(tStage.getName()); 
				stage.setPhase(phase);
				stage.setRequired(tStage.getRequired());
				stage.setProfile(tStage.getProfile());
				phase.addStage(stage);
			}
			
			entity.addPhase(phase);
		}
		
		// Persist Workflow Entity
		this.save(entity);
		
		// Associa o tema ao workflow
		theme.setWorkflow(entity);
		themeService.save(theme);
	}

}
