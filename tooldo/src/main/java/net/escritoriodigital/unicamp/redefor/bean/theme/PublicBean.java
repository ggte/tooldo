package net.escritoriodigital.unicamp.redefor.bean.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.page.PageService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.unicamp.redefor.service.topic.TopicService;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("publicBean")
public class PublicBean extends BaseBean {

	private static final String MAINPAGE = "public-content-page";

	public static final String TITLE_EDUCACAO_FISICA = "tit-educacao_fisica.png";
	public static final String TITLE_FISICA = "tit-fisica.png";
	public static final String TITLE_HISTORIA = "tit-historia.png";
	public static final String TITLE_PORTUGUES = "tit-portugues.png";
	public static final String TITLE_MATEMATICA = "tit-matematica.png";

	private Logger logger = Logger.getLogger(PublicBean.class);

	@Autowired
	private ThemeService themeService;

	@Autowired
	private TopicService topicService;

	@Autowired
	private PageService pageService;

	private Theme theme = new Theme();
	private Page page = null;
	private List<Topic> topics = new ArrayList<Topic>();

	private Boolean hasNext = false;
	private Boolean hasPrev = false;

	private String imageTitle = TITLE_MATEMATICA;

	private boolean showTestResult = false;

	private void selectStyle() {
		
		if (theme.getDiscipline().getCourse().getId().equals(new Long(1))) {
			// Educação Física
			imageTitle = TITLE_EDUCACAO_FISICA;
		} else if (theme.getDiscipline().getCourse().getId()
				.equals(new Long(2))) {
			// Física
			imageTitle = TITLE_FISICA;
		} else if (theme.getDiscipline().getCourse().getId()
				.equals(new Long(3))) {
			// História
			imageTitle = TITLE_HISTORIA;
		} else if (theme.getDiscipline().getCourse().getId()
				.equals(new Long(4))) {
			// Língua Portuguesa
			imageTitle = TITLE_PORTUGUES;
		} else if (theme.getDiscipline().getCourse().getId()
				.equals(new Long(5))) {
			// Matemática
			imageTitle = TITLE_MATEMATICA;
		} else {
			imageTitle = TITLE_MATEMATICA;
		}
	}

	/**
	 * Carrega o conteúdo de uma página única, com base no parâmetro pageId
	 * enviado no request
	 */
	public String loadContentByPage() throws Exception {

		Long pageId = Long.valueOf(getRequest().getParameter("pageId"));
		showTestResult = false;

		if (pageId != null) {
			page = pageService.getById(pageId);
			proccessTheme(page.getTopic().getTheme().getId());
			selectStyle();
			return MAINPAGE;
		}

		return ERROR;
	}

	/**
	 * Método que carrega o theme e popula o bean
	 * @return
	 * @throws Exception
	 */
	private Boolean loadContentByThemeSupport() throws Exception {
		
		Long themeId = null;
		
		try {
			// caso o acesso seja feito interno, o parameter themeId
			if(getRequest().getParameter("themeId") != null) {
				themeId = Long.valueOf(getRequest().getParameter("themeId"));
				
			} else if(queryString("hash") != null) {
				// se o acesso estiver sendo pelo teleduc, o codigo de publicação virá no hash
				String decripted = TripleDESUtils.newInstance().decrypt(queryString("hash"));
				
				if(decripted.startsWith(Theme.SALT_VALUE)) {
					decripted = decripted.replace(Theme.SALT_VALUE, "");
					Integer publishId = Integer.valueOf(decripted);
					if(publishId != null) {
						Theme th = themeService.getThemeByPublishCode(publishId);
						if(th != null) themeId = th.getId();
					}
				}
			}
		} catch (NumberFormatException e) {
			return false;
		}
		
		showTestResult = false;

		if (themeId != null) {
			if (proccessTheme(themeId)) {
				for (Topic topic : theme.getTopics()) {
					for (Page pag : topic.getPages()) {
						page = pageService.getById(pag.getId());
						selectStyle();
						break;
					}
					break;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Carrega o conteúdo com base no theme, e mostra a primeira página do theme
	 */
	public String loadContentByTheme() throws Exception {
		if (loadContentByThemeSupport())
			return MAINPAGE;
		else
			return ERROR;
	}

	private boolean proccessTheme(Long idTheme) {
		try {

			this.theme = themeService.getById(idTheme);

			if (this.theme == null) {
				return false;
			} else {
				topics = theme.getTopicsEnabled();
			}

			if (!this.theme.getDiscipline().getCourse().getEnabled()) {
				FacesUtils.addErrorMessage("Curso foi desativado.");
				return false;
			}

			if (!this.theme.getDiscipline().getEnabled()) {
				FacesUtils.addErrorMessage("Disciplina foi desativada.");
				return false;
			}

		} catch (ServiceException e) {
			logger.error("PublicBean.loadTheme: " + e.getMessage());
			FacesUtils.addErrorMessage("Acesso restrito.");
			return false;
		}

		return true;
	}


	/**
	 * Além de retornar o title, carrega todos os objetos
	 * 
	 * @return the titlePage
	 */
	public String getTitlePage() throws Exception {
		
		if(queryString("hash") != null) {
			
			loadContentByThemeSupport();
			return theme.getName();
			
		} else return "";
	}

	public void loadContentByPostion() {
		Integer position = new Integer(FacesUtils.getParameterValue("position"));
		Long idTopic = new Long(FacesUtils.getParameterValue("idTopic"));
		Boolean isNext = new Boolean(FacesUtils.getParameterValue("isNext"));
		showTestResult = false;

		// Verificar se é a próxima página
		if (isNext) {
			try {
				Topic topic = topicService.getById(idTopic);
				if (topic != null && topic.getPages().size() > 0)
					page = topic.getPages().get(position);
			} catch (ServiceException e) {
				logger.error("PublicBean.loadContent > IsNext: "
						+ e.getMessage());
			}
		} else {
			position = position - 2;

			try {
				Topic topic = topicService.getById(idTopic);
				if (topic != null && position >= 0)
					page = topic.getPages().get(position);
			} catch (ServiceException e) {
				logger.error("PublicBean.loadContent > IsNext: "
						+ e.getMessage());
			}
		}
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
	 * @return the topics
	 */
	public List<Topic> getTopics() {
		return topics;
	}

	/**
	 * @param topics
	 *            the topics to set
	 */
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @param page
	 *            the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the hasNext
	 */
	public Boolean getHasNext() {
		if (page != null
				&& page.getTopic().getPages().size() > page.getPosition())
			this.hasNext = true;
		else
			this.hasNext = false;
		return hasNext;
	}

	/**
	 * @param hasNext
	 *            the hasNext to set
	 */
	public void setHasNext(Boolean hasNext) {
		this.hasNext = hasNext;
	}

	/**
	 * @return the hasPrev
	 */
	public Boolean getHasPrev() {
		if (page != null && page.getPosition() > 1)
			this.hasPrev = true;
		else
			this.hasPrev = false;
		return hasPrev;
	}

	/**
	 * @param hasPrev
	 *            the hasPrev to set
	 */
	public void setHasPrev(Boolean hasPrev) {
		this.hasPrev = hasPrev;
	}

	/**
	 * @return the imageTitle
	 */
	public String getImageTitle() {
		return imageTitle;
	}

	/**
	 * @param imageTitle
	 *            the imageTitle to set
	 */
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

	/**
	 * @return the showTestResult
	 */
	public boolean isShowTestResult() {
		return showTestResult;
	}

	/**
	 * @param showTestResult
	 *            the showTestResult to set
	 */
	public void setShowTestResult(boolean showTestResult) {
		this.showTestResult = showTestResult;
	}

	private String queryString(String key) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> params = context.getExternalContext()
				.getRequestParameterMap();
		String pID = params.get(key);
		return pID;
	}

	private String decrypt() {

		String result = null;

		try {
			String param = queryString("hash");
			if (param != null && !"".equals(param))
				result = TripleDESUtils.newInstance().decrypt(param);
		} catch (Exception e) {
			return result;
		}

		return result;
	}

}
