package net.escritoriodigital.unicamp.redefor.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.utils.secure.PasswordEncoder;
import net.escritoriodigital.unicamp.redefor.model.content.AlternativeQuestion;
import net.escritoriodigital.unicamp.redefor.model.content.Content;
import net.escritoriodigital.unicamp.redefor.model.content.QuestionContent;
import net.escritoriodigital.unicamp.redefor.model.content.TypeContent;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.page.Page;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.Role;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.model.topic.Topic;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class AbstractToolDoServiceTests extends AbstractServiceTests {

	@Autowired
	protected UserService userService;

	protected void authenticates(UserService service) throws Exception {
		User user = null;

		try {
			user = service.getById(new Long(1));
		} catch (Exception e) {
			throw new RuntimeException("Erro na Autenticação do Usuário no Teste!", e);
		}

		//busca as roles do usuário
		Profile profile = user.getProfile();
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
		for (Role role : profile.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	protected User getUserAuthor() throws ServiceException {
		User author = null;
		Collection<User> autores = userService.getUserByProfile(Profile.PROFESSOR_AUTOR_ID);
		for (User user : autores) {
			author = user;
			break;
		}
		return author;
	}


	protected Course generateCourse() {
		Course entity = new Course();
		entity.setCreated(new Date());
		entity.setDescription(StringUtils.randomString(200));
		entity.setEnabled(true);
		entity.setName(StringUtils.randomString(20));

		return entity;
	}

	protected Discipline generateDiscipline() throws ServiceException {
		Discipline entity = new Discipline();
		entity.setCreated(new Date());
		entity.setDescription(StringUtils.randomString(50));
		entity.setEnabled(true);
		entity.setName(StringUtils.randomString(10));
		entity.setOwner(getUserAuthor());

		return entity;
	}

	protected Theme generateTheme() throws ServiceException {
		Theme entity = new Theme();
		entity.setCreated(new Date());
		entity.setDescription(StringUtils.randomString(50));
		entity.setCssFile("style-azul.css");
		entity.setEnabled(true);
		entity.setName(StringUtils.randomString(10));
		entity.setAuthor(getUserAuthor());

		return entity;
	}

	protected Topic generateTopic() throws ServiceException{
		Topic entity = new Topic();
		entity.setCreated(new Date());
		entity.setDescription(StringUtils.randomString(100));
		entity.setEnabled(true);
		entity.setName(StringUtils.randomString(10));
		entity.setAuthor(getUserAuthor());
		entity.setPosition(StringUtils.randomInteger(1, 10));

		return entity;
	}

	protected Page generatePage() throws ServiceException{
		Page entity = new Page();
		entity.setCreated(new Date());
		entity.setDescription(StringUtils.randomString(100));
		entity.setEnabled(true);
		entity.setName(StringUtils.randomString(20));
		entity.setAuthor(getUserAuthor());
		entity.setContent(generateContent());
		entity.getContent().setPage(entity);

		return entity;
	}

	private Content generateContent() {
		Content entity = new Content();
		
		entity.setCreated(new Date());
		
		entity.setContent(StringUtils.randomString(200));
		
		QuestionContent q1 = generateQuestionContent();
		QuestionContent q2 = generateQuestionContent();
		
		entity.addQuestion(q1);
		entity.addQuestion(q2);

		entity.setTypeContent(generateTypeContent());
		
		return entity;
	}
	
	private QuestionContent generateQuestionContent() {
		QuestionContent entity = new QuestionContent();
		
		entity.setQuestion(StringUtils.randomString(50));
		AlternativeQuestion a1 = generateAlternativeQuestion();
		a1.setCorrect(true);
		AlternativeQuestion a2 = generateAlternativeQuestion();
		a2.setCorrect(true);
		
		entity.addAlternative(a1);
		entity.addAlternative(a2);
		
		return entity;
	}
	
	private AlternativeQuestion generateAlternativeQuestion() {
		AlternativeQuestion entity = new AlternativeQuestion();
		entity.setAlternative(StringUtils.randomString(50));
		entity.setComments(StringUtils.randomString(50));		
		return entity;
	}

	private TypeContent generateTypeContent(){
		TypeContent entity = new TypeContent();
		entity.setName(StringUtils.randomString(10));
		entity.setTypeContent(StringUtils.randomString(100));
		return entity;
	}

	protected User generateUser(){
		User entity = new User();
		entity.setName(StringUtils.randomString(20));
		entity.setCreated(new Date());
		entity.setEnabled(true);
		entity.setEmail(StringUtils.randomString(20));
		String password = PasswordEncoder.getMd5PasswordEncoder("test");
		entity.setPassword(password);
		entity.setPhonenumber(StringUtils.randomString(10));
		entity.setUsername(StringUtils.randomString(10));

		return entity;
	}

	protected Profile generateProfile(){
		Profile entity = new Profile();
		entity.setName(StringUtils.randomString(20));

		return entity;
	}

	protected Role generateRole(){
		Role entity = new Role();
		entity.setName(StringUtils.randomString(20));

		return entity;
	}
}
