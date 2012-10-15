package net.escritoriodigital.workflow.model.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;

@Entity
@Table(name = "RFR_HISTORY_WORKFLOW_THEME")
public class HistoryWorkflowEntity extends ModelBase{

	
	private static final long serialVersionUID = 3702749757407283452L;

	private Date created;
	
	private String comments;
	
	private boolean accept;
	
	@ManyToOne @JoinColumn(name="FK_RFR_THEME")
	private Theme theme;
	
	@ManyToOne @JoinColumn(name="FK_RFR_WORKFLOW_STAGE")
	private StageEntity stage;

	@ManyToOne @JoinColumn(name="FK_RFR_USER")
	private User user;
	
	/**
	 * @return the accept
	 */
	public boolean isAccept() {
		return accept;
	}

	/**
	 * @param accept the accept to set
	 */
	public void setAccept(boolean accept) {
		this.accept = accept;
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
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the theme
	 */
	public Theme getTheme() {
		return theme;
	}

	/**
	 * @param theme the theme to set
	 */
	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	
	/**
	 * @return the stage
	 */
	public StageEntity getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(StageEntity stage) {
		this.stage = stage;
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
	
	
	
	
}
