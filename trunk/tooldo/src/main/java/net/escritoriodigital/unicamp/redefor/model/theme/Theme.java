package net.escritoriodigital.unicamp.redefor.model.theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;

import org.hibernate.annotations.Where;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Theme
 */
@Entity
@Table (name="RFR_THEME")
public class Theme extends ModelBase implements Cloneable {

	private static final long serialVersionUID = -5730572728365794786L;
	
	public static final String SALT_VALUE = "UN1F0R_";
	public static final String PATH_PAGE_THEME = "/pages/public/main.jsf?hash=";

	private String name;

	private String description;
	
	private String cssFile;

	@Temporal(TemporalType.DATE)
	private Date created = new Date();

	private Boolean enabled = true;

	private Boolean published = false;
	
	private Integer publishCode;
	
	private Long replaceId;
	
	private Long duplicateId;

	@ManyToOne
	@JoinColumn (name="FK_RFR_DISCIPLINE")
	private Discipline discipline;

	@OneToMany(mappedBy = "theme", cascade= CascadeType.ALL)
	private List<Topic> topics;

	@OneToMany(mappedBy = "theme", cascade= CascadeType.ALL)
	@Where (clause = "enabled=1")
	private List<Topic> topicsEnabled;

	@ManyToOne
	@JoinColumn (name="FK_RFR_USER")
	private User author;

	@OneToOne
	@JoinColumn (name="FK_RFR_USER_LOCK")
	private User lock;

	@Transient
	private boolean locked;

	@Transient
	private String paramHash;

	@Transient
	private boolean userLocked;

	@OneToOne (cascade= CascadeType.ALL)
	@JoinColumn (name="FK_RFR_WORKFLOW_ENTITY")
	private WorkflowEntity workflow;

	@Transient
	private boolean startWorflow;

	@Transient
	private boolean readjust;

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Theme clone = (Theme) super.clone();
		
		clone.setId(null);
		clone.setPublished(false);
		clone.setReplaceId(this.getId());
		clone.setDuplicateId(null);
		clone.setPublishCode(null);
		clone.setCreated(new Date());
		clone.setEnabled(true);
		clone.setLock(null);
		clone.setLocked(false);
		clone.setUserLocked(false);
		clone.setStartWorflow(false);
		clone.setReadjust(false);
		clone.setWorkflow(null);
		clone.setTopics(null);
		clone.setTopicsEnabled(null);
		
		if(topics != null) {
			clone.setTopics(new ArrayList<Topic>());
			clone.setTopicsEnabled(new ArrayList<Topic>());
			for (Topic topic : topics) {
				Topic tclone = (Topic) topic.clone();
				tclone.setTheme(clone);
				clone.getTopics().add(tclone);
				if(tclone.getEnabled()) 
					clone.getTopicsEnabled().add(tclone);
			}
		}
		
		return clone;
	}

	/**
	 * @return the readjust
	 */
	public boolean isReadjust() {
		this.readjust = true;
		if(this.workflow != null){
			for (PhaseEntity phaseEntity : this.workflow.getPhases()) {
				if(phaseEntity.getOrder().equals(new Integer(1)) && (!phaseEntity.getFinished())){
					this.readjust = false;
					break;
				}
			}
		}
		return readjust;
	}

	/**
	 * @param readjust the readjust to set
	 */
	public void setReadjust(boolean readjust) {
		this.readjust = readjust;
	}

	/**
	 * @return the startWorflow
	 */
	public boolean getStartWorflow() {
		if(workflow != null) this.startWorflow = true;
		else this.startWorflow = false;

		return this.startWorflow;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		if(lock == null) this.locked =  false;
		else this.locked =  true;
		return this.locked;
	}

	/**
	 * @return the userLocked
	 */
	public boolean isUserLocked() {
		return userLocked;
	}

	/**
	 * @return the workflow
	 */
	public WorkflowEntity getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow the workflow to set
	 */
	public void setWorkflow(WorkflowEntity workflow) {
		this.workflow = workflow;
	}

	/**
	 * @param startWorflow the startWorflow to set
	 */
	public void setStartWorflow(boolean startWorflow) {
		this.startWorflow = startWorflow;
	}

	/**
	 * @param userLocked the userLocked to set
	 */
	public void setUserLocked(boolean userLocked) {
		this.userLocked = userLocked;
	}

	/**
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the topics
	 */
	public List<Topic> getTopics() {
		if(topics != null) Collections.sort(topics);
		return topics;
	}
	/**
	 * @param topics the topics to set
	 */
	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	/**
	 * @return the discipline
	 */
	public Discipline getDiscipline() {
		return discipline;
	}
	/**
	 * @param discipline the discipline to set
	 */
	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the author
	 */
	public User getAuthor() {
		return author;
	}
	/**
	 * @param author the author to set
	 */
	public void setAuthor(User author) {
		this.author = author;
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
	 * @return the enabled
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the lock
	 */
	public User getLock() {
		return lock;
	}
	/**
	 * @param lock the lock to set
	 */
	public void setLock(User lock) {
		this.lock = lock;
	}
	/**
	 * @return the topicsEnabled
	 */
	public List<Topic> getTopicsEnabled() {
		return topicsEnabled;
	}

	/**
	 * @param topicsEnabled the topicsEnabled to set
	 */
	public void setTopicsEnabled(List<Topic> topicsEnabled) {
		this.topicsEnabled = topicsEnabled;
	}

	/**
	 * @return the published
	 */
	public Boolean getPublished() {
		return published;
	}

	/**
	 * @param published the published to set
	 */
	public void setPublished(Boolean published) {
		this.published = published;
	}

	/**
	 * @return the paramHash
	 */
	public String getParamHash() {
		return paramHash;
	}

	/**
	 * @param paramHash the paramHash to set
	 */
	public void setParamHash(String paramHash) {
		this.paramHash = paramHash;
	}

	/**
	 * @return the publishCode
	 */
	public Integer getPublishCode() {
		return publishCode;
	}

	/**
	 * @param publishCode the publishCode to set
	 */
	public void setPublishCode(Integer publishCode) {
		this.publishCode = publishCode;
	}

	/**
	 * @return the replaceId
	 */
	public Long getReplaceId() {
		return replaceId;
	}

	/**
	 * @param replaceId the replaceId to set
	 */
	public void setReplaceId(Long replaceId) {
		this.replaceId = replaceId;
	}

	/**
	 * @return the duplicateId
	 */
	public Long getDuplicateId() {
		return duplicateId;
	}

	/**
	 * @param duplicateId the duplicateId to set
	 */
	public void setDuplicateId(Long duplicateId) {
		this.duplicateId = duplicateId;
	}

	public String getCssFile() {
		return cssFile;
	}

	public void setCssFile(String cssFile) {
		this.cssFile = cssFile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + (locked ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((paramHash == null) ? 0 : paramHash.hashCode());
		result = prime * result
				+ ((publishCode == null) ? 0 : publishCode.hashCode());
		result = prime * result
				+ ((published == null) ? 0 : published.hashCode());
		result = prime * result + (readjust ? 1231 : 1237);
		result = prime * result
				+ ((replaceId == null) ? 0 : replaceId.hashCode());
		result = prime * result
				+ ((duplicateId == null) ? 0 : duplicateId.hashCode());
		result = prime * result + (startWorflow ? 1231 : 1237);
		result = prime * result
				+ ((topicsEnabled == null) ? 0 : topicsEnabled.hashCode());
		result = prime * result + (userLocked ? 1231 : 1237);
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
		Theme other = (Theme) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (enabled == null) {
			if (other.enabled != null)
				return false;
		} else if (!enabled.equals(other.enabled))
			return false;
		if (locked != other.locked)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (paramHash == null) {
			if (other.paramHash != null)
				return false;
		} else if (!paramHash.equals(other.paramHash))
			return false;
		if (publishCode == null) {
			if (other.publishCode != null)
				return false;
		} else if (!publishCode.equals(other.publishCode))
			return false;
		if (published == null) {
			if (other.published != null)
				return false;
		} else if (!published.equals(other.published))
			return false;
		if (readjust != other.readjust)
			return false;
		if (replaceId == null) {
			if (other.replaceId != null)
				return false;
		} else if (!replaceId.equals(other.replaceId))
			return false;
		if (duplicateId == null) {
			if (other.duplicateId != null)
				return false;
		} else if (!duplicateId.equals(other.duplicateId))
			return false;
		if (startWorflow != other.startWorflow)
			return false;
		if (topicsEnabled == null) {
			if (other.topicsEnabled != null)
				return false;
		} else if (!topicsEnabled.equals(other.topicsEnabled))
			return false;
		if (userLocked != other.userLocked)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Theme [name=" + name + ", description=" + description
				+ ", created=" + created + ", enabled=" + enabled
				+ ", published=" + published + ", publishCode=" + publishCode
				+ ", replaceId=" + replaceId + ", topicsEnabled="
				+ topicsEnabled + ", locked=" + locked + ", paramHash="
				+ paramHash + ", userLocked=" + userLocked + ", startWorflow="
				+ startWorflow + ", readjust=" + readjust + ", id=" + id + "]";
	}

}
