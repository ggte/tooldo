/**
 *
 */
package net.escritoriodigital.components.service.generic.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import net.escritoriodigital.components.service.exception.ServiceException;
import net.escritoriodigital.components.service.generic.IGeneralService;

import org.springframework.transaction.annotation.Transactional;


/**
 * @author André Fabbro
 * @category Service
 * @created 04/05/2010
 * @description Serviço Generico
 */
@Transactional
public abstract class GeneralService<T> implements IGeneralService<T> {

	private static final long serialVersionUID = -8171698065931141086L;

	protected EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Override
	public EntityManager getEntityManager() {
		return em;
	}

	@Transactional(readOnly=false)
	public void deleteEntity(T entity) throws ServiceException {
		if(entity != null) em.remove(entity);
	}

	@Transactional(readOnly=false)
	public void delete(Object id) throws ServiceException {
		T entity = getById(id);
		if(entity != null) em.remove(entity);
	}

	@Transactional(readOnly=true)
	public Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive) throws ServiceException {
		return findByFields(fields, exclusive, 0, null);
	}

	@Transactional(readOnly=true)
	public Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, String orderBy) throws ServiceException {
		return findByFields(fields, exclusive, 0, orderBy);
	}

	@Transactional(readOnly=true)
	public Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, int maxResults) throws ServiceException {
		return findByFields(fields, exclusive, maxResults, null);
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, int maxResults, String orderBy) throws ServiceException {

		StringBuffer sql = new StringBuffer("from " + getClassT().getName() + " t");
		String connector = " where ";

		String param = "";

		if(fields != null) {
			for (String key : fields.keySet()) {
				param = key.replace(".", "");
				if(fields.get(key).equals("is null")) sql.append(connector + "t." + key + " is null");
				if(fields.get(key).equals("is not null")) sql.append(connector + "t." + key + " is not null");
				else sql.append(connector + "t." + key + " = :" + param);
				if(exclusive) connector = " and ";
				else connector = " or ";
			}
		}

		if(orderBy != null) {
			if(!"".equals(orderBy.trim())) {
				sql.append(" order by t." + orderBy);
			}
		}

		Query q = em.createQuery(sql.toString());
		if(maxResults > 0) q.setMaxResults(maxResults);
		
		for (String key : fields.keySet()){
			if(!fields.get(key).equals("is null") && !fields.get(key).equals("is not null")){
				param = key.replace(".", "");
				q.setParameter(param, fields.get(key));
			}
		}

		return (Collection<T>) q.getResultList();
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Integer getMaxByFields(String maxField, Boolean exclusive, Map<String, Object> fields) throws ServiceException {

		StringBuffer sql = new StringBuffer("select max(t."+maxField+") from " + getClassT().getName() + " t");
		String connector = " where ";

		Integer result = 0;

		String param = "";

		if(fields != null) {
			for (String key : fields.keySet()) {
				param = key.replace(".", "");
				sql.append(connector + "t." + key + " = :" + param);
				if(exclusive) connector = " and ";
				else connector = " or ";
			}
		}

		Query q = em.createQuery(sql.toString());

		for (String key : fields.keySet()){
			param = key.replace(".", "");
			q.setParameter(param, fields.get(key));
		}

		result = (Integer) q.getResultList().get(0);
		if(result == null) result = new Integer(0);
		return (result+1);
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Collection<T> findByFieldsMemberOf(Map<String, Object> fields, Map<String, Object> memeberOf, Boolean exclusive) throws ServiceException {

		StringBuffer sql = new StringBuffer("from " + getClassT().getName() + " t");
		String connector = " where ";

		String param = "";

		if(fields != null) {
			for (String key : fields.keySet()) {
				param = key.replace(".", "");
				sql.append(connector + "t." + key + " = :" + param);
				if(exclusive) connector = " and ";
			}
		}

		if(memeberOf != null) {
			for (String key : memeberOf.keySet()) {
				param = key.replace(".", "");
				// :param MEMBER OF a.tags
				if(exclusive && fields != null) connector = " and ";
				sql.append(connector + " :" + param + " MEMBER OF t." + key);
				if(exclusive) connector = " and ";
				else connector = " or ";
			}
		}

		Query q = em.createQuery(sql.toString());

		if(fields != null) {
			for (String key : fields.keySet()){
				param = key.replace(".", "");
				q.setParameter(param, fields.get(key));
			}
		}

		if(memeberOf != null) {
			for (String key : memeberOf.keySet()){
				param = key.replace(".", "");
				q.setParameter(param, memeberOf.get(key));
			}
		}

		return (Collection<T>) q.getResultList();
	}

	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Collection<T> findAll() throws ServiceException{
		Query q = em.createQuery("from " + getClassT().getName() + " t");
		return (Collection<T>) q.getResultList();
	}

	@Transactional(readOnly=true)
	public T getById(Object id) throws ServiceException{
		return (id != null) ? (T) em.find(getClassT(), id) : null;
	}

	@Transactional(readOnly=false)
	public void save(T entity) throws ServiceException {

		Object id;
		try {
			id = getIdGenericType(entity);
		} catch (RuntimeException e) {
			throw new RuntimeException("Falha no método getIdGenericType");
		}

		if(id == null) em.persist(entity);
		else em.merge(entity);

	}

	@Transactional(readOnly=true)
	public void refresh(T entity) throws ServiceException{
		em.refresh(entity);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getClassT() throws ServiceException{

		Type type = getClass().getGenericSuperclass();

		if (type instanceof ParameterizedType) {

			ParameterizedType paramType = (ParameterizedType) type;

			Class<T> tClass = (Class<T>) paramType.getActualTypeArguments()[0];

			return tClass;

		} else {
			return null;
		}
	}

	public void clear(){
		em.flush();
		em.clear();
	}

	private Object getIdGenericType(T entity) throws ServiceException{

		Class<T> clazz = getClassT();

		try {
			Method invokeMethod = clazz.getMethod("getId", null);
			Object id = invokeMethod.invoke(entity);

			return id;

		} catch (IllegalArgumentException e) {
			throw new RuntimeException("IllegalArgumentException: Não foi possível encontrar nenhum método getId() na classe " + clazz.getName());
		} catch (IllegalAccessException e) {
			throw new RuntimeException("IllegalAccessException na chamada do método getId() da class " + clazz.getName());
		} catch (InvocationTargetException e) {
			throw new RuntimeException("InvocationTargetException na chamada do método getId() da class " + clazz.getName());
		} catch (SecurityException e) {
			throw new RuntimeException("SecurityException na chamada do método getId() da classe " + clazz.getName());
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("NoSuchMethodException na chamada do método getId() da classe " + clazz.getName());
		}
	}

}
