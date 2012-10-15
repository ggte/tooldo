package net.escritoriodigital.unicamp.redefor.bean.course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.event.ActionEvent;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.unicamp.redefor.model.course.Course;
import net.escritoriodigital.unicamp.redefor.service.course.CourseService;
import net.escritoriodigital.unicamp.redefor.utils.jsf.FacesUtils;

import org.apache.log4j.Logger;
import org.richfaces.component.UICommandButton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

//Component
//Scope("session")
//Qualifier("courseBean")

@Component
@Scope("session")
@Qualifier("courseBean")
public class CourseBean implements Serializable {

	private static final long serialVersionUID = 8404834328226808059L;

	private Logger logger = Logger.getLogger(CourseBean.class);

	@Autowired
	private CourseService courseService;

	private Course course = new Course();

	private List<Course> courses = new ArrayList<Course>();

	public String loadCourse() {
		try {
			courses = (List<Course>) courseService.findAll();
		} catch (ServiceException e) {
			logger.error("CourseBean.loadCourse: " + e.getMessage());
		}
		return "courseSuccess";
	}

	public String save() {
		if (!course.getName().equals("")) {
			try {
				if (course.getCreated() == null)
					course.setCreated(new Date());
				courseService.save(course);

			} catch (ServiceException e) {
				logger.error("CourseBean.save: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao incluir o curso.");
				return "Error";
			}
		} else {
			FacesUtils.addErrorMessage(" Por favor preencha o campo Nome.");
			return "Error";
		}

		this.loadCourse();
		this.clear();
		return "courseSuccess";
	}

	public String getCourseById(ActionEvent event) {
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		
		System.out.println("id é: " + id);
		
		try {
			course = courseService.getById(id);
		} catch (ServiceException e) {
			logger.error("CourseBean.update: " + e.getMessage());
		}
		
		return "courseSuccess";
	}

	public String remove() {
		
		if (course != null) {
			if (course.getDisciplines().size() == 0) {
				try {

					Course c = new Course();
					c = courseService.getById(course.getId());

					courseService.deleteEntity(c);
					FacesUtils.addInfoMessage(" Curso excluído com sucesso.");

				} catch (ServiceException e) {
					logger.error("CourseBean.remove: " + e.getMessage());
					FacesUtils.addErrorMessage(" Erro ao excluir o curso "
							+ course.getName());
				}
			} else {
				FacesUtils
						.addErrorMessage(" Existe disciplinas associado ao curso "
								+ course.getName());
			}
		} else {
			FacesUtils.addErrorMessage(" Não foi possível localizar o curso.");
		}

		this.clear();
		this.loadCourse();

		return "courseSuccess";
	}

	public void enableCourse(ActionEvent event) {
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		if (course != null) {
			try {
				course = courseService.getById(id);
				course.setEnabled(true);
				courseService.save(course);
				FacesUtils.addInfoMessage(" O curso " + course.getName()
						+ " foi ativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("CourseBean.enableCourse: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao ativar o curso "
						+ course.getName());
			}
		}
		this.loadCourse();
	}

	public void disableCourse(ActionEvent event) {
		UICommandButton command = (UICommandButton) event.getComponent();
		Long id = (Long) command.getData();
		if (course != null) {
			try {
				course = courseService.getById(id);
				course.setEnabled(false);
				courseService.save(course);
				FacesUtils.addInfoMessage(" O curso " + course.getName()
						+ " foi desativado com sucesso.");
			} catch (ServiceException e) {
				logger.error("CourseBean.disableCourse: " + e.getMessage());
				FacesUtils.addErrorMessage(" Erro ao desativar o curso "
						+ course.getName());
			}
		}
		this.loadCourse();
	}

	public void clear() {
		this.course = new Course();
	}

	/**
	 * @return the courses
	 */
	public List<Course> getCourses() {
		return courses;
	}

	/**
	 * @param courses
	 *            the courses to set
	 */
	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Course getCourse() {
		return course;
	}

}
