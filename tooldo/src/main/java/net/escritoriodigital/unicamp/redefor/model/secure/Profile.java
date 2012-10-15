package net.escritoriodigital.unicamp.redefor.model.secure;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;

/**
 * @author Concon
 * @category Model
 * @created 16/06/2010
 * @description Objeto Perfil Redefor
 */
@Entity
@Table(name = "RFR_PROFILE")
public class Profile extends ModelBase implements
		net.escritoriodigital.components.model.secure.IProfile {

	public static final Long PROFESSOR_AUTOR_ID = new Long(2);

	private static final long serialVersionUID = -942469553472908338L;

	private String name;
	private Boolean fixed;

	@ManyToMany
	@GenericGenerator(name = "hibseq", strategy = "increment")
	@CollectionId(columns = @Column(name = "ID"), type = @org.hibernate.annotations.Type(type = "long"), generator = "hibseq")
	@JoinTable(name = "RFR_GRANTED_PROFILE_ROLES", joinColumns = { @JoinColumn(name = "FK_RFR_PROFILE") }, inverseJoinColumns = { @JoinColumn(name = "FK_RFR_ROLE") })
	private List<Role> roles;

	@ManyToMany (mappedBy="profile")
	private List<User> users;

	public void addRole(Role item) {
		if (item == null)
			throw new IllegalArgumentException("roles is null!");

		if (roles == null)
			setRoles(new ArrayList<Role>());

		roles.add(item);
	}

	public void removeRole(Role item) {
		if (item == null)
			throw new IllegalArgumentException("roles is null!");

		if (roles == null)
			setRoles(new ArrayList<Role>());
		else
			roles.remove(item);
	}

	public void addUser(User item) {
		if (item == null)
			throw new IllegalArgumentException("users is null!");

		if (users == null)
			setUsers(new ArrayList<User>());

		users.add(item);
	}

	public void removeUser(User item) {
		if (item == null)
			throw new IllegalArgumentException("users is null!");

		if (users == null)
			setUsers(new ArrayList<User>());
		else
			users.remove(item);
	}



	/**
	 * @return the fixed
	 */
	public Boolean getFixed() {
		return fixed;
	}

	/**
	 * @param fixed the fixed to set
	 */
	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the name
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the roles
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
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
		Profile other = (Profile) obj;
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
		return "Profile [name=" + name + "]";
	}

}
