/**
 *
 */
package net.escritoriodigital.components.spring;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * @author andre
 * http://blog.jdevelop.eu/2008/07/06/access-the-spring-applicationcontext-from-everywhere-in-your-application/
 */
public class ApplicationContextProvider implements ApplicationContextAware {

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {

		// Wiring the ApplicationContext into a static method
		AppContext.setApplicationContext(applicationContext);
	}

}
