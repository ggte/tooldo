package net.escritoriodigital.unicamp.redefor.model.discipline;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;


/**
 * @author Concon
 * @category Model
 * @created 16/06/2010
 * @description Object Discipline
 */
@Entity
@Table (name="RFR_DISCIPLINE")
public class Discipline extends ModelBase {

	private static final long serialVersionUID = 8011555137784449463L;

	private String name;

	private String description;
	
	@Temporal(TemporalType.DATE)
	private Date created;

	private Boolean enabled;

	@ManyToOne
	@JoinColumn (name="FK_RFR_COURSE")
	private Course course;

	@ManyToOne
	@JoinColumn (name="FK_RFR_USER")
	private User owner;

	@OneToMany(mappedBy = "discipline")
	private List<Theme> themes;

	public Discipline() { }

	public Discipline(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.description = "";
		this.created = new Date();
		this.enabled = true;
		this.course = new Course();
		this.owner = new User();
		this.themes = new ArrayList<Theme>();
	}

	/**
	 * @return the owner
	 */
	public User getOwner() {
		return owner;
	}
	/**
	 * @param owner the owner to set
	 */
	public void setOwner(User owner) {
		this.owner = owner;
	}
	/**
	 * @return the themes
	 */
	public List<Theme> getThemes() {
		return themes;
	}
	/**
	 * @param themes the themes to set
	 */
	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}
	/**
	 * @param course the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
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
		result = prime * result + ((course == null) ? 0 : course.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((themes == null) ? 0 : themes.hashCode());
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
		Discipline other = (Discipline) obj;
		if (course == null) {
			if (other.course != null)
				return false;
		} else if (!course.equals(other.course))
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (themes == null) {
			if (other.themes != null)
				return false;
		} else if (!themes.equals(other.themes))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Discipline [id=" + id + ", name=" + name + ", description=" + description
				+ ", created=" + created + ", enabled=" + enabled + ", themes=" + themes + "]";
	}




}
