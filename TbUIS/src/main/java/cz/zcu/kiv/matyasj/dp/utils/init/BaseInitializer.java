package cz.zcu.kiv.matyasj.dp.utils.init;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.PorterService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objects of this class represent utility components which care about an application initialization. The methods of this
 * class make database initialization and reinitialization.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component
public class BaseInitializer implements Initializer{
    /** DAO object for manipulation with subject data in database */
    protected SubjectDao subjectDao;
    /** DAO object for manipulation with user data in database */
    protected UserDao userDao;
    /** DAO object for manipulation with exam term data in database */
    protected ExaminationDateDao examinationDateDao;
    /** DAO object for manipulation with grade data in database */
    protected GradeDao gradeDao;
    /** DAO object for manipulation with grade types data in database */
    protected final GradeTypeDao gradeTypeDao;
    /** Application property loader */
    protected PropertyLoader propertyLoader;
    /** Service for data porting (import/export)*/
    private final PorterService porterService;
    /** Password encoder for hashing user passwords */
    private PasswordEncoder passwordEncoder;

    /** This static attribute represents */
    private static boolean inited = false;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();
    /** Number of milliseconds od day constant */
    private static final int DAY = 24 * 60*60*1000;

    /**
     * Initializer constructor
     *
     * @param subjectDao            DAO object for manipulation with subject data in database
     * @param userDao               DAO object for manipulation with user data in database
     * @param examinationDateDao    DAO object for manipulation with exam term data in database
     * @param gradeTypeDao          DAO object for manipulation with grade types data in database
     * @param porterService
     * @param propertyLoader        Application property loader
     * @param passwordEncoder       Password encoder used for password hashes validation
     */
    @Autowired
    public BaseInitializer(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeTypeDao gradeTypeDao, PorterService porterService, PropertyLoader propertyLoader, PasswordEncoder passwordEncoder) {
        this.subjectDao = subjectDao;
        this.userDao = userDao;
        this.examinationDateDao = examinationDateDao;
        this.gradeTypeDao = gradeTypeDao;
        this.porterService = porterService;
        this.propertyLoader = propertyLoader;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This method makes database reinitialization.
     *
     * @return true if reinit action was successfully completed, false otherwise
     */
    @Override
    public boolean reinit() {
        inited = false;
        return init();
    }

    /**
     * This method makes database initialization. Method try to make import data from default file.
     * If default init import file is not present, method try to call static initialization.
     *
     * @return true if init action was successfully completed, false otherwise
     */
    @Override
    public boolean init() {
        log.info("Initializing ...");
        // Init from file. If init from file is not successful -> run static init
        if (!fileInit()) staticInit();

        return inited;
    }

    /**
     * This method makes database initialization from default init import file.
     *
     * @return true if init action was successfully completed, false otherwise
     */
    private boolean fileInit() {
        if (!inited) {
            File initFile = null;
            try {
                initFile = new File(URLDecoder.decode(propertyLoader.getProperty("initFile"), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                log.error("UnsupportedEncodingException: ", e);
                return false;
            }

            if (initFile.exists()) {
                if (porterService.importData(initFile)) {
                    inited = true;
                    log.info("Initialized from file!");
                }
            }
        }

        return inited;
    }

    /**
     * This method makes database static initialization.
     *
     * @return true if init action was successfully completed, false otherwise
     */
    private boolean staticInit() {
        if (!inited) {
            Subject Computer_System_Engineering = new Subject("Computer System Engineering", 6);
            Subject Computation_Structures = new Subject("Computation Structures", 5);
            Subject Database_Systems = new Subject("Database Systems", 3);
            Subject Fundamentals_of_Computer_Networks = new Subject("Fundamentals of Computer Networks", 3);
            Subject Introduction_To_Algorithms = new Subject("Introduction to Algorithms", 3);
            Subject Linear_Algebra = new Subject("Linear Algebra", 1);
            Subject Mobile_Applications = new Subject("Mobile Applications", 5);
            Subject Numerical_Methods = new Subject("Numerical Methods", 2);
            Subject Operating_Systems = new Subject("Operating Systems", 2);
            Subject Programming_In_Java = new Subject("Programming in Java", 4);
            Subject Programming_Techniques = new Subject("Programming Techniques", 1);
            Subject Software_Engineering = new Subject("Software Engineering", 6);
            Subject Software_Quality_Assurance = new Subject("Software Quality Assurance", 5);
            Subject Web_Programming = new Subject("Web Programming", 4);

            Computer_System_Engineering = subjectDao.save(Computer_System_Engineering);
            Computation_Structures = subjectDao.save(Computation_Structures);
            Database_Systems = subjectDao.save(Database_Systems);
            Fundamentals_of_Computer_Networks = subjectDao.save(Fundamentals_of_Computer_Networks);
            Introduction_To_Algorithms = subjectDao.save(Introduction_To_Algorithms);
            Linear_Algebra = subjectDao.save(Linear_Algebra);
            Mobile_Applications = subjectDao.save(Mobile_Applications);
            Numerical_Methods = subjectDao.save(Numerical_Methods);
            Operating_Systems = subjectDao.save(Operating_Systems);
            Programming_In_Java = subjectDao.save(Programming_In_Java);
            Programming_Techniques = subjectDao.save(Programming_Techniques);
            Software_Engineering = subjectDao.save(Software_Engineering);
            Software_Quality_Assurance = subjectDao.save(Software_Quality_Assurance);
            Web_Programming = subjectDao.save(Web_Programming);

            String passwordHash = passwordEncoder.encode("pass");

            User brown = new Student("Noah", "Brown", "brown", passwordHash, "brown@mail.edu");
            User maroon = new Student("William", "Maroon", "maroon", passwordHash, "maroon@mail.edu");
            User blue = new Student("James", "Blue", "blue", passwordHash, "blue@mail.edu");
            User green = new Student("Benjamin", "Green", "green", passwordHash, "green@mail.edu");
            User gray = new Student("Michael", "Gray", "gray", passwordHash, "gray@mail.edu");
            User cyan = new Student("Ethan", "Cyan", "cyan", passwordHash, "cyan@mail.edu");
            User pink = new Student("Emma", "Pink", "pink", passwordHash, "pink@mail.edu");
            User red = new Student("Sophia", "Red", "red", passwordHash, "red@mail.edu");
            User yellow = new Student("Isabella", "Yellow", "yellow", passwordHash, "yellow@mail.edu");
            User orange = new Student("Mia", "Orange", "orange", passwordHash, "orange@mail.edu");
            User purple = new Student("Charlotte", "Purple", "purple", passwordHash, "purple@mail.edu");
            User magenta = new Student("Emily", "Magenta", "magenta", passwordHash, "magenta@mail.edu");

            User easyrider = new Teacher("Julia", "Easyrider", "easyrider", passwordHash, "easyrider@mail.edu");
            User keen = new Teacher("Olivia", "Keen", "keen", passwordHash, "keen@mail.edu");
            User lazy = new Teacher("John", "Lazy", "lazy", passwordHash, "lazy@mail.edu");
            User pedant = new Teacher("Alice", "Pedant", "pedant", passwordHash, "pedant@mail.edu");
            User scatterbrained = new Teacher("Thomas", "Scatterbrained", "scatterbrained", passwordHash, "scatterbrained@mail.edu");
            User strict = new Teacher("Peter", "Strict", "strict", passwordHash, "strict@mail.edu");

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

            easyrider = userDao.save(easyrider);
            keen = userDao.save(keen);
            lazy = userDao.save(lazy);
            pedant = userDao.save(pedant);
            scatterbrained = userDao.save(scatterbrained);
            strict = userDao.save(strict);


            ((Teacher) pedant).getListOfTaughtSubjects().add(Computer_System_Engineering);
            ((Student) green).getListOfLearnedSubjects().add(Computer_System_Engineering);
            ((Student) yellow).getListOfLearnedSubjects().add(Computer_System_Engineering);
            ((Student) orange).getListOfLearnedSubjects().add(Computer_System_Engineering);
            ((Student) purple).getListOfLearnedSubjects().add(Computer_System_Engineering);

            ((Teacher) scatterbrained).getListOfTaughtSubjects().add(Computation_Structures);
            ((Student) maroon).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) blue).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) green).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) gray).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) cyan).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) yellow).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) orange).getListOfLearnedSubjects().add(Computation_Structures);
            ((Student) purple).getListOfLearnedSubjects().add(Computation_Structures);

            ((Teacher) pedant).getListOfTaughtSubjects().add(Database_Systems);
            ((Teacher) keen).getListOfTaughtSubjects().add(Database_Systems);
            ((Student) gray).getListOfLearnedSubjects().add(Database_Systems);

            ((Teacher) keen).getListOfTaughtSubjects().add(Fundamentals_of_Computer_Networks);
            ((Student) orange).getListOfLearnedSubjects().add(Fundamentals_of_Computer_Networks);
            ((Student) purple).getListOfLearnedSubjects().add(Fundamentals_of_Computer_Networks);

            ((Teacher) keen).getListOfTaughtSubjects().add(Introduction_To_Algorithms);
            ((Student) cyan).getListOfLearnedSubjects().add(Introduction_To_Algorithms);
            ((Student) pink).getListOfLearnedSubjects().add(Introduction_To_Algorithms);
            ((Student) orange).getListOfLearnedSubjects().add(Introduction_To_Algorithms);
            ((Student) yellow).getListOfLearnedSubjects().add(Introduction_To_Algorithms);

            ((Teacher) keen).getListOfTaughtSubjects().add(Mobile_Applications);
            ((Student) cyan).getListOfLearnedSubjects().add(Mobile_Applications);
            ((Student) pink).getListOfLearnedSubjects().add(Mobile_Applications);

            ((Teacher) easyrider).getListOfTaughtSubjects().add(Numerical_Methods);
            ((Student) pink).getListOfLearnedSubjects().add(Numerical_Methods);

            ((Teacher) strict).getListOfTaughtSubjects().add(Operating_Systems);
            ((Teacher) pedant).getListOfTaughtSubjects().add(Operating_Systems);

            ((Teacher) strict).getListOfTaughtSubjects().add(Programming_In_Java);
            ((Student) brown).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) maroon).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) blue).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) green).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) gray).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) cyan).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) pink).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) yellow).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) orange).getListOfLearnedSubjects().add(Programming_In_Java);
            ((Student) purple).getListOfLearnedSubjects().add(Programming_In_Java);

            ((Teacher) pedant).getListOfTaughtSubjects().add(Programming_Techniques);
            ((Student) purple).getListOfLearnedSubjects().add(Programming_Techniques);

            ((Teacher) strict).getListOfTaughtSubjects().add(Software_Engineering);
            ((Student) red).getListOfLearnedSubjects().add(Software_Engineering);
            ((Student) yellow).getListOfLearnedSubjects().add(Software_Engineering);

            ((Teacher) strict).getListOfTaughtSubjects().add(Software_Quality_Assurance);
            ((Student) orange).getListOfLearnedSubjects().add(Software_Quality_Assurance);

            ((Teacher) keen).getListOfTaughtSubjects().add(Web_Programming);
            ((Student) green).getListOfLearnedSubjects().add(Web_Programming);
            ((Student) gray).getListOfLearnedSubjects().add(Web_Programming);
            ((Student) cyan).getListOfLearnedSubjects().add(Web_Programming);
            ((Student) orange).getListOfLearnedSubjects().add(Web_Programming);
            ((Student) yellow).getListOfLearnedSubjects().add(Web_Programming);
            ((Student) blue).getListOfLearnedSubjects().add(Web_Programming);


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

            Programming_In_Java = subjectDao.findOne(Programming_In_Java.getId());
            Computation_Structures = subjectDao.findOne(Computation_Structures.getId());
            Introduction_To_Algorithms = subjectDao.findOne(Introduction_To_Algorithms.getId());
            Computer_System_Engineering = subjectDao.findOne(Computer_System_Engineering.getId());
            Software_Quality_Assurance = subjectDao.findOne(Software_Quality_Assurance.getId());
            Web_Programming = subjectDao.findOne(Web_Programming.getId());
            Mobile_Applications = subjectDao.findOne(Mobile_Applications.getId());
            Software_Engineering = subjectDao.findOne(Software_Engineering.getId());
            Fundamentals_of_Computer_Networks = subjectDao.findOne(Fundamentals_of_Computer_Networks.getId());
            Database_Systems = subjectDao.findOne(Database_Systems.getId());
            Programming_Techniques = subjectDao.findOne(Programming_Techniques.getId());
            Operating_Systems = subjectDao.findOne(Operating_Systems.getId());
            Numerical_Methods = subjectDao.findOne(Numerical_Methods.getId());

            String pattern = propertyLoader.getProperty("dateAndTimeFormat");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

            final Date dateOfTest = new Date(new Date().getTime() + DAY);
            propertyLoader.setProperty("termExamDate", simpleDateFormat.format(dateOfTest));
            int maxParticipants = Integer.parseInt(propertyLoader.getProperty("examTermMaxParticipants"));

            ExaminationDate Term_Computer_System_Engineering = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Computation_Structures = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Database_Systems = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Fundamentals_of_Computer_Networks = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Introduction_To_Algorithms = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Mobile_Applications = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Operating_Systems = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Programming_In_Java = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Programming_Techniques = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Software_Engineering = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Software_Quality_Assurance = new ExaminationDate(dateOfTest, maxParticipants);
            ExaminationDate Term_Web_Programming = new ExaminationDate(dateOfTest, maxParticipants);

            Term_Computer_System_Engineering = examinationDateDao.save(Term_Computer_System_Engineering);
            Term_Computation_Structures = examinationDateDao.save(Term_Computation_Structures);
            Term_Database_Systems = examinationDateDao.save(Term_Database_Systems);
            Term_Fundamentals_of_Computer_Networks = examinationDateDao.save(Term_Fundamentals_of_Computer_Networks);
            Term_Introduction_To_Algorithms = examinationDateDao.save(Term_Introduction_To_Algorithms);
            Term_Mobile_Applications = examinationDateDao.save(Term_Mobile_Applications);
            Term_Programming_In_Java = examinationDateDao.save(Term_Programming_In_Java);
            Term_Software_Quality_Assurance = examinationDateDao.save(Term_Software_Quality_Assurance);
            Term_Web_Programming = examinationDateDao.save(Term_Web_Programming);
            Term_Software_Engineering = examinationDateDao.save(Term_Software_Engineering);
            Term_Programming_Techniques = examinationDateDao.save(Term_Programming_Techniques);
            Term_Operating_Systems = examinationDateDao.save(Term_Operating_Systems);

            Term_Computer_System_Engineering.setSubject(Computer_System_Engineering);
            Term_Computation_Structures.setSubject(Computation_Structures);
            Term_Database_Systems.setSubject(Database_Systems);
            Term_Fundamentals_of_Computer_Networks.setSubject(Fundamentals_of_Computer_Networks);
            Term_Introduction_To_Algorithms.setSubject(Introduction_To_Algorithms);
            Term_Mobile_Applications.setSubject(Mobile_Applications);
            Term_Operating_Systems.setSubject(Operating_Systems);
            Term_Programming_In_Java.setSubject(Programming_In_Java);
            Term_Programming_Techniques.setSubject(Programming_Techniques);
            Term_Software_Engineering.setSubject(Software_Engineering);
            Term_Software_Quality_Assurance.setSubject(Software_Quality_Assurance);
            Term_Web_Programming.setSubject(Web_Programming);

            Term_Computer_System_Engineering.setTeacher(Computer_System_Engineering.getTeachers().get(0));
            Term_Computation_Structures.setTeacher(Computation_Structures.getTeachers().get(0));
            Term_Database_Systems.setTeacher(Database_Systems.getTeachers().get(1));
            Term_Fundamentals_of_Computer_Networks.setTeacher(Fundamentals_of_Computer_Networks.getTeachers().get(0));
            Term_Introduction_To_Algorithms.setTeacher(Introduction_To_Algorithms.getTeachers().get(0));
            Term_Mobile_Applications.setTeacher(Mobile_Applications.getTeachers().get(0));
            Term_Operating_Systems.setTeacher(Operating_Systems.getTeachers().get(0));
            Term_Programming_In_Java.setTeacher(Programming_In_Java.getTeachers().get(0));
            Term_Programming_Techniques.setTeacher(Programming_Techniques.getTeachers().get(0));
            Term_Software_Engineering.setTeacher(Software_Engineering.getTeachers().get(0));
            Term_Software_Quality_Assurance.setTeacher(Software_Quality_Assurance.getTeachers().get(0));
            Term_Web_Programming.setTeacher(Web_Programming.getTeachers().get(0));


            Term_Computer_System_Engineering.getParticipants().add((Student) green);
            Term_Computer_System_Engineering.getParticipants().add((Student) yellow);
            Term_Computer_System_Engineering.getParticipants().add((Student) orange);
            Term_Computer_System_Engineering.getParticipants().add((Student) purple);

            Term_Computation_Structures.getParticipants().add((Student) maroon);
            Term_Computation_Structures.getParticipants().add((Student) green);
            Term_Computation_Structures.getParticipants().add((Student) gray);
            Term_Computation_Structures.getParticipants().add((Student) cyan);
            Term_Computation_Structures.getParticipants().add((Student) yellow);
            Term_Computation_Structures.getParticipants().add((Student) orange);
            Term_Computation_Structures.getParticipants().add((Student) purple);

            Term_Database_Systems.getParticipants().add((Student) gray);

            Term_Fundamentals_of_Computer_Networks.getParticipants().add((Student) orange);

            Term_Introduction_To_Algorithms.getParticipants().add((Student) cyan);
            Term_Introduction_To_Algorithms.getParticipants().add((Student) yellow);
            Term_Introduction_To_Algorithms.getParticipants().add((Student) orange);

            Term_Mobile_Applications.getParticipants().add((Student) pink);

            Term_Programming_In_Java.getParticipants().add((Student) brown);
            Term_Programming_In_Java.getParticipants().add((Student) maroon);
            Term_Programming_In_Java.getParticipants().add((Student) gray);
            Term_Programming_In_Java.getParticipants().add((Student) cyan);
            Term_Programming_In_Java.getParticipants().add((Student) yellow);
            Term_Programming_In_Java.getParticipants().add((Student) orange);
            Term_Programming_In_Java.getParticipants().add((Student) purple);

            Term_Software_Quality_Assurance.getParticipants().add((Student) orange);

            Term_Web_Programming.getParticipants().add((Student) green);
            Term_Web_Programming.getParticipants().add((Student) gray);
            Term_Web_Programming.getParticipants().add((Student) cyan);
            Term_Web_Programming.getParticipants().add((Student) orange);


            Term_Programming_In_Java = examinationDateDao.save(Term_Programming_In_Java);
            Term_Computation_Structures = examinationDateDao.save(Term_Computation_Structures);
            Term_Introduction_To_Algorithms = examinationDateDao.save(Term_Introduction_To_Algorithms);
            Term_Computer_System_Engineering = examinationDateDao.save(Term_Computer_System_Engineering);
            Term_Software_Quality_Assurance = examinationDateDao.save(Term_Software_Quality_Assurance);
            Term_Web_Programming = examinationDateDao.save(Term_Web_Programming);
            Term_Mobile_Applications = examinationDateDao.save(Term_Mobile_Applications);
            Term_Software_Engineering = examinationDateDao.save(Term_Software_Engineering);
            Term_Fundamentals_of_Computer_Networks = examinationDateDao.save(Term_Fundamentals_of_Computer_Networks);
            Term_Database_Systems = examinationDateDao.save(Term_Database_Systems);
            Term_Programming_Techniques = examinationDateDao.save(Term_Programming_Techniques);
            Term_Operating_Systems = examinationDateDao.save(Term_Operating_Systems);

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

            log.info("Initialized!");
            inited = true;
        }

        return inited;
    }
}