package net.escritoriodigital.unicamp.redefor.bean.discipline;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.bean.BaseBean;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.model.secure.Profile;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.service.course.CourseService;
import net.escritoriodigital.unicamp.redefor.service.discipline.DisciplineService;
import net.escritoriodigital.unicamp.redefor.service.secure.UserService;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.richfaces.component.UICommandButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
@Qualifier("disciplineBean")
public class DisciplineBean extends BaseBean {

	private Logger logger = Logger.getLogger(DisciplineBean.class);

	@Autowired
	private DisciplineService disciplineService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private ThemeService themeService;
	@Autowired
	private UserService userService;

	private Discipline discipline = new Discipline();
	private Course course = new Course();

	private List<Discipline> disciplines = new ArrayList<Discipline>();
	private List<SelectItem> coursesItem = new ArrayList<SelectItem>();
	private List<SelectItem> usersItem = new ArrayList<SelectItem>();

	private String titlePage;

	public String loadDiscipline() {

		try {
			
			if (ifAnyGranted("ROLE_ADMINISTRATOR")) {
				
				if (course.getId() != null) {
					loadDisciplineByCourse();
				} else {
					disciplines = (List<Discipline>) disciplineService.findAll();
				}
				
			} else {
				
				if (getCredential().getProfile().getId().equals(Profile.PROFESSOR_AUTOR_ID))
					disciplines = (List<Discipline>) disciplineService
							.findByCourseAndOwner(null, getCredential().getId());
				else if (getCredential().getDisciplines() == null
						|| getCredential().getDisciplines().size() == 0)
					FacesUtils
							.addErrorMessage("Usuário sem acesso as disciplina. Por favor contate o administrador.");
				else
					disciplines = getCredential().getDisciplines();
			}
			
		} catch (ServiceException e) {
			logger.error("DisciplineBean.loadDiscipline: " + e.getMessage());
		}
		
		this.loadCourse();
		this.loadUser();
		this.clear();
		
		titlePage = "Disciplinas";

		return "disciplineSuccess";
	}

	public void loadCourse() {

		try {
			List<Course> courses = (List<Course>) courseService.findAll();
			Iterator<Course> it = courses.iterator();
			coursesItem.clear();
			while (it.hasNext()) {
				Course icourse = it.next();
				coursesItem.add(new SelectItem(icourse.getId(), icourse
						.getName()));
			}
		} catch (ServiceException e) {
			logger.error("DisciplineBean.loadCourse: " + e.getMessage());

		}
	}

	public void loadUser() {

		try {
			List<User> users = (List<User>) userService
					.getUserByProfile(new Long(Profile.PROFESSOR_AUTOR_ID));
			usersItem.clear();
			Iterator<User> it = users.iterator();
			while (it.hasNext()) {
				User user = it.next();
				usersItem.add(new SelectItem(user.getId(), user.getName()));
			}
		} catch (ServiceException e) {
			logger.error("DisciplineBean.loadUser: " + e.getMessage());
		}
	}

	public String loadDisciplineByCourse() {

		try {
			if (course.getId() != null) {
				if (ifAnyGranted("ROLE_ADMINISTRATOR"))
					disciplines = (List<Discipline>) disciplineService
							.getDisciplineByCourse(course.getId());
				else {
					if (getCredential().getProfile().getId()
							.equals(new Long(2)))
						disciplines = (List<Discipline>) disciplineService
								.findByCourseAndOwner(course.getId(),
										getCredential().getId());
					else if (getCredential().getDisciplines() == null
							|| getCredential().getDisciplines().size() == 0)
						FacesUtils
								.addErrorMessage("Usuário sem acesso as disciplina. Por favor contate o administrador.");
					else
						disciplines = (List<Discipline>) disciplineService
								.getDisciplineByCourse(course.getId(),
										getCredential().getDisciplines());
				}
			} else
				FacesUtils
						.addErrorMessage(" Erro ao carregar lista de disciplinas.");

		} catch (ServiceException e) {
			logger.error("DisciplineBean.loadDisciplineByCourse: "
					+ e.getMessage());

		}
		this.loadCourse();
		this.loadUser();
		this.clear();

		discipline.setCourse(course);
		titlePage = course.getName();

		return "disciplineSuccess";
	}

	public String save() {
		if (!discipline.getName().equals("")) {
			try {
				if (discipline.getCreated() == null)
					discipline.setCreated(new Date());

				discipline.setCourse(getCourseById(discipline.getCourse()
						.getId()));
				discipline.setOwner(getUserById(discipline.getOwner().getId()));
				disciplineService.save(discipline);
			} catch (ServiceException e) {
				logger.error("DisciplineBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao incluir a disciplina.");
				return "Error";
			}
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}

		if (course.getId() == null)
			this.loadDiscipline();
		else
			this.loadDisciplineByCourse();

		this.clear();
		return "Success";
	}

	private Course getCourseById(Long id) throws ServiceException {
		return courseService.getById(id);
	}

	private User getUserById(Long id) throws ServiceException {
		return userService.getById(id);
	}

	public String getDisciplineByCourse() {
		try {
			Long id = new Long(FacesUtils.getParameterValue("idCourse"));
			course = courseService.getById(id);
			this.loadDisciplineByCourse();
		} catch (ServiceException e) {
			logger.error("DisciplineBean.getDisciplineByCourse: "
					+ e.getMessage());
		}
		return "disciplineSuccess";
	}

	public String getDisciplineById(ActionEvent event) {
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		try {
			discipline = disciplineService.getById(id);
		} catch (ServiceException e) {
			logger.error("DisciplineBean.getDisciplineById: " + e.getMessage());
		}
		return "Success";
	}

	public String remove() {
		if (discipline != null) {

			try {

				List<Theme> themes = (List<Theme>) themeService
						.getThemeByDiscipline(discipline.getId());

				if (themes.size() == 0) {

					Discipline entity = new Discipline();
					entity = disciplineService.getById(discipline.getId());

					disciplineService.delete(entity.getId());
					FacesUtils
							.addInfoMessage(" Disciplina excluído com sucesso.");

				} else {
					FacesUtils
							.addErrorMessage(" Existe temas associado a disciplina "
									+ discipline.getName());
				}

			} catch (ServiceException e) {
				logger.error("DisciplineBean.remove: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao excluir o Disciplina "
						+ discipline.getName());
			}
		} else {
			FacesUtils
					.addErrorMessage(" Não foi possível localizar a disciplina.");
		}

		this.clear();

		if (course.getId() == null)
			this.loadDiscipline();
		else
			this.loadDisciplineByCourse();

		return "Success";
	}

	public void clear() {
		this.discipline = new Discipline();
		this.discipline.setCourse(new Course());
		this.discipline.setOwner(new User());
		if (course.getId() != null) {
			this.discipline.setCourse(course);
		} else {
			course = new Course();
		}
	}

	public void enableDiscipline(ActionEvent event) {
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		if (discipline != null) {
			try {
				discipline = disciplineService.getById(id);
				discipline.setEnabled(true);
				disciplineService.save(discipline);
				FacesUtils.addInfoMessage(" A disciplina "
						+ discipline.getName() + " foi ativada com sucesso.");
			} catch (ServiceException e) {
				logger.error("DisciplineBean.enableDiscipline: "
						+ e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar a disciplina "
						+ discipline.getName());
			}
		}
		this.loadDiscipline();
	}

	public void disableDiscipline(ActionEvent event) {
		
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		
		try {
			
			discipline = disciplineService.getById(id);
			discipline.setEnabled(false);
			disciplineService.save(discipline);
			FacesUtils
					.addInfoMessage(" A disciplina " + discipline.getName()
							+ " foi desativada com sucesso.");
		} catch (ServiceException e) {
			logger.error("DisciplineBean.disableDiscipline: "
					+ e.getMessage());
			FacesUtils.addErrorMessage(" Erro ao desativar a disciplina "
					+ discipline.getName());
		}
		
		this.loadDiscipline();
	}

	/**
	 * @return the discipline
	 */
	public Discipline getDiscipline() {
		return discipline;
	}

	/**
	 * @param discipline
	 *            the discipline to set
	 */
	public void setDiscipline(Discipline discipline) {
		this.discipline = discipline;
	}

	/**
	 * @return the course
	 */
	public Course getCourse() {
		return course;
	}

	/**
	 * @param course
	 *            the course to set
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * @return the disciplines
	 */
	public List<Discipline> getDisciplines() {
		return disciplines;
	}

	/**
	 * @param disciplines
	 *            the disciplines to set
	 */
	public void setDisciplines(List<Discipline> disciplines) {
		this.disciplines = disciplines;
	}

	/**
	 * @return the coursesItem
	 */
	public List<SelectItem> getCoursesItem() {
		return coursesItem;
	}

	/**
	 * @param coursesItem
	 *            the coursesItem to set
	 */
	public void setCoursesItem(List<SelectItem> coursesItem) {
		this.coursesItem = coursesItem;
	}

	/**
	 * @return the usersItem
	 */
	public List<SelectItem> getUsersItem() {
		return usersItem;
	}

	/**
	 * @param usersItem
	 *            the usersItem to set
	 */
	public void setUsersItem(List<SelectItem> usersItem) {
		this.usersItem = usersItem;
	}

	/**
	 * @return the titlePage
	 */
	public String getTitlePage() {
		return titlePage;
	}

	/**
	 * @param titlePage
	 *            the titlePage to set
	 */
	public void setTitlePage(String titlePage) {
		this.titlePage = titlePage;
	}

}
