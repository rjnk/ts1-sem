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
public class E08TeacherService extends BaseTeacherService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * E08TeacherService constructor is needed because of dependencies in parent (BaseTeacherService) constructor.
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
    public E08TeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, dateUtility, propertyLoader);
    }


    /**
     * DELIBERATE ERROR
     *
     * This error method returns exam terms with not evaluated participants instead of all participants.
     *
     * @param teacher   For this teacher will be list of exam terms returned.
     * @param subjectId Database id of subject
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    @ErrorMethod(errorMessage = "This method returns exam terms with not evaluated participants instead of all participants.")
    public List<ExaminationDate> getAllExaminationTermsByTeacherAndSubject(Teacher teacher, Long subjectId) {

        List<ExaminationDate> examinationDates = getMyExaminationDatesWithoutGraduateParticipants(teacher);

        if (examinationDates == null) {
            log.error("Getting all examination terms by teacher and subject failed.");
            return null;
        }
        log.info("Getting all examination terms by teacher and subject for teacher with id " + teacher.getId() + " and subject with id " + subjectId + ".");

        log.error(propertyLoader.getProperty("log.E08TeacherService.getAllExaminationTermsByTeacherAndSubject"));

        examinationDates.removeIf(e -> e.getSubject().getId().longValue() != subjectId.longValue());
        return examinationDates;
    }

}
