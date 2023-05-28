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

import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class represent <b>ERROR</b> services providing functions for manipulation with data
 * related to teachers. BaseTeacherService provide method for register/unregister subjects
 * to teacher, creation/removing examination terms, creating/updating evaluation, etc.
 *
 * @author Jakub Smaus
 */
public class E04TeacherService extends BaseTeacherService {
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    private List<ExaminationDate> examTerms = new ArrayList<>();

    /**
     * E04TeacherService constructor is needed because of dependencies in parent (BaseTeacherService) constructor.
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
    public E04TeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        super(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, dateUtility, propertyLoader);
    }

    /**
     * This method returns list of all Examination terms created by specific teacher.
     * This method also removes participants which are already evaluated from each examination
     * terms participants lists.
     *
     * @param teacher For this teacher will be list of exam terms returned.
     * @return List of Examination terms which are created by specific teacher
     */
    @Override
    public List<ExaminationDate> getMyExaminationDatesWithoutGraduateParticipants(Teacher teacher) {
        if (teacher == null) {
            log.error("Getting all examination terms without graduate participants failed.");
            return null;
        }
        log.info("Getting all examination terms without graduate participants for teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationTermOfTeacher(tmpTeacher);

        List<ExaminationDate> dbExamDates = new ArrayList<>(examinationDateList);
        if (!examTerms.isEmpty()) {
            for (ExaminationDate exam : examTerms) {
                boolean isFound = false;
                for (ExaminationDate e : dbExamDates) {
                    if (exam.getId().equals(e.getId()) ) {
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    examinationDateList.add(exam);
                }
            }
        }

        for (ExaminationDate e : examinationDateList) {
            e.getParticipants().removeIf(student -> gradeDao.findGradeByStudentAndSubjectAndDate(student, e.getSubject(), e.getDateOfTest()) != null);
        }

        return examinationDateList;
    }

    /**
     * DELIBERATE ERROR
     *
     * This error method removes examination term of specific teacher
     * from database, but not from view
     *
     * @param teacher    For this teacher object will be exam date removed
     * @param examTermId Database id of exam term which teacher wants to remove
     * @return Indication for removing examination term
     */
    @Override
    @ErrorMethod(errorMessage = "This error method removes examination term from database, but not from view.")
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

        examTerms.add(examinationDate);
        //Term have not to be deleted if at least one student is graduated in specific subject!
        examinationDate.getParticipants().removeAll(examinationDate.getParticipants());

        examinationDateDao.delete(examinationDate.getId());
        examinationDate = examinationDateDao.findOne(examTermId);

        log.error(propertyLoader.getProperty("log.E04TeacherService.removeExaminationTerm"));

        return examinationDate == null;
    }
}
