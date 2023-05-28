package cz.zcu.kiv.matyasj.dp.dao.jpa.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.jpa.correct.UserDaoCriteria;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class represent <b>ERROR</b> DAOs for manipulation with User data in system database.
 * Objects are able to finding/saving/deleting Grades.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
//@Repository
public class E01UserDao extends UserDaoCriteria {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** Application property loader */
    protected final PropertyLoader propertyLoader;

    public E01UserDao(PropertyLoader propertyLoader) {
        this.propertyLoader = propertyLoader;
    }

    /**
     * DELIBERATE ERROR
     *
     * This error method returns just students instead of all users.
     *
     * @return List of all students
     */
    @Override
    @ErrorMethod(errorMessage = "This error method returns just students instead of all users.")
    public List<User> findAllUsers() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);

        query.select(root);

        List<User> allUsers = entityManager.createQuery(query).getResultList();
        List<User> allUsersWithoutTeachers = new ArrayList<>();
        for(User u : allUsers){
            if(!(u instanceof Teacher)){
                allUsersWithoutTeachers.add(u);
            }
        }

        log.error(propertyLoader.getProperty("log.E01UserDao.findAllUsers"));
        return allUsersWithoutTeachers;
    }
}
