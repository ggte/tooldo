/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.content;

import static org.junit.Assert.assertTrue;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andre
 *
 */
@TransactionConfiguration(defaultRollback = true)
@Transactional
public class RoadMapContentServiceTest extends AbstractToolDoServiceTests {

	@Autowired
	private RoadMapContentService service;

	private Course course = new Course();

	@Test
	public void testSaveRoadMap() throws Exception {
		// TODO: implementar testes!!!
		assertTrue(true);

	}

}
