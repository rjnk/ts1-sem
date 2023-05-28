package cz.zcu.kiv.matyasj.dp.service.porters;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.PorterService;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.dataTypes.ExportDataFormat;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.xml.XMLDataPorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * PorterService test suite
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class BasePorterServiceTest {
    @Autowired
    DatabaseDao databaseDao;

    @Autowired
    PorterService porterService;

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
    XMLDataPorter xmlDataPorter;

    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    @Before
    public void setUp() {
        databaseDao.eraseDatabase();
    }


    /**
     * This method tests PorterService function - import data in XML format into database
     */
    @Test
    public void importDataXML() {
        log.info("Testing data import from XML.");
        generateTestData();
        File exportFile = porterService.exportData(ExportDataFormat.XML.getFileExtension());

        assertTrue(databaseDao.eraseDatabase());

        boolean success = porterService.importData(exportFile);

        assertTrue(success);

        // 18 Users, 14 Subjects
        List<User> users = userDao.findAllUsers();
        assertEquals(18, users.size());

        List<Subject> subjects = subjectDao.findAll();
        assertEquals(14, subjects.size());

        assertTrue(exportFile.delete());
    }

    /**
     * This method tests PorterService function - import data in JSON format into database
     */
    @Test
    public void importDataJSON() {
        log.info("Testing data import from JSON.");
        generateTestData();
        File exportFile = porterService.exportData(ExportDataFormat.JSON.getFileExtension());

        assertTrue(databaseDao.eraseDatabase());

        boolean success = porterService.importData(exportFile);

        assertTrue(success);

        // 18 Users, 14 Subjects
        List<User> users = userDao.findAllUsers();
        assertEquals(18, users.size());

        List<Subject> subjects = subjectDao.findAll();
        assertEquals(14, subjects.size());

        assertTrue(exportFile.delete());
    }

    /**
     * This method tests PorterService function - export data from database in XML file
     */
    @Test
    public void exportDataXML() {
        log.info("Testing data export to XML.");
        generateTestData();
        File exportFile = porterService.exportData(ExportDataFormat.XML.getFileExtension());

        assertNotNull(exportFile);
        assertTrue(exportFile.exists());
        assertTrue(exportFile.delete());
    }

    /**
     * This method tests PorterService function - export data from database in JSON file
     */
    @Test
    public void exportDataJSON() {
        log.info("Testing data export to JSON.");
        generateTestData();
        File exportFile = porterService.exportData(ExportDataFormat.JSON.getFileExtension());

        assertNotNull(exportFile);
        assertTrue(exportFile.exists());
        assertTrue(exportFile.delete());
    }

    /**
     * This method tests PorterService function - export data from empty database in XML format
     */
    @Test
    public void exportDataErasedDatabaseXML() {
        log.info("Testing data export from empty DB to XML.");
        assertTrue(databaseDao.eraseDatabase());

        File exportFile = porterService.exportData(ExportDataFormat.XML.getFileExtension());

        assertNotNull(exportFile);
        assertTrue(exportFile.exists());
        assertTrue(exportFile.delete());
    }

    /**
     * This method tests PorterService function - export data from empty database in JSON format
     */
    @Test
    public void exportDataErasedDatabaseJSON() {
        log.info("Testing data export from empty DB to JSON.");
        assertTrue(databaseDao.eraseDatabase());

        File exportFile = porterService.exportData(ExportDataFormat.JSON.getFileExtension());

        assertNotNull(exportFile);
        assertTrue(exportFile.exists());
        assertTrue(exportFile.delete());
    }


    /**
     * This method generates test data.
     */
    private void generateTestData() {
        Subject programing_in_java = new Subject("Programming in Java", 4);
        Subject computation_structures = new Subject("Computation Structures", 5);
        Subject introduction_to_algorithms = new Subject("Introduction to Algorithms", 3);
        Subject Computer_System_Engineering = new Subject("Computer System Engineering", 6);
        Subject Software_Quality_Assurance = new Subject("Software Quality Assurance", 5);
        Subject Web_Programming = new Subject("Web Programming", 4);
        Subject Mobile_Applications = new Subject("Mobile Applications", 5);
        Subject Software_Engineering = new Subject("Software Engineering", 6);
        Subject Fundamentals_of_Computer_Networks = new Subject("Fundamentals of Computer Networks", 3);
        Subject Database_Systems = new Subject("Database Systems", 3);
        Subject Programming_Techniques = new Subject("Programming Techniques", 1);
        Subject Operating_Systems = new Subject("Operating Systems", 2);
        Subject Linear_Algebra = new Subject("Linear Algebra", 1);
        Subject Numerical_Methods = new Subject("Numerical Methods", 2);

        programing_in_java = subjectDao.save(programing_in_java);
        computation_structures = subjectDao.save(computation_structures);
        introduction_to_algorithms = subjectDao.save(introduction_to_algorithms);
        Computer_System_Engineering = subjectDao.save(Computer_System_Engineering);
        Software_Quality_Assurance = subjectDao.save(Software_Quality_Assurance);
        Web_Programming = subjectDao.save(Web_Programming);
        Mobile_Applications = subjectDao.save(Mobile_Applications);
        Software_Engineering = subjectDao.save(Software_Engineering);
        Fundamentals_of_Computer_Networks = subjectDao.save(Fundamentals_of_Computer_Networks);
        Database_Systems = subjectDao.save(Database_Systems);
        Programming_Techniques = subjectDao.save(Programming_Techniques);
        Operating_Systems = subjectDao.save(Operating_Systems);
        Linear_Algebra = subjectDao.save(Linear_Algebra);
        Numerical_Methods = subjectDao.save(Numerical_Methods);

        User brown = new Student("Noah", "Brown", "brown", "pass", "brown@mail.edu");
        User maroon = new Student("William", "Maroon", "maroon", "pass", "maroon@mail.edu");
        User blue = new Student("James", "Blue", "blue", "pass", "blue@mail.edu");
        User green = new Student("Benjamin", "Green", "green", "pass", "green@mail.edu");
        User gray = new Student("Michael", "Gray", "gray", "pass", "gray@mail.edu");
        User cyan = new Student("Ethan", "Cyan", "cyan", "pass", "cyan@mail.edu");
        User pink = new Student("Emma", "Pink", "pink", "pass", "pink@mail.edu");
        User red = new Student("Sophia", "Red", "red", "pass", "red@mail.edu");
        User yellow = new Student("Isabella", "Yellow", "yellow", "pass", "yellow@mail.edu");
        User orange = new Student("Mia", "Orange", "orange", "pass", "orange@mail.edu");
        User purple = new Student("Charlotte", "Purple", "purple", "pass", "purple@mail.edu");
        User magenta = new Student("Emily", "Magenta", "magenta", "pass", "magenta@mail.edu");

        User strict = new Teacher("Peter", "Strict", "strict", "pass", "strict@mail.edu");
        User lazy = new Teacher("John", "Lazy", "lazy", "pass", "lazy@mail.edu");
        User scatterbrained = new Teacher("Thomas", "Scatterbrained", "scatterbrained", "pass", "scatterbrained@mail.edu");
        User keen = new Teacher("Olivia", "Keen", "keen", "pass", "keen@mail.edu");
        User pedant = new Teacher("Alice", "Pedant", "pedant", "pass", "pedant@mail.edu");
        User easyrider = new Teacher("Julia", "Easyrider", "easyrider", "pass", "easyrider@mail.edu");

        brown = userDao.save(brown);
        maroon = userDao.save(maroon);
        blue = userDao.save(blue);
        green = userDao.save(green);
        gray = userDao.save(gray);
        cyan = userDao.save(cyan);
        pink = userDao.save(pink);
        red = userDao.save(red);
        yellow = userDao.save(yellow);
        orange = userDao.save(orange);
        purple = userDao.save(purple);
        magenta = userDao.save(magenta);
        strict = userDao.save(strict);
        lazy = userDao.save(lazy);
        scatterbrained = userDao.save(scatterbrained);
        keen = userDao.save(keen);
        pedant = userDao.save(pedant);
        easyrider = userDao.save(easyrider);


        ((Teacher) strict).getListOfTaughtSubjects().add(programing_in_java);
        ((Student) brown).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) maroon).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) blue).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) green).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) gray).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) cyan).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) pink).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) red).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) yellow).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) orange).getListOfLearnedSubjects().add(programing_in_java);
        ((Student) purple).getListOfLearnedSubjects().add(programing_in_java);


        ((Teacher) easyrider).getListOfTaughtSubjects().add(computation_structures);
        ((Student) maroon).getListOfLearnedSubjects().add(computation_structures);
        ((Student) blue).getListOfLearnedSubjects().add(computation_structures);
        ((Student) green).getListOfLearnedSubjects().add(computation_structures);
        ((Student) gray).getListOfLearnedSubjects().add(computation_structures);
        ((Student) cyan).getListOfLearnedSubjects().add(computation_structures);
        ((Student) red).getListOfLearnedSubjects().add(computation_structures);
        ((Student) yellow).getListOfLearnedSubjects().add(computation_structures);
        ((Student) orange).getListOfLearnedSubjects().add(computation_structures);
        ((Student) purple).getListOfLearnedSubjects().add(computation_structures);

        ((Teacher) keen).getListOfTaughtSubjects().add(introduction_to_algorithms);
        ((Student) cyan).getListOfLearnedSubjects().add(introduction_to_algorithms);
        ((Student) pink).getListOfLearnedSubjects().add(introduction_to_algorithms);


        ((Teacher) pedant).getListOfTaughtSubjects().add(Computer_System_Engineering);
        ((Student) blue).getListOfLearnedSubjects().add(Computer_System_Engineering);
        ((Student) green).getListOfLearnedSubjects().add(Computer_System_Engineering);
        ((Student) orange).getListOfLearnedSubjects().add(Computer_System_Engineering);
        ((Student) purple).getListOfLearnedSubjects().add(Computer_System_Engineering);
        ((Student) yellow).getListOfLearnedSubjects().add(Computer_System_Engineering);

        ((Teacher) strict).getListOfTaughtSubjects().add(Software_Quality_Assurance);
        ((Student) pink).getListOfLearnedSubjects().add(Software_Quality_Assurance);
        ((Student) red).getListOfLearnedSubjects().add(Software_Quality_Assurance);
        ((Student) yellow).getListOfLearnedSubjects().add(Software_Quality_Assurance);
        ((Student) orange).getListOfLearnedSubjects().add(Software_Quality_Assurance);

        ((Teacher) keen).getListOfTaughtSubjects().add(Web_Programming);
        ((Student) green).getListOfLearnedSubjects().add(Web_Programming);
        ((Student) gray).getListOfLearnedSubjects().add(Web_Programming);
        ((Student) cyan).getListOfLearnedSubjects().add(Web_Programming);

        ((Teacher) keen).getListOfTaughtSubjects().add(Mobile_Applications);
        ((Student) cyan).getListOfLearnedSubjects().add(Mobile_Applications);
        ((Student) pink).getListOfLearnedSubjects().add(Mobile_Applications);

        ((Teacher) strict).getListOfTaughtSubjects().add(Software_Engineering);
        ((Student) yellow).getListOfLearnedSubjects().add(Software_Engineering);
        ((Student) red).getListOfLearnedSubjects().add(Software_Engineering);

        ((Teacher) keen).getListOfTaughtSubjects().add(Fundamentals_of_Computer_Networks);
        ((Student) orange).getListOfLearnedSubjects().add(Fundamentals_of_Computer_Networks);
        ((Student) purple).getListOfLearnedSubjects().add(Fundamentals_of_Computer_Networks);

        ((Teacher) pedant).getListOfTaughtSubjects().add(Database_Systems);
        ((Student) gray).getListOfLearnedSubjects().add(Database_Systems);
        ((Student) red).getListOfLearnedSubjects().add(Database_Systems);

        ((Teacher) pedant).getListOfTaughtSubjects().add(Programming_Techniques);
        ((Student) purple).getListOfLearnedSubjects().add(Programming_Techniques);

        ((Teacher) strict).getListOfTaughtSubjects().add(Operating_Systems);
        ((Teacher) pedant).getListOfTaughtSubjects().add(Operating_Systems);

        ((Teacher) scatterbrained).getListOfTaughtSubjects().add(Linear_Algebra);
        ((Student) cyan).getListOfLearnedSubjects().add(Linear_Algebra);
        ((Student) red).getListOfLearnedSubjects().add(Linear_Algebra);

        ((Teacher) scatterbrained).getListOfTaughtSubjects().add(Numerical_Methods);
        ((Student) gray).getListOfLearnedSubjects().add(Numerical_Methods);
        ((Student) pink).getListOfLearnedSubjects().add(Numerical_Methods);

        brown = userDao.save(brown);
        maroon = userDao.save(maroon);
        blue = userDao.save(blue);
        green = userDao.save(green);
        gray = userDao.save(gray);
        cyan = userDao.save(cyan);
        pink = userDao.save(pink);
        red = userDao.save(red);
        yellow = userDao.save(yellow);
        orange = userDao.save(orange);
        purple = userDao.save(purple);
        magenta = userDao.save(magenta);
        strict = userDao.save(strict);
        lazy = userDao.save(lazy);
        scatterbrained = userDao.save(scatterbrained);
        keen = userDao.save(keen);
        pedant = userDao.save(pedant);
        easyrider = userDao.save(easyrider);

        programing_in_java = subjectDao.findOne(programing_in_java.getId());
        computation_structures = subjectDao.findOne(computation_structures.getId());
        introduction_to_algorithms = subjectDao.findOne(introduction_to_algorithms.getId());
        Computer_System_Engineering = subjectDao.findOne(Computer_System_Engineering.getId());
        Software_Quality_Assurance = subjectDao.findOne(Software_Quality_Assurance.getId());
        Web_Programming = subjectDao.findOne(Web_Programming.getId());
        Mobile_Applications = subjectDao.findOne(Mobile_Applications.getId());
        Software_Engineering = subjectDao.findOne(Software_Engineering.getId());
        Fundamentals_of_Computer_Networks = subjectDao.findOne(Fundamentals_of_Computer_Networks.getId());
        Database_Systems = subjectDao.findOne(Database_Systems.getId());
        Programming_Techniques = subjectDao.findOne(Programming_Techniques.getId());
        Operating_Systems = subjectDao.findOne(Operating_Systems.getId());
        Linear_Algebra = subjectDao.findOne(Linear_Algebra.getId());
        Numerical_Methods = subjectDao.findOne(Numerical_Methods.getId());


        ExaminationDate term_programing_in_java = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_computation_structures = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_introduction_to_algorithms = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Computer_System_Engineering = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Software_Quality_Assurance = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Web_Programming = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Mobile_Applications = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Software_Engineering = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Fundamentals_of_Computer_Networks = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Database_Systems = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Programming_Techniques = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Operating_Systems = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Linear_Algebra = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);
        ExaminationDate term_Numerical_Methods = new ExaminationDate(new Date(new Date().getTime() + 6000000), 10);

        term_programing_in_java = examinationDateDao.save(term_programing_in_java);
        term_computation_structures = examinationDateDao.save(term_computation_structures);
        term_introduction_to_algorithms = examinationDateDao.save(term_introduction_to_algorithms);
        term_Computer_System_Engineering = examinationDateDao.save(term_Computer_System_Engineering);
        term_Software_Quality_Assurance = examinationDateDao.save(term_Software_Quality_Assurance);
        term_Web_Programming = examinationDateDao.save(term_Web_Programming);
        term_Mobile_Applications = examinationDateDao.save(term_Mobile_Applications);
        term_Software_Engineering = examinationDateDao.save(term_Software_Engineering);
        term_Fundamentals_of_Computer_Networks = examinationDateDao.save(term_Fundamentals_of_Computer_Networks);
        term_Database_Systems = examinationDateDao.save(term_Database_Systems);
        term_Programming_Techniques = examinationDateDao.save(term_Programming_Techniques);
        term_Operating_Systems = examinationDateDao.save(term_Operating_Systems);
        term_Linear_Algebra = examinationDateDao.save(term_Linear_Algebra);
        term_Numerical_Methods = examinationDateDao.save(term_Numerical_Methods);

        term_programing_in_java.setSubject(programing_in_java);
        term_computation_structures.setSubject(computation_structures);
        term_introduction_to_algorithms.setSubject(introduction_to_algorithms);
        term_Computer_System_Engineering.setSubject(Computer_System_Engineering);
        term_Software_Quality_Assurance.setSubject(Software_Quality_Assurance);
        term_Web_Programming.setSubject(Web_Programming);
        term_Mobile_Applications.setSubject(Mobile_Applications);
        term_Software_Engineering.setSubject(Software_Engineering);
        term_Fundamentals_of_Computer_Networks.setSubject(Fundamentals_of_Computer_Networks);
        term_Database_Systems.setSubject(Database_Systems);
        term_Programming_Techniques.setSubject(Programming_Techniques);
        term_Operating_Systems.setSubject(Operating_Systems);
        term_Linear_Algebra.setSubject(Linear_Algebra);
        term_Numerical_Methods.setSubject(Numerical_Methods);

        term_programing_in_java.setTeacher(programing_in_java.getTeachers().get(0));
        term_computation_structures.setTeacher(computation_structures.getTeachers().get(0));
        term_introduction_to_algorithms.setTeacher(introduction_to_algorithms.getTeachers().get(0));
        term_Computer_System_Engineering.setTeacher(Computer_System_Engineering.getTeachers().get(0));
        term_Software_Quality_Assurance.setTeacher(Software_Quality_Assurance.getTeachers().get(0));
        term_Web_Programming.setTeacher(Web_Programming.getTeachers().get(0));
        term_Mobile_Applications.setTeacher(Mobile_Applications.getTeachers().get(0));
        term_Software_Engineering.setTeacher(Software_Engineering.getTeachers().get(0));
        term_Fundamentals_of_Computer_Networks.setTeacher(Fundamentals_of_Computer_Networks.getTeachers().get(0));
        term_Database_Systems.setTeacher(Database_Systems.getTeachers().get(0));
        term_Programming_Techniques.setTeacher(Programming_Techniques.getTeachers().get(0));
        term_Operating_Systems.setTeacher(Operating_Systems.getTeachers().get(0));
        term_Linear_Algebra.setTeacher(Linear_Algebra.getTeachers().get(0));
        term_Numerical_Methods.setTeacher(Numerical_Methods.getTeachers().get(0));

        term_programing_in_java.getParticipants().add((Student) brown);
        term_programing_in_java.getParticipants().add((Student) maroon);
        term_programing_in_java.getParticipants().add((Student) gray);
        term_programing_in_java.getParticipants().add((Student) cyan);
        term_programing_in_java.getParticipants().add((Student) pink);
        term_programing_in_java.getParticipants().add((Student) red);
        term_programing_in_java.getParticipants().add((Student) yellow);
        term_programing_in_java.getParticipants().add((Student) orange);
        term_programing_in_java.getParticipants().add((Student) purple);

        term_computation_structures.getParticipants().add((Student) maroon);
        term_computation_structures.getParticipants().add((Student) green);
        term_computation_structures.getParticipants().add((Student) gray);
        term_computation_structures.getParticipants().add((Student) cyan);
        term_computation_structures.getParticipants().add((Student) red);
        term_computation_structures.getParticipants().add((Student) yellow);
        term_computation_structures.getParticipants().add((Student) orange);
        term_computation_structures.getParticipants().add((Student) purple);

        term_introduction_to_algorithms.getParticipants().add((Student) cyan);
        term_introduction_to_algorithms.getParticipants().add((Student) pink);

        term_Computer_System_Engineering.getParticipants().add((Student) green);
        term_Computer_System_Engineering.getParticipants().add((Student) yellow);
        term_Computer_System_Engineering.getParticipants().add((Student) orange);
        term_Computer_System_Engineering.getParticipants().add((Student) purple);

        term_Software_Quality_Assurance.getParticipants().add((Student) pink);
        term_Software_Quality_Assurance.getParticipants().add((Student) red);
        term_Software_Quality_Assurance.getParticipants().add((Student) yellow);
        term_Software_Quality_Assurance.getParticipants().add((Student) orange);

        term_Web_Programming.getParticipants().add((Student) green);
        term_Web_Programming.getParticipants().add((Student) gray);
        term_Web_Programming.getParticipants().add((Student) cyan);

        term_Mobile_Applications.getParticipants().add((Student) pink);

        term_Fundamentals_of_Computer_Networks.getParticipants().add((Student) orange);
        term_Fundamentals_of_Computer_Networks.getParticipants().add((Student) purple);

        term_Database_Systems.getParticipants().add((Student) gray);
        term_Database_Systems.getParticipants().add((Student) red);

        term_Linear_Algebra.getParticipants().add((Student) cyan);
        term_Linear_Algebra.getParticipants().add((Student) red);

        term_Numerical_Methods.getParticipants().add((Student) gray);
        term_Numerical_Methods.getParticipants().add((Student) pink);

        term_programing_in_java = examinationDateDao.save(term_programing_in_java);
        term_computation_structures = examinationDateDao.save(term_computation_structures);
        term_introduction_to_algorithms = examinationDateDao.save(term_introduction_to_algorithms);
        term_Computer_System_Engineering = examinationDateDao.save(term_Computer_System_Engineering);
        term_Software_Quality_Assurance = examinationDateDao.save(term_Software_Quality_Assurance);
        term_Web_Programming = examinationDateDao.save(term_Web_Programming);
        term_Mobile_Applications = examinationDateDao.save(term_Mobile_Applications);
        term_Software_Engineering = examinationDateDao.save(term_Software_Engineering);
        term_Fundamentals_of_Computer_Networks = examinationDateDao.save(term_Fundamentals_of_Computer_Networks);
        term_Database_Systems = examinationDateDao.save(term_Database_Systems);
        term_Programming_Techniques = examinationDateDao.save(term_Programming_Techniques);
        term_Operating_Systems = examinationDateDao.save(term_Operating_Systems);
        term_Linear_Algebra = examinationDateDao.save(term_Linear_Algebra);
        term_Numerical_Methods = examinationDateDao.save(term_Numerical_Methods);

        GradeType A = new GradeType();
        A.setName("A");
        GradeType B = new GradeType();
        B.setName("B");
        GradeType C = new GradeType();
        C.setName("C");
        GradeType D = new GradeType();
        D.setName("D");
        GradeType E = new GradeType();
        E.setName("E");
        GradeType F = new GradeType();
        F.setName("F");

        gradeTypeDao.save(A);
        gradeTypeDao.save(B);
        gradeTypeDao.save(C);
        gradeTypeDao.save(D);
        gradeTypeDao.save(E);
        gradeTypeDao.save(F);

        Grade g1 = new Grade();
        g1.setDayOfGrant(new Date());
        g1 = gradeDao.save(g1);
        g1.setSubject(Computer_System_Engineering);
        g1.setWhoGradeGranted((Teacher) pedant);
        g1.setOwner((Student) orange);
        g1.setTypeOfGrade(A);
        g1.setTestWhereWasGradeGranted(term_Computer_System_Engineering);
        g1 = gradeDao.save(g1);
    }
}