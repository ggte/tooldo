package net.escritoriodigital.components.model.secure;

public interface IRole {

	/**
	 * @return the id
	 */
	public abstract Long getId();

	/**
	 * @param id
	 *            the id to set
	 */
	public abstract void setId(Long id);
	/**
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * @param name the name to set
	 */
	public abstract void setName(String name);

}