package net.escritoriodigital.unicamp.redefor.model.secure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Email;

/**
 * @author Concon
 * @category Model
 * @created 16/06/2010
 * @description Objeto Usuário Redefor
 */
@Entity
@Table(name = "RFR_USER")

public class User extends ModelBase implements net.escritoriodigital.components.model.secure.IUser {

	private static final long serialVersionUID = -5835458059349579972L;
	
	private String name;
	
	@Email(message="Email inválido")
	private String email;
	
	private String phonenumber;
	
	private String username;
	private String password;
	private Date created;
	private Boolean enabled;
	private Boolean changePassword;


	@ManyToOne
	@JoinColumn (name="FK_RFR_PROFILE")
	private Profile profile;

	@ManyToMany(fetch=FetchType.EAGER)
	@GenericGenerator(name="hibseq", strategy = "increment")
	@CollectionId(columns = @Column(name = "ID"), type = @org.hibernate.annotations.Type(type = "long"), generator = "hibseq")
	@JoinTable(name = "RFR_GRANTED_USERS_DISCIPLINE", joinColumns = { @JoinColumn(name = "FK_RFR_USER") }, inverseJoinColumns = { @JoinColumn(name = "FK_RFR_DISCIPLINE") })
	private List<Discipline> disciplines;

	public void addDiscipline(Discipline item) {
		if (item == null)
			throw new IllegalArgumentException("disciplines is null!");

		if (disciplines == null)
			setDisciplines(new ArrayList<Discipline>());

		disciplines.add(item);
	}

	public void removeDiscipline(Discipline item) {
		if (item == null)
			throw new IllegalArgumentException("disciplines is null!");

		if (disciplines == null)
			setDisciplines(new ArrayList<Discipline>());
		else
			disciplines.remove(item);
	}
	
	public Profile getProfile() {
		return profile;
	}
	
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getEmail() {
		return email;
	}
	
	@Override
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getPhonenumber() {
		return phonenumber;
	}
	
	@Override
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Override
	public String getPassword() {
		return password;
	}
	
	@Override
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public Date getCreated() {
		return created;
	}
	
	@Override
	public void setCreated(Date created) {
		this.created = created;
	}
	
	@Override
	public Boolean getEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	
	@Override
	public Boolean getChangePassword() {
		return changePassword;
	}

	@Override
	public void setChangePassword(Boolean changePassword) {
		this.changePassword = changePassword;
	}

	/**
	 * @return the disciplines
	 */
	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines
	 *            the disciplines to set
	 */
	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((enabled == null) ? 0 : enabled.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result
				+ ((phonenumber == null) ? 0 : phonenumber.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime * result
				+ ((username == null) ? 0 : username.hashCode());
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
		User other = (User) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
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
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (phonenumber == null) {
			if (other.phonenumber != null)
				return false;
		} else if (!phonenumber.equals(other.phonenumber))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", phonenumber="
				+ phonenumber + ", username=" + username + ", password="
				+ password + ", created=" + created + ", enabled=" + enabled
				+ ", profile=" + profile + "]";
	}



}
