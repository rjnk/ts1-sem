package cz.zcu.kiv.matyasj.dp.dao.jpa.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.GradeTypeDao;
import cz.zcu.kiv.matyasj.dp.dao.jpa.correct.GenericDaoJpa;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class represent <b>ERROR</b> DAOs for manipulation with Grade types in system database.
 * Objects are able to finding/saving/deleting Grades.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
//@Repository
public class E01GradeTypeDao extends GenericDaoJpa<GradeType, Long> implements GradeTypeDao {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** Application property loader */
    protected final PropertyLoader propertyLoader;

    /**
     * E01GradeTypeDao constructor
     *
     * @param em Entity Manager for communication with database
     */
    public E01GradeTypeDao(EntityManager em, PropertyLoader propertyLoader) {
        super(em, GradeType.class);
        this.propertyLoader = propertyLoader;
    }

    /**
     * Base E01GradeTypeDao constructor
     */
    @Autowired
    public E01GradeTypeDao(PropertyLoader propertyLoader) {
        super(GradeType.class);
        this.propertyLoader = propertyLoader;
    }

    /**
     * DELIBERATE ERROR
     *
     * This error method returns always type of grades 1, 2, 3, 4
     *
     * @return list of grade types
     */
    @Override
    @ErrorMethod(errorMessage = "This method return bad grade types")
    public List<GradeType> getAllGradeTypes() {
        GradeType A = new GradeType();
        A.setName("1");
        A.setId(100L);

        GradeType B = new GradeType();
        B.setName("2");
        B.setId(101L);

        GradeType C = new GradeType();
        C.setName("3");
        C.setId(102L);

        GradeType D = new GradeType();
        D.setName("4");
        D.setId(103L);

        List<GradeType> resultList = new ArrayList<>();
        resultList.add(A);
        resultList.add(B);
        resultList.add(C);
        resultList.add(D);

        log.error(propertyLoader.getProperty("log.E01GradeTypeDao.getAllGradeTypes"));
        return resultList;
    }
}

