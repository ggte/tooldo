/**
 *
 */
package net.escritoriodigital.unicamp.redefor.utils.common;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.random;
import static java.lang.Math.round;
import static org.apache.commons.lang.StringUtils.leftPad;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Andre Fabbro
 *
 */
public class StringUtils implements Serializable {

	private static final long serialVersionUID = -4954222037926363566L;

	/**
	 * Converte o texto enviado como par‚metro em forma canonica, retirando
	 * simbolos, espaÁos e acentos
	 *
	 * @param text
	 * @return texto convertido
	 */
	public static String convertToCanonicalString(String text) {
		if (text != null) {
			text = text.toLowerCase();
			return text.replaceAll("[„‚‡·‰]", "a").replaceAll("[ÍËÈÎ]", "e")
					.replaceAll("[ÓÏÌÔ]", "i").replaceAll("[ıÙÚÛˆ]", "o")
					.replaceAll("[˚˙˘¸]", "u").replaceAll("[√¬¿¡ƒ]", "A")
					.replaceAll("[ »…À]", "E").replaceAll("[ŒÃÕœ]", "I")
					.replaceAll("[’‘“”÷]", "O").replaceAll("[€Ÿ⁄‹]", "U")
					.replace('Á', 'c').replace('«', 'C').replace('Ò', 'n')
					.replace("'", "").replace('—', 'N').replace("!", "")
					.replace("~", "").replace("`", "").replace("@", "")
					.replace("#", "").replace("$", "").replace("%", "")
					.replace("^", "").replace("&", "").replace("*", "")
					.replace("(", "").replace(")", "").replace("+", "")
					.replace("=", "").replace(":", "").replace(";", "")
					.replace("<", "").replace(">", "").replace("?", "")
					.replace("\\", "").replace(' ', '_');
		}
		return null;
	}

	/**
	 * Retorna uma string renderizando as tags latex cgi
	 *
	 * @param content
	 * @param cgiPath
	 * @return string renderizada
	 */
	public static String renderizeLatexTags(String content, String cgiPath) {

		if (content.contains("[tex]")) {
			content = content.replace("[tex]", "<img src='" + cgiPath + "?");
			content = content.replace("[/tex]", "' align='top'/>");
		}

		return content;
	}

	/**
	 * @param d
	 * @return true se o inteiro enviado como par‚metro È igual a zero ou nulo
	 */
	public static boolean isZeroOrNullLong(Long d) {

		if (d == null)
			return true;
		else {
			if (d.equals(new Long(0)))
				return true;
		}

		return false;
	}

	/**
	 * @param d
	 * @return true se o inteiro enviado como par‚metro È igual a zero ou nulo
	 */
	public static boolean isZeroOrNullInteger(Integer d) {

		if (d == null)
			return true;
		else {
			if (d.equals(new Integer(0)))
				return true;
		}

		return false;
	}

	/**
	 * @param d
	 * @return true se o double enviado como par‚metro È igual a zero ou nulo
	 */
	public static boolean isZeroOrNullDouble(Double d) {

		if (d == null)
			return true;
		else {
			if (d.equals(new Double(0)))
				return true;
		}

		return false;
	}

	/**
	 * @param s
	 * @return true se a string enviada como par‚metro n„o È nula ou vazia
	 */
	public static boolean isNotEmptyOrNullString(String s) {

		if (s != null && !"".equals(s))
			return true;

		return false;
	}

	/**
	 * @param s
	 * @return true se a string enviada como par‚metro È nula ou vazia
	 */
	public static boolean isEmptyOrNullString(String s) {
		if (s == null)
			return true;
		else if ("".equals(s.trim()))
			return true;

		return false;
	}

	/**
	 * Formata um nome de arquivo, ex.: C:/pasta/arquivo.doc para arquivo.doc
	 *
	 * @param fileName
	 * @return nome do arquivo
	 */
	public static String formatFileNameWithFullPath(String fileName) {

		int indexSeparator = fileName.lastIndexOf(File.separator);
		if (indexSeparator > 0)
			fileName = fileName.substring(indexSeparator + 1);

		return fileName;
	}

	/**
	 * Gera uma String randÙmica de comprimento fixo (length)
	 *
	 * @param length
	 * @return
	 */
	public static String randomString(int length) {

		StringBuffer sb = new StringBuffer();
		for (int i = length; i > 0; i -= 12) {
			int n = min(12, abs(i));
			sb.append(leftPad(Long.toString(round(random() * pow(36, n)), 36),
					n, '0'));
		}

		return sb.toString();
	}

	/**
	 * Gera uma String randÙmica com comprimento entre lo (menor) e hi (maior)
	 *
	 * @param lo
	 * @param hi
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String randomString(int lo, int hi) {
		int n = randomInteger(lo, hi);
		byte b[] = new byte[n];
		for (int i = 0; i < n; i++)
			b[i] = (byte) randomInteger('a', 'z');
		return new String(b, 0);
	}

	/**
	 * Gera um inteiro randÙmico dentro do intervalo lo e hi
	 *
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static int randomInteger(int lo, int hi) {
		java.util.Random rn = new java.util.Random();
		int n = hi - lo + 1;
		int i = rn.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}

	/**
	 * Gera um double randÙmico de duas casas decimais no intervalo lo e hi
	 *
	 * @param lo
	 * @param hi
	 * @return
	 */
	public static Double randomDouble(int lo, int hi) {
		int inteiro = randomInteger(lo, hi);
		int decimal = randomInteger(0, 99);
		return Double.valueOf(inteiro + "." + decimal);
	}

	/**
	 * Remove simbolos da String enviada como par‚metro
	 */
	public static String removeStringSymbols(String str) {

		if (str != null) {
			if (!str.trim().equals("")) {
				str = str.replace(".", "");
				str = str.replace("-", "");
				str = str.replace("/", "");
				str = str.replace("@", "");
				str = str.replace(",", "");
				str = str.replace("\\", "");
				str = str.replace(" ", "");
			}

		}

		return str;
	}

	/**
	 * MÈtodo que converte uma String em Date, a String deve estar no formato do
	 * pattern enviado como par‚metro
	 *
	 * @param str
	 *            , pattern
	 * @return Date data
	 */
	public static Date StringToDate(String str, String pattern) {

		Date data = null;

		DateFormat dateFormat = new SimpleDateFormat(pattern);

		try {
			data = dateFormat.parse(str);
		} catch (ParseException e) {

		}

		return data;
	}

	/**
	 * MÈtodo que converte uma data em uma String no formato do pattern enviado
	 * como par‚metro
	 *
	 * @param data
	 *            , pattern
	 * @return string formatada
	 */
	public static String DateToString(Date data, String pattern) {

		String str = "";

		DateFormat dateFormat = new SimpleDateFormat(pattern);

		str = dateFormat.format(data);

		return str;
	}

	/**
	 * FunÁ„o para retornar um BigDecimal a partir de uma String no formato de
	 * Moeda, removendo os pontos e substituindo as vÌrgulas decimais por pontos
	 *
	 * @param str
	 * @return o BigDecimal da string, caso n„o seja possÌvel transformar
	 *         (NumberFormatException), retorna null
	 */
	public static BigDecimal StringToDecimal(String str) {

		String strTmp = "";
		String arrChar[];

		// substitui todos os pontos por chars vazios
		str = str.replace('.', ' ');

		// aloca a string em um vetor para remover os espaÁos
		arrChar = str.split(" ");

		for (int i = 0; i < arrChar.length; i++) {
			strTmp += arrChar[i];
		}

		/*
		 * depois de substituir os pontos de mil, substitui as vÌrgulas por
		 * pontos para entrar no padr„o do BigDecimal
		 */
		strTmp = strTmp.replace(',', '.');

		BigDecimal bgd_ret = null;
		try {
			bgd_ret = new BigDecimal(strTmp);
		} catch (NumberFormatException e) {
			// vai retornar null no mÈtodo
		}

		return bgd_ret;
	}

	/**
	 * FunÁ„o para retornar uma String a partir do BigDecimal enviado, com todos
	 * os pontos separadores de mil
	 */
	public static String DecimalToString(BigDecimal num, int decimals) {

		String str_num = "";

		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setDecimalFormatSymbols(new DecimalFormatSymbols(
				new Locale("pt", "BR")));
		decimalFormat.setMinimumFractionDigits(decimals);
		decimalFormat.setMaximumFractionDigits(decimals);

		if (num != null) {

			num = num.setScale(decimals, BigDecimal.ROUND_HALF_EVEN);
			str_num = decimalFormat.format(num);

		}

		return str_num;

	}

	/**
	 * Formata a string enviada como par‚metro com a m·scara do CNPJ
	 * (99.999.999/9999-99)
	 *
	 * @param cnpj
	 * @return a string formatada
	 */
	public static String formataCNPJToString(String cnpj) {

		String retorno = "";

		cnpj = removeStringSymbols(cnpj);

		if (cnpj != null && !"".equals(cnpj)) {

			int l = cnpj.length();
			String tmp = "";

			for (int i = 0; i < l; i++) {
				tmp += cnpj.charAt(i);
				if (i == 1 || i == 4)
					tmp += ".";
				if (i == 7)
					tmp += "/";
				if (i == 11)
					tmp += "-";
			}

			retorno = tmp;
		}

		return retorno;
	}

	/**
	 * Aplica m·scara do CPF
	 *
	 * @param cpf
	 * @return string formatada
	 */
	public static String formataCPFToString(String cpf) {

		String retorno = "";

		cpf = removeStringSymbols(cpf);

		if (cpf != null && !"".equals(cpf)) {

			int l = cpf.length();
			String tmp = "";

			for (int i = 0; i < l; i++) {
				tmp += cpf.charAt(i);
				if (i == 2 || i == 5)
					tmp += ".";
				if (i == 8)
					tmp += "-";
			}

			retorno = tmp;
		}

		return retorno;
	}

}
