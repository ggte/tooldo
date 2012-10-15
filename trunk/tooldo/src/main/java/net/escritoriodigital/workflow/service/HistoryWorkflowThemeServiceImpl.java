/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.workflow.model.entity.HistoryWorkflowEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author andre
 *
 */
@Service ("historyWorkflowThemeService")
public class HistoryWorkflowThemeServiceImpl extends GeneralService<HistoryWorkflowEntity> implements HistoryWorkflowThemeService {


	private static final long serialVersionUID = 4038321092049032033L;

	@Autowired
	private UserService userService;
	@Autowired
	private WorkflowStageEntityService workflowStageEntityService;
	@Autowired
	private ThemeService themeService;
	
	@Override
	public void saveHistoryTheme(Theme theme, StageEntity stageC, User credential, String comments, boolean accept) throws ServiceException {
		
		HistoryWorkflowEntity entity = new HistoryWorkflowEntity();
		
		entity.setComments(comments);
		entity.setCreated(new Date());
		stageC = workflowStageEntityService.getById(stageC.getId());
		entity.setStage(stageC);
		theme = themeService.getById(theme.getId());
		entity.setTheme(theme);
		credential = userService.getById(credential.getId());
		entity.setUser(credential);
		entity.setAccept(accept);
		
		this.save(entity);
		
	}

	@Override
	public Collection<HistoryWorkflowEntity> getHistoryByTheme(Long themeId)
			throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("theme.id", themeId);
		
		return findByFields(fields, false);
	}




	
}
