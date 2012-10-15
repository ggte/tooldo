/**
 *
 */
package net.escritoriodigital.unicamp.redefor.model.content;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Content
 */
@Entity
@Table (name="RFR_ALTERNATIVE_QUESTION")
public class AlternativeQuestion extends ModelBase implements Cloneable {

	private static final long serialVersionUID = 5236964085037148339L;

	private String alternative;

	private String comments;

	private Boolean correct;

	@ManyToOne
	@JoinColumn (name="FK_RFR_QUESTION_CONTENT")
	private QuestionContent question;

	public AlternativeQuestion() {
		super();
	}

	// TODO: verificar por que é usado
	public AlternativeQuestion(Long id, String alternative, Boolean correct) {
		super();
		this.id = id;
		this.alternative = alternative;
		this.correct = correct;
	}

	public AlternativeQuestion(Long id, String alternative, Boolean correct, String comments) {
		super();
		this.id = id;
		this.alternative = alternative;
		this.correct = correct;
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		AlternativeQuestion clone = (AlternativeQuestion) super.clone();
		
		clone.setId(null);
		clone.setQuestion(null);
		
		return clone;
	}

	/**
	 * @return the question
	 */
	public QuestionContent getQuestion() {
		return question;
	}
	/**
	 * @param question the question to set
	 */
	public void setQuestion(QuestionContent question) {
		this.question = question;
	}
	/**
	 * @return the alternative
	 */
	public String getAlternative() {
		return alternative;
	}
	/**
	 * @param alternative the alternative to set
	 */
	public void setAlternative(String alternative) {
		this.alternative = alternative;
	}
	/**
	 * @return the correct
	 */
	public Boolean getCorrect() {
		return correct;
	}
	/**
	 * @param correct the correct to set
	 */
	public void setCorrect(Boolean correct) {
		this.correct = correct;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((alternative == null) ? 0 : alternative.hashCode());
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((correct == null) ? 0 : correct.hashCode());
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
		AlternativeQuestion other = (AlternativeQuestion) obj;
		if (alternative == null) {
			if (other.alternative != null)
				return false;
		} else if (!alternative.equals(other.alternative))
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (correct == null) {
			if (other.correct != null)
				return false;
		} else if (!correct.equals(other.correct))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AlternativeQuestion [alternative=" + alternative
				+ ", comments=" + comments + ", correct=" + correct + ", id="
				+ id + "]";
	}



}
