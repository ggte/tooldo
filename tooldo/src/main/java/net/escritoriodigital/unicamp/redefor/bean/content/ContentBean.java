package net.escritoriodigital.unicamp.redefor.bean.content;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletResponse;

import net.escritoriodigital.components.model.file.AttachmentFile;
import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.bean.theme.PublicBean;
import net.escritoriodigital.unicamp.redefor.model.content.AlternativeQuestion;
import net.escritoriodigital.unicamp.redefor.model.content.Content;
import net.escritoriodigital.unicamp.redefor.model.content.QuestionContent;
import net.escritoriodigital.unicamp.redefor.model.content.RoadMapContent;
import net.escritoriodigital.unicamp.redefor.model.content.TypeContent;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.service.content.ContentService;
import net.escritoriodigital.unicamp.redefor.service.content.RoadMapContentService;
import net.escritoriodigital.unicamp.redefor.service.content.TypeContentService;
import net.escritoriodigital.unicamp.redefor.service.page.PageService;
import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("contentBean")
public class ContentBean extends BaseBean implements Serializable {

	private static final long serialVersionUID = 5027010804015211654L;

	private Logger logger = Logger.getLogger(ContentBean.class);

	private static final String PREVIEW_CONTENT_PAGE = "preview-content-page";
	
	private static final String TINYMCE_HEIGHT_CONTENT = "500px";
	private static final String TINYMCE_HEIGHT_QUESTION = "200px";

	@Autowired
	private ContentService contentService;

	@Autowired
	private TypeContentService typeContentService;

	@Autowired
	private PageService pageService;

	@Autowired
	private RoadMapContentService roadMapContentService;

	private Content content = new Content();
	private Page page = new Page();
	
	private QuestionContent question = new QuestionContent();
	private AlternativeQuestion alternative = new AlternativeQuestion();

	private List<SelectItem> typeContentItem = new ArrayList<SelectItem>();

	private Boolean showContent = true;

	private String contentText = "";
	
	private String tinyMCEHeight;

	private String imageTitle = PublicBean.TITLE_MATEMATICA;

	private boolean showTestResult = false;
	
	private Boolean showUploadedFile = true;
	
	/**
	 * @return the contentText
	 */
	public String getContentText() {
		return contentText;
	}

	/**
	 * @param contentText the contentText to set
	 */
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	
	/**
	 * Listener para o upload do arquivo do roadMap
	 * @param event
	 */
	public void uploadListener(FileUploadEvent event) throws Exception {

		UploadedFile item = event.getUploadedFile();

		AttachmentFile attachment = new AttachmentFile();
		attachment.setLength(Long.valueOf(item.getData().length));
		attachment.setMime(item.getContentType());
		attachment.setFile(StringUtils.formatFileNameWithFullPath(item.getName()));
		attachment.setData(item.getData());

		content.getRoadMap().setAttachment(attachment);
		loadRoadMap();
	}
	
	/**
	 * Realiza o download do arquivo anexo ao RoadMap
	 */
	public String downloadAttachment() throws Exception {
		
		if(content.getRoadMap().getAttachment() != null) {

			AttachmentFile attachment = content.getRoadMap().getAttachment();

			HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

			res.setContentType(attachment.getMime());

			res.setHeader("Content-disposition", "attachment;filename=" + attachment.getFile());
			res.getOutputStream().write(attachment.getData());
			res.getCharacterEncoding();

			FacesContext.getCurrentInstance().responseComplete();
		}

		return "success";
	}
	
	/**
	 * Remove o arquivo anexado ao roadMap
	 * @throws Exception
	 */
	public void removeFile() throws Exception {
		content.getRoadMap().setAttachment(null);
		loadRoadMap();
	}
	
	/**
	 * Carrega o modal do roadMap, atualizando e buscando da base de dados
	 */
	public void loadRoadMap() throws Exception {
		if(content.getRoadMap() == null)
			content.setRoadMap(new RoadMapContent());

		if(content.getRoadMap().getAttachment() != null) {
			System.out.println("content.getRoadMap().getAttachment() != null");
			if(content.getRoadMap().getAttachment().getFile() != null)
				showUploadedFile = true;
			else
				showUploadedFile = false;
		} else
			showUploadedFile = false;
	}

	/**
	 * Carrega o content para edição a partir da lista de páginas
	 */
	public String getContentByPage() throws Exception {
		Long id = new Long(FacesUtils.getParameterValue("idPage"));
		showTestResult = false;

		try {
			page = pageService.getById(id);
			
			content = page.getContent();
			
			if (content == null) clear();

			if(page.isLocked() && page.getLock().equals(this.getCredential())) page.setUserLocked(true);
			else page.setUserLocked(false);

			if(new Long(4).equals(content.getTypeContent().getId())) {
				this.showContent = false;
				this.tinyMCEHeight = TINYMCE_HEIGHT_QUESTION;
			} else { 
				this.showContent = true;
				this.tinyMCEHeight = TINYMCE_HEIGHT_CONTENT;
			}

			if(content.getRoadMap() != null) {
				content.setRoadMap(roadMapContentService.getById(content.getRoadMap().getId()));
			} else {
				content.setRoadMap(new RoadMapContent());
			}
		} catch (ServiceException e) {
			logger.error("ContentBean.getContentByPage: " + e.getMessage());
		}
		
		this.loadRoadMap();
		this.loadContentType();
		return "contentSuccess";
	}

	public String loadContentType() {
		/*
		typeContentItem.clear();
		typeContentItem.add(new SelectItem(new Long(3), "Modelo para livre formatação"));
		typeContentItem.add(new SelectItem(new Long(4), "Modelo de Testes"));
		*/
		
		try {
			List<TypeContent> itens = (List<TypeContent>) typeContentService
					.findAll();
			Iterator<TypeContent> it = itens.iterator();
			typeContentItem.clear();
			while (it.hasNext()) {
				TypeContent obj = it.next();
				typeContentItem.add(new SelectItem(obj.getId(), obj.getName()));
			}
		} catch (ServiceException e) {
			logger.error("ContentBean.loadContentType: " + e.getMessage());
		}
		
		return "Success";
	}

	/**
	 * Salva o content na base, o roadMap é atribuido no método roadMapSave
	 * @return
	 */
	public String save() throws Exception {

		if (content.getContent().equals("") && !content.getTypeContent().getId().equals(new Long(4))) {
			FacesUtils.addErrorMessage(" Por favor preencha o conteúdo.");
			return "Error";
		}

		if (content.getQuestions().size() == 0 && content.getTypeContent().getId().equals(new Long(4))) {
			FacesUtils.addErrorMessage(" Por favor cadastre pelo menos uma questão com duas alternativas.");
			return "Error";
		}

		if (content.getCreated() == null)
			content.setCreated(new Date());
		try {

			if(content.getTypeContent().getId().equals(new Long(0))) {
				FacesUtils.addErrorMessage(" Você deve selecionar o tipo de conteúdo.");
				return "Error";
			}


			TypeContent typeContent = typeContentService.getById(content
					.getTypeContent().getId());
			content.setPage(page);
			content.setTypeContent(typeContent);
			contentService.save(content);
		} catch (ServiceException e) {
			logger.error("ContentBean.save: " + e.getMessage());
			FacesUtils.addErrorMessage(" Erro ao salvar o conteúdo.");
			return "Error";
		}

		return "Success";
	}
	

	public void tipoConteudoValueChanged(ValueChangeEvent event) {
		
		PhaseId phaseId = event.getPhaseId();
		if (phaseId.equals(PhaseId.ANY_PHASE)) {
			// agenda o evento para a fase que nos interessa
			event.setPhaseId(PhaseId.UPDATE_MODEL_VALUES);
			event.queue();
		} else if (phaseId.equals(PhaseId.UPDATE_MODEL_VALUES)) {
			if (content.getTypeContent().getId() != 0) {
				
				this.tinyMCEHeight = TINYMCE_HEIGHT_CONTENT;
				String conteudo = "";
				String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
				
				if(content.getTypeContent().getId().equals(new Long(1))) {
					conteudo = "<img src='" + path + "/images/secure/blank.jpg' width='250' height='250' style='float: left; padding: 0 10px 10px 0' /> " +
							"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget leo in nisi commodo malesuada vitae sit amet diam. " +
							"Maecenas nunc ipsum; molestie sed sagittis non, dictum sit amet lectus. Donec eu dui massa, a gravida risus.";
					
				} else if (content.getTypeContent().getId().equals(new Long(2))) {
					conteudo = "<img src='" + path + "/images/secure/blank.jpg' width='250' height='250' style='float: right; padding: 0 0 10px 10px' /> " +
							"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras eget leo in nisi commodo malesuada vitae sit amet diam. " +
							"Maecenas nunc ipsum; molestie sed sagittis non, dictum sit amet lectus. Donec eu dui massa, a gravida risus.";	
				} else if (content.getTypeContent().getId().equals(new Long(4))) {
					this.tinyMCEHeight = TINYMCE_HEIGHT_QUESTION;;
				}
				
				content.setContent(conteudo);
			}
		}
		
	}

	public void clear() {
		
		this.tinyMCEHeight = TINYMCE_HEIGHT_CONTENT;
		this.question = new QuestionContent();
		this.alternative = new AlternativeQuestion();
		
		this.content = new Content();
		this.content.setPage(new Page());
		this.content.setTypeContent(new TypeContent());
		this.content.setRoadMap(new RoadMapContent());
		this.content.setQuestionsContent(new ArrayList<QuestionContent>());
		if (page.getId() != null)
			this.content.setPage(page);
	}

	/**
	 * Método usado para visualizar um conteúdo antes de salvá-lo
	 * deve ser chamado por um link com target=_blank no xhtml
	 * @return
	 * @throws Exception
	 */
	public String preview() throws Exception {
		return PREVIEW_CONTENT_PAGE;
	}

	/**
	 * @return true caso o usuário possa alterar o conteúdo
	 */
	public Boolean getCanUserSavePage() {
		//if(content.getPage().isLocked())
		return false;
	}
	
	public void addQuestion() {
		question = new QuestionContent();
		content.addQuestion(question);
	}
	
	public void removeQuestion() {
		content.removeQuestion(question);
	}
	
	public void addAlternative() {
		alternative = new AlternativeQuestion();
		question.addAlternative(alternative);
	}
	
	public void removeAlternative() {
		
		System.out.print("questao = ");
		System.out.println(question.getQuestion());
		
		System.out.print("removendo alternativa = ");
		System.out.println(alternative.getAlternative());
		
		question.removeAlternative(alternative);
	}
	
	public Boolean getShowContent() {
		return showContent;
	}
	
	public void setShowContent(Boolean showContent) {
		this.showContent = showContent;
	}
	
	public Content getContent() {
		return content;
	}
	
	public void setContent(Content content) {
		this.content = content;
	}
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(Page page) {
		this.page = page;
	}
	
	public List<SelectItem> getTypeContentItem() {
		return typeContentItem;
	}
	
	public void setTypeContentItem(List<SelectItem> typeContentItem) {
		this.typeContentItem = typeContentItem;
	}
	
	public boolean isShowTestResult() {
		return showTestResult;
	}
	
	public void setShowTestResult(boolean showTestResult) {
		this.showTestResult = showTestResult;
	}
	
	public QuestionContent getQuestion() {
		return question;
	}
	
	public void setQuestion(QuestionContent question) {
		this.question = question;
	}


	public AlternativeQuestion getAlternative() {
		return alternative;
	}


	public void setAlternative(AlternativeQuestion alternative) {
		this.alternative = alternative;
	}


	public String getTinyMCEHeight() {
		return tinyMCEHeight;
	}


	public void setTinyMCEHeight(String tinyMCEHeight) {
		this.tinyMCEHeight = tinyMCEHeight;
	}

	public Boolean getShowUploadedFile() {
		return showUploadedFile;
	}


	public void setShowUploadedFile(Boolean showUploadedFile) {
		this.showUploadedFile = showUploadedFile;
	}

	/**
	 * @return the imageTitle
	 */
	public String getImageTitle() {
		if(page.getTopic().getTheme().getDiscipline().getCourse().getId().equals(new Long(1))) {
			// Educação Física
			imageTitle = PublicBean.TITLE_EDUCACAO_FISICA;
		} else if(page.getTopic().getTheme().getDiscipline().getCourse().getId().equals(new Long(2))) {
			// Física
			imageTitle = PublicBean.TITLE_FISICA;
		} else if(page.getTopic().getTheme().getDiscipline().getCourse().getId().equals(new Long(3))) {
			// História
			imageTitle = PublicBean.TITLE_HISTORIA;
		} else if(page.getTopic().getTheme().getDiscipline().getCourse().getId().equals(new Long(4))) {
			// Língua Portuguesa
			imageTitle = PublicBean.TITLE_PORTUGUES;
		} else if(page.getTopic().getTheme().getDiscipline().getCourse().getId().equals(new Long(5))) {
			// Matemática
			imageTitle = PublicBean.TITLE_MATEMATICA;
		} else {
			imageTitle = PublicBean.TITLE_MATEMATICA;
		}
		return imageTitle;
	}

	/**
	 * @param imageTitle the imageTitle to set
	 */
	public void setImageTitle(String imageTitle) {
		this.imageTitle = imageTitle;
	}

}
