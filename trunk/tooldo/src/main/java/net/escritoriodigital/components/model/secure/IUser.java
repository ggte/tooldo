package net.escritoriodigital.components.model.secure;

import java.util.Date;

public interface IUser {

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
	 * @param name
	 *            the name to set
	 */
	public abstract void setName(String name);

	/**
	 * @return the email
	 */
	public abstract String getEmail();

	/**
	 * @param email
	 *            the email to set
	 */
	public abstract void setEmail(String email);

	/**
	 * @return the phonenumber
	 */
	public abstract String getPhonenumber();

	/**
	 * @param phonenumber
	 *            the phonenumber to set
	 */
	public abstract void setPhonenumber(String phonenumber);

	/**
	 * @return the username
	 */
	public abstract String getUsername();

	/**
	 * @param username
	 *            the username to set
	 */
	public abstract void setUsername(String username);

	/**
	 * @return the password
	 */
	public abstract String getPassword();

	/**
	 * @param password
	 *            the password to set
	 */
	public abstract void setPassword(String password);

	/**
	 * @return the created
	 */
	public abstract Date getCreated();

	/**
	 * @param created
	 *            the created to set
	 */
	public abstract void setCreated(Date created);

	/**
	 * @return the enabled
	 */
	public abstract Boolean getEnabled();

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public abstract void setEnabled(Boolean enabled);
	
	/**
	 * @return the changePassword
	 */
	public abstract Boolean getChangePassword();
	
	/**
	 * @param changePassword
	 *            the changePassword to set
	 */
	public abstract void setChangePassword(Boolean changePassword);

	
}