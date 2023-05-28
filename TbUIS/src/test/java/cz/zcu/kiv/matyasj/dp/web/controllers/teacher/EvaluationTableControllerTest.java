package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
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

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * EvaluationTableController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class EvaluationTableControllerTest extends BaseControllerTest{
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GradeDao gradeDao;
    @Autowired
    GradeTypeDao gradeTypeDao;

    private ExaminationDate testExamTerm;
    private Subject testSubject;
    private Grade testGrade;
    private GradeType testGradeType;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp(){
        super.setUp();
        testExamTerm = new ExaminationDate(new Date(), 10);
        testSubject = new Subject("Test subject", 6);
        testGrade = new Grade();
        testGradeType = new GradeType();
        GradeType testFailGrade = new GradeType();

        testExamTerm = examinationDateDao.save(testExamTerm);
        testSubject = subjectDao.save(testSubject);
        testGrade = gradeDao.save(testGrade);
        testGradeType = gradeTypeDao.save(testGradeType);

        testFailGrade = gradeTypeDao.save(testFailGrade);

        testGrade.setSubject(testSubject);
        //testGrade.setOwner(testStudent1);
        testGrade.setWhoGradeGranted(testTeacher1);
        testGrade = gradeDao.save(testGrade);

        testGradeType.setName("X");
        testGradeType = gradeTypeDao.save(testGradeType);
        testFailGrade.setName("F");
        testFailGrade = gradeTypeDao.save(testFailGrade);

        testExamTerm.setSubject(testSubject);
        testExamTerm.getParticipants().add(testStudent1);
        testExamTerm = examinationDateDao.save(testExamTerm);

        testStudent1.getListOfLearnedSubjects().add(testSubject);
        testStudent1 = (Student) userDao.save(testStudent1);

        testTeacher1.getListOfTaughtSubjects().add(testSubject);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);
    }

    @After
    public void tearDown(){
        testExamTerm = examinationDateDao.findOne(testExamTerm.getId());
        testExamTerm.setSubject(null);
        if(!testExamTerm.getParticipants().isEmpty()){
            testExamTerm.getParticipants().remove(0);
        }
        testExamTerm = examinationDateDao.save(testExamTerm);

        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        if(!testStudent1.getListOfLearnedSubjects().isEmpty()){
            testStudent1.getListOfLearnedSubjects().remove(0);
        }
        if(!testStudent1.getListOfAbsolvedSubjects().isEmpty()){
            testStudent1.getListOfAbsolvedSubjects().remove(0);
        }
        testStudent1 = (Student) userDao.save(testStudent1);

        testTeacher1 = (Teacher) userDao.findByUsername(testTeacher1.getUsername());
        if(!testTeacher1.getListOfTaughtSubjects().isEmpty()){
            testTeacher1.getListOfTaughtSubjects().remove(0);
        }

        examinationDateDao.delete(testExamTerm.getId());


        Grade tmpGrade = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, testExamTerm.getSubject(), testExamTerm.getDateOfTest());

        if(tmpGrade!= null){
            tmpGrade.setSubject(null);
            tmpGrade.setOwner(null);
            tmpGrade.setWhoGradeGranted(null);
            tmpGrade.setTypeOfGrade(null);
            gradeDao.save(tmpGrade);
            gradeDao.delete(tmpGrade.getId());
        }

        subjectDao.delete(testSubject.getId());
        testGrade = gradeDao.findOne(testGrade.getId());
        if(testGrade != null){
            testGrade.setSubject(null);
            testGrade.setOwner(null);
            testGrade.setWhoGradeGranted(null);
            testGrade = gradeDao.findOne(testGrade.getId());
            gradeDao.delete(testGrade.getId());
        }

        super.tearDown();
    }

    /**
     * This method tests EvaluationTableController serving user request - GET /teacher-view/evaluationTable (without filter)
     */
    @Test
    public void showEvaluationTableWithoutFilter() throws Exception {
        log.info("Testing evaluation table without filter accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attribute("view", "evaluationTable"));
    }

    /**
     * This method tests EvaluationTableController serving user request - GET /teacher-view/evaluationTable (with filter)
     * @throws Exception
     */
    @Test
    public void showEvaluationTableWithFilteredSubject() throws Exception {
        log.info("Testing evaluation table with filtered subject accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable").param("filterSubjectId", testSubject.getId()+"").param("filterIncludeGraduateStudents", "on"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("subjectList"))
                .andExpect(model().attributeExists("gradeTypes"))
                .andExpect(model().attributeExists("listOfExamTerms"))
                .andExpect(model().attributeExists("gradeList"))
                .andExpect(model().attributeExists("selectedSubjectId"))
                .andExpect(model().attribute("view", "evaluationTable"));
    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/evaluationTable/createNewGrade (with filter, create new evaluation)
     */
    @Test
    public void createNewGrade() throws Exception {
        log.info("Testing new grade creation.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable").param("filterSubjectId", testSubject.getId()+"").param("filterIncludeGraduateStudents", "on"));
        mockMvc.perform(post("/teacher-view/evaluationTable/createNewGrade").param("studentId", testStudent1.getId()+"").param("gradeTypeId", testGradeType.getId()+"").param("subjectId", testSubject.getId()+"").param("examinationDateId", testExamTerm.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "evaluationTable"));

        Grade tmpGrade = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, testExamTerm.getSubject(), testExamTerm.getDateOfTest());

        if(tmpGrade!= null){
            tmpGrade.setSubject(null);
            tmpGrade.setOwner(null);
            tmpGrade.setWhoGradeGranted(null);
            tmpGrade.setTypeOfGrade(null);
            gradeDao.save(tmpGrade);
            gradeDao.delete(tmpGrade.getId());
        }
    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/evaluationTable/createNewGrade
     * (with filter, create evaluation for non existent student)
     */
    @Test
    public void createNewGradeNonExistentStudent() throws Exception {
        log.info("Testing new grade for not existing student creation.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable").param("filterSubjectId", testSubject.getId()+"").param("filterIncludeGraduateStudents", "on"));
        mockMvc.perform(post("/teacher-view/evaluationTable/createNewGrade").param("studentId", "-1").param("gradeTypeId", testGradeType.getId()+"").param("subjectId", testSubject.getId()+"").param("examinationDateId", testExamTerm.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "evaluationTable"));

        Grade tmpGrade = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, testExamTerm.getSubject(), testExamTerm.getDateOfTest());

        if(tmpGrade!= null){
            tmpGrade.setSubject(null);
            tmpGrade.setOwner(null);
            tmpGrade.setWhoGradeGranted(null);
            tmpGrade.setTypeOfGrade(null);
            gradeDao.save(tmpGrade);
            gradeDao.delete(tmpGrade.getId());
        }
    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/evaluationTable/updateGrade
     * (with filter)
     */
    @Test
    public void updateGrade() throws Exception {
        log.info("Testing grade update.");
        testGrade = gradeDao.findOne(testGrade.getId());
        testGrade.setOwner(testStudent1);
        testGrade.setWhoGradeGranted(testTeacher1);
        testGrade = gradeDao.save(testGrade);
        testGradeType = gradeTypeDao.findOne(testGradeType.getId());
        testExamTerm.getParticipants().add(testStudent1);
        examinationDateDao.save(testExamTerm);

        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable").param("filterSubjectId", testSubject.getId()+"").param("filterIncludeGraduateStudents", "on"));
        mockMvc.perform(post("/teacher-view/evaluationTable/updateGrade").param("gradeTypeId", testGradeType.getId()+"").param("gradeId", testGrade.getId()+""))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("successMessage"))
                .andExpect(model().attribute("view", "evaluationTable"));

    }

    /**
     * This method tests EvaluationTableController serving user request - POST /teacher-view/evaluationTable/updateGrade
     * (with filter, update non existent grade)
     */
    @Test
    public void updateGradeNonExistentGrade() throws Exception {
        log.info("Testing not existing grade update.");
        testGradeType = gradeTypeDao.findOne(testGradeType.getId());

        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/evaluationTable").param("filterSubjectId", testSubject.getId()+"").param("filterIncludeGraduateStudents", "on"));
        mockMvc.perform(post("/teacher-view/evaluationTable/updateGrade").param("gradeTypeId", testGradeType.getId()+"").param("gradeId", "-1"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("view", "evaluationTable"));

    }
}