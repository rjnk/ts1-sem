package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * GradeDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class GradeDaoCriteriaTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private GradeDao gradeDao;
    @Autowired
    private GradeTypeDao gradeTypeDao;
    @Autowired
    private SubjectDao subjectDao;

    @Autowired
    private ExaminationDateDao examinationDateDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * fixme should not be here
     * This method tests GradeDao function - Finding one grade by student and subject
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(false)
    public void findGradeByStudentAndSubject() throws Exception {
        log.info("Testing finding grade by student and subject.");

        Student s1 = new Student();
        s1.setEmail("");
        s1.setUsername("student1");
        s1.setFirstName("John");
        s1.setLastName("Doe");
        s1 = (Student) userDao.save(s1);

        Subject sub1 = new Subject("Math", 5);
        sub1 = subjectDao.save(sub1);

        Teacher t1 = new Teacher();
        t1.setEmail("");
        t1.setUsername("student1");
        t1.setFirstName("John");
        t1.setLastName("Doe");

        t1 = (Teacher) userDao.save(t1);

        ExaminationDate examTerm = new ExaminationDate();
        examTerm.setDateOfTest(new Date());
        examTerm = examinationDateDao.save(examTerm);

        examTerm.setSubject(sub1);
        examTerm.setTeacher(t1);
        examTerm = examinationDateDao.save(examTerm);

        Grade grade1 = new Grade();
        grade1.setDayOfGrant(new Date());
        grade1 = gradeDao.save(grade1);

        GradeType gradeType = new GradeType();
        gradeType.setName("X");
        gradeType = gradeTypeDao.save(gradeType);

        grade1.setOwner(s1);
        grade1.setTypeOfGrade(gradeType);
        grade1.setWhoGradeGranted(t1);
        grade1.setSubject(examTerm.getSubject());
        //grade1.setTestWhereWasGradeGranted(examTerm);

        grade1 = gradeDao.save(grade1);

        Grade foundGrade = gradeDao.findGradeByStudentAndSubjectAndDate(s1, examTerm.getSubject(), examTerm.getDateOfTest());
        assertNotNull(foundGrade);
        assertEquals(foundGrade.getId().longValue(), grade1.getId().longValue());
    }

    /**
     * This method tests GradeDao function - Finding one grade by student and subject (with non existent subject)
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(false)
    public void findGradeByStudentAndSubjectNonExistentSubject() throws Exception {
        log.info("Testing finding grade by student and ot existing subject.");

        Student s1 = new Student();
        s1.setEmail("");
        s1.setUsername("student1");
        s1.setFirstName("John");
        s1.setLastName("Doe");
        s1 = (Student) userDao.save(s1);

        Grade foundGrade = gradeDao.findGradeByStudentAndSubjectAndDate(s1, null, null);
        assertNull(foundGrade);
    }

    /**
     * This method tests GradeDao function - Finding list of grades by subject
     */
    @Test
    @Transactional
    @Rollback(false)
    public void findGradesBySubject() {
        log.info("Testing finding grade by subject.");

        Subject sub1 = new Subject("Math", 5);
        sub1 = subjectDao.save(sub1);
        Grade g = new Grade();
        g = gradeDao.save(g);
        g.setSubject(sub1);
        g = gradeDao.save(g);

        List<Grade> gradeList = gradeDao.findGradesBySubject(sub1);

        assertNotNull(gradeList);
        assertTrue(!gradeList.isEmpty());
        assertEquals(g.getId(), gradeList.get(0).getId());
    }

    /**
     * This method tests GradeDao function - Finding list of grades by subject (non existent result)
     */
    @Test
    @Transactional
    @Rollback(false)
    public void findGradesBySubjectNonExistentGrade() {
        log.info("Testing finding not existing grade by subject.");
        Subject sub1 = new Subject("Math", 5);
        sub1 = subjectDao.save(sub1);
        List<Grade> gradeList = gradeDao.findGradesBySubject(sub1);

        assertNotNull(gradeList);
        assertTrue(gradeList.isEmpty());
    }
}