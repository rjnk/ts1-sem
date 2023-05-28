package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.IEntity;

import java.io.Serializable;
import java.util.List;

/**
 * This interface defines methods of Generic DAOs
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface GenericDao<E extends IEntity<PK>, PK extends Serializable> {
    /**
     * Either inserts new or updates existing instance.
     *
     * @param instance to be persisted
     * @return persisted instance
     */
    E save(E instance);

    /**
     * Get one instance from database by primary key.
     *
     * @param id Database id of entity
     * @return instance with the given id or null if not found
     */
    E findOne(PK id);

    /**
     * Removes the given entity from persistence.
     *
     * @param id if of the entity instance
     */
    void delete(PK id);

    /**
     * Get all found entities from database and return it in list
     *
     * @return list of entities
     */
    List<E> findAll();
}
