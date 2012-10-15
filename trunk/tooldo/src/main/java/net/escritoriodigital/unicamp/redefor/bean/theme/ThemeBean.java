package net.escritoriodigital.unicamp.redefor.bean.theme;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.secure.CredentialBean;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.theme.ThemeUtil;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.discipline.DisciplineService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.unicamp.redefor.service.topic.TopicService;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.service.HistoryWorkflowThemeService;
import net.escritoriodigital.workflow.service.WorkflowEntityService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("themeBean")
public class ThemeBean extends CredentialBean {
	
	protected static final String SUCCESS = "themeSuccess";

	private Logger logger = Logger.getLogger(ThemeBean.class);

	@Autowired
	private ThemeService themeService;
	@Autowired
	private DisciplineService disciplineService;
	@Autowired
	private TopicService topicService;
	@Autowired
	private WorkflowEntityService workflowEntityService;
	@Autowired
	private HistoryWorkflowThemeService historyService;

	private Theme theme = new Theme();
	private Discipline discipline = new Discipline();

	private List<Theme> themes = new ArrayList<Theme>();
	private List<SelectItem> disciplineItem = new ArrayList<SelectItem>();

	private Boolean showErrorMessage = false;

	public String loadThemeByDiscipline() {

		try {
			if (discipline.getId() != null) {

				if (this.ifNotGranted("ROLE_ADMINISTRATOR")
						&& this.ifAllGranted("ROLE_WORKFLOW_ONLY")) {

					/*
					 * se o usuário for somente workflow, só mostra o que tem
					 * workflow e ainda aquilo que está para a sua fase
					 */

					themes = (List<Theme>) themeService
							.getThemeWorkflowByProfile(this.getCredential()
									.getProfile().getId());
					List<Theme> toDelete = new ArrayList<Theme>();

					for (Theme th : themes) {
						if (!th.getDiscipline().getId()
								.equals(discipline.getId())
								&& !th.getEnabled())
							toDelete.add(th);
					}

					if (!toDelete.isEmpty())
						themes.removeAll(toDelete);

				} else {
					themes = (List<Theme>) themeService
							.getThemeByDiscipline(discipline.getId());

					/*
					 * Se não for administrador, mostra apenas os temas ativos
					 */
					if (this.ifNotGranted("ROLE_ADMINISTRATOR")) {
						List<Theme> toDelete = new ArrayList<Theme>();
						for (Theme th : themes) {
							if (!th.getEnabled())
								toDelete.add(th);
						}

						if (!toDelete.isEmpty())
							themes.removeAll(toDelete);
					}
				}

				for (Theme th : themes) {
					if (th.isLocked() && th.getLock().equals(this.getCredential()))
						th.setUserLocked(true);
					else
						th.setUserLocked(false);

					th.setParamHash(encrypt(th.getId()));
				}

			} else
				FacesUtils.addErrorMessage(" Erro ao carregar lista de temas.");

		} catch (ServiceException e) {
			logger.error("ThemeBean.loadThemeByDiscipline: " + e.getMessage());

		}
		
		this.clear();
		return SUCCESS;
	}

	// copiado de workflowbean, rever depois
	private String encrypt(Long themeId) {

		String result = "";

		try {
			result = TripleDESUtils.newInstance().encrypt(1 + "," + themeId);
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * Duplica o tema atual para um novo que irá substituí-lo
	 */
	public String duplicateTheme() throws Exception {
		if (!theme.getPublished()) return SUCCESS;
		themeService.duplicateTheme(theme);
		return this.loadThemeByDiscipline();
	}

	public String save() {
		
		if (!theme.getName().equals("")) {
			try {
				
				if(theme.getEnabled() == null) theme.setEnabled(true);
				
				if (theme.getCreated() == null)
					theme.setCreated(new Date());

				/* Condição válida apenas para temas novos */
				if (theme.getId() == null) {
					discipline = this.getDisciplineById(theme.getDiscipline()
							.getId());
					User user = discipline.getOwner();
					theme.setAuthor(user);
					theme.setDiscipline(discipline);
				}
				
				themeService.save(theme);
				FacesUtils.addInfoMessage("Thema " + theme.getName() + " salvo com sucesso.");
				
			} catch (ServiceException e) {
				logger.error("ThemeBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao salvar o tema.");
				return "Error";
			}
			
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}

		this.loadThemeByDiscipline();
		this.clear();
		
		return SUCCESS;
	}

	private Discipline getDisciplineById(Long id) throws ServiceException {
		return disciplineService.getById(id);
	}

	public String getThemeByDiscipline() {
		try {
			Long id = new Long(FacesUtils.getParameterValue("idDiscipline"));
			discipline = disciplineService.getById(id);
			this.loadThemeByDiscipline();
		} catch (ServiceException e) {
			logger.error("ThemeBean.getThemeByDiscipline: " + e.getMessage());
		}
		return "themeSuccess";
	}
	
	public void getThemeById() {
		try {
			theme = themeService.getById(theme.getId());
			if (theme.getPublished() && theme.getPublishCode() != null) {
				try {

					HttpServletRequest request = (HttpServletRequest) getFacesContext()
							.getExternalContext().getRequest();
					String requestURL = request.getRequestURL().toString();
					int pos = requestURL.indexOf("/pages/secure/manager/");

					if (pos > 0) {
						requestURL = requestURL.substring(0, pos);
						String hash = TripleDESUtils.newInstance().encrypt(
								Theme.SALT_VALUE
										+ theme.getPublishCode().toString());
						theme.setParamHash(requestURL + Theme.PATH_PAGE_THEME
								+ URLEncoder.encode(hash, "UTF-8"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (ServiceException e) {
			logger.error("ThemeBean.getThemeById: " + e.getMessage());
		}
	}

	public String remove() {
		if (theme != null) {
			/*
			try { 
				Theme entity = new Theme();
				entity = themeService.getById(theme.getId());
	
				themeService.delete(entity.getId());
				FacesUtils.addInfoMessage(" Tema excluído com sucesso.");
			} catch (ServiceException e) {
				logger.error("ThemeBean.remove: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao excluir o Disciplina "
						+ theme.getName());
			}
			*/
			
			try {

				List<Topic> topics = (List<Topic>) topicService
						.getTopicByTheme(theme.getId());

				if (topics.size() == 0) {

					Theme entity = new Theme();
					entity = themeService.getById(theme.getId());

					themeService.delete(entity.getId());
					FacesUtils.addInfoMessage(" Tema excluído com sucesso.");

				} else {
					FacesUtils
							.addErrorMessage(" Existe topicos associado ao tema "
									+ theme.getName());
				}

			} catch (ServiceException e) {
				logger.error("ThemeBean.remove: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao excluir o Disciplina "
						+ theme.getName());
			}
			
			
		} else {
			FacesUtils.addErrorMessage(" Não foi possível localizar o tema.");
		}

		this.clear();

		this.loadThemeByDiscipline();

		return "Success";
	}
	
	public void unlockTheme() throws Exception {
		try {
			ThemeUtil.unlockTheme(themeService, theme);
			FacesUtils.addInfoMessage(" O tema " + theme.getName() + " foi desbloqueado com sucesso.");
			this.loadThemeByDiscipline();
		} catch (ServiceException e) {
			FacesUtils.addInfoMessage(" Erro ao desbloquear o tema " + theme.getName() + ".");
		}
	}

	public void enableTheme() {
		if (theme != null) {
			try {
				theme.setEnabled(true);
				themeService.save(theme);
				FacesUtils.addInfoMessage(" O tema " + theme.getName()
						+ " foi ativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("ThemeBean.enableTheme: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar o tema "
						+ theme.getName());
			}
		}
		this.loadThemeByDiscipline();
	}

	public void disableTheme() {
		if (theme != null) {
			try {
				theme.setEnabled(false);
				themeService.save(theme);
				FacesUtils.addInfoMessage(" O tema " + theme.getName()
						+ " foi desativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("ThemeBean.disableTheme: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao desativar o tema "
						+ theme.getName());
			}
		}
		this.loadThemeByDiscipline();
	}

	public void confirmStartWorkflow() {
		
		this.showErrorMessage = true;
		try {
			theme = themeService.getById(theme.getId());

			boolean errorTopics = false;
			boolean errorPages = false;
			StringBuilder topicsMessage = new StringBuilder();
			StringBuilder pagesMessage = new StringBuilder();

			List<Topic> topics = theme.getTopics();
			// Validando se o tema tem ao menos 1 tópico
			if (topics != null && topics.size() <= 0) {
				FacesUtils.addErrorMessage(" O tema " + theme.getName()
						+ " não possui tópicos associado.");
			} else {
				// Validando se todos os tópicos tem ao menos 1 página
				for (Topic topic : topics) {
					List<Page> pages = topic.getPages();
					if (pages != null && pages.size() <= 0) {
						if (topicsMessage.length() != 0)
							topicsMessage.append(", ");
						topicsMessage.append(topic.getName());
						errorTopics = true;
					} else {
						for (Page page : pages) {
							if (page.getContent() == null) {
								if (pagesMessage.length() != 0)
									pagesMessage.append(", ");
								pagesMessage.append(page.getName());
								errorPages = true;
							}
						}
					}
				}

				this.showErrorMessage = false;

				if (errorTopics) {
					FacesUtils
							.addErrorMessage(" Os tópicos a seguir não possuem páginas associadas: "
									+ topicsMessage.toString());
					this.showErrorMessage = true;
				}
				if (errorPages) {
					FacesUtils
							.addErrorMessage(" As seguintes páginas não possuem conteúdo: "
									+ pagesMessage.toString());
					this.showErrorMessage = true;
				}
			}

		} catch (ServiceException e) {
			logger.error("ThemeBean.confirmStartWorkflow: " + e.getMessage());
		}
	}

	public void startWorkflow() {
		if (theme != null) {
			try {
				workflowEntityService.firstStepWorkflow(theme);
				
				theme = themeService.getById(theme.getId());
				
				if(theme.getWorkflow() != null) {
					if(theme.getWorkflow().getPhases() != null) {
						// verifica o estágio para criar o historico do workflow
						StageEntity stageC = null;
						for (PhaseEntity ph : theme.getWorkflow().getPhases()) {
							if(ph.getOrder().equals(new Integer(1))) {
								for (StageEntity stg : ph.getStages()) {
									stageC = stg;
									break;
								}
								break;
							}
						}
						
						// Persist historico
						historyService.saveHistoryTheme(theme, stageC, this.getCredential(), "", true);
					}
				}
				
				this.loadThemeByDiscipline();
				
			} catch (ServiceException e) {
				logger.error("Erro: " + e.getMessage());
			}
		}
	}

	public void clear() {
		this.theme = new Theme();
		this.theme.setDiscipline(new Discipline());
		this.theme.setAuthor(new User());
		if (discipline.getId() != null)
			this.theme.setDiscipline(discipline);
		if (discipline.getId() != null)
			this.theme.setAuthor(discipline.getOwner());
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme
	 *            the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	/**
	 * @return the discipline
	 */
	public Discipline getDiscipline() {
		return discipline;
	}

	/**
	 * @param discipline
	 *            the discipline to set
	 */
	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	/**
	 * @return the themes
	 */
	public List<Theme> getThemes() {
		return themes;
	}

	/**
	 * @param themes
	 *            the themes to set
	 */
	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	/**
	 * @return the disciplineItem
	 */
	public List<SelectItem> getDisciplineItem() {
		return disciplineItem;
	}

	/**
	 * @param disciplineItem
	 *            the disciplineItem to set
	 */
	public void setDisciplineItem(List<SelectItem> disciplineItem) {
		this.disciplineItem = disciplineItem;
	}

	/**
	 * @return the showErrorMessage
	 */
	public Boolean getShowErrorMessage() {
		return showErrorMessage;
	}

}
