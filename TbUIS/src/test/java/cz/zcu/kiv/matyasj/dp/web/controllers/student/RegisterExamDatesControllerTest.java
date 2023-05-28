package cz.zcu.kiv.matyasj.dp.web.controllers.student;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * RegisterExamDatesController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class RegisterExamDatesControllerTest extends BaseControllerTest{
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    UserDao userDao;

    private ExaminationDate testExamDate;
    private Subject testSubject;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp(){
        super.setUp();
        testExamDate = new ExaminationDate(new Date(), 10);
        testExamDate = examinationDateDao.save(testExamDate);

        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);

        testExamDate.setSubject(testSubject);
        testExamDate.getParticipants().add(testStudent1);
        testExamDate = examinationDateDao.save(testExamDate);

        testStudent1.getListOfLearnedSubjects().add(testSubject);
        testStudent1 = (Student) userDao.save(testStudent1);
    }

    @After
    public void tearDown(){
        testExamDate = examinationDateDao.findOne(testExamDate.getId());
        testExamDate.setSubject(null);
        if(!testExamDate.getParticipants().isEmpty()){
            testExamDate.getParticipants().remove(0);
        }
        testExamDate = examinationDateDao.save(testExamDate);

        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        testStudent1.getListOfLearnedSubjects().remove(0);
        testStudent1 = (Student) userDao.save(testStudent1);

        examinationDateDao.delete(testExamDate.getId());
        subjectDao.delete(testSubject.getId());

        super.tearDown();
    }

    /**
     * This method tests RegisterExamDatesControllerTest serving user request - GET /student-view/myExamDates
     */
    @Test
    public void showRegisteredExamDateList() throws Exception {
        log.info("Testing registered exam date list accessibility.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(get("/student-view/myExamDates"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("examinationDateList"))
                .andExpect(model().attribute("view", "myExamDates"));
    }

    /**
     * This method tests RegisterExamDatesControllerTest serving user request - POST /student-view/myExamDates
     */
    @Test
    public void unregisterExamDate() throws Exception {
        log.info("Testing exam date unregistration.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/myExamDates").param("examDateId", testExamDate.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("successMessage"));
    }

    /**
     * This method tests RegisterExamDatesControllerTest serving user request - POST /student-view/myExamDates (incorrect examDateId parameter)
     */
    @Test
    public void unregisterExamDateNonExistentExamDate() throws Exception {
        log.info("Testing not existing exam date unregistration.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/myExamDates").param("examDateId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}