package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * ListOfTaughtSubjectController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ListOfTaughtSubjectControllerTest extends BaseControllerTest{
    @Autowired
    SubjectDao subjectDao;

    private Subject testSubject;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);

        testTeacher1.getListOfTaughtSubjects().add(testSubject);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);
    }

    @After
    public void tearDown(){
        if(!testTeacher1.getListOfTaughtSubjects().isEmpty()){
            testTeacher1.getListOfTaughtSubjects().remove(0);
            testTeacher1 = (Teacher) userDao.save(testTeacher1);
        }

        subjectDao.delete(testSubject.getId());

        super.tearDown();
    }

    /**
     * This method tests ListOfTaughtSubjectController serving user request - GET /teacher-view/mySubjects
     */
    @Test
    public void showTeachedSubjectList() throws Exception {
        log.info("Testing list of teached subjects accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/mySubjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("subjectList"))
                .andExpect(model().attribute("view", "mySubjects"));
    }

    /**
     * This method tests ListOfTaughtSubjectController serving user request - POST /teacher-view/mySubjects
     */
    @Test
    public void removeStudiedSubject() throws Exception {
        log.info("Testing studied subject removal.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/mySubjects").param("subjectId", testSubject.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "mySubjects"));
    }

    /**
     * This method tests ListOfTaughtSubjectController serving user request - POST /teacher-view/mySubjects (non existent subject)
     */
    @Test
    public void removeStudiedSubjectNonExistentSubject() throws Exception {
        log.info("Testing not existing studied subject removal.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/mySubjects").param("subjectId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "mySubjects"));
    }
}