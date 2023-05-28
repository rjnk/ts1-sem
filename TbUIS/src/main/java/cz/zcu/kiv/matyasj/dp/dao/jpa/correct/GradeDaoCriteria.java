package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;

import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class represent DAOs for manipulation with Grades in system database.
 * Objects are able to finding/saving/deleting Grades.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Repository
public class GradeDaoCriteria extends GenericDaoJpa<Grade, Long> implements GradeDao {
    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * GradeDaoCriteria constructor
     *
     * @param em Entity Manager for communication with database
     */
    public GradeDaoCriteria(EntityManager em) {
        super(em, Grade.class);
    }

    /**
     * Base GradeDaoCriteria constructor
     */
    public GradeDaoCriteria() {
        super(Grade.class);
    }


    /**
     * Finds and returns list of grades created in one subject.
     *
     * @param subject For this subject will be grades found.
     * @return List of grades
     */
    @Override
    public List<Grade> findGradesBySubject(Subject subject) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Grade> query = cb.createQuery(Grade.class);

        Root<Grade> root = query.from(Grade.class);
        Predicate bySubjectPredicate = cb.equal(root.get("subject"), subject);

        query.where(bySubjectPredicate);
        TypedQuery<Grade> q = entityManager.createQuery(query);

        try {
            List<Grade> grades = q.getResultList();
            log.info("Returning list of " + grades.size() + " grades for subject with id " + subject.getId() + ".");
            return grades;
        } catch (NoResultException e) {
            log.error(" Grades for subject" + subject.getName() + " not found!");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Grade> findGradesByExaminationDate(ExaminationDate examinationDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Grade> query = cb.createQuery(Grade.class);

        Root<Grade> root = query.from(Grade.class);
        Predicate bySubjectPredicate = cb.equal(root.get("testWhereWasGradeGranted"), examinationDate);

        query.where(bySubjectPredicate);
        TypedQuery<Grade> q = entityManager.createQuery(query);

        try {
            List<Grade> grades = q.getResultList();
            log.info("Returning list of " + grades.size() + " grades for subject with id " + examinationDate.getId() + ".");
            return grades;
        } catch (NoResultException e) {
            log.error(" Grades for exam:" + examinationDate.getDateOfTest() + " " + examinationDate.getSubject().getName() + " not found!");
            return new ArrayList<>();
        }
    }

    /**
     * Finds and returns list of grades of one student.
     *
     * @param student For this Student will be grades found.
     * @return List of grades
     */
    @Override
    public List<Grade> findGradesByStudent(Student student) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Grade> query = cb.createQuery(Grade.class);

        Root<Grade> root = query.from(Grade.class);
        Predicate byStudentPredicate = cb.equal(root.get("owner"), student);

        query.where(byStudentPredicate);
        TypedQuery<Grade> q = entityManager.createQuery(query);

        try {
            List<Grade> grades = q.getResultList();
            log.info("Returning list of " + grades.size() + " for student with id " + student.getId());
            return grades;
        } catch (NoResultException e) {
            log.error("Grades for student " + student.getFirstName() + " " + student.getLastName() + " not found!", e);
            return null;
        }
    }


    /**
     * Finds and returns Grade of one student created in subject.
     *
     * @param student Student object for who is grade finding
     * @param subject Subject object
     * @param date    Date of exam date
     * @return Grade object
     */
    @Override
    public Grade findGradeByStudentAndSubjectAndDate(Student student, Subject subject, Date date) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Grade> query = cb.createQuery(Grade.class);

        Root<Grade> root = query.from(Grade.class);
        Predicate byStudentPredicate = cb.equal(root.get("owner"), student);
        Predicate byExaminationDate = cb.equal(root.get("subject"), subject);
        Predicate byDate = cb.equal(root.get("dayOfGrant"), date);

        query.where(cb.and(byStudentPredicate, byExaminationDate, byExaminationDate, byDate));
        TypedQuery<Grade> q = entityManager.createQuery(query);

        try {
            Grade grade = q.getSingleResult();
            log.info("Returning grade with id " + grade.getId() + " for subject with id " + subject.getId() + " and student with id " + student.getId());
            return grade;
        } catch (NoResultException e) {
            log.warn("Grade for student " + (student != null ? student.getFirstName() : null) + " " + (student != null ? student.getLastName() : null) + " and for subject " + (subject != null ? subject.getName() : null) + " and for date " + (date != null ? date.toString() : null) + " not found.");
            return null;
        }
    }
}
