package org.sdl.salesdemo.dao;

import java.util.List;

import org.sdl.salesdemo.common.SalesTaxDBException;

/**
 * The following is the abstract dao and defines the common
 * db methods required
 * 
 * @author shannonlal
 */
public interface AbstractDao<T> {
    
    public T findById( Long id) throws SalesTaxDBException;
    public T saveOrUpdate( T domain )throws SalesTaxDBException;
    public List<T> getTypes() throws SalesTaxDBException;
    public void setEntityClass(Class<T> entityClass);
}
