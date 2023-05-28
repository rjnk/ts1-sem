package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;

import java.util.Date;
import java.util.List;

/**
 * This interface defines methods of DAOs for manipulation with Grade data in database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface GradeDao extends GenericDao<Grade, Long> {
    /**
     * Finds and returns list of grades created in one subject.
     *
     * @param subject For this subject will be grades found.
     * @return List of grades
     */
    List<Grade> findGradesBySubject(Subject subject);

    /**
     * Finds and returns list of grades created in one exam date.
     *
     * @param examinationDate For this examination date will be grades found.
     * @return List of grades
     */
    List<Grade> findGradesByExaminationDate(ExaminationDate examinationDate);

    /**
     * Finds and returns list of grades of one student.
     *
     * @param student For this Student will be grades found.
     * @return List of grades
     */
    List<Grade> findGradesByStudent(Student student);

    /**
     * Finds
     *
     * @param student   For this Student will be grades found.
     * @param subject   For this subject will be grades found.
     * @param date      For this Date will be grades found.
     * @return  List of grades
     */
    Grade findGradeByStudentAndSubjectAndDate(Student student, Subject subject, Date date);
}
