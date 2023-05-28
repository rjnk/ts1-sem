package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.GenericDao;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * GenericDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class GenericDaoJpaTest {
    @Autowired
    private GenericDao userDao;
    @Autowired
    private GenericDao gradeDaoCriteria;
    @Autowired
    private GenericDao subjectDaoCriteria;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();


    /**
     * This method tests GenericDao function - Saving entities
     *
     * @throws Exception
     */
    @Transactional
    @Rollback(true)
    @Test
    public void save() throws Exception {
        log.info("Testing user saving.");

        User u1 = new Student();
        u1.setUsername("User 1");
        User u2 = new Student();
        u2.setUsername("User 2");
        User u3 = new Teacher();
        u3.setUsername("User 3");

        Subject s1 = new Subject();
        s1.setName("Subject 1");
        Subject s2 = new Subject();
        s2.setName("Subject 2");

        Grade gradeDao = new Grade();
        gradeDao.setOwner((Student) u1);
        gradeDao.setWhoGradeGranted((Teacher) u3);
        gradeDao.setDayOfGrant(new Date());
        Grade g2 = new Grade();
        g2.setOwner((Student) u2);
        g2.setWhoGradeGranted((Teacher) u3);
        g2.setDayOfGrant(new Date());

        u1 = (User) userDao.save(u1);
        u2 = (User) userDao.save(u2);
        u3 = (User) userDao.save(u3);

        s1 = (Subject) subjectDaoCriteria.save(s1);
        s2 = (Subject) subjectDaoCriteria.save(s2);

        g2 = (Grade) gradeDaoCriteria.save(g2);

        assertTrue(u1 != null);
        assertTrue(u2 != null);
        assertTrue(u3 != null);
        assertTrue(s1 != null);
        assertTrue(s2 != null);
        assertTrue(g2 != null);
    }

    /**
     * This method tests GenericDao function - finding one entity
     *
     * @throws Exception
     */
    @Transactional
    @Rollback(true)
    @Test
    public void findOne() throws Exception {
        log.info("Testing user finding.");

        User u1 = new Student();
        u1.setUsername("User 1");
        u1.setFirstName("John");
        u1.setLastName("Doe");

        u1 = (User) userDao.save(u1);

        User u2 = (Student) userDao.findOne(u1.getId());

        assertEquals(u1.getId(), u2.getId());
    }

    /**
     * This method tests GenericDao function - Deleting entities
     *
     * @throws Exception
     */
    @Transactional
    @Rollback(true)
    @Test
    public void delete() throws Exception {
        log.info("Testing user deleting.");
        User u1 = new Student();
        u1.setUsername("User 1");
        u1.setFirstName("John");
        u1.setLastName("Doe");

        u1 = (User) userDao.save(u1);

        User u2 = (Student) userDao.findOne(u1.getId());

        assertEquals(u1.getId(), u2.getId());

        userDao.delete(u1.getId());

        User u3 = (Student) userDao.findOne(u1.getId());

        assertNull(u3);
    }
}