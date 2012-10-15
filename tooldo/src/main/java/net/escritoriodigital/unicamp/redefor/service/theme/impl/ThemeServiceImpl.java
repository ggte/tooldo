package net.escritoriodigital.unicamp.redefor.service.theme.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.impl.GeneralService;
import net.escritoriodigital.unicamp.redefor.model.secure.User;
import net.escritoriodigital.unicamp.redefor.model.theme.Theme;
import net.escritoriodigital.unicamp.redefor.service.theme.ThemeService;
import net.escritoriodigital.workflow.model.entity.HistoryWorkflowEntity;
import net.escritoriodigital.workflow.model.entity.PhaseEntity;
import net.escritoriodigital.workflow.model.entity.StageEntity;
import net.escritoriodigital.workflow.model.entity.WorkflowEntity;
import net.escritoriodigital.workflow.service.WorkflowPhaseEntityService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Leandro Concon
 * @category Service
 * @created 18/06/2010
 * @description Servico Tema
 */
@Service("themeService")
public class ThemeServiceImpl extends GeneralService<Theme> implements
		ThemeService {

	private static final long serialVersionUID = 1332093755426181512L;

	@Autowired
	private WorkflowPhaseEntityService phaseService;

	@Override
	public Theme getThemeByPublishCode(Integer publishCode)
			throws ServiceException {
		Query q = em
				.createQuery("from Theme t where t.publishCode = :publishCode and t.enabled = true and t.published = true");
		q.setParameter("publishCode", publishCode);

		Theme result = null;
		try {
			result = (Theme) q.setMaxResults(1).getSingleResult();
		} catch (EntityNotFoundException e) {
			// nada a fazer
		}

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Integer getMaxPublishCode() throws ServiceException {
		Query q = em.createQuery("select max(t.publishCode) from Theme t");

		Integer max = new Integer(0);

		try {
			max = (Integer) q.getSingleResult();
		} catch (EntityNotFoundException e) {
			// não faz nada, apenas deixa o max como zero
		}

		if (max != null)
			max++;
		else
			max = new Integer(1);

		return max;
	}

	@Override
	@Transactional(readOnly = false)
	public Theme duplicateTheme(Theme theme) throws ServiceException {
		
		theme = em.find(Theme.class, theme.getId());
		
		if (!theme.getPublished())
			throw new ServiceException(
					"Só é possível duplicar um tema já publicado.");

		Theme clone = null;
		try {

			clone = (Theme) theme.clone();
			em.persist(clone);
			
			theme.setDuplicateId(clone.getId());

		} catch (CloneNotSupportedException e) {
			throw new ServiceException(e.getMessage());
		}

		return clone;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Theme> getThemeByDiscipline(Long disciplineId)
			throws ServiceException {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("discipline.id", disciplineId);

		return findByFields(fields, false);
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Theme> listThemesWorkflowByUser(User user)
			throws ServiceException {
		
		// TODO: tentar otimizar o select das pendencias por usuário, depreciando o getThemeWorkflowByProfile
		
		String phaseSQL = "from PhaseEntity p where p.finished = false ";
		String stageSQL = "from StageEntity s where s.finished = false and s.profile.id = :profileId";
		StringBuilder sql = new StringBuilder("from Theme t where ");
		sql.append(" (t.workflow is not null) ");
		sql.append(" and (t.lock.id = :userId or ");
		sql.append(" (:stage MEMBER OF t.workflow.phases) ");
		sql.append(" ) ");
		
		Query q = em.createQuery("");
		
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public Collection<Theme> getThemeWorkflowByProfile(Long profileId)
			throws ServiceException {

		//Map<String, Object> fields = new HashMap<String, Object>();
		//fields.put("workflow", "is not null");
		
		Query q = em.createQuery("from Theme t where t.workflow is not null");

		Collection<Theme> themes = q.getResultList();

		Collection<Theme> result = new ArrayList<Theme>();

		Iterator<Theme> it = themes.iterator();

		while (it.hasNext()) {

			Theme theme = it.next();

			WorkflowEntity workflow = theme.getWorkflow();

			Iterator<PhaseEntity> itPhases = workflow.getPhases().iterator();

			while (itPhases.hasNext()) {

				PhaseEntity phase = itPhases.next();

				Iterator<StageEntity> itStages = phase.getStages().iterator();

				while (itStages.hasNext()) {

					StageEntity stageEntity = itStages.next();

					if (stageEntity.getProfile().getId().equals(profileId)) {

						if (phase.getFinished() == false
								&& stageEntity.getFinished() == false) {

							if (phase.getOrder() == 1)
								result.add(theme);
							else if (phaseService.isFinished(workflow.getId(),
									(phase.getOrder() - 1)))
								result.add(theme);
						}
					}
				}
			}

		}

		return result;
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Object id) throws ServiceException {
		Long ident = (Long) id;
		
		if(ident != null) {
			Query q = em.createQuery("from HistoryWorkflowEntity h where theme.id = :themeId");
			q.setParameter("themeId", ident);
			
			List<HistoryWorkflowEntity> res = q.getResultList();
			if(res != null) {
				if(!res.isEmpty()) {
					List<Long> ids = new ArrayList<Long>();
					
					for (HistoryWorkflowEntity hs : res) ids.add(hs.getId());
					
					if(!ids.isEmpty()) {
						for (Long idh : ids) {
							Query ex = em.createQuery("delete from HistoryWorkflowEntity where id = :idh");
							ex.setParameter("idh", idh);
							ex.executeUpdate();
						}
					}
				}
			}
			
			Theme entity = getById(id);
			em.remove(entity);
		}
	}
	
	@Override
	@Transactional(readOnly = false)
	public void deleteEntity(Theme entity) throws ServiceException {
		if(entity.getId() != null)
			this.delete(entity.getId());
	}
}
