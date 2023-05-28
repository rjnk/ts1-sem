package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
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
 * TeachersExamTermsController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class TeachersExamDatesControllerTest extends BaseControllerTest{
    @Autowired
    SubjectDao subjectDao;

    @Autowired
    ExaminationDateDao examinationDateDao;

    private Subject testSubject;
    private ExaminationDate testExamTerm;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        super.setUp();
        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);

        testTeacher1.getListOfTaughtSubjects().add(testSubject);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        testExamTerm = new ExaminationDate();
        testExamTerm = examinationDateDao.save(testExamTerm);
        testExamTerm.setSubject(testSubject);
        testExamTerm.setTeacher(testTeacher1);
        testExamTerm = examinationDateDao.save(testExamTerm);
    }

    @After
    public void tearDown(){
        if(!testTeacher1.getListOfTaughtSubjects().isEmpty()){
            testTeacher1.getListOfTaughtSubjects().remove(0);
            testTeacher1 = (Teacher) userDao.save(testTeacher1);
        }
        subjectDao.delete(testSubject.getId());

        testExamTerm = examinationDateDao.findOne(testExamTerm.getId());
        if(testExamTerm != null){
            testExamTerm.setSubject(null);
            testExamTerm.setTeacher(null);
            testExamTerm = examinationDateDao.save(testExamTerm);
            examinationDateDao.delete(testExamTerm.getId());
        }

        super.tearDown();
    }

    /**
     * This method tests TeachersExamTermsController serving user request - GET /teacher-view/myExamDates
     */
    @Test
    public void showTeachersExamTermsList() throws Exception {
        log.info("Testing teachers list of exam terms accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/myExamDates"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("listOfExamDates"))
                .andExpect(model().attributeExists("taughtSubjectList"))
                .andExpect(model().attribute("view", "myExamDates"));
    }

    /**
     * This method tests TeachersExamTermsController serving user request - POST /teacher-view/myExamDates
     */
    @Test
    public void removeExamTerm() throws Exception {
        log.info("Testing exam term removal.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/myExamDates").param("examDateId", testExamTerm.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "myExamDates"));
    }

    /**
     * This method tests TeachersExamTermsController serving user request - POST /teacher-view/myExamDates (non existent exam date)
     */
    @Test
    public void removeExamTermNonExistentTerm() throws Exception {
        log.info("Testing not existing exam term removal.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/myExamDates").param("examDateId", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "myExamDates"));
    }
}