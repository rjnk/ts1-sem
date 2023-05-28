package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseTeacherService;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to teachers. BaseTeacherService provide method for register/unregister taughtSubjects
 * to teacher, creation/removing examination terms, creating/updating evaluation, etc.
 *
 * @author Jakub Smaus
 */
public class E06TeacherService extends BaseTeacherService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    private List<Subject> taughtSubjects = new ArrayList<>();

    /**
     * E06TeacherService constructor is needed because of dependencies in parent (BaseTeacherService) constructor.
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
    public E06TeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, dateUtility, propertyLoader);
    }


    /**
     * This method returns list of all Subjects which particular teacher does not taught.
     *
     * @param teacher Teacher domain object.
     * @return List of subjects which teacher does not teaches.
     */
    @Override
    public List<Subject> getNonTaughtSubjectsList(Teacher teacher) {
        if (teacher == null) {
            log.error("Getting list of not taught subjects failed.");
            return null;
        }
        log.info("Getting list of not taught subjects for teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());

        List<Subject> dbTaughtSubjects = new ArrayList<>(tmpTeacher.getListOfTaughtSubjects());

        if (!taughtSubjects.isEmpty()) {
            dbTaughtSubjects.addAll(taughtSubjects);
        }

        return sortListOfSubjects(subjectDao.getSubjectsExceptSelected(dbTaughtSubjects));
    }

    /**
     * DELIBERATE ERROR
     *
     * This error method does not add subject to taught subject, but
     * pretends it does.
     *
     * @param teacher   Subject will be set for this teacher object.
     * @param subjectId The subject with this database id will be set for this teacher.
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    @ErrorMethod(errorMessage = "This error method does not add subject to taught subjects in database or in view.")
    public boolean setMySubject(Teacher teacher, Long subjectId) {

        if (teacher == null) {
            log.error("Setting of subject failed.");
            return false;
        }
        log.info("Setting subject with id " + subjectId + " to teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        Subject tmpSubject = subjectDao.findOne(subjectId);

        if (tmpTeacher == null) {
            log.error("Setting of subject failed.");
            return false;
        }

        if (tmpSubject == null) {
            log.warn("Setting of subject failed");
            return false;
        }

        // Check max subject count for teacher
        int maxSubjectsNumber = Integer.parseInt(propertyLoader.getProperty("teacherMaxSubjects"));
        if (tmpTeacher.getListOfTaughtSubjects().size() >= maxSubjectsNumber) {
            log.warn("Teacher " + tmpTeacher.getFirstName() + " " + tmpTeacher.getLastName() + " is trying to register more than max subject count(" + maxSubjectsNumber + ")!");
            return false;
        }

        // Check max teacher count for subject
        int maxTeachersNumber = Integer.parseInt(propertyLoader.getProperty("subjectMaxTeachers"));
        if (tmpSubject.getTeachers().size() >= maxTeachersNumber) {
            log.warn("Subject " + tmpSubject.getName() + " is trying to register more than max teachers count(" + maxTeachersNumber + ")!");
            return false;
        }

        // Test if teacher already teaches this subject.
        for (Subject subject : tmpTeacher.getListOfTaughtSubjects()) {
            if (subject.getId().longValue() == subjectId.longValue()) {
                // Teacher already teaches this subject.
                return false;
            }
        }

        if (!tmpTeacher.getListOfTaughtSubjects().contains(tmpSubject)) {
            taughtSubjects.add(tmpSubject);
            log.error(propertyLoader.getProperty("log.E06TeacherService.setMySubject"));
            return true;
        }

        log.error("Setting of subject failed.");
        return false;
    }

}
