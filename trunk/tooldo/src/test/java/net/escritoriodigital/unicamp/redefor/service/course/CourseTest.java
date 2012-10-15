package net.escritoriodigital.unicamp.redefor.service.course;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@TransactionConfiguration(defaultRollback = true)
@Transactional
public class CourseTest extends AbstractToolDoServiceTests {

	@Autowired
	private CourseService service;



	@Test
	public void testInsertCourse() {


		try {

			Course entity = generateCourse();
			service.save(entity);

			Course entity2 = generateCourse();
			service.save(entity2);

			Course entity3 = generateCourse();
			service.save(entity3);

			Course entity4 = generateCourse();
			service.save(entity4);

		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
