package net.escritoriodigital.unicamp.redefor.bean.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.JPADataModel;
import net.escritoriodigital.components.utils.secure.PasswordEncoder;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.service.course.CourseService;
import net.escritoriodigital.unicamp.redefor.service.discipline.DisciplineService;
import net.escritoriodigital.unicamp.redefor.service.secure.ProfileService;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.richfaces.component.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Qualifier("userBean")
@Scope("session")
public class UserBean extends BaseBean {
	
	private static final String LIST_USERS = "listUsers";
	private static final String EDIT_USER = "editUser";
	
	private Logger logger = Logger.getLogger(UserBean.class);
	
	private static final class UserDataModel extends JPADataModel<User> {
        private UserDataModel(EntityManager entityManager) {
            super(entityManager, User.class);
        }
    }
	
	@Autowired
	private ProfileService profileService;

	@Autowired
	private DisciplineService disciplineService;

	@Autowired
	private CourseService courseService;

	private User user = new User();
	
	private User currentUser;
	
	private Discipline discipline = new Discipline();
	
	private Course course = new Course();

	private List<User> users = new ArrayList<User>();

	private List<SelectItem> profileItem = new ArrayList<SelectItem>();

	private List<SelectItem> disciplinesItem = new ArrayList<SelectItem>();

	private List<SelectItem> courseItem = new ArrayList<SelectItem>();

	private Long courseId = new Long(0);

	private Boolean showDiscipline = true;

	private Long disciplineId = new Long(0);

	private String passwordUser;

	private String confirmPasswordUser;
	
	@PostConstruct
	public void proccessCurrentUser() {
		sortOrders.put("name", SortOrder.unsorted);
		sortOrders.put("email", SortOrder.unsorted);
		currentUser = getCredential();
	}
	
	public String list() throws Exception {
		this.clear();
		users = (List<User>) userService.findAll();
		return LIST_USERS;
	}
	
	public String edit() throws Exception {
		user = userService.getById(user.getId());
		loadCourses();
		loadProfiles();
		this.setShowDiscipline(!user.getProfile().getId().equals(Profile.PROFESSOR_AUTOR_ID));
		return EDIT_USER;
	}
	
	public String create() throws Exception {
		this.clear();
		loadCourses();
		loadProfiles();
		this.setShowDiscipline(false);
		return EDIT_USER;
	}

	private void loadCourses() {
		try {
			courseItem.clear();
			disciplinesItem.clear();
			
			List<Course> list = (List<Course>) courseService.findAll();
			Iterator<Course> it = list.iterator();
			while (it.hasNext()) {
				Course ent = it.next();
				courseItem.add(new SelectItem(ent.getId(), ent.getName()));
			}
			
		} catch (ServiceException e) {
			logger.error("UserBean.loadCourse: " + e.getMessage());
		}
	}

	/**
	 * Método que executa quando o operador alterar o combo de profile
	 * define se mostra ou nao o panel de disciplinas
	 */
	public void changeProfile() {
		this.setShowDiscipline(!(user.getProfile().getId().equals(
				Profile.PROFESSOR_AUTOR_ID) || 
					user.getProfile().getId().equals(new Long(0))));
	}

	public void loadDisciplineByCourse() {
		try {
			
			disciplinesItem.clear();
			
			List<Discipline> list = (List<Discipline>) disciplineService
					.getDisciplineByCourse(courseId);
			Iterator<Discipline> it = list.iterator();
			while (it.hasNext()) {
				Discipline ent = it.next();
				disciplinesItem.add(new SelectItem(ent.getId(), ent.getName()));
			}
			
		} catch (ServiceException e) {
			logger.error("UserBean.loadDisciplineByCourse: " + e.getMessage());
		}
	}

	public void addDiscipline() {
		try {
			if (disciplineId != 0) {
				Discipline item = disciplineService.getById(disciplineId);
				if (user.getDisciplines().contains(item) == false)
					user.addDiscipline(item);
				else
					FacesUtils.addErrorMessage("Disciplina já existe.");
			} else
				FacesUtils.addErrorMessage("Disciplina inválida.");
		} catch (ServiceException e) {
			logger.error("UserBean.addDiscipline: " + e.getMessage());
		}
	}

	public void removeDiscipline() throws ServiceException {
		user.removeDiscipline(discipline);
	}

	public void loadProfiles() {
		try {
			List<Profile> profiles = (List<Profile>) profileService.findAll();
			Iterator<Profile> it = profiles.iterator();
			profileItem.clear();
			while (it.hasNext()) {
				Profile profile = it.next();
				profileItem.add(new SelectItem(profile.getId(), profile
						.getName()));
			}
		} catch (ServiceException e) {
			logger.error("UserBean.loadProfiles: " + e.getMessage());
		}
	}

	/**
	 * Método que salva ou atualiza um usuário no banco só pode ser chamado pelo
	 * usuário que tenha permissão para editar usuários
	 *
	 * @return
	 */
	public String save() throws Exception {

		/*
		 * caso seja uma atualização de usuário, devemos verificar se o campos
		 * de senha estão preenchidos, se não estiverem, iremos manter a mesma
		 * senha do usuário, se estiverem, alteraremos a sua senha. Verificamos
		 * aqui também se já existe um outro usuário com o mesmo email ou login
		 * cadastrado
		 */

		boolean loginChanged = true;
		boolean emailChanged = true;
		boolean passwChanged = true;

		// é uma atualização de usuário
		if (user.getId() != null) {

			if (passwordUser.equals("") && confirmPasswordUser.equals(""))
				passwChanged = false;

			// retorna o usuário do banco para fazer as comparações
			User userDB = userService.getById(user.getId());

			if(userDB.getEmail().equals(user.getEmail())) emailChanged = false;
			if(userDB.getUsername().equals(user.getUsername())) loginChanged = false;
		}

		if (passwChanged && !passwordUser.equals(confirmPasswordUser)) {
			FacesUtils.addErrorMessage(" Senha não confere.");
			return "Error";
		}

		if (passwChanged && (passwordUser.equals("") || confirmPasswordUser.equals(""))) {
			FacesUtils.addErrorMessage(" Senha é um campo obrigatório");
			return "Error";
		}

		if (user.getProfile().getId() == 0) {
			FacesUtils.addErrorMessage(" Perfil é obrigatório.");
			return "Error";
		}

		// verifica se já existe um usuário com o mesmo e-mail na base
		if(emailChanged) {
			if(userService.getUserByEmail(user.getEmail()) != null) {
				FacesUtils.addErrorMessage(" Já existe um outro usuário com o mesmo e-mail.");
				return "Error";
			}
		}

		// verifica se já existe um usuário com o mesmo login na base
		if(loginChanged) {
			if(userService.getUserByUsername(user.getUsername()) != null) {
				FacesUtils.addErrorMessage(" Já existe um outro usuário com o mesmo username.");
				return "Error";
			}
		}

		if (user.getCreated() == null) user.setCreated(new Date());
		if (user.getEnabled() == null) user.setEnabled(true);
		user.setProfile(profileService.getById(user.getProfile().getId()));
		if(passwChanged) user.setPassword(PasswordEncoder.getMd5PasswordEncoder(passwordUser));

		try {
			userService.save(user);
			FacesUtils.addInfoMessage(" Usuário salvo com sucesso.");

		} catch (ServiceException e) {
			logger.error("UserBean.save: " + e.getMessage());
			FacesUtils.addErrorMessage(" Erro ao incluir o usuário.");
			return "Error";

		} catch (Exception e) {
			logger.error("UserBean.save: " + e.getMessage());
			FacesUtils.addErrorMessage(" Login já existe.");
			return "Error";
		}
		
		return list();
	}

	/**
	 * Salva o usuário a partir do menu preferências (nesse caso o próprio
	 * usuário está alterando seus dados
	 * @return
	 */
	public String savePreference() throws Exception {

		if (!passwordUser.equals(confirmPasswordUser)) {
			FacesUtils.addErrorMessage(" Senha não confere.");
			return "Error";
		}

		if (!currentUser.getName().equals("")) {
			try {

				if (currentUser.getCreated() == null) currentUser.setCreated(new Date());
				if (currentUser.getEnabled() == null) currentUser.setEnabled(true);

				currentUser.setPassword(PasswordEncoder.getMd5PasswordEncoder(passwordUser));
				userService.save(currentUser);
				FacesUtils.addInfoMessage(" Suas preferências foram alteradas com sucesso.");

			} catch (ServiceException e) {
				logger.error("UserBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao alterar cadastro.");
				return "Error";

			} catch (Exception e) {
				logger.error("UserBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Login já existe.");
				return "Error";

			}
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}
		
		return list();
	}

	public void enableUser() throws Exception {
		if (user != null) {
			try {
				user.setEnabled(true);
				userService.save(user);
				FacesUtils.addInfoMessage(" O usuário " + user.getName()
						+ " foi ativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("UserBean.enableUser: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar o usuário "
						+ user.getName());
			}
		}
		//this.list();
	}

	public void disableUser() throws Exception {
		if (user != null) {
			try {
				user.setEnabled(false);
				userService.save(user);
				FacesUtils.addInfoMessage(" O usuário " + user.getName()
						+ " foi desativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("UserBean.disableUser: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao desativar o usuário "
						+ user.getName());
			}
		}
		//this.list();
	}

	public void clear() {
		this.user = new User();
		this.user.setDisciplines(new ArrayList<Discipline>());
		this.user.setProfile(new Profile());
		this.disciplinesItem = new ArrayList<SelectItem>();
		this.courseItem = new ArrayList<SelectItem>();
		this.courseId = new Long(0);
		this.disciplineId = new Long(0);
		this.discipline = new Discipline();
		this.course = new Course();
	}

	public Object getDataModel() {
	    return new UserDataModel(profileService.getEntityManager());
	}
	
	public List<SelectItem> getProfileItem() {
		return profileItem;
	}
	
	public void setProfileItem(List<SelectItem> profileItem) {
		this.profileItem = profileItem;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	public List<SelectItem> getDisciplinesItem() {
		return disciplinesItem;
	}
	
	public void setDisciplinesItem(List<SelectItem> disciplinesItem) {
		this.disciplinesItem = disciplinesItem;
	}
	
	public List<SelectItem> getCourseItem() {
		return courseItem;
	}
	
	public void setCourseItem(List<SelectItem> courseItem) {
		this.courseItem = courseItem;
	}
	
	public Long getCourseId() {
		return courseId;
	}
	
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	
	public Long getDisciplineId() {
		return disciplineId;
	}
	
	public void setDisciplineId(Long disciplineId) {
		this.disciplineId = disciplineId;
	}
	
	public String getPasswordUser() {
		return passwordUser;
	}
	
	public void setPasswordUser(String passwordUser) {
		this.passwordUser = passwordUser;
	}
	
	public String getConfirmPasswordUser() {
		return confirmPasswordUser;
	}
	
	public void setConfirmPasswordUser(String confirmPasswordUser) {
		this.confirmPasswordUser = confirmPasswordUser;
	}
	
	public Boolean getShowDiscipline() {
		return showDiscipline;
	}
	
	public void setShowDiscipline(Boolean showDiscipline) {
		this.showDiscipline = showDiscipline;
	}

	public Discipline getDiscipline() {
		return discipline;
	}

	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

}
