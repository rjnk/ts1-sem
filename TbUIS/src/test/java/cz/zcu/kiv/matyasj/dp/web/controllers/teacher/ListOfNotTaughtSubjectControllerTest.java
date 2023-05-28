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
 * ListOfNotTaughtSubjectController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ListOfNotTaughtSubjectControllerTest extends BaseControllerTest{
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
     * This method tests EvaluationTableController serving user request - GET /teacher-view/otherSubjects
     */
    @Test
    public void showNotTeachedSubjectList() throws Exception {
        log.info("Testing list of not teached subjects accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/otherSubjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("subjectList"))
                .andExpect(model().attribute("view", "otherSubjects"));
    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/otherSubjects
     */
    @Test
    public void setTeachedSubject() throws Exception {
        log.info("Testing teached subject setting.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/otherSubjects").param("subjectId", testSubject.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "otherSubjects"));
    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/otherSubjects (non existent subject)
     */
    @Test
    public void setTeachedSubjectNonExistentSubject() throws Exception {
        log.info("Testing not existing teached subject setting.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/otherSubjects").param("subjectId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "otherSubjects"));
    }
}