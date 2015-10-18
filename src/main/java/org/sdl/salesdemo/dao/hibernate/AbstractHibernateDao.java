package org.sdl.salesdemo.dao.hibernate;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.sdl.salesdemo.common.SalesDemoDBException;
import org.sdl.salesdemo.dao.AbstractDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * The following class is the generic implementation of the hibernate 
 * dao for the sales tax application.  It will allow generic class
 * to use just one DAO implementation for basic GET, FIND ALL 
 * and SAVE operation
 * @author shannonlal
 * @param <T>
 */
public abstract class AbstractHibernateDao<T extends Serializable> implements AbstractDao<T> {

    public static final Logger LOGGER = Logger.getLogger(AbstractHibernateDao.class.getName());

    private Class<T> entityClass;

    @Autowired
    private SessionFactory sessionFactory;


    /**
     * The following method will find the entity based on its id
     *
     * @param id
     * @return E
     * @throws SalesDemoDBException
     */
    @SuppressWarnings("unchecked")
	public T findById(Long id) throws SalesDemoDBException {

        String msg = "Finding Entity by Id ->" + id;
        LOGGER.info( msg);
        try {
            return (T) sessionFactory.getCurrentSession().get(entityClass, id);
            
        } catch (HibernateException e) {
            String errMsg = "Unexpected Exception while getting object->" + id + " Msg " + e.getMessage();
            LOGGER.error( errMsg, e);
            throw new SalesDemoDBException(errMsg);
        }
    }

    /**
     * The following method will save or update the domain
     *
     * @param domain
     * @return
     * @throws SalesDemoDBException
     */
    public T saveOrUpdate(T domain) throws SalesDemoDBException {
        String msg = "Save or Update Entity ->" + domain;
        LOGGER.info( msg);
        try {
            sessionFactory.getCurrentSession().saveOrUpdate(domain);
        } catch (HibernateException e) {
            String errMsg = "Unexpected Exception while save or update object->" + e.getMessage();
            LOGGER.info( errMsg, e);
            throw new SalesDemoDBException(errMsg);
        }
        return domain;
    }

    /**
     * The following method will return a list of entities
     *
     * @return
     * @throws SalesDemoDBException
     */
    public List<T> getTypes() throws SalesDemoDBException {
        String msg = "Get all the types ->" + entityClass;
        LOGGER.info( msg);
        try {
            Criteria criteria = sessionFactory.getCurrentSession().createCriteria(entityClass);
            
            @SuppressWarnings("unchecked")
            List<T> results = criteria.list();

            return results;
        } catch (HibernateException e) {
            String errMsg = "Unexpected Exception while getting object types->" + e.getMessage();
            LOGGER.info( errMsg, e);
            throw new SalesDemoDBException(errMsg);
        }
    }

    /**
     *
     * @return
     */
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     *
     * @param sessionFactory
     */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * 
     * @return 
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }

    /**
     * 
     * @param entityClass 
     */
    public void setEntityClass(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    
}
