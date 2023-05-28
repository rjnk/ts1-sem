package cz.zcu.kiv.matyasj.dp.service.users;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * TeacherService test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class TeacherServiceTest {
    @Autowired
    TeacherService teacherService;

    @Autowired
    UserDao userDao;

    @Autowired
    SubjectDao subjectDao;

    @Autowired
    GradeDao gradeDao;

    @Autowired
    ExaminationDateDao examinationDateDao;

    @Autowired
    GradeTypeDao gradeTypeDao;

    @Autowired
    PropertyLoader propertyLoader;

    Teacher testTeacher1;
    Student testStudent1;


    Subject testSubject1;
    Subject testSubject2;

    ExaminationDate examinationDateCurrent;
    ExaminationDate examinationDateOld;
    ExaminationDate examinationDateFuture;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        testTeacher1 = new Teacher("Peter", "Pedant", "testTeacher", "password", "test@mail.com");
        testStudent1 = new Student("John", "Doe", "testStudent", "password", "testS@mail.com");

        testSubject1 = new Subject("Test subject 1", 1);
        testSubject2 = new Subject("Test subject 2", 1);

        examinationDateCurrent = new ExaminationDate(new Date(), 5);
        DateFormat format = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        Date testDate = new Date(new Date().getTime() + 500000);
        String testDateString = format.format(testDate);
        try {
            examinationDateFuture = new ExaminationDate(format.parse(testDateString), 5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        examinationDateOld = new ExaminationDate(new Date(new Date().getTime() - 6000000), 5);

        testTeacher1 = (Teacher) userDao.save(testTeacher1);
        testStudent1 = (Student) userDao.save(testStudent1);

        testSubject1 = subjectDao.save(testSubject1);
        testSubject2 = subjectDao.save(testSubject2);

        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);
        examinationDateFuture = examinationDateDao.save(examinationDateFuture);
        examinationDateOld = examinationDateDao.save(examinationDateOld);
    }

    @After
    public void tearDown() {
        if (examinationDateCurrent != null) {
            examinationDateDao.delete(examinationDateCurrent.getId());
        }

        if (examinationDateFuture != null) {
            examinationDateDao.delete(examinationDateFuture.getId());
        }

        if (examinationDateOld != null) {
            examinationDateDao.delete(examinationDateOld.getId());
        }

        if (testTeacher1 != null) {
            userDao.delete(testTeacher1.getId());
        }
        if (testStudent1 != null) {
            userDao.delete(testStudent1.getId());
        }

        if (testSubject1 != null) {
            subjectDao.delete(testSubject1.getId());
        }
        if (testSubject2 != null) {
            subjectDao.delete(testSubject2.getId());
        }
    }

    /**
     * This method tests TeacherService function - Getting list of taught subjects.
     */
    @Test
    public void getTeachingSubjectListTest1() {
        log.info("Testing list of taught subjects retrieving.");
        List<String> subjectNames = new ArrayList<>();
        subjectNames.add(testSubject1.getName());
        subjectNames.add(testSubject2.getName());


        testTeacher1.getListOfTaughtSubjects().add(testSubject1);
        testTeacher1.getListOfTaughtSubjects().add(testSubject2);

        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        List<Subject> taughtSubjects = teacherService.getTaughtSubjectsList(testTeacher1);

        assertTrue(taughtSubjects.size() == 2);

        assertTrue(subjectNames.contains(taughtSubjects.get(0).getName()));
        assertTrue(subjectNames.contains(taughtSubjects.get(1).getName()));
    }

    /**
     * This method tests TeacherService function - Getting list of taught subjects (empty list).
     */
    @Test
    public void getTeachingSubjectListEmptyListTest() {
        log.info("Testing empty list of taught subjects retrieving.");
        List<Subject> taughtSubjects = teacherService.getTaughtSubjectsList(testTeacher1);

        assertNotNull(taughtSubjects);
        assertTrue(taughtSubjects instanceof List);
        assertTrue(taughtSubjects.isEmpty());
    }

    /**
     * This method tests TeacherService function - Getting list of taught subjects (for null teacher).
     */
    @Test
    public void getTeachingSubjectListNullTeacher() {
        log.info("Testing list of taught subjects for null teacher retrieving.");
        List<Subject> taughtSubjects = teacherService.getTaughtSubjectsList(null);

        assertNull(taughtSubjects);
    }

    /**
     * This method tests TeacherService function - Getting list of non taught subjects.
     */
    @Test
    public void getNonTeachingSubjectListTest1() {
        log.info("Testing list of not taught subjects retrieving.");
        Subject testSubject3 = new Subject("Test subject 3", 1);

        int initNumberOfNonTeachingSubjects = teacherService.getNonTaughtSubjectsList(testTeacher1).size();

        testTeacher1.getListOfTaughtSubjects().add(testSubject1);

        testTeacher1.getListOfTaughtSubjects().add(testSubject3);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        int numberOfNonTeachingSubjects1 = teacherService.getNonTaughtSubjectsList(testTeacher1).size();


        subjectDao.save(testSubject3);
        int numberOfNonTeachingSubjects2 = teacherService.getNonTaughtSubjectsList(testTeacher1).size();

        assertEquals(initNumberOfNonTeachingSubjects, numberOfNonTeachingSubjects1 + 1);
        assertEquals(initNumberOfNonTeachingSubjects, numberOfNonTeachingSubjects2);
    }

    /**
     * This method tests TeacherService function - Getting list of non taught subjects (empty list).
     */
    @Test
    public void getNonTeachingSubjectListEmptyListTest1() {
        log.info("Testing empty list of not taught subjects retrieving.");
        List<Subject> nonTaughtSubjects = teacherService.getNonTaughtSubjectsList(testTeacher1);

        for (Subject s : nonTaughtSubjects) {
            subjectDao.delete(s.getId());
        }

        nonTaughtSubjects = teacherService.getNonTaughtSubjectsList(testTeacher1);

        assertNotNull(nonTaughtSubjects);
        assertTrue(nonTaughtSubjects instanceof List);
        assertTrue(nonTaughtSubjects.isEmpty());
    }

    /**
     * This method tests TeacherService function - Getting list of non taught subjects (empty list).
     */
    @Test
    public void getNonTeachingSubjectListEmptyListTest2() {
        log.info("Testing empty list of not taught subjects retrieving.");
        List<Subject> nonTaughtSubjects = teacherService.getNonTaughtSubjectsList(testTeacher1);

        for (Subject s : nonTaughtSubjects) {
            testTeacher1.getListOfTaughtSubjects().add(s);
        }
        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        nonTaughtSubjects = teacherService.getNonTaughtSubjectsList(testTeacher1);

        assertNotNull(nonTaughtSubjects);
        assertTrue(nonTaughtSubjects instanceof List);
        assertTrue(nonTaughtSubjects.isEmpty());
    }

    /**
     * This method tests TeacherService function - Getting list of non taught subjects (for null teacher).
     */
    @Test
    public void getNonTeachingSubjectListNullTeacher() {
        log.info("Testing list of not taught subjects for null teacher retrieving.");
        List<Subject> nonTaughtSubjects = teacherService.getNonTaughtSubjectsList(null);

        assertNull(nonTaughtSubjects);
    }

    /**
     * This method tests TeacherService function - Setting new taught subject.
     */
    @Test
    public void setMySubject() {
        log.info("Testing new taught subjects setting.");
        boolean success = teacherService.setMySubject(testTeacher1, testSubject1.getId());

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());

        assertTrue(success);

        assertNotNull(testTeacher1.getListOfTaughtSubjects());
        assertTrue(testTeacher1.getListOfTaughtSubjects() instanceof List);
        assertTrue(!testTeacher1.getListOfTaughtSubjects().isEmpty());
        assertEquals(1, testTeacher1.getListOfTaughtSubjects().size());

        assertEquals(testSubject1.getId(), testTeacher1.getListOfTaughtSubjects().get(0).getId());
        assertEquals(testSubject1.getName(), testTeacher1.getListOfTaughtSubjects().get(0).getName());
    }

    /**
     * This method tests TeacherService function - Setting new taught subject (null Subject).
     */
    @Test
    public void setMyNullSubject() {
        log.info("Testing new null taught subjects setting.");
        boolean success = teacherService.setMySubject(testTeacher1, -1L);

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());

        assertFalse(success);

        assertNotNull(testTeacher1.getListOfTaughtSubjects());
        assertTrue(testTeacher1.getListOfTaughtSubjects() instanceof List);
        assertTrue(testTeacher1.getListOfTaughtSubjects().isEmpty());
    }

    /**
     * This method tests TeacherService function - Setting new taught subject (set removed subject).
     */
    @Test
    public void setMyRemovedSubject() {
        log.info("Testing new taught removed subject setting.");
        boolean success = teacherService.setMySubject(testTeacher1, testSubject1.getId());

        // Delete subject
        subjectDao.delete(testSubject1.getId());

        testSubject1 = subjectDao.findOne(testSubject1.getId());

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());

        assertTrue(success);

        assertNotNull(testTeacher1.getListOfTaughtSubjects());
        assertTrue(testTeacher1.getListOfTaughtSubjects() instanceof List);
        assertTrue(testTeacher1.getListOfTaughtSubjects().isEmpty());
    }

    /**
     * This method tests TeacherService function - Setting new taught subject (for null teacher).
     */
    @Test
    public void setMySubjectNullTeacher() {
        log.info("Testing new taught subjects for null teacher setting.");
        boolean success = teacherService.setMySubject(null, testSubject1.getId());

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Setting new taught subject (set subject twice).
     */
    @Test
    public void setMySubjectTwice() {
        log.info("Testing new taught subjects twice setting.");
        boolean success1 = teacherService.setMySubject(testTeacher1, testSubject1.getId());
        boolean success2 = teacherService.setMySubject(testTeacher1, testSubject1.getId());

        assertTrue(success1);
        assertFalse(success2);
    }

    /**
     * This method tests TeacherService function - Setting new taught subject (set more subjects than maximum).
     */
    @Test
    public void setMySubjectMoreThanMax() {
        log.info("Testing more than maximum new taught subjects setting.");
        boolean success1 = true;

        int maxSubjectsNumber = Integer.parseInt(propertyLoader.getProperty("teacherMaxSubjects"));
        for (int i = 0; i < maxSubjectsNumber; i++) {
            Subject tmpSubject = new Subject("Test subject " + i, i);
            tmpSubject = subjectDao.save(tmpSubject);
            teacherService.setMySubject(testTeacher1, tmpSubject.getId());
        }

        success1 = teacherService.setMySubject(testTeacher1, testSubject1.getId());

        assertFalse(success1);
    }

    /**
     * This method tests TeacherService function - Unsetting new taught subject.
     */
    @Test
    public void unsetMySubject() {
        log.info("Testing taught subjects unsetting.");
        //testSubject1 = subjectDao.save(testSubject1);
        //testTeacher1 = (Teacher) userDao.save(testTeacher1);
        testTeacher1.getListOfTaughtSubjects().add(testSubject1);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        boolean success = teacherService.unsetMySubject(testTeacher1, testSubject1.getId());

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());

        assertTrue(success);

        assertTrue(testTeacher1.getListOfTaughtSubjects() instanceof List);
        assertTrue(testTeacher1.getListOfTaughtSubjects().isEmpty());
    }

    /**
     * This method tests TeacherService function - Unsetting new taught subject (unset null subject).
     */
    @Test
    public void unsetMyNullSubject() {
        log.info("Testing null taught subjects unsetting.");
        boolean success = teacherService.unsetMySubject(testTeacher1, -1L);

        testTeacher1 = (Teacher) userDao.findOne(testTeacher1.getId());

        assertFalse(success);

        assertTrue(testTeacher1.getListOfTaughtSubjects() instanceof List);
        assertTrue(testTeacher1.getListOfTaughtSubjects().isEmpty());
    }

    /**
     * This method tests TeacherService function - Unsetting new taught subject (for null teacher).
     */
    @Test
    public void unsetMySubjectNullTeacher() {
        log.info("Testing taught subjects for null teacher unsetting.");
        boolean success = teacherService.unsetMySubject(null, testSubject1.getId());

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Unsetting new taught subject (for non-empty list of participants).
     */
    @Test
    public void unsetMySubjectNotEmptySubjectParticipantList() {
        log.info("Testing taught subjects with not empty list of participants unsetting.");
        testStudent1.getListOfLearnedSubjects().add(testSubject1);
        userDao.save(testStudent1);
        boolean success = teacherService.unsetMySubject(testTeacher1, testSubject1.getId());

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Unsetting new taught subject (non-empty list of exam dates).
     */
    @Test
    public void unsetMySubjectWithExamTerms() {
        log.info("Testing taught subjects with exam terms unsetting.");
        examinationDateCurrent = new ExaminationDate(new Date(), 5);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);
        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        testTeacher1.getListOfTaughtSubjects().add(testSubject1);
        userDao.save(testTeacher1);

        boolean success = teacherService.unsetMySubject(testTeacher1, testSubject1.getId());

        examinationDateCurrent.setTeacher(null);
        examinationDateCurrent.setSubject(null);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        assertTrue(success);
    }

    /**
     * This method tests TeacherService function - Getting my examination dates.
     */
    @Test
    public void getMyExaminationTerms() {
        log.info("Testing examination terms for teacher retrieving.");
        List<ExaminationDate> examinationDates = teacherService.getExaminationTermsByTeacher(testTeacher1);

        assertTrue(examinationDates.isEmpty());

        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateDao.save(examinationDateCurrent);

        examinationDates = teacherService.getExaminationTermsByTeacher(testTeacher1);

        assertTrue(!examinationDates.isEmpty());
        assertEquals(1, examinationDates.size());
        assertEquals(testTeacher1.getUsername(), examinationDates.get(0).getTeacher().getUsername());
    }

    /**
     * This method tests TeacherService function - Getting my examination dates without graduated participants.
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipants() {
        List<ExaminationDate> examinationDates = teacherService.getMyExaminationDatesWithoutGraduateParticipants(testTeacher1);

        assertTrue(examinationDates.isEmpty());

        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateDao.save(examinationDateCurrent);

        examinationDates = teacherService.getMyExaminationDatesWithoutGraduateParticipants(testTeacher1);
        assertTrue(!examinationDates.isEmpty());
        assertEquals(1, examinationDates.size());
        assertEquals(testTeacher1.getUsername(), examinationDates.get(0).getTeacher().getUsername());
    }

    /**
     * This method tests TeacherService function - Getting my examination dates without graduated participants (for null teacher).
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipantsNullTeacher() {

        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateDao.save(examinationDateCurrent);

        List<ExaminationDate> examinationDates = teacherService.getMyExaminationDatesWithoutGraduateParticipants(null);
        assertNull(examinationDates);
    }

    /**
     * This method tests TeacherService function - Getting my examination dates without graduated participants (with some participants).
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipantsWithParticipants() {
        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent.getParticipants().add(testStudent1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        List<ExaminationDate> examinationDates = teacherService.getMyExaminationDatesWithoutGraduateParticipants(testTeacher1);

        assertTrue(!examinationDates.isEmpty());
        assertTrue(!examinationDates.get(0).getParticipants().isEmpty());
        assertEquals(testStudent1.getId(), examinationDates.get(0).getParticipants().get(0).getId());

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);
        newGrade.setOwner(testStudent1);
        newGrade.setSubject(testSubject1);
        newGrade.setWhoGradeGranted(testTeacher1);
        newGrade.setDayOfGrant(examinationDateCurrent.getDateOfTest());
        newGrade = gradeDao.save(newGrade);

        examinationDates = teacherService.getMyExaminationDatesWithoutGraduateParticipants(testTeacher1);

        assertTrue(!examinationDates.isEmpty());
        assertTrue(examinationDates.get(0).getParticipants().isEmpty());

        examinationDateCurrent.setTeacher(null);
        examinationDateCurrent.setSubject(null);
        examinationDateCurrent.getParticipants().remove(0);
        examinationDateDao.save(examinationDateCurrent);

        newGrade.setOwner(null);
        newGrade.setSubject(null);
        newGrade.setWhoGradeGranted(null);
        gradeDao.save(newGrade);
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject.
     */
    @Test
    public void getAllMyExaminationTermsBySubjectTest() {
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);
        examinationDateFuture.setSubject(testSubject2);
        examinationDateFuture.setTeacher(testTeacher1);
        examinationDateFuture = examinationDateDao.save(examinationDateFuture);

        List<ExaminationDate> examinationDates = teacherService.getAllExaminationTermsByTeacherAndSubject(testTeacher1, testSubject1.getId());

        assertNotNull(examinationDates);
        assertTrue(examinationDates instanceof List);
        assertEquals(1, examinationDates.size());
        assertEquals(examinationDateCurrent.getId(), examinationDates.get(0).getId());
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject (for null teacher).
     */
    @Test
    public void getAllMyExaminationTermsBySubjectNullTeacherTest() {
        List<ExaminationDate> examinationDates = teacherService.getAllExaminationTermsByTeacherAndSubject(null, testSubject1.getId());

        assertNull(examinationDates);
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject (non-existent subject).
     */
    @Test
    public void getAllMyExaminationTermsBySubjectNonExistentSubjectTest() {
        List<ExaminationDate> examinationDates = teacherService.getAllExaminationTermsByTeacherAndSubject(testTeacher1, -1L);

        assertNotNull(examinationDates);
        assertTrue(examinationDates instanceof List);
        assertEquals(0, examinationDates.size());
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject without already graduated participants.
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipantsBySubjectTest() {
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateCurrent.getParticipants().add(testStudent1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        examinationDateFuture.setSubject(testSubject2);
        examinationDateFuture.setTeacher(testTeacher1);
        examinationDateFuture = examinationDateDao.save(examinationDateFuture);

        List<ExaminationDate> examinationDates = teacherService.getMyExaminationTermsWithoutGradedParticipantsBySubject(testTeacher1, testSubject1.getId());

        assertNotNull(examinationDates);
        assertTrue(examinationDates instanceof List);
        assertEquals(1, examinationDates.size());
        assertEquals(examinationDateCurrent.getId(), examinationDates.get(0).getId());
        assertEquals(testStudent1.getId(), examinationDateCurrent.getParticipants().get(0).getId());

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);
        newGrade.setOwner(testStudent1);
        newGrade.setSubject(testSubject1);
        newGrade.setWhoGradeGranted(testTeacher1);
        newGrade = gradeDao.save(newGrade);

        examinationDates = teacherService.getMyExaminationTermsWithoutGradedParticipantsBySubject(testTeacher1, testSubject1.getId());

        newGrade.setOwner(null);
        newGrade.setSubject(null);
        newGrade.setWhoGradeGranted(null);
        newGrade = gradeDao.save(newGrade);
        gradeDao.delete(newGrade.getId());

        examinationDateCurrent.setSubject(null);
        examinationDateCurrent.setTeacher(null);
        examinationDateCurrent.getParticipants().remove(0);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        assertNotNull(examinationDates);
        assertTrue(examinationDates instanceof List);
        assertEquals(1, examinationDates.size());
        assertEquals(examinationDateCurrent.getId(), examinationDates.get(0).getId());
        assertTrue(examinationDateCurrent.getParticipants().isEmpty());
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject without already graduated
     * participants (for null teacher).
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipantsBySubjectNullTeacherTest() {
        List<ExaminationDate> examinationDates = teacherService.getMyExaminationTermsWithoutGradedParticipantsBySubject(null, testSubject1.getId());

        assertNull(examinationDates);
    }

    /**
     * This method tests TeacherService function - Getting my examination dates by subject without already graduated
     * participants (non-existent subject).
     */
    @Test
    public void getMyExaminationTermsWithoutGraduateParticipantsBySubjectNonExistentSubjectTest() {
        List<ExaminationDate> examinationDates = teacherService.getMyExaminationTermsWithoutGradedParticipantsBySubject(testTeacher1, -1L);

        assertNotNull(examinationDates);
        assertTrue(examinationDates instanceof List);
        assertEquals(0, examinationDates.size());
    }


    /**
     * This method tests TeacherService function - Creating of new examination term by teacher.
     */
    @Test
    public void createNewExaminationTerm() throws ParseException {
        log.info("Testing new examination term creation.");

        DateFormat format = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        String maxParticipants = propertyLoader.getProperty("examTermMaxParticipants");
        Date testDate = new Date(new Date().getTime() + 500000);
        String testDateString = format.format(testDate);

        boolean success = teacherService.createNewExaminationTerm(testTeacher1, testSubject1.getId(), testDateString, maxParticipants);
        assertTrue(success);

        List<ExaminationDate> examinationDates = examinationDateDao.getExaminationTermOfTeacher(testTeacher1);

        assertTrue(!examinationDates.isEmpty());
        assertEquals(1, examinationDates.size());
        assertEquals(0, examinationDates.get(0).getParticipants().size());
        assertEquals(testSubject1.getId(), examinationDates.get(0).getSubject().getId());

        // Parsing date string removes seconds - format.parse(testDateString) instead of testDate
        assertEquals(format.parse(testDateString).getTime(), examinationDates.get(0).getDateOfTest().getTime());
        assertEquals(Integer.parseInt(maxParticipants), examinationDates.get(0).getMaxParticipants());
        examinationDateDao.delete(examinationDates.get(0).getId());
    }

    /**
     * This method tests TeacherService function - Creating of new examination term by teacher (exam date is not in future).
     */
    @Test
    public void createNewExaminationTermDateIsNotInFuture() throws ParseException {
        log.info("Testing new not future examination term creation.");

        DateFormat format = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        String maxParticipants = propertyLoader.getProperty("examTermMaxParticipants");
        Date testDate = new Date(new Date().getTime() - 500000);
        String testDateString = format.format(testDate);


        boolean success = teacherService.createNewExaminationTerm(testTeacher1, testSubject1.getId(), testDateString, maxParticipants);
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new examination term by teacher (bad date format).
     */
    @Test
    public void createNewExaminationTermBadDateFormat() {
        log.info("Testing new examination term with bad date format creation.");

        String maxParticipants = propertyLoader.getProperty("examTermMaxParticipants");
        String testDateString = "1.1.2000";

        boolean success = teacherService.createNewExaminationTerm(testTeacher1, testSubject1.getId(), testDateString, maxParticipants);
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new examination term by teacher (more than maximum
     * number of exam date).
     */
    @Test
    public void createNewExaminationTermMoreThanMax() {
        log.info("Testing more than maximum new examination terms creation.");

        int maxExamDatesNumber = Integer.parseInt(propertyLoader.getProperty("subjectMaxExamDate"));
        String maxParticipants = propertyLoader.getProperty("examTermMaxParticipants");
        DateFormat format = new SimpleDateFormat(propertyLoader.getProperty("dateAndTimeFormat"));
        Date testDate = new Date(new Date().getTime() + 500000);
        String testDateString = format.format(testDate);

        for (int i = 0; i < maxExamDatesNumber; i++) {
            teacherService.createNewExaminationTerm(testTeacher1, testSubject1.getId(), testDateString, maxParticipants);
        }


        boolean success = teacherService.createNewExaminationTerm(testTeacher1, testSubject1.getId(), testDateString, maxParticipants);
        assertFalse(success);

        // Removing created exam terms
        List<ExaminationDate> examinationDates = teacherService.getAllExaminationTermsByTeacherAndSubject(testTeacher1, testSubject1.getId());
        for (ExaminationDate e : examinationDates) {
            examinationDateDao.delete(e.getId());
        }
    }

    /**
     * This method tests TeacherService function - Creating of new examination term by teacher (null teacher).
     */
    @Test
    public void createNewExaminationTermNullTeacher() {
        log.info("Testing new examination term for null teacher creation.");

        String maxParticipants = propertyLoader.getProperty("examTermMaxParticipants");
        String testDateString = "1.1.2000";

        boolean success = teacherService.createNewExaminationTerm(null, testSubject1.getId(), testDateString, maxParticipants);
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Removing of examination term by teacher.
     */
    @Test
    public void removeExaminationTerm() {
        log.info("Testing examination term removal.");

        boolean success = teacherService.removeExaminationTerm(testTeacher1, examinationDateCurrent.getId());
        assertFalse(success);

        // Set authorized teacher
        /*List<ExaminationDate> examinationDates = examinationDateDao.getExaminationTermOfTeacher(testTeacher1);*/
        examinationDateCurrent.setTeacher(testTeacher1);
        examinationDateDao.save(examinationDateCurrent);

        success = teacherService.removeExaminationTerm(testTeacher1, examinationDateCurrent.getId());
        assertTrue(success);

        List<ExaminationDate> examinationDates = examinationDateDao.getExaminationTermOfTeacher(testTeacher1);
        assertEquals(0, examinationDates.size());

        examinationDateCurrent.setTeacher(null);
        examinationDateDao.save(examinationDateCurrent);
    }

    /**
     * This method tests TeacherService function - Removing of examination term by teacher (null teacher).
     */
    @Test
    public void removeExaminationTermNullTeacher() {
        log.info("Testing examination term for null teacher removal.");

        boolean success = teacherService.removeExaminationTerm(null, examinationDateCurrent.getId());
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Removing of examination term by teacher (non-existent exam date).
     */
    @Test
    public void removeExaminationTermNonExistentExamTerm() {
        log.info("Testing not existing examination term removal.");

        boolean success = teacherService.removeExaminationTerm(testTeacher1, -1L);

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher.
     */
    @Test
    public void createNewGrade() {
        log.info("Testing new grade creation.");

        GradeType pass = new GradeType();
        pass.setName("X");
        GradeType failed = new GradeType();
        failed.setName("F");

        pass = gradeTypeDao.save(pass);
        gradeTypeDao.save(failed);

        testStudent1.getListOfLearnedSubjects().add(testSubject1);
        testStudent1 = (Student) userDao.save(testStudent1);
        examinationDateCurrent.setSubject(testSubject1);

        int beforeLearnedSubjects = testStudent1.getListOfLearnedSubjects().size();

        boolean success = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), pass.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertTrue(success);

        testStudent1 = (Student) userDao.findOne(testStudent1.getId());
        int afterLearnedSubjects = testStudent1.getListOfLearnedSubjects().size();

        Grade g = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, examinationDateCurrent.getSubject(), examinationDateCurrent.getDateOfTest());

        assertNotNull(g);
        assertEquals(pass.getName(), g.getTypeOfGrade().getName());

        gradeDao.delete(g.getId());
        assertEquals(beforeLearnedSubjects, afterLearnedSubjects + 1);
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (create evaluation twice).
     */
    @Test
    public void createNewGradeTwice() {
        log.info("Testing new grade creation twice.");

        GradeType pass = new GradeType();
        pass.setName("X");
        GradeType fail = new GradeType();
        fail.setName("F");

        pass = gradeTypeDao.save(pass);
        gradeTypeDao.save(fail);

        testStudent1.getListOfLearnedSubjects().add(testSubject1);
        testStudent1 = (Student) userDao.save(testStudent1);
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        boolean success1 = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), pass.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertTrue(success1);
        boolean success2 = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), pass.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertFalse(success2);

        Grade g = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, testSubject1, examinationDateCurrent.getDateOfTest());

        assertNotNull(g);
        assertEquals(pass.getName(), g.getTypeOfGrade().getName());

        gradeDao.delete(g.getId());
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (create Failed grade).
     */
    @Test
    public void createNewGradeFailed() {
        log.info("Testing new grade fail creation.");

        GradeType fail = new GradeType();
        fail.setName("F");

        gradeTypeDao.save(fail);

        testStudent1.getListOfLearnedSubjects().add(testSubject1);
        testStudent1 = (Student) userDao.save(testStudent1);

        examinationDateCurrent.getParticipants().add(testStudent1);
        examinationDateCurrent.setSubject(testSubject1);
        examinationDateCurrent = examinationDateDao.save(examinationDateCurrent);

        boolean success1 = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), fail.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertTrue(success1);

        Grade g = gradeDao.findGradeByStudentAndSubjectAndDate(testStudent1, examinationDateCurrent.getSubject(), examinationDateCurrent.getDateOfTest());
        assertNotNull(g);

        examinationDateCurrent = examinationDateDao.findOne(examinationDateCurrent.getId());
        for (Student s : examinationDateCurrent.getParticipants()) {
            assertTrue(s.getId().longValue() == testStudent1.getId().longValue());
        }

        gradeDao.delete(g.getId());
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (null teacher).
     */
    @Test
    public void createNewGradeTeacherNull() {
        log.info("Testing new grade for null teacher creation.");

        GradeType x = new GradeType();
        x.setName("X");

        x = gradeTypeDao.save(x);

        boolean success = teacherService.createNewGrade(null, testStudent1.getId(), x.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (for null student).
     */
    @Test
    public void createNewGradeStudentNull() {
        log.info("Testing new grade for null student creation.");

        GradeType x = new GradeType();
        x.setName("X");

        x = gradeTypeDao.save(x);

        boolean success = teacherService.createNewGrade(testTeacher1, -1L, x.getId(), testSubject1.getId(), examinationDateCurrent.getId());
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (null grade type).
     */
    @Test
    public void createNewGradeGradeTypeNull() {
        log.info("Testing new grade with null type creation.");

        boolean success = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), -1L, testSubject1.getId(), examinationDateCurrent.getId());
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Creating of new evaluation by teacher (null subject).
     */
    @Test
    public void createNewGradeSubjectNull() {
        log.info("Testing new grade for null subject creation.");

        GradeType x = new GradeType();
        x.setName("X");

        x = gradeTypeDao.save(x);

        boolean success = teacherService.createNewGrade(testTeacher1, testStudent1.getId(), x.getId(), -1L, examinationDateCurrent.getId());
        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Getting examination date by id.
     */
    @Test
    public void getExaminationTerm() {
        log.info("Testing examination term retrieving.");
        ExaminationDate tmpExamTerm = teacherService.getExaminationTerm(examinationDateFuture.getId());

        assertNotNull(tmpExamTerm);
        assertEquals(tmpExamTerm.getDateOfTest().getTime(), examinationDateFuture.getDateOfTest().getTime());
    }

    /**
     * This method tests TeacherService function - Getting examination date by id (non-existent examination date).
     */
    @Test
    public void getExaminationTermNonExistentTerm() {
        log.info("Testing not existing examination term retrieving.");
        ExaminationDate tmpExamTerm = teacherService.getExaminationTerm(-1L);

        assertNull(tmpExamTerm);
    }

    /**
     * This method tests TeacherService function - Getting student from one particular exam date.
     */
    @Test
    public void getStudentFromExaminationTerm() {
        examinationDateFuture.getParticipants().add(testStudent1);
        examinationDateDao.save(examinationDateFuture);

        Student s = teacherService.getStudentFromExaminationTerm(examinationDateFuture, testStudent1.getId());

        assertNotNull(s);
        assertEquals(testStudent1.getId(), s.getId());
        assertEquals(testStudent1.getUsername(), s.getUsername());
    }

    /**
     * This method tests TeacherService function - Getting student from one particular exam date (student is not participant
     * of exam date).
     */
    @Test
    public void getStudentFromExaminationTermStudentNotInParticipants() {
        Student s = teacherService.getStudentFromExaminationTerm(examinationDateFuture, testStudent1.getId());
        assertNull(s);
    }

    /**
     * This method tests TeacherService function - Getting student from one particular exam date (null student).
     */
    @Test
    public void getStudentFromExaminationTermNullStudent() {
        Student s = teacherService.getStudentFromExaminationTerm(examinationDateFuture, -1L);
        assertNull(s);
    }

    /**
     * This method tests TeacherService function - Getting student from one particular exam date (null exam date).
     */
    @Test
    public void getStudentFromExaminationTermNullExamTerm() {
        Student s = teacherService.getStudentFromExaminationTerm(null, testStudent1.getId());
        assertNull(s);
    }

    /**
     * This method tests TeacherService function - Getting all grade types from database.
     */
    @Test
    public void getAllGradeTypes() {
        log.info("Testing all grade types retrieving.");
        List<GradeType> gradeTypeList = gradeTypeDao.getAllGradeTypes();
        List<GradeType> gradeTypeListTested = teacherService.getAllGradeTypes();

        assertEquals(gradeTypeList.size(), gradeTypeListTested.size());

        for (GradeType e : gradeTypeList) {
            assertTrue(gradeTypeList.contains(e));
        }

        GradeType x = new GradeType();
        x.setName("X");
        gradeTypeDao.save(x);

        gradeTypeListTested = teacherService.getAllGradeTypes();

        assertEquals(gradeTypeList.size() + 1, gradeTypeListTested.size());
    }

    /**
     * This method tests TeacherService function - Getting all teachers from database.
     */
    @Test
    public void getAllTeachers() {
        log.info("Testing all teachers retrieving.");

        List<Teacher> teacherListTested = teacherService.getAllTeachers();
        List<Teacher> teacherList = new ArrayList<>();

        for (User u : userDao.findAllUsers()) {
            if (u instanceof Teacher) {
                teacherList.add((Teacher) u);
            }
        }

        assertEquals(teacherList.size(), teacherListTested.size());

        Teacher newTeacher = new Teacher("John", "Doe", "testTeacherJD", "password", "test@mail.com");
        userDao.save(newTeacher);

        teacherListTested = teacherService.getAllTeachers();
        assertEquals(teacherList.size() + 1, teacherListTested.size());

        Student testStudent = new Student("John", "Doe", "testStudent2", "password", "testS@mail.com");
        userDao.save(testStudent);

        teacherListTested = teacherService.getAllTeachers();
        assertEquals(teacherList.size() + 1, teacherListTested.size());
    }

    /**
     * This method tests TeacherService function - Updating grade.
     */
    @Test
    public void updateGradeTest() {
        log.info("Testing grade update.");

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);
        newGrade.setOwner(testStudent1);
        newGrade.setSubject(testSubject1);
        newGrade.setWhoGradeGranted(testTeacher1);
        newGrade = gradeDao.save(newGrade);
        testTeacher1.getListOfTaughtSubjects().add(testSubject1);
        testTeacher1 = (Teacher) userDao.save(testTeacher1);

        GradeType newGradeType = new GradeType();
        newGradeType.setName("X");
        GradeType failGrade = new GradeType();
        failGrade.setName("F");

        newGradeType = gradeTypeDao.save(newGradeType);
        gradeTypeDao.save(failGrade);

        boolean success = teacherService.updateGrade(testTeacher1.getId(), newGrade.getId(), newGradeType.getId());

        newGrade.setOwner(null);
        newGrade.setSubject(null);
        newGrade.setWhoGradeGranted(null);
        gradeDao.save(newGrade);

        testTeacher1.getListOfTaughtSubjects().remove(0);
        userDao.save(testTeacher1);

        assertTrue(success);
    }

    /**
     * This method tests TeacherService function - Updating grade (by non-existent teacher).
     */
    @Test
    public void updateGradeTestNonExistentTeacher() {
        log.info("Testing grade for not existing teacher update.");

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);

        GradeType newGradeType = new GradeType();
        newGradeType.setName("X");

        newGradeType = gradeTypeDao.save(newGradeType);

        boolean success = teacherService.updateGrade(-1L, newGrade.getId(), newGradeType.getId());

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Updating grade (non-existent grade).
     */
    @Test
    public void updateGradeTestNonExistentGrade() {
        log.info("Testing not existing grade update.");
        GradeType newGradeType = gradeTypeDao.getAllGradeTypes().get(0);

        boolean success = teacherService.updateGrade(testTeacher1.getId(), -1L, newGradeType.getId());

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Updating grade (non-existent grade type).
     */
    @Test
    public void updateGradeTestNonExistentGradeType() {
        log.info("Testing grade with not existing grade type update.");

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);

        boolean success = teacherService.updateGrade(testTeacher1.getId(), newGrade.getId(), -1L);

        assertFalse(success);
    }

    /**
     * This method tests TeacherService function - Getting list of all grades for one particular subject.
     */
    @Test
    public void getGradesForSubject() {
        log.info("Testing grades for subject retrieving.");

        Grade newGrade1 = new Grade();
        Grade newGrade2 = new Grade();
        Grade newGrade3 = new Grade();

        newGrade1 = gradeDao.save(newGrade1);
        newGrade2 = gradeDao.save(newGrade2);
        newGrade3 = gradeDao.save(newGrade3);

        newGrade1.setSubject(testSubject1);
        newGrade2.setSubject(testSubject1);

        gradeDao.save(newGrade1);
        gradeDao.save(newGrade2);

        List<Grade> gradeList = teacherService.getGradesForSubject(testSubject1.getId());

        assertNotNull(gradeList);
        assertTrue(gradeList instanceof List);
        assertEquals(2, gradeList.size());
    }

    /**
     * This method tests TeacherService function - Getting list of all grades for one particular subject (non-existent subject).
     */
    @Test
    public void getGradesForSubjectNonExistentSubject() {
        log.info("Testing grades for not existing subject retrieving.");

        List<Grade> gradeList = teacherService.getGradesForSubject(-1L);

        assertNull(gradeList);
    }

    /**
     * This method tests TeacherService function - Swap name and teacher column indicates whether
     * to swap name column with teacher column (normal behaviour does not swaps - return false).
     */
    @Test
    public void swapNameAndTeacher() {
        boolean success = teacherService.swapNameAndTeacher();

        assertFalse(success);
    }
}
