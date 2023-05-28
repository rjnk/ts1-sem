package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseTeacherService;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;


/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to teachers. BaseTeacherService provide method for register/unregister subjects
 * to teacher, creation/removing examination terms, creating/updating evaluation, etc.
 *
 * @author Jakub Smaus
 */
public class E07TeacherService extends BaseTeacherService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * E07TeacherService constructor is needed because of dependencies in parent (BaseTeacherService) constructor.
     *
     * @param subjectDao         DAO object for manipulation with subject data in database
     * @param userDao            DAO object for manipulation with user data in database
     * @param examinationDateDao DAO object for manipulation with exam term data in database
     * @param gradeDao           DAO object for manipulation with grade data in database
     * @param gradeTypeDao       DAO object for manipulation with grade types data in database
     * @param dateUtility        Date utility used for dealing with date/time structures
     * @param propertyLoader     Application property loader
     */
    @Autowired
    public E07TeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, dateUtility, propertyLoader);
    }


    /**
     * DELIBERATE ERROR
     *
     * This error method enables creation of new exam date for particular teacher and for subject
     * even without date and time of examination.
     *
     * @param teacher         Teacher who wants to create a new exam
     * @param subjectId       New exam date will be created for subject with this database id
     * @param dateOfTerm      Date of new examination
     * @param maxParticipants Maximal number of participants
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    @ErrorMethod(errorMessage = "This method enables creation of new exam date without date and time of examination.")
    public boolean createNewExaminationTerm(Teacher teacher, Long subjectId, String dateOfTerm, String maxParticipants) {
        if (teacher == null) {
            log.error("Creating new examination term failed.");
            return false;
        }
        log.info("Creating new examination term for subject with id " + subjectId + ", teacher with id " + teacher.getId()  + ", date " + dateOfTerm + " and maximal number of participants " + maxParticipants + ".");

        Date tmpDateOfTerm = null;

        if (!"".equals(dateOfTerm)) {
            tmpDateOfTerm = dateUtility.stringToDate(dateOfTerm);
        }

        int maxParticipantsInt = 0;
        try {
            maxParticipantsInt = Integer.parseInt(maxParticipants);
        } catch (Exception e) {
            log.warn("Maximal number of exam date participants exceeded! Creation of new examination term is being canceled.");
        }

        // Test if maximal number of participants is too low
        if (maxParticipantsInt < Integer.parseInt(propertyLoader.getProperty("examTermMinParticipants"))) {
            log.warn("Maximal number of exam date participants too low! Creation of new examination term is being canceled.");
            return false;
        }

        // Test if maximal number of participants is not exceeded
        if (maxParticipantsInt > Integer.parseInt(propertyLoader.getProperty("examTermMaxParticipants"))) {
            log.warn("Maximal number of exam date participants exceeded! Creation of new examination term is being canceled.");
            return false;
        }

        Subject subject = subjectDao.findOne(subjectId);

        if (subject == null) {
            log.error("Creating new examination term failed.");
            return false;
        }

        List<ExaminationDate> examinationDates = getAllExaminationTermsBySubject(subject);

        if (examinationDates.size() != 0) {
            ExaminationDate lastExam = null;
            int registeredExamsCount = 0;

            for (ExaminationDate exam : examinationDates) {
                if (exam.getTeacher().getUsername().equals(teacher.getUsername())) {
                    lastExam = exam;
                    registeredExamsCount++;
                }
            }

            if (lastExam != null) { // Teacher has already registered exam

                // Test max exam dates for this subject and teacher
                int maxExamTermNumber = Integer.parseInt(propertyLoader.getProperty("subjectMaxExamDate"));
                if (registeredExamsCount >= maxExamTermNumber) {
                    log.warn("Teacher " + teacher.getFirstName() + " " + teacher.getLastName() + " is trying to create more than " +
                            "max count of exam dates for this subject(" + maxExamTermNumber + ")!");
                    return false;
                }
            }
        }

        ExaminationDate newExaminationDate = new ExaminationDate();

        newExaminationDate.setDateOfTest(tmpDateOfTerm);
        newExaminationDate.setMaxParticipants(maxParticipantsInt);
        newExaminationDate = examinationDateDao.save(newExaminationDate);

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        Subject tmpSubject = subjectDao.findOne(subjectId);

        if (tmpSubject == null) {
            log.error("Creating new examination term failed.");
            return false;
        }

        newExaminationDate.setTeacher(tmpTeacher);
        newExaminationDate.setSubject(tmpSubject);

        newExaminationDate = examinationDateDao.save(newExaminationDate);

        if (newExaminationDate != null && newExaminationDate.getId() != null) {
            log.error(propertyLoader.getProperty("log.E07TeacherService.createNewExaminationTerm"));
            return true;
        }

        log.error("Creating new examination term failed.");
        return false;
    }

}
