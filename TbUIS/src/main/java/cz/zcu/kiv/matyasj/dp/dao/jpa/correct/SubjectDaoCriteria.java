package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Objects of this class represent DAOs for manipulation with Subjects in system database.
 * Objects are able to finding/saving/deleting Grades.
 * This DAO Objects uses JPA Criteria API.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Repository
public class SubjectDaoCriteria extends GenericDaoJpa<Subject, Long> implements SubjectDao {
    /** User DAO object for manipulation with user data*/
    @Autowired
    UserDao userDao;
    /** ExaminationDate DAO object for manipulation with student's exam dates */
    @Autowired
    ExaminationDateDao examinationDateDao;
    /** Grade DAO object for manipulation with grade data*/
    @Autowired
    GradeDao gradeDao;

    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * GradeDaoCriteria constructor
     *
     * @param em Entity Manager for communication with database
     */
    public SubjectDaoCriteria(EntityManager em) {
        super(em, Subject.class);
    }

    /**
     * Base GradeDaoCriteria constructor
     */
    public SubjectDaoCriteria() {
        super(Subject.class);
    }

    /**
     * This method finds and returns list of all subject without selected subjects.
     *
     * @param excludedSubjects list of excluded subject.
     * @return List of Subjects
     */
    @Override
    public List<Subject> getSubjectsExceptSelected(List<Subject> excludedSubjects) {
        List<Long> listOfExcludedIds;
        if (excludedSubjects != null && !excludedSubjects.isEmpty()) {
            listOfExcludedIds = excludedSubjects.stream().map(Subject::getId).collect(Collectors.toList());
        } else {
            listOfExcludedIds = new ArrayList<>();
        }

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Subject> query = cb.createQuery(Subject.class);
        Root<Subject> root = query.from(Subject.class);

        query.select(root);

        if (listOfExcludedIds != null && !listOfExcludedIds.isEmpty()) {
            Predicate byExcludedIds = cb.not(root.get("id").in(listOfExcludedIds));
            query.where(byExcludedIds);
        }
        // Return result
        List<Subject> subjectList = entityManager.createQuery(query).getResultList();
        entityManager.flush();
        log.info("Returning list of " + subjectList.size() + " subjects.");
        return subjectList;
    }

    /**
     * Complex method for deleting Subject object from database. Due to database integrity constraints it is not possible
     * to use default delete method in GenericDao. It is necessary to delete relations between objects (Students, Teachers,
     * ExaminationTerms and Grades).
     *
     * @param id database id of the Subject instance
     */
    @Override
    public void delete(Long id) {
        List<User> allUsers = userDao.findAllUsers();

        for (User u : allUsers) {
            if (u instanceof Student) {
                for (int i = 0; i < ((Student) u).getListOfAbsolvedSubjects().size(); i++) {
                    if (((Student) u).getListOfAbsolvedSubjects().get(i).getId().longValue() == id.longValue()) {
                        ((Student) u).getListOfAbsolvedSubjects().remove(i);
                        u = userDao.save(u);
                        break;
                    }
                }
                log.info("List of all absolved subjects for student with id " + id + " deleted.");

                for (int i = 0; i < ((Student) u).getListOfLearnedSubjects().size(); i++) {
                    if (((Student) u).getListOfLearnedSubjects().get(i).getId().longValue() == id.longValue()) {
                        ((Student) u).getListOfLearnedSubjects().remove(i);
                        u = userDao.save(u);
                        userDao.findOne(u.getId());
                        break;
                    }
                }
                log.info("List of all learned subjects for student with id " + id + " deleted.");

            } else if (u instanceof Teacher) {
                for (int i = 0; i < ((Teacher) u).getListOfTaughtSubjects().size(); i++) {
                    if (((Teacher) u).getListOfTaughtSubjects().get(i).getId().longValue() == id.longValue()) {
                        ((Teacher) u).getListOfTaughtSubjects().remove(i);
                        userDao.save(u);
                        break;
                    }
                }
                log.info("List of all taught subjects for teacher with id " + id + " deleted.");
            }
        }

        for (ExaminationDate examinationDate : examinationDateDao.findAll()) {
            if (examinationDate.getSubject() != null) {
                if (examinationDate.getSubject().getId().longValue() == id.longValue()) {
                    examinationDate.setSubject(null);
                    examinationDateDao.save(examinationDate);
                }
            }
        }
        log.info("List of all examination dates for user with id " + id + " deleted.");

        for (Grade grade : gradeDao.findAll()) {
            if (grade.getSubject() != null) {
                if (grade.getSubject().getId().longValue() == id.longValue()) {
                    grade.setSubject(null);
                    gradeDao.save(grade);
                }
            }
        }
        log.info("List of all grades for user with id " + id + " deleted.");

        entityManager.flush();

        super.delete(id);
        log.info("User with id " + id + " deleted.");
    }
}
