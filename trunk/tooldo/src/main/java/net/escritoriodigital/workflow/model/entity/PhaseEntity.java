/**
 *
 */
package net.escritoriodigital.workflow.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.workflow.model.AbstractWorkflowModel;
import net.escritoriodigital.workflow.model.template.PhaseTemplate;
import net.escritoriodigital.workflow.model.template.StageTemplate;

import org.hibernate.annotations.Cascade;

/**
 * @author andre
 *
 */
@SuppressWarnings("rawtypes")
@Entity
@Table(name = "RFR_WORKFLOW_PHASE_ENTITY")
public class PhaseEntity extends AbstractWorkflowModel implements Comparable {
	
	private static final long serialVersionUID = 1331295825732039645L;

	private Boolean finished = false;

	@Column(name="PHASE_ORDER")
	private Integer order;

	@ManyToOne @JoinColumn(name="FK_RFR_WORKFLOW_ENTITY")
	private WorkflowEntity workflow;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="phase")
	@Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<StageEntity> stages = new ArrayList<StageEntity>();

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		PhaseTemplate other = (PhaseTemplate) o;
		return this.getOrder().compareTo(other.getOrder());
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
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * @return the workflow
	 */
	public WorkflowEntity getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow the workflow to set
	 */
	public void setWorkflow(WorkflowEntity workflow) {
		this.workflow = workflow;
	}

	/**
	 * @return the stages
	 */
	public List<StageEntity> getStages() {
		return stages;
	}

	public void verifyStages(){
		boolean phaseFinish = true;
		for (StageEntity  stageEntity: this.stages) {
			if(!stageEntity.getFinished()) phaseFinish = false;
		}
		this.finished = phaseFinish;
	}

	/**
	 * @param stages the stages to set
	 */
	@SuppressWarnings("unused")
	private void setStages(List<StageEntity> stages) {
		this.stages = stages;
	}

	/**
	 * Adiciona a stage na phase
	 * @param phase
	 */
	public void addStage(StageEntity stage) {
		if(stage == null) throw new IllegalArgumentException("stage is null!");
		if(this.stages == null) this.stages = new ArrayList<StageEntity>();
		if(!this.stages.contains(stage)) {
			stage.setPhase(this);
			this.stages.add(stage);
		}
	}

	/**
	 * Remove a stage da phase
	 * @param phase
	 */
	public void removeStage(StageTemplate stage) {
		if(stage == null) throw new IllegalArgumentException("stage is null!");
		if(this.stages != null) {
			if(this.stages.contains(stage)) {
				this.stages.remove(stage);
				stage.setPhase(null);
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
		result = prime * result
				+ ((finished == null) ? 0 : finished.hashCode());
		result = prime * result + ((order == null) ? 0 : order.hashCode());
		result = prime * result + ((stages == null) ? 0 : stages.hashCode());
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
		PhaseEntity other = (PhaseEntity) obj;
		if (finished == null) {
			if (other.finished != null)
				return false;
		} else if (!finished.equals(other.finished))
			return false;
		if (order == null) {
			if (other.order != null)
				return false;
		} else if (!order.equals(other.order))
			return false;
		if (stages == null) {
			if (other.stages != null)
				return false;
		} else if (!stages.equals(other.stages))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PhaseEntity [finished=" + finished + ", order=" + order
				+ ", stages=" + stages + ", description=" + description
				+ ", name=" + name + ", id=" + id + "]";
	}

	/**
	 * Verifica se um determinado profile está em algum dos estágios da fase
	 * @param profile
	 * @return
	 */
	public Boolean isProfileInPhase(Profile profile) {

		for (StageEntity stage : this.stages) {
			if(stage.getProfile().getId().equals(profile.getId()))
				return true;
		}

		return false;
	}

}
