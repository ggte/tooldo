package net.escritoriodigital.components.utils.secure;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

/**
 * @author Leandro Concon
 * @category Secure
 * @created 21/06/2010
 * @description Criptografia de senha
 */
public class PasswordEncoder {

	
	public static String getMd5PasswordEncoder(String password){
		
		org.springframework.security.authentication.encoding.PasswordEncoder encoder = new Md5PasswordEncoder();
		return encoder.encodePassword(password, null);	
		
	}
	
	public static String getShaPasswordEncoder(String password){
		
		org.springframework.security.authentication.encoding.PasswordEncoder encoder = new ShaPasswordEncoder();
		return encoder.encodePassword(password, null);	
		
	}
	
}
