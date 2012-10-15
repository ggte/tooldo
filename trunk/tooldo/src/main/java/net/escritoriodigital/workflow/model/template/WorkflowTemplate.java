/**
 *
 */
package net.escritoriodigital.workflow.model.template;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.escritoriodigital.workflow.model.AbstractWorkflowModel;

import org.hibernate.annotations.Cascade;

/**
 * @author andre
 *
 */
@Entity
@Table(name = "RFR_WORKFLOW_TEMPLATE")
public class WorkflowTemplate extends AbstractWorkflowModel {

	private static final long serialVersionUID = -7227299110482725517L;
	@OneToMany(mappedBy="workflow", cascade = { CascadeType.ALL })
	@Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<PhaseTemplate> phases = new ArrayList<PhaseTemplate>();
	
	private Boolean enabled;

	/**
	 * @return the phases
	 */
	public List<PhaseTemplate> getPhases() {
		return phases;
	}

	/**
	 * @param phases the phases to set
	 */
	private void setPhases(List<PhaseTemplate> phases) {
		this.phases = phases;
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
	 * Adiciona a phase no workflow
	 * @param phase
	 */
	public void addPhase(PhaseTemplate phase) {
		if(phase == null) throw new IllegalArgumentException("phase is null!");
		if(this.phases == null) this.phases = new ArrayList<PhaseTemplate>();
		if(phase.getOrder() == null) phase.setOrder(this.phases.size());
		if(!this.phases.contains(phase)) {
			if(phase.getOrder() != null) {
				for (PhaseTemplate local : this.phases) {
					if(local.getOrder().equals(phase.getOrder())) {
						local.setOrder(this.phases.size() + 1);
					}
				}
			}
			phase.setWorkflow(this);
			this.phases.add(phase);
		}
	}

	/**
	 * Remove a phase do workflow
	 * @param phase
	 */
	public void removePhase(PhaseTemplate phase) {
		if(phase == null) throw new IllegalArgumentException("phase is null!");
		if(this.phases != null) {
			if(this.phases.contains(phase)) {
				this.phases.remove(phase);
				phase.setWorkflow(null);
				int i = 0;
				for (PhaseTemplate local : this.phases)
					local.setOrder(i++);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((phases == null) ? 0 : phases.hashCode());
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
		WorkflowTemplate other = (WorkflowTemplate) obj;
		if (phases == null) {
			if (other.phases != null)
				return false;
		} else if (!phases.equals(other.phases))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WorkflowTemplate [phases=" + phases + ", description="
				+ description + ", name=" + name + ", id=" + id + "]";
	}



}
