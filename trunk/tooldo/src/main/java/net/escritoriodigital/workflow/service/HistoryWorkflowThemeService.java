/**
 *
 */
package net.escritoriodigital.workflow.service;

import java.util.Collection;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.workflow.model.entity.HistoryWorkflowEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;

/**
 * @author andre
 *
 */
public interface HistoryWorkflowThemeService extends IGeneralService<HistoryWorkflowEntity> {
	
	public abstract void saveHistoryTheme(Theme theme, StageEntity stageC, User credential, String comments, boolean accept) throws ServiceException;
	
	public abstract Collection<HistoryWorkflowEntity> getHistoryByTheme(Long themeId) throws ServiceException;
	
}
