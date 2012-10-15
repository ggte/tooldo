/**
 *
 */
package net.escritoriodigital.unicamp.redefor.model.page;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.content.Content;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;

import org.hibernate.annotations.Cascade;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Page
 */

@SuppressWarnings("rawtypes")
@Entity
@Table (name="RFR_PAGE")
public class Page extends ModelBase implements Comparable, Cloneable {


	private static final long serialVersionUID = -968511876816570381L;

	private String name;
	private String description;
	private Date created;
	private Boolean enabled;
	private Integer position;

	@OneToOne
	@JoinColumn (name="FK_RFR_USER_LOCK")
	private User lock;

	@Transient
	private boolean locked;

	@Transient
	private boolean userLocked;

	@ManyToOne
	@JoinColumn (name="FK_RFR_USER")
	private User author;

	@ManyToOne
	@JoinColumn (name="FK_RFR_TOPIC")
	private Topic topic;

	@OneToOne (mappedBy="page", cascade = CascadeType.ALL)
	@Cascade (org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
	private Content content;

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Page clone = (Page) super.clone();
		
		clone.setId(null);
		clone.setCreated(new Date());
		clone.setLock(null);
		clone.setUserLocked(false);
		clone.setTopic(null);
		Content cont = (Content) content.clone();
		cont.setPage(clone);
		clone.setContent(cont);
		
		return clone;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		Page other = (Page) o;
		return this.position.compareTo(other.getPosition());
	}

	/**
	 * @return the userLocked
	 */
	public boolean isUserLocked() {
		return userLocked;
	}

	/**
	 * @param userLocked the userLocked to set
	 */
	public void setUserLocked(boolean userLocked) {
		this.userLocked = userLocked;
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
	 * @param locked the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
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
	 * @return the position
	 */
	public Integer getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(Integer position) {
		this.position = position;
	}
	/**
	 * @return the content
	 */
	public Content getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(Content content) {
		this.content = content;
	}
	/**
	 * @return the topic
	 */
	public Topic getTopic() {
		return topic;
	}
	/**
	 * @param topic the topic to set
	 */
	public void setTopic(Topic topic) {
		this.topic = topic;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((author == null) ? 0 : author.hashCode());
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
		Page other = (Page) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Page [description=" + description + ", enabled=" + enabled
				+ ", name=" + name + ", author=" + author + "]";
	}



}
