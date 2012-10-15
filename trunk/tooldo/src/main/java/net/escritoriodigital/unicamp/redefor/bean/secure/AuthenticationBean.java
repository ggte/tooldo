package net.escritoriodigital.unicamp.redefor.bean.secure;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Qualifier("authenticationBean")
public class AuthenticationBean {

	private String j_username = "";
	private String j_password = "";

	// managed properties for the login page, username/password/etc...

	// This is the action method called when the user clicks the "login" button
	public String authenticates() throws IOException, ServletException {
		
			ExternalContext context = FacesContext.getCurrentInstance()
					.getExternalContext();
	
			RequestDispatcher dispatcher = ((ServletRequest) context.getRequest())
					.getRequestDispatcher("/j_spring_security_check");
			
			dispatcher.forward((ServletRequest) context.getRequest(),
					(ServletResponse) context.getResponse());
	
			FacesContext.getCurrentInstance().responseComplete();
			// It's OK to return null here because Faces is just going to exit.
		return null;

	}

	public String getErrorMessage() {

		String message = "Não foi possível realizar a autenticação.";

		Exception e = (Exception) FacesUtils
				.getExternalContext()
				.getSessionMap()
				.get(
						AbstractAuthenticationProcessingFilter.SPRING_SECURITY_LAST_EXCEPTION_KEY);

		if (e instanceof BadCredentialsException) {

			message = "Usuário ou senha inválida.";
		}

		FacesUtils.addMessage(message, new FacesMessage(FacesMessage.SEVERITY_FATAL,message, message));
		
		return "";
	}

	public boolean isAuthenticated() {
		boolean result = false;
		SecurityContext context = SecurityContextHolder.getContext();
		if (context instanceof SecurityContext) {
			Authentication authentication = context.getAuthentication();
			if (authentication instanceof AnonymousAuthenticationToken) {
				// not authenticated
			} else if (authentication instanceof Authentication) {
				result = true;
			}
		}
		return result;
	}
	
	

	/**
	 * @return the j_username
	 */
	public String getJ_username() {
		return j_username;
	}

	/**
	 * @param jUsername
	 *            the j_username to set
	 */
	public void setJ_username(String jUsername) {
		j_username = jUsername;
	}

	/**
	 * @return the j_password
	 */
	public String getJ_password() {
		return j_password;
	}

	/**
	 * @param jPassword
	 *            the j_password to set
	 */
	public void setJ_password(String jPassword) {
		j_password = jPassword;
	}

}
