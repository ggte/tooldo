package net.escritoriodigital.unicamp.redefor.model.topic;

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
import javax.persistence.Transient;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;

import org.hibernate.annotations.Where;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 18/06/2010
 * @description Object Topic
 */
@Entity
@Table (name="RFR_TOPIC")
public class Topic extends ModelBase implements Cloneable, Comparable {

	private String name;
	
	private String description;
	
	private Date created;
	
	private Boolean enabled;
	
	private Integer position;

	@ManyToOne
	@JoinColumn (name="FK_RFR_USER")
	private User author;

	@ManyToOne
	@JoinColumn (name="FK_RFR_THEME")
	private Theme theme;

	@OneToMany(mappedBy = "topic", cascade= CascadeType.ALL)
	@org.hibernate.annotations.OrderBy(clause = "position asc")
	private List<Page> pages;

	@OneToMany(mappedBy = "topic", cascade= CascadeType.ALL)
	@org.hibernate.annotations.OrderBy(clause = "position asc")
	@Where (clause = "enabled=1")
	private List<Page> pagesEnabled;

	@OneToOne
	@JoinColumn (name="FK_RFR_USER_LOCK")
	private User lock;

	@Transient
	private boolean userLocked;

	@Transient
	private boolean locked;
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		
		Topic clone = (Topic) super.clone();
		
		clone.setId(null);
		clone.setTheme(null);
		clone.setLock(null);
		clone.setUserLocked(false);
		clone.setLocked(false);
		clone.setPages(null);
		clone.setPagesEnabled(null);
		clone.setCreated(new Date());
		
		if(pages != null) {
			clone.setPages(new ArrayList<Page>());
			clone.setPagesEnabled(new ArrayList<Page>());
			for (Page page : pages) {
				Page pclone = (Page) page.clone();
				pclone.setTopic(clone);
				
				clone.getPages().add(pclone);
				if(pclone.getEnabled()) clone.getPagesEnabled().add(pclone);
			}
		}
		
		return clone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object arg0) {
		Topic other = (Topic) arg0;
		return this.position.compareTo(other.getPosition());
	}

	/**
	 * @return the pagesEnabled
	 */
	public List<Page> getPagesEnabled() {
		return pagesEnabled;
	}

	/**
	 * @param pagesEnabled the pagesEnabled to set
	 */
	public void setPagesEnabled(List<Page> pagesEnabled) {
		this.pagesEnabled = pagesEnabled;
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
	 * @return the pages
	 */
	public List<Page> getPages() {
		return pages;
	}
	/**
	 * @param pages the pages to set
	 */
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}
	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((position == null) ? 0 : position.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
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
		return "Topic [created=" + created + ", description=" + description
				+ ", enabled=" + enabled + ", name=" + name + ", author="
				+ author + "]";
	}


	/**
	 * @return the quantity of pages in that topic
	 */
	public Integer getTotalPages() {
		return pages != null ? (pages.isEmpty() ? 0 : pages.size()) : 0;
	}


}
