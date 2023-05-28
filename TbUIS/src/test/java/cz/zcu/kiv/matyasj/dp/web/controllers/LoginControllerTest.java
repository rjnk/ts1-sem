package cz.zcu.kiv.matyasj.dp.web.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * LoginController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class LoginControllerTest extends BaseControllerTest{

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
    }

    @After
    public void tearDown(){
        super.tearDown();
    }

    /**
     * This method tests LoginController serving user request - GET /login
     */
    @Test
    public void showLoginForm() throws Exception {
        log.info("Testing Login screen accessibility.");
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login.jsp"));
    }

    /**
     * This method tests LoginController serving user request - POST /login (student user)
     */
    @Test
    public void loginUserStudent() throws Exception {
        log.info("Testing login action with student role.");
        mockMvc.perform(post("/login").param("username", TEST_USER_STUDENT_USERNAME).param("password", TEST_USER_STUDENT_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/student-view/overview"));
    }

    /**
     * This method tests LoginController serving user request - POST /login (teacher user)
     */
    @Test
    public void loginUserTeacher() throws Exception {
        log.info("Testing login action with teacher role.");
        mockMvc.perform(post("/login").param("username", TEST_USER_TEACHER_USERNAME).param("password", TEST_USER_TEACHER_PASSWORD))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teacher-view/overview"));
    }

    /**
     * This method tests LoginController serving user request - POST /login (non existent user - bad username/password)
     */
    @Test
    public void loginUserNonExistentUser() throws Exception {
        log.info("Testing login action with not existing user.");
        mockMvc.perform(post("/login").param("username", "randomUser").param("password", "randomPassword"))
                .andExpect(status().isForbidden());
    }
}