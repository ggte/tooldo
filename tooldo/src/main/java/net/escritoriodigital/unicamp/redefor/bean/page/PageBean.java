package net.escritoriodigital.unicamp.redefor.bean.page;

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
@Qualifier("pageBean")
public class PageBean extends BaseBean {

	private Logger logger = Logger.getLogger(PageBean.class);

	@Autowired
	private TopicService topicService;
	@Autowired
	private PageService pageService;

	private Topic topic = new Topic();
	private Page page = new Page();

	private List<Page> pages = new ArrayList<Page>();
	private List<SelectItem> topicsItem = new ArrayList<SelectItem>();

	@SuppressWarnings("unchecked")
	public String loadPage() {

		try {
			pages = (List<Page>) pageService.findAll();

			for (Page page : pages) {
				if (page.isLocked()
						&& page.getLock().equals(this.getCredential()))
					page.setUserLocked(true);
				else
					page.setUserLocked(false);

				if (page.getTopic().getTheme().getWorkflow() != null)
					page.getTopic().getTheme().setStartWorflow(true);
				else
					page.getTopic().getTheme().setStartWorflow(false);
			}

			Collections.sort(pages);
		} catch (ServiceException e) {
			logger.error("PageBean.loadPage: " + e.getMessage());
		}

		this.clear();

		return "pageSuccess";
	}

	@SuppressWarnings("unchecked")
	public String loadPageByTopic() {

		try {
			if (topic.getId() != null) {
				pages = (List<Page>) pageService.getPageByTopic(topic.getId());

				for (Page page : pages) {
					if (page.isLocked()
							&& page.getLock().equals(this.getCredential()))
						page.setUserLocked(true);
					else
						page.setUserLocked(false);
					
					if (page.getTopic().getTheme().getWorkflow() != null)
						page.getTopic().getTheme().setStartWorflow(true);
					else
						page.getTopic().getTheme().setStartWorflow(false);
				}

				Collections.sort(pages);
			} else
				FacesUtils
						.addErrorMessage(" Erro ao carregar lista de páginas.");

		} catch (ServiceException e) {
			logger.error("PageBean.loadPageByTopic: " + e.getMessage());

		}
		this.clear();

		return "pageSuccess";
	}

	public String save() {
		if (!page.getName().equals("")) {
			try {
				if (page.getCreated() == null)
					page.setCreated(new Date());

				/* Condição válida apenas para páginas novas */
				if (page.getId() == null) {
					topic = this.getTopicById(page.getTopic().getId());
					Theme theme = topic.getTheme();
					Discipline discipline = theme.getDiscipline();
					User user = discipline.getOwner();
					page.setAuthor(user);
					Integer position = pageService.getLastPostionByTopic(topic
							.getId());
					if (position == null)
						position = new Integer(1);
					page.setPosition(position);
					
					// se o tópico estiver locado, página tmb ficará
					if(topic.getLock() != null) {
						page.setLock(topic.getLock());
					}
				}

				pageService.save(page);
			} catch (ServiceException e) {
				logger.error("PageBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao salvar a página.");
				return "Error";
			}
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}

		if (topic.getId() == null)
			this.loadPage();
		else
			this.loadPageByTopic();

		this.clear();

		return "Success";
	}

	private Topic getTopicById(Long id) throws ServiceException {
		return topicService.getById(id);
	}

	public String getPageByTopic() {
		try {
			Long id = new Long(FacesUtils.getParameterValue("idTopic"));
			topic = getTopicById(id);
			this.loadPageByTopic();
		} catch (ServiceException e) {
			logger.error("PageBean.getPageByTopic: " + e.getMessage());
		}
		return "pageSuccess";
	}

	public String getPageById(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		try {
			page = pageService.getById(id);
		} catch (ServiceException e) {
			logger.error("PageBean.getPageById: " + e.getMessage());
		}
		return "Success";
	}

	public String remove() {
		if (page != null) {

			try {
				pageService.delete(page.getId());
				FacesUtils.addInfoMessage(" Página excluída com sucesso.");

				pages.remove(page);

				// reordenar as paginas ja que uma foi excluida
				Collections.sort(pages);

				int i = 1;
				for (Page pg : pages) {
					pg.setPosition(i++);
					pageService.save(pg);
				}

			} catch (ServiceException e) {
				logger.error("PageBean.remove: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao excluir a Página "
						+ page.getName());
			}
		} else {
			FacesUtils.addErrorMessage(" Não foi possível localizar a página.");
		}

		this.clear();

		if (topic.getId() == null)
			this.loadPage();
		else
			this.loadPageByTopic();

		return "Success";
	}

	public void enablePage(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		if (page != null) {
			try {
				page = pageService.getById(id);
				page.setEnabled(true);
				pageService.save(page);
				FacesUtils.addInfoMessage(" A página " + page.getName()
						+ " foi ativada com sucesso.");
			} catch (ServiceException e) {
				logger.error("PageBean.enablePage: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar a página "
						+ page.getName());
			}
		}
		this.loadPageByTopic();
	}

	public void disablePage(ActionEvent event) {
		UICommandButton command = (UICommandButton) event
				.getComponent();
		Long id = (Long) command.getData();
		if (page != null) {
			try {
				page = pageService.getById(id);
				page.setEnabled(false);
				pageService.save(page);
				FacesUtils.addInfoMessage(" A página " + page.getName()
						+ " foi desativada com sucesso.");
			} catch (ServiceException e) {
				logger.error("PageBean.disablePage: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao desativar a página "
						+ page.getName());
			}
		}
		this.loadPageByTopic();
	}

	public void clear() {
		this.page = new Page();
		this.page.setTopic(new Topic());
		this.page.setAuthor(new User());
		if (topic.getId() != null)
			this.page.setTopic(topic);
		if (topic.getId() != null)
			this.page.setAuthor(topic.getTheme().getDiscipline().getOwner());
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

		Page pageSelected = null;
		Page pageTrade = null;
		for (Page pag : pages) {
			if (pag.getId().equals(id)) {
				pageSelected = pag;
				break;
			}
		}

		Integer pos = pageSelected.getPosition();
		if (up) {
			pageSelected.setPosition(pos - 1);
			pageTrade = pages.get(pos - 2);
			if (pageTrade != null)
				pageTrade.setPosition(pos);
		} else {
			pageSelected.setPosition(pos + 1);
			pageTrade = pages.get(pos);
			if (pageTrade != null)
				pageTrade.setPosition(pos);
		}

		try {
			if (pageSelected != null)
				pageService.save(pageSelected);
			if (pageTrade != null)
				pageService.save(pageTrade);
		} catch (ServiceException e) {
			logger.error("PageBean.savePosition: " + e.getMessage());
			FacesUtils.addErrorMessage(" Erro ao salvar a posição das páginas");
		}

		Collections.sort(pages);
	}

	public Integer getPagesSize() {
		return pages.size();
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
	 * @return the pages
	 */
	public List<Page> getPages() {
		return pages;
	}

	/**
	 * @param pages
	 *            the pages to set
	 */
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}

	/**
	 * @return the topicsItem
	 */
	public List<SelectItem> getTopicsItem() {
		return topicsItem;
	}

	/**
	 * @param topicsItem
	 *            the topicsItem to set
	 */
	public void setTopicsItem(List<SelectItem> topicsItem) {
		this.topicsItem = topicsItem;
	}

}
