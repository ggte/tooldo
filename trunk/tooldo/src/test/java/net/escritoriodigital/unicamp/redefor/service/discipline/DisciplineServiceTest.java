/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.discipline;

import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;
import net.escritoriodigital.unicamp.redefor.service.course.CourseService;

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
public class DisciplineServiceTest extends AbstractToolDoServiceTests {

	@Autowired
	private DisciplineService service;

	@Autowired
	private CourseService courseService;

	@Test
	public void testSaveDiscipline() throws Exception {

		Course course = courseService.getById(new Long(1));

		Discipline disciplina = this.generateDiscipline();
		disciplina.setDescription("Testando inclusão de uma disciplina");
		disciplina.setEnabled(true);
		disciplina.setName("Natação em piscina com água");

		disciplina.setCourse(course);

		service.save(disciplina);

	}

}
