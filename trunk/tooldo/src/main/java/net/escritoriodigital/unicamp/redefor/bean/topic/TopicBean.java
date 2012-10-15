package net.escritoriodigital.unicamp.redefor.bean.topic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.page.PageService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.unicamp.redefor.service.topic.TopicService;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.richfaces.component.UICommandButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("topicBean")
public class TopicBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 7365465485974102883L;

	private Logger logger = Logger.getLogger(TopicBean.class);

	@Autowired
	private TopicService topicService;
	@Autowired
	private PageService pageService;
	@Autowired
	private ThemeService themeService;

	private Topic topic = new Topic();
	private Theme theme = new Theme();

	private List<Topic> topics = new ArrayList<Topic>();
	private List<SelectItem> themesItem = new ArrayList<SelectItem>();

	public String loadTopic() {

		try {
			if (theme != null) {
				if (theme.getId() != null)
					topics = (List<Topic>) topicService.getTopicByTheme(theme
							.getId());
			} else
				topics = (List<Topic>) topicService.findAll();

			if(topics != null) Collections.sort(topics);
			
			for (Topic top : topics) {
				if (top.isLocked()
						&& top.getLock().equals(this.getCredential()))
					top.setUserLocked(true);
				else
					top.setUserLocked(false);

				/*
				 * Verificando se o processo de workflow foi iniciado true:
				 * Inserir, Editar e Excluir indisponíveis false: Inserir,
				 * Editar e Excluir disponíveis
				 */
				if (top.getTheme().getWorkflow() != null)
					top.getTheme().setStartWorflow(true);
				else
					top.getTheme().setStartWorflow(false);
			}

		} catch (ServiceException e) {
			logger.error("TopicBean.loadTopic: " + e.getMessage());
		}

		this.clear();

		return "topicSuccess";
	}

	public String loadTopicByTheme() {

		try {
			if (theme.getId() != null)
				topics = (List<Topic>) topicService.getTopicByTheme(theme
						.getId());
			else
				FacesUtils
						.addErrorMessage(" Erro ao carregar lista de tópicos.");

			if(topics != null) Collections.sort(topics);
			
			for (Topic top : topics) {
				if (top.isLocked()
						&& top.getLock().equals(this.getCredential()))
					top.setUserLocked(true);
				else
					top.setUserLocked(false);
				
				/*
				 * Verificando se o processo de workflow foi iniciado 
				 */
				if (top.getTheme().getWorkflow() != null)
					top.getTheme().setStartWorflow(true);
				else
					top.getTheme().setStartWorflow(false);
			}

		} catch (ServiceException e) {
			logger.error("TopicBean.loadTopicByTheme: " + e.getMessage());

		}
		this.clear();

		return "topicSuccess";
	}

	public String save() {
		if (!topic.getName().equals("")) {
			try {
				if (topic.getCreated() == null)
					topic.setCreated(new Date());

				/* Condição válida apenas para topicos novos */
				if (topic.getId() == null) {
					theme = this.getThemeById(topic.getTheme().getId());
					Discipline discipline = theme.getDiscipline();
					User user = discipline.getOwner();
					topic.setAuthor(user);
					Integer pos = topicService.getLastPostionByByTheme(theme.getId());
					if (pos == null) pos = new Integer(1);
					topic.setPosition(pos);
				}
				topicService.save(topic);
			} catch (ServiceException e) {
				logger.error("TopicBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao incluir o tópico.");
				return "Error";
			}
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}

		if (topic.getId() == null)
			this.loadTopic();
		else
			this.loadTopicByTheme();

		this.clear();
		return "Success";
	}

	private Theme getThemeById(Long id) throws ServiceException {
		return themeService.getById(id);
	}

	public String getTopicByTheme() {
		try {
			Long id = new Long(FacesUtils.getParameterValue("idTheme"));
			theme = themeService.getById(id);
			this.loadTopicByTheme();
		} catch (ServiceException e) {
			logger.error("TopicBean.getTopicByTheme: " + e.getMessage());
		}
		return "topicSuccess";
	}

	public String getTopicById(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		try {
			topic = topicService.getById(id);
		} catch (ServiceException e) {
			logger.error("TopicBean.getTopicById: " + e.getMessage());
		}
		return "Success";
	}

	public String remove() {
		if (topic != null) {

			try {

				List<Page> pages = (List<Page>) pageService
						.getPageByTopic(topic.getId());

				if (pages.size() == 0) {

					Topic entity = new Topic();
					entity = topicService.getById(topic.getId());

					topicService.delete(entity.getId());
					FacesUtils.addInfoMessage(" Tópico excluído com sucesso.");

					topics.remove(entity);
					
					// reordenar as paginas ja que uma foi excluida
					Collections.sort(topics);

					int i = 1;
					for (Topic top : topics) {
						top.setPosition(i++);
						topicService.save(top);
					}

				} else {
					FacesUtils
							.addErrorMessage(" Existe páginas associadas ao tópico "
									+ topic.getName());
				}

			} catch (ServiceException e) {
				logger.error("TopicBean.remove: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao excluir o Tópico "
						+ topic.getName());
			}
		} else {
			FacesUtils.addErrorMessage(" Não foi possível localizar o tópico.");
		}

		this.clear();

		if (theme.getId() == null)
			this.loadTopic();
		else
			this.loadTopicByTheme();

		return "Success";
	}

	public void enableTopic(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		if (topic != null) {
			try {
				topic = topicService.getById(id);
				topic.setEnabled(true);
				topicService.save(topic);
				FacesUtils.addInfoMessage(" O tópico " + topic.getName()
						+ " foi ativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("TopicBean.enableTopic: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar o tópico "
						+ topic.getName());
			}
		}
		this.loadTopic();
	}

	public void disableTopic(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		if (topic != null) {
			try {
				topic = topicService.getById(id);
				topic.setEnabled(false);
				topicService.save(topic);
				FacesUtils.addInfoMessage(" O tópico " + topic.getName()
						+ " foi desativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("TopicBean.disableTopic: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao desativar o tópico "
						+ topic.getName());
			}
		}
		this.loadTopic();
	}

	public void clear() {
		this.topic = new Topic();
		this.topic.setTheme(new Theme());
		this.topic.setAuthor(new User());
		if (theme.getId() != null) {
			this.topic.setTheme(theme);
			this.topic.setAuthor(theme.getDiscipline().getOwner());
		}
	}

	public void savePositionUp(ActionEvent event) throws Exception {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		savePosition(id, true);
	}

	public void savePositionDown(ActionEvent event) throws Exception {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		savePosition(id, false);
	}

	@SuppressWarnings("unchecked")
	private void savePosition(Long id, Boolean up) {

		// para corrigir os dados legados que ficaram
		Collections.sort(topics);

		int i = 1;
		for (Topic top : topics) {
			top.setPosition(i++);
			try {
				topicService.save(top);
			} catch (ServiceException e) {
				e.printStackTrace();
				return;
			}
		}
		
		Topic topicSelected = null;
		Topic topicTrade = null;

		for (Topic top : topics) {
			if (top.getId().equals(id)) {
				topicSelected = top;
				break;
			}
		}

		Integer pos = topicSelected.getPosition();
		if (up) {
			topicSelected.setPosition(pos - 1);
			topicTrade = topics.get(pos - 2);
			if (topicTrade != null)
				topicTrade.setPosition(pos);
		} else {
			topicSelected.setPosition(pos + 1);
			topicTrade = topics.get(pos);
			if (topicTrade != null)
				topicTrade.setPosition(pos);
		}

		try {
			if (topicSelected != null)
				topicService.save(topicSelected);
			if (topicTrade != null)
				topicService.save(topicTrade);
		} catch (ServiceException e) {
			logger.error("TopicBean.savePosition: " + e.getMessage());
			FacesUtils.addErrorMessage(" Erro ao salvar a posição dos tópicos");
		}

		Collections.sort(topics);
	}

	public Integer getPagesSize() {
		return topics.size();
	}

	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
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
	 * @return the themesItem
	 */
	public List<SelectItem> getThemesItem() {
		return themesItem;
	}

	/**
	 * @param themesItem
	 *            the themesItem to set
	 */
	public void setThemesItem(List<SelectItem> themesItem) {
		this.themesItem = themesItem;
	}

}
