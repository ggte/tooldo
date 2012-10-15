/**
 *
 */
package net.escritoriodigital.components.service.exception;

/**
 * @author André Fabbro
 * @category Exception
 * @created 04/05/2010
 * @description Serviço Generico
 */
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8719457780485161960L;

	public ServiceException() {

	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

}
