package net.escritoriodigital.unicamp.redefor.utils.secure;


import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * @author Leandro Concon
 * @created 08/07/2010
 */
public abstract class AccessManagerUtils {
	
	public static final String ROLE_ADMINISTRATOR 		= "ROLE_ADMINISTRATOR";
	
	/**
	 * @param user, roles
	 * @return true se o user possui todas as roles enviadas como parametro
	 */
	public static Boolean ifAllGranted(User user, String roles) {
		
		if(user == null) return false;
		
		String[] role_list = roles.split(",");
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		int qtd_found = 0;
		for (String role : role_list) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if(grantedAuthority.toString().equals(role.trim())) qtd_found++;
			}
		}
		
		return (qtd_found == role_list.length) ? true : false;
	}
	
	/**
	 * @param user
	 * @param roles
	 * @return true se o user possuir alguma das roles enviadas como parametro
	 */
	public static Boolean ifAnyGranted(User user, String roles) {
		
		if(user == null) return false;
		
		String[] role_list = roles.split(",");
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		int qtd_found = 0;
		for (String role : role_list) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if(grantedAuthority.toString().equals(role.trim())) {
					qtd_found++; break;
				}
			}
		}
		
		return (qtd_found > 0) ? true : false;
	}
	
	/**
	 * 
	 * @param user
	 * @param roles
	 * @return true se o user não possuir nenhuma das roles enviadas como parametro
	 */
	public static Boolean ifNotGranted(User user, String roles) {
		
		if(user == null) return false;
		
		String[] role_list = roles.split(",");
		Collection<GrantedAuthority> authorities = user.getAuthorities();
		
		int qtd_found = 0;
		for (String role : role_list) {
			for (GrantedAuthority grantedAuthority : authorities) {
				if(grantedAuthority.toString().equals(role.trim())) qtd_found++;
			}
		}
		
		return (qtd_found == 0) ? true : false;
	}

}
