package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
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
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * NewExamDateController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class NewExamDateControllerTest extends BaseControllerTest{
    @Autowired
    SubjectDao subjectDao;

    @Autowired
    ExaminationDateDao examinationDateDao;

    @Autowired
    DateUtility dateUtility;

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

        List<ExaminationDate> examTermList = examinationDateDao.getExaminationTermOfTeacher(testTeacher1);
        for(ExaminationDate e : examTermList){
            examinationDateDao.delete(e.getId());
        }

        super.tearDown();
    }

    /**
     * This method tests NewExamDateController serving user request - GET /teacher-view/newExamDates
     */
    @Test
    public void showTeachersExamTermsList() throws Exception {
        log.info("Testing teachers list of exam terms accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/newExamDates"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                //.andExpect(model().attributeExists("listOfExamTerms"))
                .andExpect(model().attributeExists("taughtSubjectList"))
                .andExpect(model().attribute("view", "newExamDates"));
    }

    /**
     * This method tests NewExamDateController serving user request - GET /teacher-view/newExamDates (with selected subject parameter)
     */
    @Test
    public void showTeachersExamTermsListWithSubject() throws Exception {
        log.info("Testing teachers list of exam terms with subject accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/newExamDates/"+testSubject.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                //.andExpect(model().attributeExists("listOfExamTerms"))
                .andExpect(model().attributeExists("taughtSubjectList"))
                .andExpect(model().attributeExists("selectedSubject"))
                .andExpect(model().attribute("view", "newExamDates"));
    }

    /**
     * This method tests NewExamDateController serving user request - POST /teacher-view/newExamDates
     */
    @Test
    public void saveNewExamTerm() throws Exception {
        log.info("Testing new exam term save.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/newExamDates").param("date-of-test", dateUtility.dateToString(new Date(new Date().getTime() + 100000))).param("subject", testSubject.getId()+"").param("maxParticipants", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "newExamDates"));
    }

    /**
     * This method tests NewExamDateController serving user request - POST /teacher-view/newExamDates (non existent subject)
     */
    @Test
    public void saveNewExamTermNonExistentSubject() throws Exception {
        log.info("Testing not existing new exam term save.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(post("/teacher-view/newExamDates").param("date-of-test", dateUtility.dateToString(new Date(new Date().getTime() + 100000))).param("subject", "-1").param("maxParticipants", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "newExamDates"));
    }
}