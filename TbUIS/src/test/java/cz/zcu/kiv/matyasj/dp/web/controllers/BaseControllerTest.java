package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

/**
 * Abstract class for controller testing. Class provides Before and After methods which generates and removes testing data.
 * Class also provides method for logging user.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public abstract class BaseControllerTest {
    protected static final String TEST_USER_STUDENT_USERNAME = "testStudent1";
    protected static final String TEST_USER_STUDENT_PASSWORD = "password";
    protected static final String TEST_USER_TEACHER_USERNAME = "testTeacher1";
    protected static final String TEST_USER_TEACHER_PASSWORD = "password";

    protected MockMvc mockMvc;

    protected Student testStudent1;
    protected Teacher testTeacher1;

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    protected UserDao userDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        testStudent1 = new Student("John", "Doe", TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD, "testS@mail.com");
        testStudent1 = (Student) userDao.save(testStudent1);

        testTeacher1 = new Teacher("Johnathan", "Toe", TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD, "testTeacherS@mail.com");
        testTeacher1 = (Teacher) userDao.save(testTeacher1);
    }

    @After
    public void tearDown(){
        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        testTeacher1 = (Teacher) userDao.findByUsername(testTeacher1.getUsername());

        userDao.delete(testStudent1.getId());
        userDao.delete(testTeacher1.getId());
    }

    /**
     * This method makes logging user by username and password.
     *
     * @param userName String user username
     * @param password String user password in plaintext
     */
    protected void setUserLogin(String userName, String password){
        log.info("Testing user logging.");
        User user = new User(userName, password, new ArrayList<GrantedAuthority>());

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(user,null);

        SecurityContextHolder.getContext().setAuthentication(mockAuth);
    }
}
