package cz.cvut.rejnek.sem;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseStudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Date;
import static junit.framework.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class SetExaminationDateIntegrationTest {
    private static final int DAY = 24 * 60 * 60 * 1000;

    @Autowired
    DatabaseDao databaseDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ExaminationDateDao examinationDateDao;
    @Autowired
    GradeDao gradeDao;
    @Autowired
    GradeTypeDao gradeTypeDao;
    @Autowired
    SubjectDao subjectDao;
    @Autowired
    PropertyLoader propertyLoader;
    Subject subject;
    User student;
    ExaminationDate examDate;

    @Before
    public void setUp() {
        subject = new Subject("Test_Subject_1", 5);
        subject = subjectDao.save(subject);

        student = new Student("Lukas", "Test", "test1", "pass", "test1@mail.edu");
        student = userDao.save(student);

        ((Student) student).getListOfLearnedSubjects().add(subject);
        student = userDao.save(student);

        examDate = new ExaminationDate(new Date(new Date().getTime() + DAY), 10);
        examDate = examinationDateDao.save(examDate);
        examDate.setSubject(subject);
        examDate = examinationDateDao.save(examDate);
    }

    @Test
    public void setExaminationDate_SubjectSetExamDateOk_examSet() {
        var service = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);

        assertEquals(0, examinationDateDao.getExaminationDateOfStudent((Student) student).size());
        assertTrue(service.setExaminationDate(student.getId(), examDate.getId()));

        assertEquals(1, examinationDateDao.getExaminationDateOfStudent((Student) student).size());
        assertEquals(examDate.getSubject(), examinationDateDao.getExaminationDateOfStudent((Student) student).get(0).getSubject());
    }

    @Test
    public void setExaminationDate_SubjectNotSet_false() {
        var service = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
        service.unsetStudiedSubject(student.getId(), subject.getId());

        assertFalse(service.setExaminationDate(student.getId(), examDate.getId()));
        assertEquals(0, examinationDateDao.getExaminationDateOfStudent((Student) student).size());
    }

    @Test
    public void setExaminationDate_TwoSameExams_secondFalse() {
        var service = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);

        assertTrue(service.setExaminationDate(student.getId(), examDate.getId()));
        assertFalse(service.setExaminationDate(student.getId(), examDate.getId()));
        assertEquals(1, examinationDateDao.getExaminationDateOfStudent((Student) student).size());
    }

    @Test
    public void setExaminationDate_MaxParticipantsExceeded_secondFalse() {
        // student 2
        User stud2 = new Student("Lukas", "Test2", "test2", "pass", "test2@mail.edu");
        stud2 = userDao.save(stud2);

        ((Student) stud2).getListOfLearnedSubjects().add(subject);
        stud2 = userDao.save(stud2);

        // exam date
        examDate.setMaxParticipants(1);
        examDate = examinationDateDao.save(examDate);

        // service
        var service = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);

        // test student
        assertTrue(service.setExaminationDate(student.getId(), examDate.getId()));
        assertEquals(1, examinationDateDao.getExaminationDateOfStudent((Student) student).size());

        // test stud2
        assertFalse(service.setExaminationDate(stud2.getId(), examDate.getId()));
        assertEquals(0, examinationDateDao.getExaminationDateOfStudent((Student) stud2).size());
    }

    @Test
    public void setExaminationDate_MultipleTerms_secondFalse() {
        var exam2 = new ExaminationDate(new Date(new Date().getTime() + 2 * DAY), 10);
        exam2 = examinationDateDao.save(examDate);
        exam2.setSubject(subject);
        exam2 = examinationDateDao.save(examDate);

        var service = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);

        // assert
        assertTrue(service.setExaminationDate(student.getId(), examDate.getId()));
        assertFalse(service.setExaminationDate(student.getId(), exam2.getId()));
        assertEquals(1, examinationDateDao.getExaminationDateOfStudent((Student) student).size());
    }

    @After
    public void tearDown() {
        examinationDateDao.delete(examDate.getId());
        subjectDao.delete(subject.getId());
        userDao.delete(student.getId());
    }
}
