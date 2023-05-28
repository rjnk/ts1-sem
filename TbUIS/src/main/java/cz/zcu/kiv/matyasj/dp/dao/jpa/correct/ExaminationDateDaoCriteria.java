package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Objects of this class represent DAOs for manipulation with Examination Terms in system database.
 * Objects are able to finding/saving/deleting Examination Terms.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Repository
public class ExaminationDateDaoCriteria extends GenericDaoJpa<ExaminationDate, Long> implements ExaminationDateDao {
    /** Shared system logger */
    private final static Logger log = LogManager.getLogger();
    /** User DAO object for manipulation with user data */
    @Autowired
    private UserDao userDao;

    /**
     * ExaminationDateDaoCriteria constructor
     *
     * @param em Entity Manager for communication with database
     */
    public ExaminationDateDaoCriteria(EntityManager em) {
        super(em, ExaminationDate.class);
    }

    /**
     * Base ExaminationDateDaoCriteria constructor
     */
    public ExaminationDateDaoCriteria() {
        super(ExaminationDate.class);
    }

    /**
     * Return list of all examination terms of one student
     *
     * @param student for this student will be list of examination terms returned
     * @return List of Examination Terms
     */
    @Override
    public List<ExaminationDate> getExaminationDateOfStudent(Student student) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExaminationDate> query = cb.createQuery(ExaminationDate.class);
        Root<ExaminationDate> root = query.from(ExaminationDate.class);

        query.select(root);

        Predicate byStudent = cb.isMember(student, root.get("participants"));

        query.where(byStudent);

        List<ExaminationDate> examinationDates = entityManager.createQuery(query).getResultList();
        log.info("Returning list of " + examinationDates.size() + " examination dates for student with id " + student.getId() + ".");
        return examinationDates;
    }

    /**
     * Returns list of examination terms for one student from database. This list will be included
     * only examination dates in the future.
     *
     * @param student for this student will be list of examination terms returned
     * @return List of Examination Dates in the future
     */
    @Override
    public List<ExaminationDate> getExaminationTermOfStudentInFuture(Student student) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExaminationDate> query = cb.createQuery(ExaminationDate.class);
        Root<ExaminationDate> root = query.from(ExaminationDate.class);

        query.select(root);

        Predicate byStudent = cb.isMember(student, root.get("participants"));
        Predicate inFuture = cb.greaterThan(root.get("dateOfTest"), new Date());

        query.where(cb.and(byStudent, inFuture));

        List<ExaminationDate> examinationDates = entityManager.createQuery(query).getResultList();
        log.info("Returning list of " + examinationDates.size() + " examination dates in future for student with id " + student.getId() + ".");
        return examinationDates;
    }

    /**
     * Returns list of examination terms for one teacher who creates this exam date.
     *
     * @param teacher list of examination terms of this teacher will be returned
     * @return List of Examination Terms
     */
    @Override
    public List<ExaminationDate> getExaminationTermOfTeacher(Teacher teacher) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExaminationDate> query = cb.createQuery(ExaminationDate.class);
        Root<ExaminationDate> root = query.from(ExaminationDate.class);

        query.select(root);

        Predicate byTeacher = cb.equal(root.get("teacher"), teacher);

        query.where(byTeacher);

        List<ExaminationDate> examinationDates = entityManager.createQuery(query).getResultList();
        log.info("Returning list of " + examinationDates.size() + " examination dates for teacher with id " + teacher.getId() + ".");
        return examinationDates;
    }

    /**
     * Returns list of examination terms for one teacher who creates this exam date.
     *
     * @param subject list of examination terms of this subject will be returned
     * @return List of Examination Terms
     */
    @Override
    public List<ExaminationDate> getExaminationTermOfSubject(Subject subject) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExaminationDate> query = cb.createQuery(ExaminationDate.class);
        Root<ExaminationDate> root = query.from(ExaminationDate.class);

        query.select(root);

        Predicate bySubject = cb.equal(root.get("subject"), subject);

        query.where(bySubject);

        List<ExaminationDate> examinationDates = entityManager.createQuery(query).getResultList();
        log.info("Returning list of " + examinationDates.size() + " examination dates for subject with id " + subject.getId() + ".");
        return examinationDates;
    }


    /**
     * Get all possible exam dates for students who can sign up for or are already registered.
     *
     * @param student list of examination terms of this student will be returned
     * @return List of Examination Terms
     */
    @Override
    public List<ExaminationDate> getAllExaminationDatesOfStudent(Student student) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<ExaminationDate> query = cb.createQuery(ExaminationDate.class);
        Root<ExaminationDate> root = query.from(ExaminationDate.class);

        Join<ExaminationDate, Subject> subjectJoin = root.join("subject", JoinType.LEFT);

        query.select(root);

        Predicate byStudentParticipant = cb.isMember(student, root.get("participants"));
        Predicate byStudentsSubject = cb.isMember(student, subjectJoin.get("listOfStudents"));
        Predicate inFuture = cb.greaterThan(root.get("dateOfTest"), new Date());

        query.where(cb.and(cb.or(byStudentParticipant, byStudentsSubject), inFuture));

        List<ExaminationDate> examinationDates = entityManager.createQuery(query).getResultList();
        log.info("Returning list of all " + examinationDates.size() + " examination dates for student with id " + student.getId() + ".");
        return examinationDates;
    }

    /**
     * Register student for Examination Date. It means adding student object into list of participants
     * in Examination Term object and persist changes into the database.
     *
     * @param termId    Id number of examination term
     * @param studentId id of student who will be added to members of ExaminationDate
     * @return ExaminationDate
     */
    @Override
    public ExaminationDate registerStudentOnTerm(Long termId, Long studentId) {
        ExaminationDate term = this.findOne(termId);
        Student foundStudent = (Student) userDao.findOne(studentId);
        if (foundStudent == null) {
            log.error("Student with id " + studentId + " not found!");
            return null;
        }
        for (Student s : term.getParticipants()) {
            if (s.getId().longValue() == foundStudent.getId().longValue()) {
                log.warn("Try to register student on term which is already registered!");
                return null;
            }
        }
        term.getParticipants().add(foundStudent);

        log.info("Student with id " + studentId + " registered on term with id " + termId + ".");
        return this.save(term);
    }

    /**
     * Unregister student for ExaminationDate. It means removing student from list of participants
     * in Examination Term object and persist changes into the database.
     *
     * @param termId  Id number of examination term
     * @param student Student who will be removed from participant list of ExaminationDate
     * @return updated ExaminationDate object
     */
    @Override
    public ExaminationDate unregisterStudentOnTerm(Long termId, Student student) {
        ExaminationDate term = findOne(termId);
        boolean removed = false;

        if (term == null)
            return null;

        Iterator<Student> participantsIterator = term.getParticipants().iterator();
        while (participantsIterator.hasNext()) {
            Student participant = participantsIterator.next();
            if (student.getId().longValue() == participant.getId().longValue()) {
                participantsIterator.remove();
                removed = true;
            }
        }

        if (removed) {
            log.info("Student with id " + student.getId() + " unregistered from term with id " + termId + ".");
            return this.save(term);
        }

        return null;
    }
}
