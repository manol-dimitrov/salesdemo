
package org.sdl.salesdemo.dao.hibernate;

import java.io.Serializable;

import org.sdl.salesdemo.dao.AbstractDao;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * The following is the base implementation of the generic hibernate dao
 * it defines the class as a Spring repository and marks the bean scope
 * as prototype which ensures that spring will receate it every time
 *
 * @author shannonlal
 * @param <E>
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericHibernateDao<E extends Serializable> extends AbstractHibernateDao<E> implements AbstractDao<E> {


}
