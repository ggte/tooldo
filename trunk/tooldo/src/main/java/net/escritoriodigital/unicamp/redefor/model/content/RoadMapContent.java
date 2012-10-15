/**
 *
 */
package net.escritoriodigital.unicamp.redefor.model.content;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;
import net.escritoriodigital.components.model.file.AttachmentFile;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 16/06/2010
 * @description Object Content
 */
@Entity
@Table (name="RFR_ROADMAP_CONTENT")
public class RoadMapContent extends ModelBase {

	private static final long serialVersionUID = -7831524299340643953L;

	private String roadmap;

	@Embedded
	private AttachmentFile attachment;

	/**
	 * @return the roadmap
	 */
	public String getRoadmap() {
		return roadmap;
	}

	/**
	 * @param roadmap the roadmap to set
	 */
	public void setRoadmap(String roadmap) {
		this.roadmap = roadmap;
	}

	/**
	 * @return the attachment
	 */
	public AttachmentFile getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment the attachment to set
	 */
	public void setAttachment(AttachmentFile attachment) {
		this.attachment = attachment;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((attachment == null) ? 0 : attachment.hashCode());
		result = prime * result + ((roadmap == null) ? 0 : roadmap.hashCode());
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
		RoadMapContent other = (RoadMapContent) obj;
		if (attachment == null) {
			if (other.attachment != null)
				return false;
		} else if (!attachment.equals(other.attachment))
			return false;
		if (roadmap == null) {
			if (other.roadmap != null)
				return false;
		} else if (!roadmap.equals(other.roadmap))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RoadMapContent [attachment=" + attachment + ", roadmap="
				+ roadmap + ", id=" + id + "]";
	}

}
