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
 * ExamDaTEController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ExamDatesControllerTest extends BaseControllerTest {
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    UserDao userDao;

    private ExaminationDate testExamDate;
    private Subject testSubject;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
        testExamDate = new ExaminationDate(new Date(), 10);
        testExamDate = examinationDateDao.save(testExamDate);

        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);

        testExamDate.setSubject(testSubject);
        testExamDate = examinationDateDao.save(testExamDate);

        testStudent1.getListOfLearnedSubjects().add(testSubject);
        testStudent1 = (Student) userDao.save(testStudent1);
    }

    @After
    public void tearDown() {
        testExamDate = examinationDateDao.findOne(testExamDate.getId());
        testExamDate.setSubject(null);
        testExamDate = examinationDateDao.save(testExamDate);

        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        testStudent1.getListOfLearnedSubjects().remove(0);
        testStudent1 = (Student) userDao.save(testStudent1);

        examinationDateDao.delete(testExamDate.getId());
        subjectDao.delete(testSubject.getId());

        super.tearDown();

    }

    /**
     * This method tests ExamDateController serving user request - GET /student-view/otherExamDates
     */
    @Test
    public void showExamDateList() throws Exception {
        log.info("Testing exam date list accessibility.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(get("/student-view/otherExamDates"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("notRegisteredExaminationDatesList"))
                .andExpect(model().attributeExists("subjectsWithExamDates"))
                .andExpect(model().attributeExists("studiedSubjects"))
                .andExpect(model().attribute("view", "otherExamDates"));
    }

    /**
     * This method tests ExamDateController serving user request - POST /student-view/otherExamDates
     */
    @Test
    public void registerExamDate() throws Exception {
        log.info("Testing exam date registration.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/otherExamDates").param("examDateId", testExamDate.getId() + ""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("successMessage"));
    }

    /**
     * This method tests ExamDateController serving user request - POST /student-view/otherExamDates (incorrect examDateId parameter)
     */
    @Test
    public void registerExamDateNonExistentExamDate() throws Exception {
        log.info("Testing not existing exam date registration.");
        setUserLogin(TEST_USER_STUDENT_USERNAME, TEST_USER_STUDENT_PASSWORD);
        mockMvc.perform(post("/student-view/otherExamDates").param("examDateId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/student-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"));
    }
}
