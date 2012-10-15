/**
 *
 */
package net.escritoriodigital.workflow.model.template;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.workflow.model.AbstractWorkflowModel;

/**
 * @author andre
 *
 */
@Entity
@Table(name = "RFR_WORKFLOW_STAGE_TEMPLATE")
public class StageTemplate extends AbstractWorkflowModel {

	private static final long serialVersionUID = -4853466806321068222L;

	private Boolean required = new Boolean(true);

	@ManyToOne
    @JoinColumn(name="FK_RFR_PROFILE")
	private Profile profile;

	@ManyToOne
    @JoinColumn(name="FK_RFR_WORKFLOW_PHASE_TEMPLATE")
	private PhaseTemplate phase;

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
	 * @return the phase
	 */
	public PhaseTemplate getPhase() {
		return phase;
	}

	/**
	 * @param phase the phase to set
	 */
	public void setPhase(PhaseTemplate phase) {
		this.phase = phase;
	}

	/**
	 * @return the profile
	 */
	public Profile getProfile() {
		return profile;
	}

	/**
	 * @param profile the profile to set
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		result = prime * result + ((profile == null) ? 0 : profile.hashCode());
		result = prime * result
				+ ((required == null) ? 0 : required.hashCode());
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
		StageTemplate other = (StageTemplate) obj;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		if (profile == null) {
			if (other.profile != null)
				return false;
		} else if (!profile.equals(other.profile))
			return false;
		if (required == null) {
			if (other.required != null)
				return false;
		} else if (!required.equals(other.required))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StageTemplate [required=" + required + ", profile=" + profile
				+ ", phase=" + phase + "]";
	}

	

}
