package cz.zcu.kiv.matyasj.dp.service;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;

import java.util.Date;
import java.util.List;

/**
 * This interface defines methods of service objects providing functions for manipulation with data
 * related to teachers.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface TeacherService extends UserService {
    /**
     * This method returns list of all Subject of specific teacher.
     * This teacher teaches subjects in list.
     *
     * @param teacher Teacher domain object.
     * @return List of subjects
     */
    List<Subject> getTaughtSubjectsList(Teacher teacher);

    /**
     * This method returns list of all Subjects of specific teacher, which have not reach maximu examination dates count
     *
     * @param teacher Teacher domain object.
     * @return list of subjects to create examination
     */
    List<Subject> getSubjectsForCreateExaminations(Teacher teacher);

    /**
     * This method returns list of all Subjects which particular teacher does not taught.
     *
     * @param teacher Teacher domain object.
     * @return List of subjects which teacher does not teaches.
     */
    List<Subject> getNonTaughtSubjectsList(Teacher teacher);


    /**
     * Set the subject to a teacher.
     *
     * @param teacher   Subject will be set for this teacher object.
     * @param subjectId The subject with this database id will be set for this teacher.
     * @return true if operation will be successfully completed, false otherwise
     */
    boolean setMySubject(Teacher teacher, Long subjectId);

    /**
     * Unset the subject of this teacher.
     *
     * @param teacher   This subject will be unset for this teacher object.
     * @param subjectId The subject with this database id will be unset for this teacher.
     * @return true if operation will be successfully completed, false otherwise
     */
    boolean unsetMySubject(Teacher teacher, Long subjectId);


    /**
     * This method returns list of all Examination terms created by specific teacher.
     *
     * @param teacher For this teacher will be list of exam terms returned.
     * @return List of Examination terms which are created by specific teacher
     */
    List<ExaminationDate> getExaminationTermsByTeacher(Teacher teacher);

    List<ExaminationDate> getExaminationTermsBySubject(Subject subject);

    /**
     * This method returns list of all Examination terms created by specific teacher.
     * This method also removes participants which are already evaluated from each examination
     * terms participants lists.
     *
     * @param teacher For this teacher will be list of exam terms returned.
     * @return List of Examination terms which are created by specific teacher
     */
    List<ExaminationDate> getMyExaminationDatesWithoutGraduateParticipants(Teacher teacher);

    /**
     * This method returns list of all Examination terms created by specific teacher and for
     * specific subject.
     *
     * @param teacher   For this teacher will be list of exam terms returned.
     * @param subjectId Database id of subject
     * @return List of Examination terms which are created by particular teacher
     */
    List<ExaminationDate> getAllExaminationTermsByTeacherAndSubject(Teacher teacher, Long subjectId);

    List<ExaminationDate> getAllExaminationTermsBySubject(Subject subject);

    /**
     * This method returns list of all Examination terms created by specific teacher and for
     * specific subject. This method also removes participants which are already evaluated
     * from each examination terms participants lists.
     *
     * @param teacher   For this teacher will be list of exam terms returned.
     * @param subjectId Database id of subject
     * @return List of Examination terms which are created by specific teacher
     */
    List<ExaminationDate> getMyExaminationTermsWithoutGradedParticipantsBySubject(Teacher teacher, Long subjectId);

    /**
     * This method creates new exam date for particular teacher and for subject.
     *
     * @param teacher           Teacher who wants to create a new exam
     * @param subjectId         New exam date will be created for subject with this database id
     * @param dateOfTerm        Date of new examination
     * @param maxParticipants   Maximal number of participants
     * @return true if operation will be successfully completed, false otherwise
     */
    boolean createNewExaminationTerm(Teacher teacher, Long subjectId, String dateOfTerm, String maxParticipants);

    /**
     * This method removes exam term of specific teacher.
     *
     * @param teacher    For this teacher object will be exam date removed
     * @param examTermId Database id of exam term which teacher wants to remove
     * @return true if operation will be successfully completed, false otherwise
     */
    boolean removeExaminationTerm(Teacher teacher, Long examTermId);

    /**
     * This method creates new grade/evaluation for student by particular teacher.
     *
     * @param teacher           Object of teacher who wants te create grade
     * @param studentId         Database id of student who gets the grade
     * @param gradeTypeId       Database id of Grade type
     * @param subjectId         Database id of subject
     * @param examinationDateId Database id of examination term
     * @return true if operation will be successfully completed, false otherwise
     */
    boolean createNewGrade(Teacher teacher, Long studentId, Long gradeTypeId, Long subjectId, Long examinationDateId);

    /**
     * This method returns single examination term by its id.
     *
     * @param examTermId Database id of examination term which will be returned
     * @return ExaminationDate object
     */
    ExaminationDate getExaminationTerm(Long examTermId);

    /**
     * This method returns Student object from database for specific examination
     * date. In case student is not participant of examination date this method
     * returns null;
     *
     * @param term      Examination term object
     * @param studentId Database id of student
     * @return Student object or null.
     */
    Student getStudentFromExaminationTerm(ExaminationDate term, long studentId);

    /**
     * This method returns all grade types in university information system database.
     *
     * @return List of all grade types
     */
    List<GradeType> getAllGradeTypes();

    /**
     * This method return all teacher objects in university information system database.
     *
     * @return List of all teachers in system
     */
    List<Teacher> getAllTeachers();

    /**
     * This method return all grades created for one subject (for all examination terms).
     *
     * @param subjectId Database id of subject
     * @return List of grades for one subject
     */
    List<Grade> getGradesForSubject(Long subjectId);

    /**
     * This method update persisted grade object.
     *
     * @param teacherId      Database id of teacher
     * @param gradeId        Database id of updated grade
     * @param newGradeTypeId Database id of grade type
     * @return true if success
     */
    boolean updateGrade(Long teacherId, Long gradeId, Long newGradeTypeId);

    /**
     * This method indicates whether to swap columns
     *
     * @return Indication for swap of columns
     */
    boolean swapNameAndTeacher();

    /**
     * This method returns count actually teached subjects
     *
     * @return Number of subjects where teacher participates
     */
    int getNumberOfTaughtSubjects();
}