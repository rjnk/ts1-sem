package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Objects of this class represent DAOs for manipulation with User data in system database.
 * Objects are able to finding/saving/deleting Grades.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
//@Repository
public class UserDaoCriteria extends GenericDaoJpa<User, Long> implements UserDao {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * UserDaoCriteria constructor
     *
     * @param em Entity Manager for communication with database
     */
    public UserDaoCriteria(EntityManager em) {
        super(em, User.class);
    }

    /**
     * Base UserDaoCriteria constructor
     */
    public UserDaoCriteria() {
        super(User.class);
    }

    /**
     * Create new user in database by existent user instance
     *
     * @param user instance of new user
     * @return instance of user with id if success
     */
    public User create(User user) {
        user = this.save(user);
        if (user.getId() != null) log.info("User " + user + " has been created!");
        return user;
    }

    /**
     * Find user by username.
     *
     * @param username username of user searching for.
     * @return found user or null
     */
    public User findByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);

        Root<User> root = query.from(User.class);
        Predicate byUsernamePredicate = cb.equal(root.get("username"), username);

        query.where(byUsernamePredicate);
        TypedQuery<User> q = entityManager.createQuery(query);

        try {
            log.info("User with username " + username + " found.");
            return q.getSingleResult();
        } catch (NoResultException e) {
            log.error("User with username " + username + " not found!");
            return null;
        }
    }

    /**
     * Find all users from database and return them.
     *
     * @return List of all users
     */
    @Override
    public List<User> findAllUsers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root);

        List<User> users = entityManager.createQuery(query).getResultList();
        log.info("Returning list of " + users.size() + " users.");
        return users;
    }
}
