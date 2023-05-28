package cz.zcu.kiv.matyasj.dp.web.controllers.student;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
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
 * RegisterSubjectListController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class RegisterSubjectListControllerTest extends BaseControllerTest{
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    UserDao userDao;

    private Subject testSubject;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp(){
        super.setUp();
        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);


        testStudent1.getListOfLearnedSubjects().add(testSubject);
        testStudent1 = (Student) userDao.save(testStudent1);
    }

    @After
    public void tearDown(){
        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        if(!testStudent1.getListOfLearnedSubjects().isEmpty()){
            testStudent1.getListOfLearnedSubjects().remove(0);
            testStudent1 = (Student) userDao.save(testStudent1);
        }

        subjectDao.delete(testSubject.getId());

        super.tearDown();

    }

    /**
     * This method tests RegisterSubjectListController serving user request - GET /student-view/mySubjects
     */
    @Test
    public void showStudiedSubjectList() throws Exception {
        log.info("Testing studied subject list accessibility.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(get("/student-view/mySubjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("subjectList"))
                .andExpect(model().attributeExists("completedSubjectList"))
                .andExpect(model().attributeExists("listOfGrades"))
                .andExpect(model().attributeExists("totalCredits"))
                .andExpect(model().attribute("view", "mySubjects"));
    }

    /**
     * This method tests RegisterSubjectListController serving user request - POST /student-view/mySubjects
     */
    @Test
    public void removeStudiedSubject() throws Exception {
        log.info("Testing studied subject removal.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/mySubjects").param("subjectId", testSubject.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("successMessage"));
    }

    /**
     * This method tests RegisterSubjectListController serving user request - POST /student-view/mySubjects (incorrect
     * subjectId parameter - non existent subject)
     */
    @Test
    public void removeStudiedSubjectNonExistentSubject() throws Exception {
        log.info("Testing not existing studied subject removal.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/mySubjects").param("subjectId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"));
    }

    /**
     * This method tests RegisterSubjectListController serving user request - POST /student-view/mySubjects (incorrect
     * subjectId parameter - not registered subject)
     */
    @Test
    public void removeStudiedSubjectNotRegisteredSubject() throws Exception {
        log.info("Testing not registered studied subject removal.");
        testStudent1.getListOfLearnedSubjects().remove(0);
        testStudent1 = (Student) userDao.save(testStudent1);
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/mySubjects").param("subjectId", testSubject.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
