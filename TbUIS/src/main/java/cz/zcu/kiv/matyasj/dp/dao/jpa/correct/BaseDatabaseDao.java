package cz.zcu.kiv.matyasj.dp.dao.jpa.correct;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objects of this class represent DAOs for manipulation with all data in system database.
 * Objects are able to get and insert all data from/to database and delete all data from database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Repository
public class BaseDatabaseDao extends GenericDaoJpa<BaseEntity, Long> implements DatabaseDao {
    /** List of all DAO objects for each domain class */
    @Autowired
    private List<? extends GenericDao> daoList;
    /** User DAO object for manipulation with user data */
    @Autowired
    private UserDao userDao;
    /** Subject DAO object for manipulation with subject data */
    @Autowired
    private SubjectDao subjectDao;
    /** GradeType DAO object for manipulation with gradeType data */
    @Autowired
    private GradeTypeDao gradeTypeDao;
    /** ExaminationDate DAO object for manipulation with Exam term data */
    @Autowired
    private ExaminationDateDao examinationDateDao;
    /** Shared system logger */
    private final static Logger log = LogManager.getLogger();

    /**
     * BaseDatabaseDao constructor
     *
     * @param em Entity Manager for getting metaModel which is used for getting all domain classes in system.
     */
    public BaseDatabaseDao(EntityManager em) {
        super(em, BaseEntity.class);
    }

    /**
     * BaseDatabaseDao constructor
     */
    public BaseDatabaseDao() {
        super(BaseEntity.class);
    }

    /**
     * This method erase all data from system database without backup.
     *
     * @return true if operation has been successfully completed, false otherwise
     */
    @Override
    public boolean eraseDatabase() {
        log.warn("Removing all data from system database ...");

        try {
            // Get all data entities from db by dao objects
            for (GenericDao dao : daoList) {
                // Get all data entities from one dao object
                List list = dao.findAll();
                if (list != null && !list.isEmpty()) {
                    for (Object element : list) {
                        if (element instanceof BaseEntity) {
                            dao.delete(((BaseEntity) element).getId());
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Removing all data from system database has NOT been successfully completed:" + e.getMessage());
            return false;
        }
        log.info("Removing all data from the system database has been successfully completed.");
        return true;
    }

    /**
     * This method create dump of all data in system database and return them in list of entities objects.
     *
     * @return List of all data in system database.
     */
    @Override
    public List<BaseEntity> databaseDump() {
        List<BaseEntity> entities = new ArrayList<>();

        // Get all data entities from db by dao objects
        for (GenericDao dao : daoList) {
            // Get all data entities from one dao object
            List list = dao.findAll();

            if (list != null && !list.isEmpty()) {
                for (Object element : list) {
                    entities.add((BaseEntity) element);
                }
            }
        }

        return entities;
    }

    /**
     * This method fills a database of data created as a backup. This data are
     * given in parameter as list of domain objects.
     *
     * @param entities list of instances of db entities
     * @return true if operation has been successfully completed, false otherwise
     */
    @Override
    public boolean fillDatabase(List<BaseEntity> entities) {
        if (entities == null) return false;

        eraseDatabase();

        Long beforeIndex, afterIndex;
        /* Subject before save -> after save index map */
        Map<Long, Long> subjectsIndexMap = new HashMap<>();
        Map<String, Long> usersIndexMap = new HashMap<>();
        Map<Long, Long> gradeTypeIndexMap = new HashMap<>();
        Map<Long, Long> examTermsIndexMap = new HashMap<>();

        /* Saving subjects */
        for (BaseEntity entity : entities) {
            if (entity instanceof Subject) {
                Subject s = new Subject(((Subject) entity).getName(), ((Subject) entity).getCreditRating());
                beforeIndex = entity.getId();
                entity = this.save(s);
                afterIndex = entity.getId();
                subjectsIndexMap.put(beforeIndex, afterIndex);
            }
        }

        /* Saving users */
        for (BaseEntity entity : entities) {
            if (entity instanceof Student) {
                String firstName = ((Student) entity).getFirstName();
                String lastName = ((Student) entity).getLastName();
                String username = ((Student) entity).getUsername();
                String password = ((Student) entity).getPassword();
                String email = ((Student) entity).getEmail();
                Student newStudent = new Student(firstName, lastName, username, password, email);
                newStudent = (Student) userDao.save(newStudent);
                for (Subject s : ((Student) entity).getListOfLearnedSubjects()) {
                    newStudent.getListOfLearnedSubjects().add(subjectDao.findOne(subjectsIndexMap.get(s.getId())));
                }
                for (Subject s : ((Student) entity).getListOfAbsolvedSubjects()) {
                    newStudent.getListOfLearnedSubjects().add(subjectDao.findOne(subjectsIndexMap.get(s.getId())));
                }
                newStudent = (Student) userDao.save(newStudent);
                usersIndexMap.put(newStudent.getUsername(), newStudent.getId());
            } else if (entity instanceof Teacher) {
                String firstname = ((Teacher) entity).getFirstName();
                String lastName = ((Teacher) entity).getLastName();
                String username = ((Teacher) entity).getUsername();
                String password = ((Teacher) entity).getPassword();
                String email = ((Teacher) entity).getEmail();
                Teacher newTeacher = new Teacher(firstname, lastName, username, password, email);
                newTeacher = (Teacher) userDao.save(newTeacher);
                for (Subject s : ((Teacher) entity).getListOfTaughtSubjects()) {
                    newTeacher.getListOfTaughtSubjects().add(subjectDao.findOne(subjectsIndexMap.get(s.getId())));
                }
                newTeacher = (Teacher) userDao.save(newTeacher);
                usersIndexMap.put(newTeacher.getUsername(), newTeacher.getId());
            }
        }

        /* Saving grade types */
        for (BaseEntity entity : entities) {
            if (entity instanceof GradeType) {
                beforeIndex = entity.getId();
                entity = this.save(entity);
                gradeTypeIndexMap.put(beforeIndex, entity.getId());
            }
        }



        /* Saving Exam terms */
        for (BaseEntity entity : entities) {
            if (entity instanceof ExaminationDate) {
                List<Student> participants = ((ExaminationDate) entity).getParticipants();
                Subject subject = ((ExaminationDate) entity).getSubject();
                Teacher teacher = ((ExaminationDate) entity).getTeacher();

                ExaminationDate e = new ExaminationDate(((ExaminationDate) entity).getDateOfTest(), ((ExaminationDate) entity).getMaxParticipants());
                e = (ExaminationDate) this.save(e);

                e.setTeacher((Teacher) userDao.findOne(usersIndexMap.get(teacher.getUsername())));

                if (subject != null && subject.getId() != null) {
                    e.setSubject(subjectDao.findOne(subjectsIndexMap.get(subject.getId())));
                }

                teacher.setId(usersIndexMap.get(teacher.getUsername()));

                for (Student student : participants) {
                    e.getParticipants().add((Student) userDao.findOne(usersIndexMap.get(student.getUsername())));
                }

                this.save(e);
                examTermsIndexMap.put(entity.getId(), e.getId());
            }
        }

        /* Try to save unknown entity object */
        for (BaseEntity entity : entities) {
            if (!(entity instanceof ExaminationDate || entity instanceof User || entity instanceof GradeType || entity instanceof Grade || entity instanceof Subject)) {
                this.save(entity);
            }
        }

        /* Saving Grades */
        for (BaseEntity entity : entities) {
            if (entity instanceof Grade) {
                Grade newGrade = new Grade();
                newGrade.setDayOfGrant(((Grade) entity).getDayOfGrant());
                newGrade = (Grade) save(newGrade);

                Subject gradeSubject = ((Grade) entity).getSubject();
                Teacher gradeTeacher = ((Grade) entity).getWhoGradeGranted();
                Student gradeOwner = ((Grade) entity).getOwner();
                GradeType gradeType = ((Grade) entity).getTypeOfGrade();
                ExaminationDate gradeExamTerm = ((Grade) entity).getTestWhereWasGradeGranted();

                if (gradeSubject != null) {
                    newGrade.setSubject(subjectDao.findOne(subjectsIndexMap.get(gradeSubject.getId())));
                }

                if (gradeOwner != null) {
                    newGrade.setOwner((Student) userDao.findByUsername(gradeOwner.getUsername()));
                }

                if (gradeTeacher != null) {
                    newGrade.setWhoGradeGranted((Teacher) userDao.findByUsername(gradeTeacher.getUsername()));
                }

                if (gradeType != null) {
                    newGrade.setTypeOfGrade(gradeTypeDao.findOne(gradeTypeIndexMap.get(gradeType.getId())));
                }

                if (gradeExamTerm != null) {
                    newGrade.setTestWhereWasGradeGranted(examinationDateDao.findOne(examTermsIndexMap.get(gradeExamTerm.getId())));
                }

                save(newGrade);
            }
        }

        log.info("Database filled with data created as a backup.");
        return true;
    }

    /**
     * This method simply returns array of all domain classes used in system.
     *
     * @return array of all domain classes.
     */
    @Override
    public Class[] getDomainClasses() {
        Class[] domainClasses = new Class[entityManager.getMetamodel().getEntities().size()];

        int i = 0;
        for (EntityType e : entityManager.getMetamodel().getEntities()) {
            domainClasses[i] = e.getBindableJavaType();
            i++;
        }
        return domainClasses;
    }
}