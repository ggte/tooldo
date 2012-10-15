package net.escritoriodigital.unicamp.redefor.bean;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.component.SortOrder;

import com.google.common.collect.Maps;

import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.utils.secure.AccessManagerUtils;
import net.escritoriodigital.unicamp.redefor.utils.secure.SecurityContextAware;

public class BaseBean extends SecurityContextAware {

	protected static final String SUCCESS 	= "success";

	protected static final String ERROR 	= "error";
	
	protected User credential = new User();
	
	protected Map<String, SortOrder> sortOrders = Maps.newHashMapWithExpectedSize(1);
    
	protected Map<String, String> filterValues = Maps.newHashMap();
    
	protected String sortProperty;
	
	public void toggleSort() {
	    for (Entry<String, SortOrder> entry : sortOrders.entrySet()) {
	        SortOrder newOrder;
	        
	        if (entry.getKey().equals(sortProperty)) {
	            if (entry.getValue() == SortOrder.ascending) {
	                newOrder = SortOrder.descending;
	            } else {
	                newOrder = SortOrder.ascending;
	            }
	        } else {
	            newOrder = SortOrder.unsorted;
	        }
	        
	        entry.setValue(newOrder);
	    }
	}
	
	public Map<String, SortOrder> getSortOrders() {
        return sortOrders;
    }
    
    public Map<String, String> getFilterValues() {
        return filterValues;
    }
    
    public String getSortProperty() {
        return sortProperty;
    }
    
    public void setSortProperty(String sortPropety) {
        this.sortProperty = sortPropety;
    }
	
	public User getCredential() {
		credential = getContextUser();
		return credential;
	}

	public org.springframework.security.core.userdetails.User getUserSpring() {
		return getUserAcegi();
	}
    
	public void setCredential(User credential) {
		this.credential = credential;
	}

	/**
	 * @param user, roles
	 * @return true se o user possui todas as roles enviadas como parametro
	 */
	public  Boolean ifAllGranted(String roles) {
		return AccessManagerUtils.ifAllGranted(getUserSpring(), roles);
	}

	/**
	 * @param user
	 * @param roles
	 * @return true se o user possuir alguma das roles enviadas como parametro
	 */
	public  Boolean ifAnyGranted(String roles) {
		return AccessManagerUtils.ifAnyGranted(getUserSpring(), roles);
	}

	/**
	 *
	 * @param user
	 * @param roles
	 * @return true se o user não possuir nenhuma das roles enviadas como parametro
	 */
	public  Boolean ifNotGranted(String roles) {
		return AccessManagerUtils.ifNotGranted(getUserSpring(), roles);
	}

	/**
	 * @return true se há mensagens de erros no contexto
	 */
	public Boolean getHasErrorMessages() {
		for (Iterator<FacesMessage> messages = getFacesContext().getMessages(); messages.hasNext();) {
			FacesMessage message = messages.next();
			if(message.getSeverity().equals(FacesMessage.SEVERITY_ERROR))
				return true;
		}
		return false;
	}

	/**
	 * @return o objeto request
	 */
	protected HttpServletRequest getRequest() {
		return (HttpServletRequest) getFacesContext().getExternalContext().getRequest();
	}

	/**
	 * @return o objeto FacesContext
	 */
	protected FacesContext getFacesContext() {
		return FacesContext.getCurrentInstance();
	}

	/**
	 * Adiciona uma mensagem de um componente (pelo id) ao contexto da aplicação
	 * @param facesMessage
	 * @param idComponent
	 */
	protected void addMessage(FacesMessage facesMessage, String idComponent) {
		UIComponent component = null;
		if(idComponent != null)
			component = findComponent(getFacesContext().getViewRoot(), idComponent);
		addMessage(facesMessage, component);
	}

	/**
	 * Adiciona uma mensagem de um componente (pelo objeto) ao contexto da aplicação
	 * @param facesMessage
	 * @param component
	 */
	private void addMessage(FacesMessage facesMessage, UIComponent component) {
		if(component != null) getFacesContext().addMessage(component.getClientId(getFacesContext()), facesMessage);
		else getFacesContext().addMessage(null, facesMessage);
	}

	/**
	 * @param parent
	 * @param id
	 * @return um componente a partir de seu id e de seu parent
	 */
	protected UIComponent findComponent(UIComponent parent, String id) {
		if (id.equals(parent.getId())) {
			return parent;
		}

		Iterator<UIComponent> kids = parent.getFacetsAndChildren();
		while (kids.hasNext()) {
			UIComponent kid = kids.next();
			UIComponent found = findComponent(kid, id);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

}
