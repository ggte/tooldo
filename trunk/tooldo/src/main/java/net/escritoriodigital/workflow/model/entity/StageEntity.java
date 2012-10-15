/**
 *
 */
package net.escritoriodigital.workflow.model.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.workflow.model.AbstractWorkflowModel;

/**
 * @author andre
 *
 */
@Entity
@Table(name = "RFR_WORKFLOW_STAGE_ENTITY")
public class StageEntity extends AbstractWorkflowModel {

	private static final long serialVersionUID = 3426574709609281267L;

	private Boolean required = true;

	private Boolean finished = false;

	@ManyToOne
    @JoinColumn(name="FK_RFR_USER")
	private User user;
	
	@ManyToOne
    @JoinColumn(name="FK_RFR_PROFILE")
	private Profile profile;

	@ManyToOne
    @JoinColumn(name="FK_RFR_WORKFLOW_PHASE_ENTITY")
	private PhaseEntity phase;
	
	
	/**
	 * @return the role
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param role the role to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/**
	 * @return the required
	 */
	public Boolean getRequired() {
		return required;
	}

	/**
	 * @param required the required to set
	 */
	public void setRequired(Boolean required) {
		this.required = required;
	}

	/**
	 * @return the finished
	 */
	public Boolean getFinished() {
		return finished;
	}

	/**
	 * @param finished the finished to set
	 */
	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the phase
	 */
	public PhaseEntity getPhase() {
		return phase;
	}

	/**
	 * @param phase the phase to set
	 */
	public void setPhase(PhaseEntity phase) {
		this.phase = phase;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((finished == null) ? 0 : finished.hashCode());
		result = prime * result
				+ ((required == null) ? 0 : required.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		StageEntity other = (StageEntity) obj;
		if (finished == null) {
			if (other.finished != null)
				return false;
		} else if (!finished.equals(other.finished))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StageEntity [finished=" + finished + ", required=" + required
				+ ", user=" + user + ", description=" + description + ", name="
				+ name + ", id=" + id + "]";
	}

}
