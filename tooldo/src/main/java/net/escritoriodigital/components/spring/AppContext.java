/**
 *
 */
package net.escritoriodigital.components.spring;

import org.springframework.context.ApplicationContext;

/**
 * @author andre
 * http://blog.jdevelop.eu/2008/07/06/access-the-spring-applicationcontext-from-everywhere-in-your-application/
 *
 */
public class AppContext {

	private static ApplicationContext ctx;

	/**
	 * Injected from the class "ApplicationContextProvider" which is
	 * automatically
	 *
	 * loaded during Spring-Initialization.
	 */

	public static void setApplicationContext(ApplicationContext applicationContext) {
		ctx = applicationContext;
	}

	/**
	 * Get access to the Spring ApplicationContext from everywhere in your
	 * Application.
	 */

	public static ApplicationContext getApplicationContext() {
		return ctx;
	}

}
