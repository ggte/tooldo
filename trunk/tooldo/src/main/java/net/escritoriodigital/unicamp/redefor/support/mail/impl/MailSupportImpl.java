package net.escritoriodigital.unicamp.redefor.support.mail.impl;

import java.util.Map;

import javax.mail.internet.MimeMessage;

import net.escritoriodigital.unicamp.redefor.support.mail.MailSupport;

import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component("mailSupport")
public class MailSupportImpl implements MailSupport {

	private static final long serialVersionUID = -5266456164769733019L;

	private JavaMailSender mailSender;

	private SimpleMailMessage message;

	private final String MAIL_TO = "mails";

	private final String MAIL_BODY_TEXT = "bodyText";

	private final String MAIL_BODY_HTML = "bodyHTML";

	@Override
	public void send(final Map<String, Object> content) throws MailException{
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper mime = new MimeMessageHelper(mimeMessage);

				mime.setFrom(message.getFrom());
				mime.setSubject(message.getSubject());

				String[] to = (String[]) content.get(MAIL_TO);

				if((to == null) || (to.length == 0)) new MailParseException("Parametro 'mails' inválido");

				String bodyText = (String) content.get(MAIL_BODY_TEXT);

				if((bodyText == null) || (bodyText.equals(""))) new MailParseException("Parametro 'bodyText' inválido");

				boolean bodyHTML = (Boolean) content.get(MAIL_BODY_HTML);

				mime.setTo(to);
				mime.setText(bodyText, bodyHTML);
			}
		};

		this.mailSender.send(preparator);
	}

	/**
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * @param mailSender the mailSender to set
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * @return the message
	 */
	public SimpleMailMessage getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}



}
