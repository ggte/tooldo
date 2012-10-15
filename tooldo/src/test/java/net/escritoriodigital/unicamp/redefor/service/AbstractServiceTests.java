/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author andre
 *
 * ContextConfiguration(locations = { "classpath:/applicationContext-business.xml","classpath:/applicationContext-security.xml" })
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/applicationContext-business.xml" })
public class AbstractServiceTests {

	@Before
	public void runBeforeEveryTest() {
		// executado uma vez antes de cada teste
	}

	@After
	public void runAfterEveryTest() {
		// executado uma vez depois de cada teste
	}

	@BeforeClass
	public static void runBeforeClass() {
		// executado uma vez antes de todos os testes serem executados
	}

	@AfterClass
	public static void runAfterClass() {
		// executado uma vez depois de todos os testes serem executados
	}

}
