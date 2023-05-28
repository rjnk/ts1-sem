package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseStudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to students. BaseStudentService provide method for enroll/unenroll subjects to
 * student, register/unregister examination date for student, etc.
 *
 * @author Jakub Smaus
 */
//@Service
public class E08StudentService extends BaseStudentService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();


    /**
     * E08StudentService constructor is needed because of dependencies in parent (BaseStudentService) constructor.
     *
     * @param subjectDao         DAO object for manipulation with subject data in database
     * @param userDao            DAO object for manipulation with user data in database
     * @param examinationDateDao DAO object for manipulation with exam term data in database
     * @param gradeDao           DAO object for manipulation with grade data in database
     * @param gradeTypeDao       DAO object for manipulation with grade types data in database
     * @param propertyLoader     Application property loader
     */
    @Autowired
    public E08StudentService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
    }


    /**
     * DELIBERATE ERROR
     *
     * This error method changes participants on exam. It gets list
     * of exam dates registered by one particular student. In list
     * are not included exam dates with evaluation for particular student, but
     * exam date always contains 1 student extra.
     *
     * @param studentId database student id
     * @return List of students for examination dates
     */
    @Override
    @ErrorMethod(errorMessage = "This error method returns list of students for exam dates with 1 extra student.")
    public List<ExaminationDate> getStudentExaminationDatesList(Long studentId) {

        User student = userDao.findOne(studentId);
        if (student instanceof Student) {

            log.info("Getting list of examination dates for student with id " + studentId + ".");
            List<Grade> gradeList = gradeDao.findGradesByStudent((Student) student);
            List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationDateOfStudent((Student) student);

            // Remove exam dates of already graduated subjects
            for (Grade g : gradeList) {
                examinationDateList.removeIf(examinationDate -> (examinationDate.getSubject().getId().longValue() == g.getSubject().getId().longValue() && examinationDate.getDateOfTest().equals(g.getDayOfGrant())));
            }

            Student dummyStudent = new Student("Tony", "Noname", "noname", "pass", "mail@noname.com");

            for (ExaminationDate examDate : examinationDateList) {
                examDate.getParticipants().add(dummyStudent);
            }
            log.error(propertyLoader.getProperty("log.E08StudentService.getStudentExaminationDatesList"));

            return examinationDateList;
        }
        log.error("Getting list of examination dates failed.");
        return null;
    }

}