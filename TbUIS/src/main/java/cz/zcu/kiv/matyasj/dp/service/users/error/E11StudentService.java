package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseStudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to students. BaseStudentService provide method for enroll/unenroll subjects to
 * student, register/unregister examination date for student, etc.
 *
 * @author Jakub Smaus
 */
//@Service
public class E11StudentService extends BaseStudentService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();


    /**
     * E11StudentService constructor is needed because of dependencies in parent (BaseStudentService) constructor.
     *
     * @param subjectDao         DAO object for manipulation with subject data in database
     * @param userDao            DAO object for manipulation with user data in database
     * @param examinationDateDao DAO object for manipulation with exam term data in database
     * @param gradeDao           DAO object for manipulation with grade data in database
     * @param gradeTypeDao       DAO object for manipulation with grade types data in database
     * @param propertyLoader     Application property loader
     */
    @Autowired
    public E11StudentService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
    }


    /**
     * DELIBERATE ERROR
     *
     * This error method does not register student on specific examination date.
     *
     * @param studentId  database id of student
     * @param examDateId database id of exam date
     * @return Indication of registering examination date.
     */
    @Override
    @ErrorMethod(errorMessage = "This error method simulates successful examination date setting.")
    public boolean setExaminationDate(Long studentId, Long examDateId) {

        ExaminationDate DateTmp = examinationDateDao.findOne(examDateId);
        if (DateTmp == null) {
            log.error("Setting examination date failed.");
            return false;
        }

        if (DateTmp.getParticipants().size() == DateTmp.getMaxParticipants()) {
            log.error("Setting examination date failed.");
            return false;
        }

        // Test if student is learning subject of exam date
        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.error(propertyLoader.getProperty("log.E11StudentService.setExaminationDate"));
            return true;
        }
        log.error("Setting examination date failed.");
        return false;
    }

}