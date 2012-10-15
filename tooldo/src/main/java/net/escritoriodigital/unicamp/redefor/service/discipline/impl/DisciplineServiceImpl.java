package net.escritoriodigital.unicamp.redefor.service.discipline.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;
import net.escritoriodigital.unicamp.redefor.service.discipline.DisciplineService;
import net.escritoriodigital.unicamp.redefor.utils.common.FileUtilsED;
import net.escritoriodigital.unicamp.redefor.utils.common.StringUtils;

import org.springframework.stereotype.Service;

/**
 * @author Leandro Concon
 * @category Service
 * @created 18/06/2010
 * @description Servico Disciplina
 */
@Service ("disciplineService")
public class DisciplineServiceImpl extends GeneralService<Discipline> implements DisciplineService {

	private static final long serialVersionUID = -250635206489913177L;


	@Override
	public void save(Discipline entity) throws ServiceException {
		if(entity.getId() == null) em.persist(entity);
		else em.merge(entity);
	}

	@Override
	public Collection<Discipline> getDisciplineByCourse(Long courseId, List<Discipline> access) throws ServiceException{

		List<Discipline> result = new ArrayList<Discipline>();

		Iterator<Discipline> it = access.iterator();

		Discipline discipline = null;

		while(it.hasNext()){
			discipline = it.next();
			if(discipline.getCourse().getId().equals(courseId)) result.add(discipline);
		}

		return result;
	}

	@Override
	public Collection<Discipline> getDisciplineByCourse(Long courseId) throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("course.id", courseId);

		return findByFields(fields, false);
	}

	@Override
	public Collection<Discipline> findByCourseAndOwner(Long courseId, Long ownerId) throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		if(courseId != null) fields.put("course.id", courseId);
		if(ownerId != null) fields.put("owner.id", ownerId);

		return findByFields(fields, true);
	}












}
