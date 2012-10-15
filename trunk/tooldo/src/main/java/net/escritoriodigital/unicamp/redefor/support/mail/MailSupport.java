package net.escritoriodigital.unicamp.redefor.support.mail;

import java.io.Serializable;
import java.util.Map;


public interface MailSupport extends Serializable {

	/**
	 * Suporte para disparo de e-mail
	 * Map <"mails", String[]>
	 * 	Lista de e-mails
	 * Map <"bodyText", String>
	 * 	Conteudo do corpo do e-mail
	 * Map <"bodyHTML", boolean>
	 * 	Conteudo em html
	 * */
	public abstract void send(Map<String, Object> content);

}
