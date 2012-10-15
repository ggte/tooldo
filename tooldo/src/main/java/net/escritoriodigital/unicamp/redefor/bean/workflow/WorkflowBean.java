package net.escritoriodigital.unicamp.redefor.bean.workflow;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.bean.page.PageBean;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.theme.ThemeUtil;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.unicamp.redefor.support.mail.MailSupport;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;
import net.escritoriodigital.unicamp.redefor.utils.mail.UrlMensagem;
import net.escritoriodigital.workflow.model.entity.HistoryWorkflowEntity;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.service.HistoryWorkflowThemeService;
import net.escritoriodigital.workflow.service.WorkflowPhaseEntityService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier("workflowBean")
@Scope("session")
public class WorkflowBean extends BaseBean {

	private Logger logger = Logger.getLogger(PageBean.class);

	private static final String MAIN_PAGE = "workflow-main-page";

	@Autowired
	private ThemeService themeService;
	@Autowired
	private HistoryWorkflowThemeService historyService;
	@Autowired
	private WorkflowPhaseEntityService phaseService;
	@Autowired
	private UserService userService;
	@Autowired
	private MailSupport mailSupport;

	private String paramHash;

	private Theme theme = new Theme();

	private List<Theme> themesWorkflow = new ArrayList<Theme>();

	private List<HistoryWorkflowEntity> historys = new ArrayList<HistoryWorkflowEntity>();

	private String historyComments = "";

	private String contextTemplateMail;
	private final String MAIL_TO = "mails";
	private final String MAIL_BODY_TEXT = "bodyText";
	private final String MAIL_BODY_HTML = "bodyHTML";

	public String pageWorkflow() throws Exception {
		//this.loadThemeWorkflow();
		return MAIN_PAGE;
	}

	public void getHistoryShow() throws Exception {
		try {
			historys = (List<HistoryWorkflowEntity>) historyService.getHistoryByTheme(theme.getId());
		} catch (ServiceException e) {
			logger.error("WorkflowBean.getHistoryShow: " + e.getMessage());
			FacesUtils.addErrorMessage("Não foi possível recuperar os históricos do tema.");
		}
	}

	@PostConstruct
	public void loadThemeWorkflow() throws Exception {

		try {
			
			theme = new Theme();
			
			themesWorkflow = new ArrayList<Theme>();

			List<Theme> listThemes = (List<Theme>) themeService
					.getThemeWorkflowByProfile(this.getCredential()
							.getProfile().getId());

			// caso o usuário tenha role de liberar tema (prof. autor)
			if (this.ifAnyGranted("ROLE_ADMINISTRATOR,ROLE_WORKFLOW_START")) {

				/*
				 * Se o usuário for um professor autor, selecionar apenas os
				 * temas os quais ele é autor
				 */
				User actualUser = this.getCredential();
				if (actualUser.getProfile().getId().equals(
						Profile.PROFESSOR_AUTOR_ID)) {
					for (Theme theme : listThemes) {
						if (theme.getAuthor().getId()
								.equals(actualUser.getId()) && theme.getEnabled())
							themesWorkflow.add(theme);
					}
				} else
					themesWorkflow = listThemes;

			} else {
				List<Discipline> disciplines = this.getCredential()
						.getDisciplines();

				/* adiciona na lista apenas os temas em que o usuário tem acesso */
				for (Theme theme : listThemes) {
					if (disciplines.contains(theme.getDiscipline())) {
						if (this.ifAllGranted("ROLE_WORKFLOW_ONLY")) {
							if (theme.getStartWorflow() && theme.getEnabled())
								themesWorkflow.add(theme);
						} else
							themesWorkflow.add(theme);

					}
				}
			}

			if (!themesWorkflow.isEmpty()) {
				for (Theme theme : themesWorkflow) {
					if (theme.isLocked()
							&& theme.getLock().equals(this.getCredential()))
						theme.setUserLocked(true);
					else
						theme.setUserLocked(false);
				}
			}
		} catch (ServiceException e) {
			logger.error("WorkflowBean.loadThemeWorkflow: " + e.getMessage());
			FacesUtils.addErrorMessage("Não existe pendências para aprovação.");
		}
	}

	public void lockTheme() throws Exception {
		try {
			theme = themeService.getById(theme.getId());
			
			if (theme.getLock() != null) {
				FacesUtils.addErrorMessage(" O tema " + theme.getName()
						+ " não pode ser bloqueado.");
			} else {
				
				User user = this.getCredential();
				user = userService.getById(user.getId());
				
				theme.setLock(user);
				for (Topic topic : theme.getTopics()) {
					topic.setLock(user);
					for (Page page : topic.getPages()) {
						page.setLock(user);
					}
				}
				
				themeService.save(theme);
				FacesUtils.addInfoMessage(" O tema " + theme.getName()
						+ " foi bloqueado com sucesso.");
			}
			
			this.loadThemeWorkflow();
			
		} catch (ServiceException e) {
			logger.error("ThemeBean.lockTheme: " + e.getMessage());
			FacesUtils.addInfoMessage(" Erro ao bloquear o tema "
					+ theme.getName() + ".");
		}
	}

	public void unlockTheme() throws Exception {
		try {
			ThemeUtil.unlockTheme(themeService, theme);
			FacesUtils.addInfoMessage(" O tema " + theme.getName() + " foi desbloqueado com sucesso.");
			this.loadThemeWorkflow();
		} catch (ServiceException e) {
			FacesUtils.addInfoMessage(" Erro ao desbloquear o tema " + theme.getName() + ".");
		}
	}
	
	public void clearComments() throws Exception {
		this.historyComments = "";
	}
	
	private void proccessAcceptTheme() throws Exception {
		
		try {
			
			// Desloca o tema do usuário
			theme.setLock(null);
			for (Topic topic : theme.getTopics()) {
				topic.setLock(null);
				for (Page page : topic.getPages()) {
					page.setLock(null);
				}
			}

			// Passa para o próximo passo
			StageEntity stageC = null;
			for (PhaseEntity phaseEntity : theme.getWorkflow().getPhases()) {
				for (StageEntity stageEntity : phaseEntity.getStages()) {
					
					if (stageEntity.getProfile().equals(getCredential().getProfile())) {
						
						if (phaseEntity.getOrder().equals(new Integer(1))) {
							stageEntity.setFinished(true);
							stageEntity.setUser(this.getCredential());
							stageC = stageEntity;
						} else if (phaseService.isFinished(theme.getWorkflow()
								.getId(), (phaseEntity.getOrder() - 1))) {
							stageEntity.setFinished(true);
							stageEntity.setUser(this.getCredential());
							stageC = stageEntity;
						}
						
						// verifica se é a publicação do tema
						// se for, verificar se esse não substitui outro
						if (phaseEntity.getOrder().equals(
								theme.getWorkflow().getPhases().size())) {
							
							Integer publishCode = new Integer(0);
							if(theme.getReplaceId() != null) {
								
								Theme oldtheme = themeService.getById(theme.getReplaceId());
								
								if(oldtheme == null) publishCode = themeService.getMaxPublishCode();
								else {
									publishCode = oldtheme.getPublishCode();
									oldtheme.setPublishCode(null);
									oldtheme.setEnabled(false);
									themeService.save(oldtheme);
								}
								
							} else {
								publishCode = themeService.getMaxPublishCode();
							}
							
							theme.setPublishCode(publishCode);
							theme.setPublished(true);
						}
					}
				}
				
				phaseEntity.verifyStages();

				// Enviando e-mail para professor
				Boolean phaseFinished = phaseEntity.getFinished();
				if (phaseFinished && phaseEntity.getOrder() == 4) {

					String template = "templateMailProfessor";

					String[] mailTo = { theme.getDiscipline().getOwner()
							.getEmail() };
					Map<String, Object> tokens = new HashMap<String, Object>();
					tokens.put("theme", theme.getName());
					tokens.put("profileName", this.getCredential().getName());

					tokens.put("date", new Date().toString());

					this.sendMail(template, mailTo, tokens);
				}

				// Enviando e-mail para o coordenador
				phaseFinished = phaseEntity.getFinished();
				if (phaseFinished && phaseEntity.getOrder() == 5) {

					String template = "templateMailCoordenador";

					// Profile de coordenador
					List<User> users = (List<User>) userService
							.getUserByProfileAndDiscipline(new Long(3), theme
									.getDiscipline());
					String[] mailTo = new String[users.size()];
					for (int i = 0; i < users.size(); i++) {
						mailTo[i] = users.get(i).getEmail();
					}
					Map<String, Object> tokens = new HashMap<String, Object>();
					tokens.put("theme", theme.getName());
					tokens.put("profileName", this.getCredential().getName());

					tokens.put("date", new Date().toString());

					this.sendMail(template, mailTo, tokens);
				}
			}
			
			// Persist theme
			themeService.save(theme);

			// Persist historico
			historyService.saveHistoryTheme(theme, stageC,
					this.getCredential(), this.historyComments, true);
			
			FacesUtils.addInfoMessage(" O tema " + theme.getName()
					+ " foi aprovado com sucesso.");

			this.loadThemeWorkflow();
			
		} catch (ServiceException e) {
			logger.error("ThemeBean.acceptTheme: " + e.getMessage());
			FacesUtils.addInfoMessage(" Erro ao aprovar o tema "
					+ theme.getName() + ".");
		}
	}

	public void acceptTheme() throws Exception {
		
		theme = themeService.getById(theme.getId());
		
		// se o tema não estiver travado, nem deixa aprovar
		if(theme.getLock() == null) {
			FacesUtils.addErrorMessage(" O tema " + theme.getName() + " não está locado para o seu usuário.");
			
			// atualiza a lista de pendências do usuário
			this.loadThemeWorkflow();
		} else {
			
			// verifica se o thema ja nao está locado para outro usuário
			if(!theme.getLock().getId().equals(getCredential().getId())) {
				FacesUtils.addErrorMessage(" O tema " + theme.getName()
						+ " foi travado por outro usuário.");
			} else {
				proccessAcceptTheme();
			}
			
		}
	}

	public void readjustTheme() throws Exception {

		try {
			
			theme = themeService.getById(theme.getId());
			// Desloca o tema do usuário
			
			theme.setLock(null);
			for (Topic topic : theme.getTopics()) {
				topic.setLock(null);
				for (Page page : topic.getPages()) {
					page.setLock(null);
				}
			}

			// Readequar todos os passos devem voltar para o ínicio
			StageEntity stageC = null;
			for (PhaseEntity phaseEntity : theme.getWorkflow().getPhases()) {
				phaseEntity.setFinished(false);
				for (StageEntity stageEntity : phaseEntity.getStages()) {
					if (stageEntity.getProfile().equals(
							getCredential().getProfile()))
						stageC = stageEntity;

					stageEntity.setFinished(false);
					stageEntity.setUser(null);
				}
			}

			theme.setPublished(false);
			// Persist theme
			themeService.save(theme);

			// Persist historico
			historyService.saveHistoryTheme(theme, stageC, this
					.getCredential(), historyComments, false);
			
			FacesUtils.addInfoMessage(" A solicitação para readequação ao tema "
							+ theme.getName() + " foi solicitado com sucesso.");
			
			this.loadThemeWorkflow();

		} catch (ServiceException e) {
			FacesUtils.addErrorMessage(" Erro ao solicitar a readequação do tema "
							+ theme.getName() + ".");
		}
	}

	public boolean sendMail(String templateName, String[] mailTo,
			Map<String, Object> tokens) {
		// get template
		
		String template = contextTemplateMail + templateName + ".html";

		StringBuffer text = this.readFile(template);

		try {
			text = UrlMensagem.replaceByToken(text, FacesUtils.getHostname(),
					"[urlSite]");
			for (String key : tokens.keySet()) {
				text = UrlMensagem.replaceByToken(text, tokens.get(key)
						.toString(), key);
			}
			Map<String, Object> content = new HashMap<String, Object>();
			content.put(this.MAIL_TO, mailTo);
			content.put(this.MAIL_BODY_TEXT, text.toString());
			content.put(this.MAIL_BODY_HTML, true);

			mailSupport.send(content);

		} catch (Exception e) {
			logger
					.error("ThemeBean.sendMail.replaceByToken: "
							+ e.getMessage());
			return false;
		}
		
		return true;

	}

	private StringBuffer readFile(String template) {
		StringBuffer contents = new StringBuffer();
		try {
			BufferedReader input = new BufferedReader(new FileReader(template));
			String line = null;
			while ((line = input.readLine()) != null) {
				contents.append(line);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return contents;
	}

	/**
	 * @return the historys
	 */
	public List<HistoryWorkflowEntity> getHistorys() {
		return historys;
	}

	/**
	 * @param historys
	 *            the historys to set
	 */
	public void setHistorys(List<HistoryWorkflowEntity> historys) {
		this.historys = historys;
	}

	/**
	 * @return the historyComments
	 */
	public String getHistoryComments() {
		return historyComments;
	}

	/**
	 * @param historyComments
	 *            the historyComments to set
	 */
	public void setHistoryComments(String historyComments) {
		this.historyComments = historyComments;
	}

	/**
	 * @return the paramHash
	 */
	public String getParamHash() {
		return paramHash;
	}

	/**
	 * @param paramHash
	 *            the paramHash to set
	 */
	public void setParamHash(String paramHash) {
		this.paramHash = paramHash;
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
	 * @return the themesWorkflow
	 */
	public List<Theme> getThemesWorkflow() {
		return themesWorkflow;
	}

	/**
	 * @param themesWorkflow
	 *            the themesWorkflow to set
	 */
	public void setThemesWorkflow(List<Theme> themesWorkflow) {
		this.themesWorkflow = themesWorkflow;
	}

	/**
	 * @return the contextTemplateMail
	 */
	public String getContextTemplateMail() {
		return contextTemplateMail;
	}

	/**
	 * @param contextTemplateMail
	 *            the contextTemplateMail to set
	 */
	public void setContextTemplateMail(String contextTemplateMail) {
		this.contextTemplateMail = contextTemplateMail;
	}

}
