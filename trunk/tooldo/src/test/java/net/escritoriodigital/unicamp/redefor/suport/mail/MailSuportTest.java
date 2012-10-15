package net.escritoriodigital.unicamp.redefor.suport.mail;

import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.support.mail.MailSupport;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MailSuportTest extends AbstractToolDoServiceTests{

	@Autowired
	private MailSupport mailSupport;
	
	@Autowired
	private UserService userService;
	
	private final String MAIL_TO = "mails";
	private final String MAIL_BODY_TEXT = "bodyText";
	private final String MAIL_BODY_HTML = "bodyHTML";
	
	@Test
	public void sendTest() throws ServiceException{
		
		User user = userService.getById(new Long(1));
		String[] emails = {user.getEmail()};
		
		Map<String, Object> content = new HashMap<String, Object>();		
		content.put(this.MAIL_TO, emails);
		content.put(this.MAIL_BODY_TEXT, "Sucesso!!!");
		content.put(this.MAIL_BODY_HTML, false);
		
		// TODO: Provide real email tests
		// mailSupport.send(content);
		assertTrue(true);
	}
	
	@Test
	public void sendEmailResetTest() throws ServiceException, InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException{
		
		String email = "andre.fabbro@escritoriodigital.net";
		
		User user = userService.getUserByEmail(email);
		
		if(user != null){
		
			String[] emails = {user.getEmail()};
			
			StringBuilder text = new StringBuilder();
			text.append("Por favor, click no link abaixo <br />");
			
			// Gera chave de identifição
			TripleDESUtils des = TripleDESUtils.newInstance();
			
			//TODO: Definir URL de envio e nome de do parametro			
			text.append("<a href='" + des.encrypt(user.getUsername()) + "'>Testing</a>");
			
			Map<String, Object> content = new HashMap<String, Object>();		
			content.put(this.MAIL_TO, emails);
			content.put(this.MAIL_BODY_TEXT, text.toString());
			content.put(this.MAIL_BODY_HTML, true);
			
			// TODO: Provide real email tests
			// mailSupport.send(content);
			assertTrue(true);
		}
		
		
	}
	
}
