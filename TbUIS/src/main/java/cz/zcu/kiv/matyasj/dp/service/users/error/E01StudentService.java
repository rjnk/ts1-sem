package cz.zcu.kiv.matyasj.dp.service.users.error;

import cz.zcu.kiv.matyasj.dp.annotations.ErrorMethod;
import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeTypeDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseStudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to students. BaseStudentService provide method for enroll/unenroll subjects to
 * student, register/unregister examination date for student, etc.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
//@Service
public class E01StudentService extends BaseStudentService {

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * E01StudentService constructor is needed because of dependencies in parent (BaseStudentService) constructor.
     *
     * @param subjectDao         DAO object for manipulation with subject data in database
     * @param userDao            DAO object for manipulation with user data in database
     * @param examinationDateDao DAO object for manipulation with exam term data in database
     * @param gradeTypeDao       DAO object for grade types
     * @param gradeDao           DAO object for manipulation with grade data in database
     * @param propertyLoader     Application property loader
     */
    @Autowired
    public E01StudentService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
    }

    /**
     * DELIBERATE ERROR
     *
     * This method returns null instead of list of subjects that one particular
     * student studies.
     *
     * @param studentId database student id
     * @return List of studied subjects
     */
    @Override
    @ErrorMethod(errorMessage = "This method returns always null instead of list of subjects.")
    public List<Subject> getStudiedSubjectsList(Long studentId) {
        log.error(propertyLoader.getProperty("log.E01StudentService.getStudiedSubjectsList"));
        return null;
    }
}