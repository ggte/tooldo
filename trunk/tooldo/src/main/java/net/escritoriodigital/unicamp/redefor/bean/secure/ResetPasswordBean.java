package net.escritoriodigital.unicamp.redefor.bean.secure;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.NotNull;

import net.escritoriodigital.components.constraints.FieldMatch;
import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.constant.ApplicationConstants;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.support.mail.MailSupport;
import net.escritoriodigital.unicamp.redefor.utils.criptography.TripleDESUtils;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.hibernate.validator.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("request")
@Qualifier("resetPasswordBean")
@FieldMatch.List( { @FieldMatch(first = "password", second = "passwordConfirmation", message = "{validator.fieldmatch.password}") })
public class ResetPasswordBean implements Serializable {

	private static final long serialVersionUID = -7822868448003937932L;

	@Autowired
	private UserService userService;

	@Autowired
	private MailSupport mailSupport;

	@NotNull
	private String password;

	@NotNull
	private String passwordConfirmation;
	
	@NotNull
	@Email(message = "{validator.email}")
	private String email;
	
	private String hashCode;

	private Validator validator;

	private final String MAIL_TO = "mails";

	private final String MAIL_BODY_TEXT = "bodyText";

	private final String MAIL_BODY_HTML = "bodyHTML";

	/**
	 * Reseta a senha do usuário a partir do username criptografado
	 *
	 * @return
	 * @throws Exception
	 */
	public String reset() throws Exception {

		if (password.equals(passwordConfirmation)) {
			
			Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
			hashCode = params.get("hashCode");
			
			try {
				
				userService.resetPassword(password, hashCode);
				FacesUtils.addInfoMessage("Sua senha foi atualizada com sucesso.");
				
			} catch (ServiceException e) {
				FacesUtils.addErrorMessage(e.getMessage());
				return "resetPassword";
			}

		} else {
			FacesUtils.addErrorMessage("A senha não confere.");
			return "resetPassword";
		}

		return "workflow-main-page";
	}

	/**
	 * Envia o e-mail para o usuário resetar sua senha
	 *
	 * @throws ServiceException
	 */
	public void send() throws ServiceException {

		User user = userService.getUserByEmail(email);

		if (user != null) {

			String[] emails = { user.getEmail() };

			StringBuilder text = new StringBuilder();

			// Gera chave de identifição
			try {
				TripleDESUtils des = TripleDESUtils.newInstance();

				//Definir URL de envio e nome de do parametro

				text.append("<a href='http://"
						+ FacesUtils.getHostname()
						+ FacesUtils.getExternalContext()
								.getRequestContextPath()
						+ "/pages/secure/resetPassword.jsf?hash="
						+ URLEncoder.encode(des.encrypt(user.getUsername() 
						+ ApplicationConstants.SALT_VALUE), "ISO-8859-1")
						+ "'>Clique aqui</a> para resetar sua senha.");

				Map<String, Object> content = new HashMap<String, Object>();
				content.put(this.MAIL_TO, emails);
				content.put(this.MAIL_BODY_TEXT, text.toString());
				content.put(this.MAIL_BODY_HTML, true);

				mailSupport.send(content);

				FacesUtils
						.addInfoMessage("Uma mensagem automática foi enviada ao seu e-mail, por favor verifique.");

			} catch (Exception e) {
				FacesUtils
						.addErrorMessage("Sistema indisponível, tente novamente mais tarde.");
			}
		} else {
			FacesUtils
					.addErrorMessage("Não foi encontrado nenhum usuário com o e-mail indicado, contate o administrador.");
			// validateConfirmedEmail();
		}

		this.email = "";
	}

	public Validator getValidator() {
		if (validator == null) {
			ValidatorFactory factory = Validation
					.buildDefaultValidatorFactory();
			validator = factory.getValidator();
		}
		return validator;
	}

	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	public void validateConfirmedPassword() {
		Set<ConstraintViolation<ResetPasswordBean>> constraintViolations = getValidator()
				.validate(this);
		if (constraintViolations.size() > 0) {
			ConstraintViolation<ResetPasswordBean> cv = constraintViolations
					.iterator().next();
			if (cv.getMessageTemplate().equals(
					"{validator.fieldmatch.password}")) {
				FacesUtils.addErrorMessage("A senha não confere.");
			}
		}
	}

	public void validateConfirmedEmail() {
		Set<ConstraintViolation<ResetPasswordBean>> constraintViolations = getValidator()
				.validate(this);
		if (constraintViolations.size() > 0) {
			ConstraintViolation<ResetPasswordBean> cv = constraintViolations
					.iterator().next();
			if (cv.getMessageTemplate().equals("{validator.email}")) {
				FacesUtils.addErrorMessage("E-mail inválido.");
			}
		}
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public String getHashCode() {
		return hashCode;
	}

	public void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}

}
