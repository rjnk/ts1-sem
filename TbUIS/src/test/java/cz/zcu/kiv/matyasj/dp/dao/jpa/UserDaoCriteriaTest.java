package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
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

import static org.junit.Assert.assertEquals;

/**
 * UserDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class UserDaoCriteriaTest {
    @Autowired
    private UserDao userDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * This method tests UserDao function - Adding student into the database
     */
    @Test
    @Transactional
    @Rollback(true)
    public void testAddStudent() {
        log.info("Testing student adding.");

        User newUser = new Student();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setUsername("doej");
        newUser.setPassword("password54");
        newUser.setEmail("doej@gmail.com");
        userDao.save(newUser);
        Long userId = newUser.getId();

        User foundUser = userDao.findOne(userId);
        assertEquals(newUser, foundUser);
    }

    /**
     * This method tests UserDao function - Getting student from the database
     */
    @Test
    @Transactional
    @Rollback(true)
    public void testGetUserByUsername() {
        log.info("Testing user by username retrieving.");

        User newUser = new Student();
        newUser.setFirstName("John");
        newUser.setLastName("Doe");
        newUser.setUsername("doej");
        newUser.setPassword("password54");
        newUser.setEmail("doej@gmail.com");

        userDao.create(newUser);
        User foundUser = userDao.findByUsername("doej");
        assertEquals(foundUser.getId(), newUser.getId());
    }
}