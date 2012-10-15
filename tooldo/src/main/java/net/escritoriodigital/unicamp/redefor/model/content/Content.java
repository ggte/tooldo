/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.model.content;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.page.Page;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Content
 */
@Entity
@Table (name="RFR_CONTENT")
public class Content extends ModelBase implements Cloneable {

	private static final long serialVersionUID = -4777223832996094516L;
		
	private String content;
	private Date created;
	
	@OneToOne
	@JoinColumn (name="FK_RFR_PAGE")
	private Page page;
	
	@ManyToOne
	@JoinColumn (name="FK_RFR_TYPE_CONTENT")
	private TypeContent typeContent;
	
	@OneToOne (cascade = CascadeType.ALL, orphanRemoval=true)
	@JoinColumn (name="FK_RFR_ROADMAP_CONTENT")
	private RoadMapContent roadMap;
	
	@OneToMany(mappedBy = "content", cascade = { CascadeType.ALL }, orphanRemoval=true)
	private List<QuestionContent> questions;
		
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Content clone = (Content) super.clone();
		
		clone.setId(null);
		clone.setCreated(new Date());
		clone.setRoadMap(null);
		clone.setPage(null);
		clone.setQuestions(null);
		
		if(questions != null) {
			for (QuestionContent question : questions)
				clone.addQuestion((QuestionContent) question.clone());
		}
		
		return clone;
	}

	public void addQuestion(QuestionContent item) {
		if (item == null)
			throw new IllegalArgumentException("question is null!");

		if (questions == null)
			setQuestionsContent(new ArrayList<QuestionContent>());

		item.setContent(this);
		questions.add(item);
	}

	public void removeQuestion(QuestionContent item) {
		if (item == null)
			throw new IllegalArgumentException("question is null!");

		if (questions == null)
			setQuestionsContent(new ArrayList<QuestionContent>());
		else {
			item.setContent(null);
			questions.remove(item);
		}
	}
	
	
	/**
	 * @return the questionsContent
	 */
	public List<QuestionContent> getQuestions() {
		return questions;
	}
	/**
	 * @param questionsContent the questionsContent to set
	 */
	public void setQuestionsContent(List<QuestionContent> questions) {
		this.questions = questions;
	}
	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}
	/**
	 * @param page the page to set
	 */
	public void setPage(Page page) {
		this.page = page;
	}
	/**
	 * @return the typeContent
	 */
	public TypeContent getTypeContent() {
		return typeContent;
	}
	/**
	 * @param typeContent the typeContent to set
	 */
	public void setTypeContent(TypeContent typeContent) {
		this.typeContent = typeContent;
	}
	/**
	 * @return the roadMap
	 */
	public RoadMapContent getRoadMap() {
		return roadMap;
	}
	/**
	 * @param roadMap the roadMap to set
	 */
	public void setRoadMap(RoadMapContent roadMap) {
		this.roadMap = roadMap;
	}
	
	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	
	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}
	
	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @param questions the questions to set
	 */
	public void setQuestions(List<QuestionContent> questions) {
		this.questions = questions;
	}
	
	/**
	 * @return the questionsSize
	 */
	public int getQuestionsSize() {
		return (questions != null) ? questions.size() : 0;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((page == null) ? 0 : page.hashCode());
		result = prime * result
				+ ((typeContent == null) ? 0 : typeContent.hashCode());
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
		Content other = (Content) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (page == null) {
			if (other.page != null)
				return false;
		} else if (!page.equals(other.page))
			return false;
		if (typeContent == null) {
			if (other.typeContent != null)
				return false;
		} else if (!typeContent.equals(other.typeContent))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Content [content=" + content + ", created=" + created
				+ ", page=" + page + ", typeContent=" + typeContent + "]";
	}
	
	/**
	 * @return hasRoadMap - true se possui algo no roadMap
	 */
	public Boolean getHasRoadMap() {
		if(roadMap == null) return false;
		
		boolean emptyDescription = false;
		boolean emptyAttachment = false;
		
		if(roadMap.getRoadmap() != null) {
			if(roadMap.getRoadmap().trim().equals("")) emptyDescription = true;
		} else emptyDescription = true;
		
		if(roadMap.getAttachment() != null) {
			if(roadMap.getAttachment().getFile() == null) emptyAttachment = true;
		} else emptyAttachment = true;
		
		return !(emptyAttachment && emptyDescription);
	}
}
