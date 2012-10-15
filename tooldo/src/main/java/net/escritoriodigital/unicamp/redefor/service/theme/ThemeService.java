/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.theme;

import java.util.Collection;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Serviço
 */
public interface ThemeService extends IGeneralService<Theme> {

	/**
	 * Busca o thema pelo seu publishCode
	 * 
	 * @param publishCode
	 * @return
	 * @throws ServiceException
	 */
	public abstract Theme getThemeByPublishCode(Integer publishCode)
			throws ServiceException;

	/**
	 * Pega o próximo publish code disponível para o caso de publicação de temas
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public abstract Integer getMaxPublishCode() throws ServiceException;

	/**
	 * Duplica um tema para substituir o que está sendo enviado como parâmetro
	 * quando for publicado
	 * 
	 * @param theme
	 * @return Theme duplicado
	 * @throws ServiceException
	 */
	public abstract Theme duplicateTheme(Theme theme) throws ServiceException;

	/**
	 * Retorna os temas a partir do id da disciplina
	 * 
	 * @param disciplineId
	 * @return Collection<Theme>
	 * @throws ServiceException
	 */
	public abstract Collection<Theme> getThemeByDiscipline(Long disciplineId)
			throws ServiceException;

	/**
	 * Busca de temas pelo profile do usuário, ou seja, tudo que está pendente
	 * para o user
	 * 
	 * @param profileId
	 * @return Collection<Theme>
	 * @throws ServiceException
	 */
	public abstract Collection<Theme> getThemeWorkflowByProfile(Long profileId)
			throws ServiceException;

	/**
	 * Busca os temas no workflow pelo usuario, tudo que esta pendente ou pode
	 * ser manipulado por ele
	 * 
	 * @param user
	 * @return Collection<Theme>
	 * @throws ServiceException
	 */
	public abstract Collection<Theme> listThemesWorkflowByUser(User user)
			throws ServiceException;
}
