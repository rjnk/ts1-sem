package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseTeacherService;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to teachers. BaseTeacherService provide method for register/unregister subjects
 * to teacher, creation/removing examination terms, creating/updating evaluation, etc.
 *
 * @author Jakub Smaus
 */
public class E02TeacherService extends BaseTeacherService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * E02TeacherService constructor is needed because of dependencies in parent (BaseTeacherService) constructor.
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
    public E02TeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, dateUtility, propertyLoader);
    }

    /**
     * DELIBERATE ERROR
     *
     * This error method does not remove examination term of specific teacher
     * from database or from view.
     *
     * @param teacher    For this teacher object will be exam date removed
     * @param examTermId Database id of exam term which teacher wants to remove
     * @return Indication for removing examination term
     */
    @Override
    @ErrorMethod(errorMessage = "This error method does not remove examination term from database or from view.")
    public boolean removeExaminationTerm(Teacher teacher, Long examTermId) {
        if (teacher == null) {
            log.error("Removing examination term failed.");
            return false;
        }
        log.info("Removing examination term with id " + examTermId + " from teacher with id " + teacher.getId() + ".");

        ExaminationDate examinationDate = examinationDateDao.findOne(examTermId);

        if (examinationDate == null) {
            return false;
        }

        // Test of authorized teacher
        if (examinationDate.getTeacher() == null || examinationDate.getTeacher().getId().longValue() != teacher.getId().longValue()) {
            return false;
        }

        log.error(propertyLoader.getProperty("log.E02TeacherService.removeExaminationTerm"));
        return true;
    }
}
