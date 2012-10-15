/**
 * 
 */
package net.escritoriodigital.unicamp.redefor.service.theme;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.AbstractToolDoServiceTests;
import net.escritoriodigital.unicamp.redefor.service.content.TypeContentService;
import net.escritoriodigital.unicamp.redefor.service.course.CourseService;
import net.escritoriodigital.unicamp.redefor.service.discipline.DisciplineService;
import net.escritoriodigital.unicamp.redefor.service.page.PageService;
import net.escritoriodigital.unicamp.redefor.service.topic.TopicService;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author andre
 *
 */
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class ThemeServiceTest extends AbstractToolDoServiceTests {
	
	@Autowired
	private ThemeService service;
	
	@Autowired
	private DisciplineService disciplineService;
	
	@Autowired
	private CourseService courseService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private PageService pageService;
	
	@Autowired
	private TypeContentService typeContentService;
	
	@Test
	public void duplicateThemeTest() throws Exception {
				
		Course course = courseService.getById(new Long(1));
		
		Discipline discipline = generateDiscipline();
		discipline.setCourse(course);
		
		disciplineService.save(discipline);
		
		Theme theme = generateTheme();
		theme.setDiscipline(discipline);
		service.save(theme);
		
		Topic topic = generateTopic();
		topic.setTheme(theme);
		topicService.save(topic);
		
		topic.setPages(new ArrayList<Page>());
		
		for (int i = 0; i < 3; i++) {
			Page page = generatePage();
			page.setPosition(i+1);
			
			page.getContent().setTypeContent(typeContentService.getById(new Long(1)));
			page.setTopic(topic);
			
			pageService.save(page);
			
			topic.getPages().add(page);
		}
		
		topicService.save(topic);
		
		theme.setTopics(new ArrayList<Topic>());
		theme.getTopics().add(topic);
		theme.setPublished(true);
		
		Theme clone = service.duplicateTheme(theme);
		
		assertEquals(clone.getReplaceId(), theme.getId());
	}

}
