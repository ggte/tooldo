/**
 *
 */
package net.escritoriodigital.components.service.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.persistence.EntityManager;

import net.escritoriodigital.components.service.exception.ServiceException;


/**
 * @author André Fabbro
 * @category Service
 * @created 04/05/2010
 * @description Interface Serviço Generico
 */
public interface IGeneralService<T> extends Serializable {

	public abstract void save(T entity) throws ServiceException;

	public abstract void refresh(T entity) throws ServiceException;

	/**
	 * Exclui um objeto pelo seu ID
	 * @param id
	 * @throws ServiceException
	 */
	public abstract void delete(Object id) throws ServiceException;

	/**
	 * Exclui uma entidade sem fazer select, porém não se
	 * deve passar um objeto desatachado!
	 * @param entity
	 */
	public abstract void deleteEntity(T entity) throws ServiceException;

	public abstract void clear() throws ServiceException;

	public abstract T getById(Object id) throws ServiceException;

	public abstract Collection<T> findAll() throws ServiceException;

	public abstract EntityManager getEntityManager();

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @return
	 */
	public abstract Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive) throws ServiceException;

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro, até o limite enviado pelo maxResults
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @param maxResults - o máximo de resultados retornados
	 * @return
	 */
	public abstract Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, int maxResults) throws ServiceException;

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro, até o limite enviado pelo maxResults
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @param orderBy - define o campo para ordenação dos resultados, ex.: 'name DESC', 'codigo', etc.
	 * @return
	 */
	public abstract Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, String orderBy) throws ServiceException;

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro, até o limite enviado pelo maxResults
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @param maxResults - o máximo de resultados retornados
	 * @param orderBy - define o campo para ordenação dos resultados, ex.: 'name DESC', 'codigo', etc.
	 * @return
	 */
	public abstract Collection<T> findByFields(Map<String, Object> fields, Boolean exclusive, int maxResults, String orderBy) throws ServiceException;

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro, até o limite enviado pelo maxResults
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param memeberOf - um Map contendo o nome do membro e o objeto a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @param maxResults - o máximo de resultados retornados
	 * @return
	 */
	public abstract Collection<T> findByFieldsMemberOf(Map<String, Object> fields, Map<String, Object> memeberOf, Boolean exclusive) throws ServiceException;

	/**
	 * Busca a entidade pelos atributos enviados como parâmetro, até o limite enviado pelo maxResults
	 * @param maxField - o nome do atributo a ser utilizado para realizar o max
	 * @param fields - um Map contendo o nome do atributo e o valor do atributo a ser comparado
	 * @param memeberOf - um Map contendo o nome do membro e o objeto a ser comparado
	 * @param exclusive - define se a comparação é exclusiva (AND), se for falso será com conector OR
	 * @return
	 */
	public abstract Integer getMaxByFields(String maxField, Boolean exclusive, Map<String, Object> fields) throws ServiceException;

}
