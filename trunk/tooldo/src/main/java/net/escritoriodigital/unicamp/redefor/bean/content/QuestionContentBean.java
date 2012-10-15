package net.escritoriodigital.unicamp.redefor.bean.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.escritoriodigital.components.model.file.AttachmentFile;
import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.theme.PublicBean;
import net.escritoriodigital.unicamp.redefor.model.content.AlternativeQuestion;
import net.escritoriodigital.unicamp.redefor.model.content.QuestionContent;
import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.richfaces.component.UICommandButton;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("questionContentBean")
public class QuestionContentBean {

	private QuestionContent question = new QuestionContent();

	private AlternativeQuestion alternative = new AlternativeQuestion();

	@Autowired
	private ContentBean contentBean;

	@Autowired
	private PublicBean publicBean;

	private boolean editQuestion = false;

	private boolean editAlternative = false;

	/**
	 * Listener para o upload da imagem da questão, grava o arquivo na base de dados
	 */
	public void uploadListener(FileUploadEvent event) throws Exception {
		UploadedFile item = event.getUploadedFile();

		AttachmentFile attachment = new AttachmentFile();
		attachment.setLength(Long.valueOf(item.getData().length));
		attachment.setMime(item.getContentType());
		attachment.setFile(StringUtils.formatFileNameWithFullPath(item.getName()));
		attachment.setData(item.getData());

		question.setImage(attachment);
	}

	/**
	 * Action para remover a imagem da questão
	 */
	public void removeImage(ActionEvent event) throws Exception {
		question.setImage(null);
	}

	/**
	 * Método usado para escrever no Outputstream os bytes da imagem da questão corrente,
	 * o componente a4j:mediaOutput é usado para renderizar a imagem no response
	 */
	public void paintImage(OutputStream out, Object data) throws IOException{
		if(question.getImage() != null) {
        	if(question.getImage().getData() != null)
        		out.write(question.getImage().getData());
        }
    }

	/**
	 * Método usado para escrever no Outputstream os bytes da imagem da questão
	 * enviada pelo parâmetro data (usado nas listas de questões) e usando o ContentBean
	 * da sessão
	 */
	public void paintImageDynamic(OutputStream out, Object data) throws IOException {
		if(data instanceof String) {
			String filename = (String) data;

			if(contentBean.getContent().getQuestions() != null) {
				List<QuestionContent> questions = contentBean.getContent().getQuestions();
				paintImageToOutput(out, questions, filename);
			}
		}
    }

	/**
	 * Método usado para escrever no Outputstream os bytes da imagem da questão
	 * enviada pelo parâmetro data (usado nas listas de questões) e usando o PublicBean
	 * da sessão
	 */
	public void paintImageDynamicFromPublic(OutputStream out, Object data) throws IOException {
		if(data instanceof String) {
			String filename = (String) data;

			if(publicBean.getPage().getContent().getQuestions() != null) {
				List<QuestionContent> questions = publicBean.getPage().getContent().getQuestions();
				paintImageToOutput(out, questions, filename);
			}
		}
    }

	/**
	 * Método de apoio que recebe o outputstream e a lista de questões para renderizar
	 * a imagem das questões
	 */
	private void paintImageToOutput(OutputStream out, List<QuestionContent> questions, String filename) throws IOException {
		if(!questions.isEmpty()) {

			QuestionContent quest = null;

			for (QuestionContent q : questions) {
				if(q.getImage() != null) {
					if(q.getImage().getFile() != null) {
						if(q.getImage().getFile().equals(filename))
							quest = q;
					}
				}
			}

			if(quest != null) {
				if(quest.getImage() != null) {
		        	if(quest.getImage().getData() != null)
		        		out.write(quest.getImage().getData());
		        }
			}
		}
	}

	/**
	 * Executa o teste retornando o resultado ao usuário do que está
	 * certo e do que está errado
	 */
	public void executeTest() throws Exception {
		List<QuestionContent> list = contentBean.getContent().getQuestions();
		proccessTest(list);
		contentBean.setShowTestResult(true);
	}

	/**
	 * Reinicia o modelo de testes para uma nova aplicação
	 */
	public void restartTest() throws Exception {
		List<QuestionContent> list = contentBean.getContent().getQuestions();
		for (QuestionContent q : list) q.setCorrect(false);
		contentBean.setShowTestResult(false);
	}

	/**
	 * Executa o teste retornando o resultado ao usuário do que está
	 * certo e do que está errado a partir do PublicBean
	 */
	public void executeTestFromPublic() throws Exception {
		List<QuestionContent> list = publicBean.getPage().getContent().getQuestions();
		proccessTest(list);
		publicBean.setShowTestResult(true);
	}
	
	private void proccessTest(List<QuestionContent> list) {
		for (QuestionContent q : list) {
			for (AlternativeQuestion alt : q.getAlternatives()) {
				if(q.getAlternative().equals(alt.getAlternative())) {
					q.setAlternativeComments(alt.getComments());
					if(alt.getCorrect()) {
						q.setCorrect(true);
						break;
					}
				}
			}
		}
	}

	/**
	 * Reinicia o modelo de testes para uma nova aplicação
	 */
	public void restartTestFromPublic() throws Exception {
		List<QuestionContent> list = publicBean.getPage().getContent().getQuestions();
		for (QuestionContent q : list) q.setCorrect(false);
		publicBean.setShowTestResult(false);
	}


	public void clear() {
		question = new QuestionContent();
		question.setAlternatives(new ArrayList<AlternativeQuestion>());
		alternative = new AlternativeQuestion();
	}

	public void addQuestion() {
		if (question.getQuestion() == null || question.getQuestion().isEmpty()) {
			FacesUtils.addErrorMessage("Questão é obrigatório.");
			return;
		}
		if (question.getAlternatives() == null
				|| question.getAlternatives().isEmpty()) {
			FacesUtils.addErrorMessage("Alternativas é obrigatório.");
			return;
		}
		if (!contentBean.getContent().getQuestions().contains(question)) {
			question.setContent(contentBean.getContent());
			contentBean.getContent().addQuestion(question);
		} else {
			if (this.editQuestion == false)
				FacesUtils.addErrorMessage("Questão já existe.");
			else
				this.editQuestion = false;
		}

		clear();
	}

	public void addAlternative() {
		if (alternative.getAlternative() == null
				|| alternative.getAlternative().isEmpty()) {
			FacesUtils.addErrorMessage("Alternativa é obrigatório.");
			return;
		}
		if (alternative.getCorrect() == null) {
			FacesUtils.addErrorMessage("Correta é obrigatório.");
			return;
		}
		if (!question.getAlternatives().contains(alternative)) {
			alternative.setQuestion(question);
			question.addAlternative(alternative);
		} else {
			if (this.editAlternative == false)
				FacesUtils.addErrorMessage("Alternativa já existe.");
			else
				this.editAlternative = false;
		}

		alternative = new AlternativeQuestion();
	}

	public void removeQuestion(ActionEvent event) throws ServiceException {

		UICommandButton command = (UICommandButton) event
				.getComponent();
		QuestionContent item = (QuestionContent) command.getData();
		contentBean.getContent().removeQuestion(item);
	}

	public void editQuestion(ActionEvent event) throws ServiceException {

		UICommandButton command = (UICommandButton) event
				.getComponent();
		Integer index = (Integer) command.getData();

		this.question = contentBean.getContent().getQuestions().get(index);
		this.editQuestion = true;

		// if(!question.getId().equals(null) && !question.getId().equals(new
		// Long(0))) this.question =
		// questionService.getById(this.question.getId());

		if (question == null)
			clear();
	}

	public void editAlternative(ActionEvent event) throws ServiceException {

		UICommandButton command = (UICommandButton) event
				.getComponent();
		Integer index = (Integer) command.getData();

		this.alternative = this.question.getAlternatives().get(index);
		this.editAlternative = true;

		if (alternative == null)
			alternative = new AlternativeQuestion();
	}

	public void removeAlternative(ActionEvent event) throws ServiceException {

		UICommandButton command = (UICommandButton) event
				.getComponent();
		AlternativeQuestion item = (AlternativeQuestion) command.getData();
		question.removeAlternative(item);

		System.out.println("Hello!");
	}

	/**
	 * @return the alternative
	 */
	public AlternativeQuestion getAlternative() {
		return alternative;
	}

	/**
	 * @param alternative
	 *            the alternative to set
	 */
	public void setAlternative(AlternativeQuestion alternative) {
		this.alternative = alternative;
	}

	/**
	 * @return the question
	 */
	public QuestionContent getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(QuestionContent question) {
		this.question = question;
	}
}
