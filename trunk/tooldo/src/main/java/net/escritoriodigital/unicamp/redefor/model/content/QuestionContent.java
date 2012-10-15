/**
 *
 */
package net.escritoriodigital.unicamp.redefor.model.content;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.components.model.file.AttachmentFile;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Content
 */
@Entity
@Table (name="RFR_QUESTION_CONTENT")
public class QuestionContent extends ModelBase implements Cloneable {

	private static final long serialVersionUID = -1646061917770126642L;

	private String question;

	@Embedded
	private AttachmentFile image;

	@ManyToOne
	@JoinColumn (name="FK_RFR_CONTENT")
	private Content content;

	//Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy="question", fetch=FetchType.EAGER, orphanRemoval=true)
	private List<AlternativeQuestion> alternatives;

	@Transient
	private String alternative;

	@Transient
	private String alternativeComments;

	@Transient
	private boolean correct = false;

	@Transient
	private List<SelectItem> alternativeItens = new ArrayList<SelectItem>();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		QuestionContent clone = (QuestionContent) super.clone();
		
		clone.setId(null);
		clone.setContent(null);
		clone.setAlternatives(null);
		clone.setAlternativeItens(new ArrayList<SelectItem>());
		
		if(alternatives != null) {
			for (AlternativeQuestion a : alternatives)
				clone.addAlternative((AlternativeQuestion) a.clone());
		}
		
		return clone;
	}

	public String getAlternative() {
		return alternative;
	}

	public void setAlternative(String alternative) {
		this.alternative = alternative;
	}

	/**
	 * Retorna uma lista de SelectItem para ser usado em combos, checks, etc.
	 * @return
	 */
	public List<SelectItem> getAlternativeItens() {

		Iterator<AlternativeQuestion> it = alternatives.iterator();
		alternativeItens.clear();
		while(it.hasNext()){
			AlternativeQuestion item = it.next();
			alternativeItens.add(new SelectItem(item.getAlternative()));
		}

		return alternativeItens;
	}

	public void setAlternativeItens(List<SelectItem> alternativeItens) {
		this.alternativeItens = alternativeItens;
	}

	public void addAlternative(AlternativeQuestion item) {
		if (item == null)
			throw new IllegalArgumentException("alternativeQuestion is null!");

		if (alternatives == null)
			setAlternatives(new ArrayList<AlternativeQuestion>());
		
		item.setQuestion(this);
		alternatives.add(item);
	}

	public void removeAlternative(AlternativeQuestion item) {
		if (item == null)
			throw new IllegalArgumentException("alternativeQuestion is null!");

		if (alternatives == null)
			setAlternatives(new ArrayList<AlternativeQuestion>());
		else {
			item.setQuestion(null);
			alternatives.remove(item);
		}
			
	}
	
	
	public List<AlternativeQuestion> getAlternatives() {
		return alternatives;
	}
	
	public void setAlternatives(List<AlternativeQuestion> alternatives) {
		this.alternatives = alternatives;
	}
	
	public Content getContent() {
		return content;
	}
	
	public void setContent(Content content) {
		this.content = content;
	}
	
	public String getQuestion() {
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	public AttachmentFile getImage() {
		return image;
	}
	
	public void setImage(AttachmentFile image) {
		this.image = image;
	}
	
	public String getAlternativeComments() {
		return alternativeComments;
	}
	
	public void setAlternativeComments(String alternativeComments) {
		this.alternativeComments = alternativeComments;
	}
	
	public boolean isCorrect() {
		return correct;
	}
	
	public void setCorrect(boolean correct) {
		this.correct = correct;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((question == null) ? 0 : question.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuestionContent other = (QuestionContent) obj;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuestionContent [question=" + question + "]";
	}

	/**
	 * Método usado para escrever no Outputstream os bytes da imagem da questão,
	 * o componente a4j:mediaOutput é usado para renderizar a imagem no response
	 * @param out
	 * @param data
	 * @throws IOException
	 */
	public void paintImage(OutputStream out, Object data) throws IOException{
        if(this.getImage() != null) {
        	if(this.getImage().getData() != null)
        		out.write(this.getImage().getData());
        }
    }

}
