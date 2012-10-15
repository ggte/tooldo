/**
 *
 */
package net.escritoriodigital.unicamp.redefor.service.discipline;

import java.util.Collection;
import java.util.List;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;
import net.escritoriodigital.unicamp.redefor.model.discipline.Discipline;

/**
 * @author Concon
 * @category Service
 * @created 21/06/2010
 * @description Interface Serviço
 */
public interface DisciplineService extends IGeneralService<Discipline> {

	public abstract Collection<Discipline> getDisciplineByCourse(Long courseId) throws ServiceException;
	
	public abstract Collection<Discipline> findByCourseAndOwner(Long courseId, Long ownerId) throws ServiceException;
		
	public abstract Collection<Discipline> getDisciplineByCourse(Long courseId, List<Discipline> access) throws ServiceException;
	
	
	
	
}
