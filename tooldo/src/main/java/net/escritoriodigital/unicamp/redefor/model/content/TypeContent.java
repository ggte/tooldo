/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.model.content;

import javax.persistence.Entity;
import javax.persistence.Table;

import net.escritoriodigital.components.model.ModelBase;

/**
 * @author Felipe Alexandre
 * @category Model
 * @created 16/06/2010
 * @description Object Content
 */
@Entity
@Table (name="RFR_TYPE_CONTENT")
public class TypeContent extends ModelBase{
	
	private static final long serialVersionUID = 2834562218429495600L;
	
	private String name;
	private String typeContent;
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the typeContent
	 */
	public String getTypeContent() {
		return typeContent;
	}
	/**
	 * @param typeContent the typeContent to set
	 */
	public void setTypeContent(String typeContent) {
		this.typeContent = typeContent;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((typeContent == null) ? 0 : typeContent.hashCode());
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
		TypeContent other = (TypeContent) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (typeContent == null) {
			if (other.typeContent != null)
				return false;
		} else if (!typeContent.equals(other.typeContent))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TypeContent [name=" + name + ", typeContent=" + typeContent
				+ "]";
	}
	
	
}
