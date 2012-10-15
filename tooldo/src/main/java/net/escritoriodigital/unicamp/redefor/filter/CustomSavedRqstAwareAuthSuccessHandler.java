/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.filter;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.constant.ApplicationConstants;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * @author andrefabbro
 *
 */
public class CustomSavedRqstAwareAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Autowired
	protected UserService userService;
	
	@Override
	protected String determineTargetUrl(HttpServletRequest request,
			HttpServletResponse response) {
		
		String target = "/pages/secure/main.jsf";
		User user = getUserApplication();
		
		if(user.getChangePassword()) {
			
			try {
				
				TripleDESUtils des = TripleDESUtils.newInstance();
				String hash = des.encrypt(user.getUsername() + ApplicationConstants.SALT_VALUE);
				target = "/pages/secure/resetPassword.jsf?hash=" + URLEncoder.encode(hash, "ISO-8859-1");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return target;
	}
	
	protected User getUserApplication() {
		org.springframework.security.core.userdetails.User userS = getUserSpring();
		
		if(userS != null) {
			try {
				User user = userService.getUserByUsername(userS.getUsername());
				return user;
			} catch (ServiceException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	protected org.springframework.security.core.userdetails.User getUserSpring() {
		SecurityContext sc = SecurityContextHolder.getContext();
		Authentication auth = sc.getAuthentication();
		if(auth != null) {
			if(auth.getPrincipal() instanceof org.springframework.security.core.userdetails.User) 
				return (org.springframework.security.core.userdetails.User) auth.getPrincipal();
		}
		return null;
	}
	

}
