package cz.zcu.kiv.matyasj.dp.web.controllers;

import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.LocaleResolver;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * OverviewController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class OverviewControllerTest extends BaseControllerTest{
    @Autowired
    AuthenticationManagerBuilder auth;

    /** Shared system logger */
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
     * This method tests OverviewController serving user request - GET /student-view/overview
     */
    @Test
    public void showStudentOverviewTest() throws Exception {
        log.info("Testing Student Overview screen accessibility.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);

        mockMvc.perform(get("/student-view/overview"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("view", "overview"))
                .andExpect(model().attribute("currentUser", testStudent1));
    }

    /**
     * This method tests OverviewController serving user request - GET /teacher-view/overview
     */
    @Test
    public void showTeacherOverviewTest() throws Exception {
        log.info("Testing Teacher Overview screen accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);

        mockMvc.perform(get("/teacher-view/overview"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("view", "overview"))
                .andExpect(model().attribute("currentUser", testTeacher1));
    }

    /**
     * This method tests OverviewController serving user request - POST /student-view/overview
     */
    @Test
    public void saveUserSettingsStudent() throws Exception {
        log.info("Testing user settings change for student role.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/overview").param("firstName", "Tom")
                .param("lastName","Cat")
                .param("email", "randomMail@edu.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"));

        testStudent1 = (Student) userDao.findOne(testStudent1.getId());
        assertEquals("Tom", testStudent1.getFirstName());
        assertEquals("Cat", testStudent1.getLastName());
    }

    /**
     * This method tests OverviewController serving user request - POST /teacher-view/overview
     */
    @Test
    public void saveUserSettingsTeacher() throws Exception {
        log.info("Testing user settings change for teacher role.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/student-view/overview").param("firstName", "Tom")
                .param("lastName","Cat")
                .param("email", "randomMail@edu.com"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("successMessage"));

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());
        assertEquals("Tom", testTeacher1.getFirstName());
        assertEquals("Cat", testTeacher1.getLastName());
    }
}