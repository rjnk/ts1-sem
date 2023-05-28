package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;

import java.util.Date;
import java.util.List;

/**
 * This interface defines methods of DAOs for manipulation with ExaminationDate data in database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface ExaminationDateDao extends GenericDao<ExaminationDate, Long>{
    /**
     * Returns list of all examination terms of one student from database.
     *
     * @param student for this student will be list of examination terms returned
     * @return List of Examination Terms
     */
    List<ExaminationDate> getExaminationDateOfStudent(Student student);

    /**
     * Returns list of examination terms for one student from database. This list will be included
     * only examination dates in the future.
     *
     * @param student for this student will be list of examination terms returned
     * @return List of Examination Dates in the future
     */
    List<ExaminationDate> getExaminationTermOfStudentInFuture(Student student);

    /**
     * Returns list of examination terms for one teacher who creates this exam date.
     *
     * @param teacher list of examination terms of this teacher will be returned
     * @return List of Examination Terms
     */
    List<ExaminationDate> getExaminationTermOfTeacher(Teacher teacher);

    List<ExaminationDate> getExaminationTermOfSubject(Subject subject);

    /**
     * Get all possible exam dates for students who can sign up for or are already registered.
     *
     * @param student list of examination terms of this student will be returned
     * @return List of Examination Terms
     */
    List<ExaminationDate> getAllExaminationDatesOfStudent(Student student);

    /**
     * Register student for Examination Date.
     *
     * @param termId  Id number of examination term
     * @param studentId id of student who will be added to members of ExaminationDate
     * @return ExaminationDate
     */
    ExaminationDate registerStudentOnTerm(Long termId, Long studentId);

    /**
     * Unregister student for ExaminationDate.
     *
     * @param termId  Id number of examination term
     * @param student Student who will be removed from participant list of ExaminationDate
     * @return updated ExaminationDate object
     */
    ExaminationDate unregisterStudentOnTerm(Long termId, Student student);
}
