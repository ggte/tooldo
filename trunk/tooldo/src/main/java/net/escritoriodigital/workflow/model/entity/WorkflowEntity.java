/**
 *
 */
package net.escritoriodigital.workflow.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import net.escritoriodigital.workflow.model.AbstractWorkflowModel;

import org.hibernate.annotations.Cascade;

/**
 * @author andre
 *
 */
@Entity
@Table(name = "RFR_WORKFLOW_ENTITY")
public class WorkflowEntity extends AbstractWorkflowModel {

	@OneToMany(mappedBy="workflow", cascade = { CascadeType.ALL })
	@Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<PhaseEntity> phases = new ArrayList<PhaseEntity>();
	
	//OneToOne
	//JoinColumn (name="FK_RFR_CURRENT_PHASE_ENTITY")
	//private PhaseEntity currentPhase;
	
	public List<PhaseEntity> getPhases() {
		return phases;
	}
	
	private void setPhases(List<PhaseEntity> phases) {
		this.phases = phases;
	}

	/**
	 * Adiciona a phase no workflow
	 * @param phase
	 */
	public void addPhase(PhaseEntity phase) {
		if(phase == null) throw new IllegalArgumentException("phase is null!");
		if(this.phases == null) this.phases = new ArrayList<PhaseEntity>();
		if(phase.getOrder() == null) phase.setOrder(this.phases.size());
		if(!this.phases.contains(phase)) {
			if(phase.getOrder() != null) {
				for (PhaseEntity local : this.phases) {
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
	public void removePhase(PhaseEntity phase) {
		if(phase == null) throw new IllegalArgumentException("phase is null!");
		if(this.phases != null) {
			if(this.phases.contains(phase)) {
				this.phases.remove(phase);
				phase.setWorkflow(null);
				int i = 0;
				for (PhaseEntity local : this.phases)
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
		WorkflowEntity other = (WorkflowEntity) obj;
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
		return "WorkflowEntity [phases=" + phases + ", description="
				+ description + ", name=" + name + ", id=" + id + "]";
	}

}
