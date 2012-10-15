/**
 *
 */
package net.escritoriodigital.workflow.model.template;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.escritoriodigital.workflow.model.AbstractWorkflowModel;

import org.hibernate.annotations.Cascade;

/**
 * @author andre
 *
 */
@Entity
@Table(name = "RFR_WORKFLOW_PHASE_TEMPLATE")
public class PhaseTemplate extends AbstractWorkflowModel implements Comparable {

	@Column(name="PHASE_ORDER")
	private Integer order;

	@ManyToOne @JoinColumn(name="FK_RFR_WORKFLOW_TEMPLATE")
	private WorkflowTemplate workflow;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="phase")
	@Cascade( { org.hibernate.annotations.CascadeType.DELETE_ORPHAN })
	private List<StageTemplate> stages = new ArrayList<StageTemplate>();

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Object o) {
		PhaseTemplate other = (PhaseTemplate) o;
		return this.getOrder().compareTo(other.getOrder());
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
	public WorkflowTemplate getWorkflow() {
		return workflow;
	}

	/**
	 * @param workflow the workflow to set
	 */
	public void setWorkflow(WorkflowTemplate workflow) {
		this.workflow = workflow;
	}

	/**
	 * @return the stages
	 */
	public List<StageTemplate> getStages() {
		return stages;
	}

	/**
	 * @param stages the stages to set
	 */
	private void setStages(List<StageTemplate> stages) {
		this.stages = stages;
	}

	/**
	 * Adiciona a stage na phase
	 * @param phase
	 */
	public void addStage(StageTemplate stage) {
		if(stage == null) throw new IllegalArgumentException("stage is null!");
		if(this.stages == null) this.stages = new ArrayList<StageTemplate>();
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
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		PhaseTemplate other = (PhaseTemplate) obj;
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
		return "PhaseTemplate [description=" + description + ", name=" + name
				+ ", order=" + order + ", stages=" + stages + ", id=" + id
				+ "]";
	}

}
