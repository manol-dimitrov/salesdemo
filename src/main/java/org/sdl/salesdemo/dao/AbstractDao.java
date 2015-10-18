package org.sdl.salesdemo.dao;

import java.util.List;

import org.sdl.salesdemo.common.SalesDemoDBException;

/**
 * The following is the abstract dao and defines the common
 * db methods required
 * 
 * @author shannonlal
 */
public interface AbstractDao<T> {
    
    public T findById( Long id) throws SalesDemoDBException;
    public T saveOrUpdate( T domain )throws SalesDemoDBException;
    public List<T> getTypes() throws SalesDemoDBException;
    public void setEntityClass(Class<T> entityClass);
}
