package cz.zcu.kiv.matyasj.dp.dao.jpa;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ExaminationDateDao test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class ExaminationDateDaoTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ExaminationDateDao examinationDateDao;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * This method tests ExaminationDateDao function - Get Exam Dates list of one particular student.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void testGetExaminationTermOfStudent() throws Exception {
        log.info("Testing examination terms of student retrieving.");

        Student student1 = new Student();
        Student student2 = new Student();
        Student student3 = new Student();
        Student student4 = new Student();

        student1.setUsername("user1");
        student2.setUsername("user2");
        student3.setUsername("user3");
        student4.setUsername("user4");

        ExaminationDate term1 = new ExaminationDate();
        ExaminationDate term2 = new ExaminationDate();
        ExaminationDate term3 = new ExaminationDate();
        ExaminationDate term4 = new ExaminationDate();

        term1.setParticipants(new ArrayList<Student>());
        term2.setParticipants(new ArrayList<Student>());
        term3.setParticipants(new ArrayList<Student>());
        term4.setParticipants(new ArrayList<Student>());

        term1.getParticipants().add(student1);
        term1.getParticipants().add(student2);
        term1.getParticipants().add(student3);

        term2.getParticipants().add(student4);
        term2.getParticipants().add(student2);

        term3.getParticipants().add(student4);
        term3.getParticipants().add(student3);

        term4.getParticipants().add(student1);
        term4.getParticipants().add(student2);
        term4.getParticipants().add(student3);
        term4.getParticipants().add(student4);

        userDao.save(student1);
        userDao.save(student2);
        userDao.save(student3);
        userDao.save(student4);

        examinationDateDao.save(term1);
        examinationDateDao.save(term2);
        examinationDateDao.save(term3);
        examinationDateDao.save(term4);

        List<ExaminationDate> examinationDateList1 = examinationDateDao.getExaminationDateOfStudent(student2);
        List<ExaminationDate> examinationDateList2 = examinationDateDao.getExaminationDateOfStudent(student1);
        List<ExaminationDate> examinationDateList3 = examinationDateDao.getExaminationDateOfStudent(student4);

        assertNotNull(examinationDateList1);
        assertNotNull(examinationDateList2);
        assertNotNull(examinationDateList3);

        assertTrue(examinationDateList1.size() == 3);
        assertTrue(examinationDateList2.size() == 2);
        assertTrue(examinationDateList3.size() == 3);

        // Test if returned list contains all 3 ExaminationTerms
        assertTrue(examinationDateList1.contains(term1));
        assertTrue(examinationDateList1.contains(term2));
        assertTrue(examinationDateList1.contains(term4));

        assertTrue(examinationDateList2.contains(term1));
        assertTrue(examinationDateList2.contains(term4));

        assertTrue(examinationDateList3.contains(term2));
        assertTrue(examinationDateList3.contains(term3));
        assertTrue(examinationDateList3.contains(term4));
    }

    /**
     * This method tests ExaminationDateDao function - Get Exam Dates list of one particular student in the future.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void getExaminationTermOfStudentInFuture() throws Exception {
        log.info("Testing future examination terms of student retrieving.");

        Date today = new Date();
        Date yesterday, tomorrow, dayAfterTomorrow, afterHour;

        Calendar cal = Calendar.getInstance();
        cal.setTime(today);

        cal.add(Calendar.HOUR, 1);
        afterHour = cal.getTime();

        cal.add(Calendar.DATE, 1);
        tomorrow = cal.getTime();

        cal.add(Calendar.DATE, 1);
        dayAfterTomorrow = cal.getTime();

        cal.add(Calendar.DATE, -3);
        yesterday = cal.getTime();

        Student student = new Student();
        student.setUsername("user1");

        ExaminationDate term1 = new ExaminationDate();
        ExaminationDate term2 = new ExaminationDate();
        ExaminationDate term3 = new ExaminationDate();
        ExaminationDate term4 = new ExaminationDate();

        term1.setParticipants(new ArrayList<Student>());
        term2.setParticipants(new ArrayList<Student>());
        term3.setParticipants(new ArrayList<Student>());
        term4.setParticipants(new ArrayList<Student>());

        term1.getParticipants().add(student);
        term2.getParticipants().add(student);
        term3.getParticipants().add(student);
        term4.getParticipants().add(student);

        term1.setDateOfTest(yesterday);
        term2.setDateOfTest(tomorrow);
        term3.setDateOfTest(dayAfterTomorrow);
        term4.setDateOfTest(afterHour);

        userDao.save(student);

        examinationDateDao.save(term1);
        examinationDateDao.save(term2);
        examinationDateDao.save(term3);
        examinationDateDao.save(term4);

        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationTermOfStudentInFuture(student);

        assertNotNull(examinationDateList);
        assertEquals(3, examinationDateList.size());

        assertTrue(examinationDateList.contains(term2));
        assertTrue(examinationDateList.contains(term3));
        assertTrue(examinationDateList.contains(term4));
    }

    /**
     * This method tests ExaminationDateDao function - Get Exam Dates list of one particular teacher.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void getExaminationTermOfTeacher() throws Exception {
        log.info("Testing examination terms of teacher retrieving.");

        Teacher teacher = new Teacher();
        teacher.setUsername("teacher1");
        userDao.save(teacher);

        ExaminationDate term1 = new ExaminationDate();
        term1.setTeacher(teacher);
        ExaminationDate term2 = new ExaminationDate();
        term2.setTeacher(teacher);
        ExaminationDate term3 = new ExaminationDate();

        examinationDateDao.save(term1);
        examinationDateDao.save(term2);
        examinationDateDao.save(term3);

        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationTermOfTeacher(teacher);

        assertNotNull(examinationDateList);
        assertEquals(2, examinationDateList.size());
        assertEquals("teacher1", examinationDateList.get(0).getTeacher().getUsername());
        assertEquals("teacher1", examinationDateList.get(1).getTeacher().getUsername());
    }

    /**
     * This method tests ExaminationDateDao function - Register one particular student on exam term.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void registerStudentOnTerm() throws Exception {
        log.info("Testing student on term registration.");

        Student student = new Student();
        student.setUsername("student1");

        ExaminationDate term = new ExaminationDate();

        userDao.save(student);
        examinationDateDao.save(term);

        examinationDateDao.registerStudentOnTerm(term.getId(), student.getId());

        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationDateOfStudent(student);

        assertNotNull(examinationDateList);
        assertEquals(1, examinationDateList.size());
        assertTrue(examinationDateList.contains(term));
        assertEquals("student1", examinationDateList.get(0).getParticipants().get(0).getUsername());
    }

    /**
     * This method tests ExaminationDateDao function - Register one particular student on existing Exam Date
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void registerStudentOnTermNonExistentStudent() throws Exception {
        log.info("Testing student on not existing term registration.");

        ExaminationDate term = new ExaminationDate();


        examinationDateDao.save(term);

        ExaminationDate examTerm = examinationDateDao.registerStudentOnTerm(term.getId(), -1L);

        assertNull(examTerm);
    }

    /**
     * This method tests ExaminationDateDao function - Try to register one particular student on existing Exam Date twice.
     *
     * @throws Exception
     */
    @Test
    @Transactional
    @Rollback(true)
    public void registerStudentOnTermStudentAlreadyRegistered() throws Exception {
        log.info("Testing student on already registered term registration.");

        Student student = new Student();
        student.setUsername("student1");
        userDao.save(student);

        ExaminationDate term = new ExaminationDate();
        term = examinationDateDao.save(term);
        term.getParticipants().add(student);
        term = examinationDateDao.save(term);

        ExaminationDate examTerm = examinationDateDao.registerStudentOnTerm(term.getId(), student.getId());

        assertNull(examTerm);
    }
}