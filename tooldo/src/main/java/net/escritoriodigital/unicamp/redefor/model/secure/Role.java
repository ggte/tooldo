package net.escritoriodigital.unicamp.redefor.model.secure;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;

/**
 * @author Concon
 * @category Model
 * @created 16/06/2010
 * @description Objeto Regra Redefor
 */
@Entity
@Table(name = "RFR_ROLE")
public class Role extends ModelBase implements
		net.escritoriodigital.components.model.secure.IRole {

	private static final long serialVersionUID = -7232412578091473247L;

	private String name;
	private String description;
	
	@ManyToMany (mappedBy="roles")
	private List<Profile> profiles;
	
	public Role() { }
	
	public Role(Long id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public void addProfile(Profile item) {
		if (item == null)
			throw new IllegalArgumentException("profiles is null!");

		if (profiles == null)
			setProfiles(new ArrayList<Profile>());

		profiles.add(item);
	}

	public void removeProfile(Profile item) {
		if (item == null)
			throw new IllegalArgumentException("profiles is null!");

		if (profiles == null)
			setProfiles(new ArrayList<Profile>());
		else
			profiles.remove(item);
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
	 * @return the profiles
	 */
	public List<Profile> getProfiles() {
		return profiles;
	}

	/**
	 * @param profiles the profiles to set
	 */
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Role other = (Role) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Role [name=" + name + ", description=" + description + ", id=" + id + "]";
	}

}
