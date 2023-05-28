package cz.zcu.kiv.matyasj.dp.service.users;

import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.StudentService;
import cz.zcu.kiv.matyasj.dp.web.controllers.BaseControllerTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * UserService test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class BaseUserServiceTest extends BaseControllerTest {
    @Autowired
    private StudentService studentService;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown() {
        super.tearDown();
    }

    /**
     * This method tests UserService function - Getting currently logged in user.
     */
    @Test
    public void getCurrentUser() {
        log.info("Testing current user retrieving.");
        // User login
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        User currentUser = studentService.getCurrentUser();
        assertEquals(TEST_USER_STUDENT_USERNAME, currentUser.getUsername());
    }

    /**
     * This method tests UserService function - Updating currently logged in user (First name, Last name, email).
     */
    @Test
    public void updateUser() {
        log.info("Testing user update.");
        // User login
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        studentService.updateUser("Tom", "Cat", "changedMail");

        User currentUser = studentService.getCurrentUser();
        assertEquals("Tom", currentUser.getFirstName());
        assertEquals("Cat", currentUser.getLastName());
        assertEquals("changedMail", currentUser.getEmail());
    }
}