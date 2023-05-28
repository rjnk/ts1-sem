package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * SubjectDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class SubjectDaoCriteriaTest {
    @Autowired
    private SubjectDao subjectDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ExaminationDateDao examinationDateDao;
    @Autowired
    private GradeDao gradeDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * This method tests SubjectDao function - Getting list of subjects without excluded selected subjects.
     */
    @Test
    @Transactional
    @Rollback(true)
    public void getSubjectsExceptSelected() throws Exception {
        log.info("Testing subject except selected retrieving.");

        Subject math1 = new Subject();
        math1.setName("Math1");
        math1.setCreditRating(6);

        Subject math2 = new Subject();
        math2.setName("Math2");
        math2.setCreditRating(6);

        Subject programming1 = new Subject();
        programming1.setName("Programming 1");
        programming1.setCreditRating(7);

        Subject programming2 = new Subject();
        programming2.setName("Programming 2");
        programming2.setCreditRating(7);

        subjectDao.save(math1);
        subjectDao.save(math2);
        subjectDao.save(programming1);
        subjectDao.save(programming2);

        List<Subject> listOfExcludedSubjects = new ArrayList<Subject>();
        listOfExcludedSubjects.add(math1);
        listOfExcludedSubjects.add(programming1);

        List<Subject> otherSubjects = subjectDao.getSubjectsExceptSelected(listOfExcludedSubjects);

        assertTrue(otherSubjects.contains(math2));
        assertTrue(otherSubjects.contains(programming2));
    }

    /**
     * This method tests SubjectDao function - Deleting subject from database (more complex operation than delete one item from db)
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void deleteTest() throws Exception {
        log.info("Testing subject deleting.");

        Subject subject = new Subject("test subject", 6);
        subject = subjectDao.save(subject);

        Teacher teacher = new Teacher("John", "Doe", "jDoe", "pass", "testEamil@test.edu");
        teacher = (Teacher) userDao.save(teacher);
        teacher.getListOfTaughtSubjects().add(subject);
        teacher = (Teacher) userDao.save(teacher);

        Student student = new Student("John", "Doe", "jDoe", "pass", "testEamil@test.edu");
        student = (Student) userDao.save(student);
        student.getListOfLearnedSubjects().add(subject);
        student.getListOfAbsolvedSubjects().add(subject);
        student = (Student) userDao.save(student);

        ExaminationDate testExamTerm = new ExaminationDate(new Date(), 5);
        testExamTerm = examinationDateDao.save(testExamTerm);
        testExamTerm.setSubject(subject);
        testExamTerm = examinationDateDao.save(testExamTerm);

        Grade grade = new Grade();
        grade = gradeDao.save(grade);
        grade.setSubject(subject);
        grade = gradeDao.save(grade);

        subjectDao.delete(subject.getId());
    }
}