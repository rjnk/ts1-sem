package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.GenericDao;
import cz.zcu.kiv.matyasj.dp.domain.IEntity;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * This class represents generic dao objects which are able to save/create/find (CRUD operations) domain instances in
 * persistent store.
 *
 * @param <E> Domain Entity type
 * @param <PK> Primary key of entities
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Transactional
public class GenericDaoJpa<E extends IEntity<PK>, PK extends Serializable> implements GenericDao<E, PK> {
    /** Entity service for communication with database. */
    @PersistenceContext
    protected EntityManager entityManager;
    /**
     * Class which will be persisted
     */
    private Class<E> persistedClass;

    /**
     *  GenericDaoJpa constructor
     *
     * @param entityManager Entity Manager for comunication with database
     * @param persistedClass Domain class to persist
     */
    public GenericDaoJpa(EntityManager entityManager, Class<E> persistedClass){
        this.entityManager = entityManager;
        this.persistedClass = persistedClass;
    }

    /**
     *  GenericDaoJpa constructor
     *
     * @param persistedClass Domain class to persist
     */
    public GenericDaoJpa(Class<E> persistedClass){
        this.persistedClass = persistedClass;
    }

    public GenericDaoJpa() {
    }

    /**
     * Either inserts new or updates existing instance.
     *
     * @param instance to be persisted
     * @return persisted instance
     */
    @Override
    public E save(E instance) {
        if(instance.getPK() == null){
            this.entityManager.persist(instance);
            return instance;
        }
        entityManager.flush();
        return entityManager.merge(instance);
    }

    /**
     * Get one instance from database by primary key.
     *
     * @param id Database id of entity
     * @return instance with the given id or null if not found
     */
    @Override
    public E findOne(PK id) {
        return entityManager.find(persistedClass, id);
    }

    /**
     * Removes the given entity from persistence.
     *
     * @param id if of the entity instance
     */
    @Override
    public void delete(PK id) {
        E foundedObject = entityManager.find(persistedClass, id);

        if(foundedObject != null)
            entityManager.remove(foundedObject);
    }

    /**
     * Get all found entities from database and return it in list
     *
     * @return list of entities
     */
    @Override
    public List<E> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<E> query = cb.createQuery(persistedClass);
        Root<E> root = query.from(persistedClass);

        query.select(root);

        return entityManager.createQuery(query).getResultList();
    }
}
