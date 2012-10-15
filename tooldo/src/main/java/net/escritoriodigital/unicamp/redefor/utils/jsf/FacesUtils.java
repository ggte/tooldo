package net.escritoriodigital.unicamp.redefor.utils.jsf;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FacesUtils {
	private final Log log = LogFactory.getLog(getClass());

	private static FacesUtils instance;

	protected FacesUtils() {
		if (instance != null)
			throw new RuntimeException("Somente uma instancia permitido por Aplicação!");
	}

	@PostConstruct
	public void initialize() {
		log.info("Inicializado");
	}

	@PreDestroy
	public void destroy() {
		log.info("destruido");
	}

	public static String lookupManagedBeanName(Object bean) {

		return getInstance()._lookupManagedBeanName(bean);
	}
	
	public static String getParameterValue( String paramName ) {		
		HttpServletRequest req = (HttpServletRequest) getFacesContext().getExternalContext().getRequest();		
		return req.getParameter( paramName );
	}
	
	public static void redirect( String url ) {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext external = context.getExternalContext();

			String path = external.getRequestContextPath();
			context.getExternalContext().redirect( path + url );
		}
		catch ( Exception e ) {
			throw new RuntimeException( e );
		}
	}

	private String _lookupManagedBeanName(Object bean) {
		final ExternalContext externalContext = _getExternalContext();

		// Get requestmap.
		final Map<String, Object> requestMap = externalContext.getRequestMap();

		// Lookup the current bean instance in the request scope.
		for (final String key : requestMap.keySet()) {
			if (bean.equals(requestMap.get(key))) {
				// The key is the managed bean name.
				return key;
			}
		}

		// Bean is not in the request scope. Get the sessionmap then.
		final Map<String, Object> sessionMap = externalContext.getSessionMap();

		// Lookup the current bean instance in the session scope.
		for (final String key : sessionMap.keySet()) {
			if (bean.equals(sessionMap.get(key))) {
				// The key is the managed bean name.
				return key;
			}
		}

		// Bean is also not in the session scope. Get the applicationmap then.
		final Map<String, Object> applicationMap = externalContext.getApplicationMap();

		// Lookup the current bean instance in the application scope.
		for (final String key : applicationMap.keySet()) {
			if (bean.equals(applicationMap.get(key))) {
				// The key is the managed bean name.
				return key;
			}
		}

		// Bean is also not in the application scope.
		// Is the bean's instance actually a managed bean instance then? =)
		return null;

	}

	private static synchronized FacesUtils getInstance() {
		if (instance == null) {
			instance = new FacesUtils();
		}

		return instance;
	}

	// Getters
	// -----------------------------------------------------------------------------------

	public static Object getApplicationMapValue(String key) {
		return getInstance()._getApplicationMap().get(key);
	}

	public static Object getSessionMapValue(String key) {
		return getInstance()._getSessionMap().get(key);
	}

	public static Object getRequestMapValue(String key) {
		return getInstance()._getRequestMap().get(key);
	}

	public static Map<String, Object> getRequestMap() {
		return getInstance()._getRequestMap();
	}

	private Map<String, Object> _getRequestMap() {
		return _getExternalContext().getRequestMap();
	}

	private ExternalContext _getExternalContext() {
		return FacesContext.getCurrentInstance().getExternalContext();
	}
	
	public static String getHostname() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap().get("Host");
	}

	public static void setRequestMapValue(String key, Object value) {
		getRequestMap().put(key, value);
	}

	// Setters
	// -----------------------------------------------------------------------------------

	public static void setApplicationMapValue(String key, Object value) {

		getApplicationMap().put(key, value);
	}

	public static Map<String, Object> getApplicationMap() {
		return getInstance()._getApplicationMap();
	}

	private Map<String, Object> _getApplicationMap() {
		return _getExternalContext().getApplicationMap();
	}

	public static ExternalContext getExternalContext() {
		return getInstance()._getExternalContext();
	}

	public static void setSessionMapValue(String key, Object value) {
		getSessionMap().put(key, value);
	}

	public static Map<String, Object> getSessionMap() {
		return getInstance()._getSessionMap();
	}

	private Map<String, Object> _getSessionMap() {
		return _getExternalContext().getSessionMap();
	}

	public static String getMessageBundle() {
		return getInstance()._getMessageBundle();
	}

	private String _getMessageBundle() {
		return _getApplication().getMessageBundle();
	}

	private Application _getApplication() {
		return getFacesContext().getApplication();

	}

	/**
	 * Retorna instancia corrente do FacesContext
	 * 
	 * @return
	 */
	public static FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	public static Locale getLocale() {
		return getInstance()._getLocale();
	}

	private Locale _getLocale() {
		UIViewRoot l_viewRoot = _getViewRoot();
		Locale locale = null;
		if (l_viewRoot != null)
			locale = l_viewRoot.getLocale();
		if (locale == null)
			locale = Locale.getDefault();
		return locale;
	}

	private UIViewRoot _getViewRoot() {
		return getFacesContext().getViewRoot();
	}

	public static ClassLoader getClassLoader() {
		ClassLoader l_loader = Thread.currentThread().getContextClassLoader();
		if (l_loader == null)
			l_loader = ClassLoader.getSystemClassLoader();
		return l_loader;
	}

	/**
	 * @param _bundle
	 * @param _string
	 * @return
	 */
	public static String getString(String _bundle, String _string) {
		String appBundle = FacesUtils.getMessageBundle();
		Locale locale = FacesUtils.getLocale();
		ClassLoader loader = FacesUtils.getClassLoader();
		String _str = FacesUtils.getString(appBundle, _bundle, _string, locale, loader);
		return _str;

	}

	/**
	 * @param _appBundle
	 * @param _bundle
	 * @param _string
	 * @param _locale
	 * @param _loader
	 * @return
	 */
	private static String getString(String _appBundle, String _bundle, String _string, Locale _locale,
			ClassLoader _loader) {
		String summary = null;
		// TODO
		if (summary == null)
			summary = "???" + _string + "???";
		return summary;
	}

	/**
	 * @param _string
	 * @param _message
	 */
	public static void addMessage(String _string, FacesMessage _message) {
		getFacesContext().addMessage(_string, _message);
	}
	
	/**
     * Get parameter value from request scope.
     *
     * @param name the name of the parameter
     * @return the parameter value
     */
    public static String getRequestParameter(String name) {
        return (String) FacesContext.getCurrentInstance().getExternalContext()
                                    .getRequestParameterMap().get(name);
    }

    /**
     * Add information message.
     *
     * @param msg the information message
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }

    /**
     * Add information message to a sepcific client.
     *
     * @param clientId the client id
     * @param msg the information message
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                                                     new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                                      msg, msg));
    }

    /**
     * Add error message.
     *
     * @param msg the error message
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }

    /**
     * Add error message to a sepcific client.
     *
     * @param clientId the client id
     * @param msg the error message
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId,
                                                     new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                                      msg, msg));
    }
    
    public static boolean isMessage() {
        return FacesContext.getCurrentInstance().getMessages().hasNext();
    }

	/**
	 * Tendo um Request e um Response dentro do um Servlet retorna o
	 * FacesContext.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public static FacesContext getFacesContext(HttpServletRequest request, HttpServletResponse response) {
		// Get current FacesContext.
		FacesContext facesContext = FacesContext.getCurrentInstance();

		// Check current FacesContext.
		if (facesContext == null) {

			// Create new Lifecycle.
			LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
					.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
			Lifecycle lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

			// Create new FacesContext.
			FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
					.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
			facesContext = contextFactory.getFacesContext(request.getSession().getServletContext(), request, response,
					lifecycle);

			// Create new View.
			UIViewRoot view = facesContext.getApplication().getViewHandler().createView(facesContext, "");
			facesContext.setViewRoot(view);

			// Set current FacesContext.
			FacesContextWrapper.setCurrentInstance(facesContext);
		}

		return facesContext;
	}

	// Wrap the protected FacesContext.setCurrentInstance() in a inner class.
	private static abstract class FacesContextWrapper extends FacesContext {
		protected static void setCurrentInstance(FacesContext facesContext) {
			FacesContext.setCurrentInstance(facesContext);
		}
	}


	/**
	 * @return
	 */
	public static Application getApplication() {
		return FacesContext.getCurrentInstance().getApplication();
	}


	/**
	 * <p>
	 * Leverage the Throwable.getStackTrace() method to produce a String version
	 * of the stack trace, with a "\n" before each line.
	 * </p>
	 * 
	 * @param e
	 *            the Throwable to obtain the stacktrace from
	 * 
	 * @return the String representation ofthe stack trace obtained by calling
	 *         getStackTrace() on the passed in exception. If null is passed in,
	 *         we return the empty String.
	 */
	public static String getStackTraceString(Throwable e) {
		if (null == e) {
			return "";
		}

		StackTraceElement[] stacks = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < stacks.length; i++) {
			sb.append(stacks[i].toString()).append('\n');
		}
		return sb.toString();
	}



	public static boolean prefixViewTraversal(FacesContext context, UIComponent root, TreeTraversalCallback action)
			throws FacesException {
		boolean keepGoing;
		if (keepGoing = action.takeActionOnNode(context, root)) {
			Iterator<UIComponent> kids = root.getFacetsAndChildren();
			while (kids.hasNext() && keepGoing) {
				keepGoing = prefixViewTraversal(context, kids.next(), action);
			}
		}
		return keepGoing;
	}

	public static interface TreeTraversalCallback {
		public boolean takeActionOnNode(FacesContext context, UIComponent curNode) throws FacesException;
	}



}