package net.escritoriodigital.unicamp.redefor.utils.secure;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Leandro Concon
 *
 */
public abstract class SecurityContextAware {
		
	@Autowired
	protected UserService userService;
	
	private User contextUser;
	
	/**
	 * @return the usuario
	 */
	public User getContextUser() {
		if(contextUser != null) {
			if(!contextUser.getName().equals(getUserNameAcegi())) refreshUser();
		} else {
			refreshUser();
		}
		return contextUser;
	}
	
	protected String getUserNameAcegi() {
		org.springframework.security.core.userdetails.User userS = getUserAcegi();
		if(userS != null) return userS.getUsername();
		return "";
	}
	
	protected org.springframework.security.core.userdetails.User getUserAcegi() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();
		if(auth != null) {
			if(auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) return (org.springframework.security.core.userdetails.User) auth.getPrincipal();
			else {
				//GrantedAuthority[] authorities = {new GrantedAuthorityImpl("ROLE_ANONYMOUS")};
				//return new org.springframework.security.core.userdetails.User((String) auth.getPrincipal(), "", false, authorities);
			}
		}
		return null;
	}
	
	protected void refreshUser() {
		org.springframework.security.core.userdetails.User userS = getUserAcegi();
		if(userS != null) {
			try {
				contextUser = userService.getUserByUsername(userS.getUsername());
			} catch (ServiceException e) {
				//não faz nada
			}
		}
	}	
}
