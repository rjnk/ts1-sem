package cz.zcu.kiv.matyasj.dp.web.controllers.teacher;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
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
 * NewGradeController test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class NewGradeControllerTest extends BaseControllerTest{
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    UserDao userDao;
    @Autowired
    GradeTypeDao gradeTypeDao;
    @Autowired
    GradeDao gradeDao;

    private ExaminationDate testExamTerm;
    private Subject testSubject;
    private GradeType testGradeType;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp(){
        super.setUp();
        testExamTerm = new ExaminationDate(new Date(), 10);
        testExamTerm = examinationDateDao.save(testExamTerm);

        testSubject = new Subject("Test subject", 6);
        testSubject = subjectDao.save(testSubject);

        testExamTerm.setSubject(testSubject);
        testExamTerm.getParticipants().add(testStudent1);
        testExamTerm = examinationDateDao.save(testExamTerm);

        testStudent1.getListOfLearnedSubjects().add(testSubject);
        testStudent1 = (Student) userDao.save(testStudent1);

        testGradeType = new GradeType();
        testGradeType = gradeTypeDao.save(testGradeType);
        testGradeType.setName("X");
        testGradeType = gradeTypeDao.save(testGradeType);

    }

    @After
    public void tearDown(){
        testExamTerm = examinationDateDao.findOne(testExamTerm.getId());
        testExamTerm.setSubject(null);
        if(!testExamTerm.getParticipants().isEmpty()){
            testExamTerm.getParticipants().remove(0);
        }
        testExamTerm = examinationDateDao.save(testExamTerm);

        Grade tmpGrade = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, testExamTerm.getSubject(), testExamTerm.getDateOfTest());

        if(tmpGrade!= null){
            tmpGrade.setSubject(null);
            tmpGrade.setOwner(null);
            tmpGrade.setWhoGradeGranted(null);
            tmpGrade.setTypeOfGrade(null);
            gradeDao.save(tmpGrade);
            gradeDao.delete(tmpGrade.getId());
        }

        testStudent1 = (Student) userDao.findByUsername(testStudent1.getUsername());
        if(!testStudent1.getListOfLearnedSubjects().isEmpty()){
            testStudent1.getListOfLearnedSubjects().remove(0);
        }
        if(!testStudent1.getListOfAbsolvedSubjects().isEmpty()){
            testStudent1.getListOfAbsolvedSubjects().remove(0);
        }
        testStudent1 = (Student) userDao.save(testStudent1);

        examinationDateDao.delete(testExamTerm.getId());
        subjectDao.delete(testSubject.getId());




        gradeTypeDao.delete(testGradeType.getId());
        super.tearDown();
    }

    /**
     * This method tests NewGradeController serving user request - GET /teacher-view/setEvaluation
     */
    @Test
    public void showNewGradeForm() throws Exception {
        log.info("Testing new grade form accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/setEvaluation"))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("gradeTypes"))
                .andExpect(model().attributeExists("examinationDateList"))
                .andExpect(model().attribute("view", "setEvaluation"));
    }

    /**
     * This method tests NewGradeController serving user request - GET /teacher-view/setEvaluation (with exam date and student parameters)
     */
    @Test
    public void showNewGradeFormByExamTermAndStudent() throws Exception {
        log.info("Testing new grade form by exam term and student accessibility.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        mockMvc.perform(get("/teacher-view/setEvaluation/"+testExamTerm.getId()+"/"+testStudent1.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"))
                .andExpect(model().attributeExists("gradeTypes"))
                .andExpect(model().attributeExists("owner"))
                .andExpect(model().attributeExists("examinationDate"))
                .andExpect(model().attribute("view", "setEvaluation"));
    }

    /**
     * This method tests NewGradeController serving user request - POST /teacher-view/setEvaluation
     */
    @Test
    public void saveNewGrade() throws Exception {
        log.info("Testing new grade save.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        /*@RequestParam("grade") long gradeTypeId, @RequestParam("ownerId") long ownerId, @RequestParam("examTermId")*/
        mockMvc.perform(post("/teacher-view/setEvaluation")
                .param("grade", testGradeType.getId()+"")
                .param("ownerId", testStudent1.getId()+"")
                .param("examTermId", testExamTerm.getId()+""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"));
    }

    /**
     * This method tests NewGradeController serving user request - POST /teacher-view/setEvaluation (non existent grade type)
     */
    @Test
    public void saveNewGradeNonExistentGradeType() throws Exception {
        log.info("Testing not existing new grade save.");
        setUserLogin(TEST_USER_TEACHER_USERNAME, TEST_USER_TEACHER_PASSWORD);
        /*@RequestParam("grade") long gradeTypeId, @RequestParam("ownerId") long ownerId, @RequestParam("examTermId")*/
        mockMvc.perform(post("/teacher-view/setEvaluation")
                .param("grade", "-1")
                .param("ownerId", testStudent1.getId()+"")
                .param("examTermId", testExamTerm.getId()+""))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("/WEB-INF/pages/teacher-view.jsp"));
    }
}