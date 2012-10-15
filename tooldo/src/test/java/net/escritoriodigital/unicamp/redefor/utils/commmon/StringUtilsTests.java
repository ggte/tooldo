/**
 *
 */
package net.escritoriodigital.unicamp.redefor.utils.commmon;

import static org.junit.Assert.assertEquals;

import java.io.File;

import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;

import org.junit.Test;

/**
 * @author andre
 *
 */
public class StringUtilsTests {

	@Test
	public void convertToCanonicalStringTest() {

		String input = "ä ë ï ç ? $ @ # _ * s";
		String esperado = "a_e_i_c________s";

		String result = StringUtils.convertToCanonicalString(input);

		assertEquals(result, esperado);
	}

}
