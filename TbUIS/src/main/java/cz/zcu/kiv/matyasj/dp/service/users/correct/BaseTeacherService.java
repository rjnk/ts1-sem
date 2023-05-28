package cz.zcu.kiv.matyasj.dp.service.users.correct;

import cz.zcu.kiv.matyasj.dp.dao.ExaminationDateDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeDao;
import cz.zcu.kiv.matyasj.dp.dao.GradeTypeDao;
import cz.zcu.kiv.matyasj.dp.dao.SubjectDao;
import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.TeacherService;
import cz.zcu.kiv.matyasj.dp.service.users.BaseUserService;
import cz.zcu.kiv.matyasj.dp.utils.comparators.ExaminationsComparator;
import cz.zcu.kiv.matyasj.dp.utils.comparators.StudentsComparator;
import cz.zcu.kiv.matyasj.dp.utils.dates.DateUtility;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;

import java.util.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Objects of this class represent services providing functions for manipulation with data
 * related to teachers. BaseTeacherService provide method for register/unregister subjects
 * to teacher, creation/removing examination terms, creating/updating evaluation, etc.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Service
public class BaseTeacherService extends BaseUserService implements TeacherService {
    /** DAO object for manipulation with subject data in database */
    protected final SubjectDao subjectDao;
    /** DAO object for manipulation with user data in database */
    protected final UserDao userDao;
    /** DAO object for manipulation with examination terms data in database */
    protected final ExaminationDateDao examinationDateDao;
    /** DAO object for manipulation with grade data in database */
    protected final GradeDao gradeDao;
    /** DAO object for manipulation with grade types data in database */
    protected final GradeTypeDao gradeTypeDao;
    /** Date utility used for dealing with date/time structures */
    protected final DateUtility dateUtility;
    /** Application property loader */
    protected final PropertyLoader propertyLoader;
    /** Shared system logger */
    protected final Logger log = LogManager.getLogger();

    /**
     * BaseTeacherService constructor for initialization objects used by spring.
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
    public BaseTeacherService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, DateUtility dateUtility, PropertyLoader propertyLoader) {
        this.subjectDao = subjectDao;
        this.userDao = userDao;
        this.examinationDateDao = examinationDateDao;
        this.gradeDao = gradeDao;
        this.gradeTypeDao = gradeTypeDao;
        this.dateUtility = dateUtility;
        this.propertyLoader = propertyLoader;
    }

    /**
     * This method returns list of all Subject of specific teacher.
     * This teacher teaches subjects in list.
     *
     * @param teacher Teacher domain object.
     * @return List of subjects
     */
    @Override
    public List<Subject> getTaughtSubjectsList(Teacher teacher) {
        if (teacher == null) {
            log.error("Getting list of taught subjects failed.");
            return null;
        }
        log.info("Getting list of taught subjects for teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());

        return sortListOfSubjects(tmpTeacher.getListOfTaughtSubjects());
    }

    @Override
    public List<Subject> getSubjectsForCreateExaminations(Teacher teacher) {
        List<Subject> taughtSubjectList = getTaughtSubjectsList(teacher);

        List<Subject> toRemove = new LinkedList<>();
        for (Subject s : taughtSubjectList) {
            List<ExaminationDate> examinations = getAllExaminationTermsByTeacherAndSubject(teacher, s.getId());
            if (examinations.size() >= Integer.parseInt(propertyLoader.getProperty("subjectMaxExamDate"))) {
                toRemove.add(s);
            }
        }
        taughtSubjectList.removeAll(toRemove);
        return sortListOfSubjects(taughtSubjectList);
    }

    /**
     * This method returns list of all Subjects which particular teacher does not taught.
     *
     * @param teacher Teacher domain object.
     * @return List of subjects which teacher does not teaches.
     */
    @Override
    public List<Subject> getNonTaughtSubjectsList(Teacher teacher) {
        if (teacher == null) {
            log.error("Getting list of not taught subjects failed.");
            return null;
        }
        log.info("Getting list of not taught subjects for teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());


        return sortListOfSubjects(subjectDao.getSubjectsExceptSelected(tmpTeacher.getListOfTaughtSubjects()));
    }

    /**
     * Set the subject to a teacher.
     *
     * @param teacher   Subject will be set for this teacher object.
     * @param subjectId The subject with this database id will be set for this teacher.
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    public boolean setMySubject(Teacher teacher, Long subjectId) {

        if (teacher == null) {
            log.error("Setting of subject failed.");
            return false;
        }
        log.info("Setting subject with id " + subjectId + " to teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        Subject tmpSubject = subjectDao.findOne(subjectId);

        if (tmpTeacher == null) {
            log.error("Setting of subject failed.");
            return false;
        }

        if (tmpSubject == null) {
            log.warn("Setting of subject failed");
            return false;
        }

        // Check max subject count for teacher
        int maxSubjectsNumber = Integer.parseInt(propertyLoader.getProperty("teacherMaxSubjects"));
        if (tmpTeacher.getListOfTaughtSubjects().size() >= maxSubjectsNumber) {
            log.warn("Teacher " + tmpTeacher.getFirstName() + " " + tmpTeacher.getLastName() + " is trying to register more than max subject count(" + maxSubjectsNumber + ")!");
            return false;
        }

        // Check max teacher count for subject
        int maxTeachersNumber = Integer.parseInt(propertyLoader.getProperty("subjectMaxTeachers"));
        if (tmpSubject.getTeachers().size() >= maxTeachersNumber) {
            log.warn("Subject " + tmpSubject.getName() + " is trying to register more than max teachers count(" + maxTeachersNumber + ")!");
            return false;
        }

        // Test if teacher already teaches this subject.
        for (Subject subject : tmpTeacher.getListOfTaughtSubjects()) {
            if (subject.getId().longValue() == subjectId.longValue()) {
                // Teacher already teaches this subject.
                return false;
            }
        }

        if (tmpSubject != null) {
            if (!tmpTeacher.getListOfTaughtSubjects().contains(tmpSubject)) {
                tmpTeacher.getListOfTaughtSubjects().add(tmpSubject);
                tmpTeacher = (Teacher) userDao.save(tmpTeacher);
                if (tmpTeacher != null) {
                    return true;
                }
            }
        }
        log.error("Setting of subject failed.");
        return false;
    }

    /**
     * Unset the subject of this teacher.
     *
     * @param teacher   This subject will be unset for this teacher object.
     * @param subjectId The subject with this database id will be unset for this teacher.
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    public boolean unsetMySubject(Teacher teacher, Long subjectId) {

        if (teacher == null) {
            log.error("Unsetting of subject failed.");
            return false;
        }
        log.info("Unsetting subject with id " + subjectId + " from teacher with id " + teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        Subject tmpSubject = subjectDao.findOne(subjectId);

        if (tmpSubject == null) {
            log.warn("It is not possible to remove null subject!");
            return false;
        }

        if (!tmpSubject.getListOfStudents().isEmpty()) {
            log.warn("It is not possible to remove subject with registered students!");
            return false;
        }

        List<ExaminationDate> examinationDateListTmp = examinationDateDao.getExaminationTermOfTeacher(tmpTeacher);
        List<Long> examinationTermListForDeleting = new ArrayList<>();

        if (tmpTeacher != null) {
            for (Subject s : tmpTeacher.getListOfTaughtSubjects()) {
                if (s.getId().longValue() == tmpSubject.getId().longValue()) {
                    // Delete all exam term of this Teacher and Subject
                    for (ExaminationDate e : examinationDateListTmp) {
                        if (e.getSubject().getId().longValue() == s.getId().longValue()) {
                            examinationTermListForDeleting.add(e.getId());
                        }
                    }

                    tmpTeacher.getListOfTaughtSubjects().remove(s);

                    tmpTeacher = (Teacher) userDao.save(tmpTeacher);

                    if (tmpTeacher != null) {
                        for (Long examTermId : examinationTermListForDeleting) {
                            examinationDateDao.delete(examTermId);
                        }
                        return true;
                    }
                }
            }
        }

        log.error("Unsetting of subject failed.");
        return false;
    }

    /**
     * This method returns list of all Examination terms created by specific teacher.
     *
     * @param teacher For this teacher will be list of exam terms returned.
     * @return List of Examination terms which are created by specific teacher
     */
    @Override
    public List<ExaminationDate> getExaminationTermsByTeacher(Teacher teacher) {

        if (teacher == null) {
            log.error("Getting all examination terms by teacher failed.");
            return null;
        }
        log.info("Getting all examination terms by teacher for teacher with id " +  teacher.getId() + ".");

        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationTermOfTeacher(tmpTeacher);

        return sortListOfExamDates(examinationDateList);
    }

    /**
     * This method returns list of all Examination terms created by specific teacher.
     *
     * @param subject For this subject will be list of exam terms returned.
     * @return List of Examination terms which are created by specific teacher
     */
    @Override
    public List<ExaminationDate> getExaminationTermsBySubject(Subject subject) {
        if (subject == null) {
            log.error("Getting all examination terms by subject failed.");
            return null;
        }
        log.info("Getting all examination terms by subject with id " + subject.getId() + ".");

        Subject tmpSubject = subjectDao.findOne(subject.getId());
        List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationTermOfSubject(tmpSubject);

        return sortListOfExamDates(examinationDateList);
    }

    /**
     * This method returns list of all Examination terms created by specific teacher.
     * This method also removes participants which are already evaluated from each examination
     * terms participants lists.
     *
     * @param teacher For this teacher will be list of exam terms returned.
     * @return ordered List of Examination terms which are created by specific teacher
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

        for (ExaminationDate e : examinationDateList) {
            e.getParticipants().removeIf(student -> gradeDao.findGradeByStudentAndSubjectAndDate(student, e.getSubject(), e.getDateOfTest()) != null);
            e.getParticipants().sort(StudentsComparator::lastNameAsc);
        }

        return sortListOfExamDates(examinationDateList);
    }

    /**
     * This method returns list of all Examination terms created by specific teacher and for
     * specific subject.
     *
     * @param teacher   For this teacher will be list of exam terms returned.
     * @param subjectId Database id of subject
     * @return List of Examination terms which are created by particular teacher
     */
    @Override
    public List<ExaminationDate> getAllExaminationTermsByTeacherAndSubject(Teacher teacher, Long subjectId) {

        List<ExaminationDate> examinationDates = getExaminationTermsByTeacher(teacher);

        if (examinationDates == null) {
            log.error("Getting all examination terms by teacher and subject failed.");
            return null;
        }
        log.info("Getting all examination terms by teacher and subject for teacher with id " + teacher.getId() + " and subject with id " + subjectId + ".");

        examinationDates.removeIf(e -> e.getSubject().getId().longValue() != subjectId.longValue());

        return sortListOfExamDates(examinationDates);
    }

    /**
     * This method returns list of all Examination terms created for specific subject.
     *
     * @param subject subject
     * @return List of Examination terms which are created by particular teacher
     */
    @Override
    public List<ExaminationDate> getAllExaminationTermsBySubject(Subject subject) {
        List<ExaminationDate> examinationDates = getExaminationTermsBySubject(subject);

        if (examinationDates == null) {
            log.error("Getting all examination terms by subject failed.");
            return null;
        }
        log.info("Getting all examination terms by subject for subject with id " + subject.getId() + ".");

        examinationDates.removeIf(e -> e.getSubject().getId().longValue() != subject.getId().longValue());

        return sortListOfExamDates(examinationDates);
    }

    /**
     * This method returns list of all Examination terms created by specific teacher and for
     * specific subject. This method also removes participants which are already evaluated
     * from each examination terms participants lists.
     *
     * @param teacher   For this teacher will be list of exam terms returned.
     * @param subjectId Database id of subject
     * @return List of Examination terms which are created by specific teacher
     */
    @Override
    public List<ExaminationDate> getMyExaminationTermsWithoutGradedParticipantsBySubject(Teacher teacher, Long subjectId) {
        List<ExaminationDate> examinationDates = getMyExaminationDatesWithoutGraduateParticipants(teacher);
        if (examinationDates == null) {
            log.error("Getting all examination terms without graded participants failed.");
            return null;
        }
        log.info("Getting all examination terms without graded participants for subject with id " + subjectId + " and teacher with id " + teacher.getId() + ".");

        examinationDates.removeIf(e -> e.getSubject().getId().longValue() != subjectId.longValue());

        return sortListOfExamDates(examinationDates);
    }

    /**
     * This method creates new exam date for particular teacher and for subject.
     *
     * @param teacher         Teacher who wants to create a new exam
     * @param subjectId       New exam date will be created for subject with this database id
     * @param dateOfTerm      Date of new examination
     * @param maxParticipants Maximal number of participants
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    public boolean createNewExaminationTerm(Teacher teacher, Long subjectId, String dateOfTerm, String maxParticipants) {
        if (teacher == null) {
            log.error("Creating new examination term failed.");
            return false;
        }
        log.info("Creating new examination term for subject with id " + subjectId + ", teacher with id " + teacher.getId()  + ", date " + dateOfTerm + " and maximal number of participants " + maxParticipants + ".");

        Date tmpDateOfTerm = dateUtility.stringToDate(dateOfTerm);

        // Test if date is null
        if (tmpDateOfTerm == null) {
            log.warn("Date of new examination term is null! Creation of new examination term is being canceled.");
            return false;
        }

        // Test if date is in future
        if (!tmpDateOfTerm.after(new Date())) {
            log.warn("Date of new examination term is not in future! Creation of new examination term is being canceled.");
            return false;
        }

        int maxParticipantsInt = 0;
        try {
            maxParticipantsInt = Integer.parseInt(maxParticipants);
        } catch (Exception e) {
            log.warn("Maximal number of exam date participants exceeded! Creation of new examination term is being canceled.");
        }

        // Test if maximal number of participants is too low
        if (maxParticipantsInt < Integer.parseInt(propertyLoader.getProperty("examTermMinParticipants"))) {
            log.warn("Maximal number of exam date participants too low! Creation of new examination term is being canceled.");
            return false;
        }

        // Test if maximal number of participants is not exceeded
        if (maxParticipantsInt > Integer.parseInt(propertyLoader.getProperty("examTermMaxParticipants"))) {
            log.warn("Maximal number of exam date participants exceeded! Creation of new examination term is being canceled.");
            return false;
        }

        Subject subject = subjectDao.findOne(subjectId);

        if (subject == null) {
            log.error("Creating new examination term failed.");
            return false;
        }

        List<ExaminationDate> examinationDates = getAllExaminationTermsBySubject(subject);
        final int millisecondsInMinute = 60000;
        final int minutesInDay = 24 * 60;

        if (examinationDates.size() != 0) {
            ExaminationDate lastExam = null;
            int registeredExamsCount = 0;

            for (ExaminationDate exam : examinationDates) {
                if (exam.getTeacher().getUsername().equals(teacher.getUsername())) {
                    if (lastExam == null || lastExam.getDateOfTest().before(exam.getDateOfTest())) {
                        lastExam = exam;
                        registeredExamsCount++;
                    }
                }
            }

            if (lastExam != null) { // Teacher has already registered exam

                // Calculated time for exam dates comparing
                long latestExamTime = lastExam.getDateOfTest().getTime() / millisecondsInMinute;
                long newExamTime = tmpDateOfTerm.getTime() / millisecondsInMinute;

                // Test if date is same as previous examination date
                if (latestExamTime == newExamTime) {
                    log.warn("Date of new examination term is same as the date of previous term! Creation of new examination term is being canceled.");
                    return false;
                }

                // Test if date is at least 24 hours after previous examination date
                if ((newExamTime - latestExamTime) < minutesInDay) {
                    log.warn("Date of new examination term is set sooner than 24 hours after previous exam date! Creation of new examination term is being canceled.");
                    return false;
                }

                // Test max exam dates for this subject and teacher
                int maxExamTermNumber = Integer.parseInt(propertyLoader.getProperty("subjectMaxExamDate"));
                if (registeredExamsCount >= maxExamTermNumber) {
                    log.warn("Teacher " + teacher.getFirstName() + " " + teacher.getLastName() + " is trying to create more than " +
                            "max count of exam dates for this subject(" + maxExamTermNumber + ")!");
                    return false;
                }
            }
        }

        ExaminationDate newExaminationDate = new ExaminationDate();

        newExaminationDate.setDateOfTest(tmpDateOfTerm);
        newExaminationDate.setMaxParticipants(maxParticipantsInt);
        newExaminationDate = examinationDateDao.save(newExaminationDate);


        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());
        Subject tmpSubject = subjectDao.findOne(subjectId);

        if (tmpSubject == null) {
            log.error("Creating new examination term failed.");
            return false;
        }

        newExaminationDate.setTeacher(tmpTeacher);
        newExaminationDate.setSubject(tmpSubject);

        newExaminationDate = examinationDateDao.save(newExaminationDate);

        if (newExaminationDate != null && newExaminationDate.getId() != null) {
            return true;
        }

        log.error("Creating new examination term failed.");
        return false;
    }

    /**
     * This method removes exam term of specific teacher.
     *
     * @param teacher    For this teacher object will be exam date removed
     * @param examTermId Database id of exam term which teacher wants to remove
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
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

        if (gradeDao.findGradesByExaminationDate(examinationDate).size()  > 0){
            return false;
        }

        // Test of authorized teacher
        if (examinationDate.getTeacher() == null || examinationDate.getTeacher().getId().longValue() != teacher.getId().longValue()) {
            return false;
        }

        //Term have not to be deleted if at least one student is graduated in specific subject!
        examinationDate.getParticipants().removeAll(examinationDate.getParticipants());

        examinationDate.setTeacher(null);
        examinationDate.setSubject(null);
        examinationDate.setDateOfTest(null);
        examinationDate = examinationDateDao.save(examinationDate);

        examinationDateDao.delete(examinationDate.getId());
        examinationDate = examinationDateDao.findOne(examTermId);

        return examinationDate == null;
    }

    /**
     * This method creates new grade/evaluation for student by particular teacher.
     *
     * @param teacher           Object of teacher who wants te create grade
     * @param studentId         Database id of student who gets the grade
     * @param gradeTypeId       Database id of Grade type
     * @param subjectId         Database id of subject
     * @param examinationDateId Database id of examination date
     * @return true if operation will be successfully completed, false otherwise
     */
    @Override
    public boolean createNewGrade(Teacher teacher, Long studentId, Long gradeTypeId, Long subjectId, Long examinationDateId) {
        if (teacher == null) {
            log.error("Creating new grade failed.");
            return false;
        }
        log.info("Creating new grade for teacher with id " + teacher.getId() + ", student with id " + studentId + ", grade type with id " + gradeTypeId + " and examination term with id " + examinationDateId + ".");

        Student tmpStudent = (Student) userDao.findOne(studentId);
        if (tmpStudent == null) {
            log.error("Creating grade for non existent student is not possible!");
            return false;
        }

        Subject subject = subjectDao.findOne(subjectId);
        if (subject == null) {
            log.error("Creating grade for non existent subject is not possible!");
            return false;
        }

        ExaminationDate examinationDate = examinationDateDao.findOne(examinationDateId);
        if (examinationDate == null) {
            log.error("Creating grade for non existent examination date is not possible!");
            return false;
        }

        /* Test if grade already exist */
        Grade testIfExistGrade = gradeDao.findGradeByStudentAndSubjectAndDate(tmpStudent, examinationDate.getSubject(), examinationDate.getDateOfTest());
        if (testIfExistGrade != null) {
            log.error("Grade with this options already exists!");
            return false;
        }

        GradeType gradeType = gradeTypeDao.findOne(gradeTypeId);
        List<GradeType> allGradeTypes = gradeTypeDao.getAllGradeTypes();
        // Last grade is a failure
        GradeType unsuccessfulGrade = allGradeTypes.get(allGradeTypes.size() - 1);

        if (gradeType == null) {
            log.error("Creating grade with non existent grade type is not possible!");
            return false;
        }
        Teacher tmpTeacher = (Teacher) userDao.findOne(teacher.getId());

        Grade newGrade = new Grade();
        newGrade = gradeDao.save(newGrade);
        newGrade.setSubject(subject);
        newGrade.setWhoGradeGranted(tmpTeacher);
        newGrade.setOwner(tmpStudent);
        newGrade.setDayOfGrant(examinationDate.getDateOfTest());
        newGrade.setTypeOfGrade(gradeType);
        newGrade.setTestWhereWasGradeGranted(examinationDate);

        newGrade = gradeDao.save(newGrade);

        // Test if new grade is passing evaluation
        if (!(gradeType.getId().longValue() == unsuccessfulGrade.getId().longValue())) {
            if (newGrade != null && newGrade.getId() != null) {
                // Removing subject from learned subjects
                removeFromSubjects(subject, tmpStudent.getListOfLearnedSubjects());
                tmpStudent.getListOfAbsolvedSubjects().add(subject);

                userDao.save(tmpStudent);
                return true;
            }
        } else {
            return true;
        }

        log.error("Creating new grade failed.");
        return false;
    }

    /**
     * This method returns single examination term by its id.
     *
     * @param examTermId Database id of examination term which will be returned
     * @return ExaminationDate object
     */
    @Override
    public ExaminationDate getExaminationTerm(Long examTermId) {
        log.info("Getting examination date with id " + examTermId + ".");
        return examinationDateDao.findOne(examTermId);
    }

    /**
     * This method returns Student object from database for specific examination
     * date. In case student is not participant of examination date this method
     * returns null;
     *
     * @param term      Examination term object
     * @param studentId Database id of student
     * @return Student object or null.
     */
    @Override
    public Student getStudentFromExaminationTerm(ExaminationDate term, long studentId) {

        if (term == null) {
            log.error("Getting student from examination term failed.");
            return null;
        }
        log.info("Getting student from examination term.");

        Student student = null;
        for (Student s : term.getParticipants()) {
            if (s.getId() == studentId) {
                student = s;
                break;
            }
        }
        return student;
    }

    /**
     * This method returns all grade types in university information system database.
     *
     * @return List of all grade types
     */
    @Override
    public List<GradeType> getAllGradeTypes() {
        log.info("Getting all grade types.");
        return gradeTypeDao.getAllGradeTypes();
    }

    /**
     * This method return all teacher objects in university information system database.
     *
     * @return List of all teachers in system
     */
    @Override
    public List<Teacher> getAllTeachers() {
        log.info("Getting all teachers.");
        List<Teacher> allTeachersList = new ArrayList<>();

        for (User u : userDao.findAllUsers()) {
            if (u instanceof Teacher) {
                allTeachersList.add((Teacher) u);
            }
        }
        sortListOfUsers(allTeachersList);
        return allTeachersList;
    }

    /**
     * This method return all grades created for one subject (for all examination terms).
     *
     * @param subjectId Database id of subject
     * @return List of grades for one subject
     */
    @Override
    public List<Grade> getGradesForSubject(Long subjectId) {
        Subject subject = subjectDao.findOne(subjectId);
        if (subject != null) {
            log.info("Getting all grades for subject with id " + subjectId + ".");
            return gradeDao.findGradesBySubject(subject);
        }
        log.info("Getting all grades for subject with failed.");
        return null;
    }

    /**
     * This method update persisted grade object.
     *
     * @param gradeId        Database id of updated grade
     * @param newGradeTypeId Database id of grade type
     * @return true if success
     */
    @Override
    public boolean updateGrade(Long teacherId, Long gradeId, Long newGradeTypeId) {
        log.info("Updating grade with id " + gradeId + " with teacher with id " + teacherId + " and grade type with id " + newGradeTypeId);
        Grade savedGrade = gradeDao.findOne(gradeId);
        User currentTeacher = userDao.findOne(teacherId);
        GradeType gradeType = gradeTypeDao.findOne(newGradeTypeId);

        List<GradeType> allGradeTypes = gradeTypeDao.getAllGradeTypes();
        // Last grade is a failure
        GradeType unsuccessfulGrade = allGradeTypes.get(allGradeTypes.size() - 1);

        // Test if teacher teaches subject of updating grade.
        if (savedGrade != null && gradeType != null && currentTeacher instanceof Teacher) {
            // Test if new grade is passing evaluation
            for (Subject s : ((Teacher) currentTeacher).getListOfTaughtSubjects()) {
                if (savedGrade.getSubject().getId().longValue() == s.getId().longValue()) {
                    savedGrade.setWhoGradeGranted((Teacher) currentTeacher);
                    savedGrade.setTypeOfGrade(gradeType);

                    savedGrade = gradeDao.save(savedGrade);
                    Student student = savedGrade.getOwner();

                    Student tmpStudent = (Student) userDao.findOne(student.getId());

                    if (!(gradeType.getId().longValue() == unsuccessfulGrade.getId().longValue())) { // successful grade
                        //Removing subject from learned subjects
                        removeFromSubjects(s, tmpStudent.getListOfLearnedSubjects());

                        if (!tmpStudent.getListOfAbsolvedSubjects().contains(s)) {
                            tmpStudent.getListOfAbsolvedSubjects().add(s);
                        }

                    } else { // unsuccessful grade
                        // Removing subject from absolved subjects
                        removeFromSubjects(s, tmpStudent.getListOfAbsolvedSubjects());

                        if (!tmpStudent.getListOfLearnedSubjects().contains(s)) {
                            tmpStudent.getListOfLearnedSubjects().add(s);
                        }

                    }

                    userDao.save(tmpStudent);
                    return true;
                }
            }
        }
        log.error("Updating grade failed.");
        return false;
    }

    /**
     * This method indicates whether to
     * swap name column with teacher column
     *
     * @return Indication for swap of columns
     */
    @Override
    public boolean swapNameAndTeacher() {
        return false;
    }

    /**
     * This method returns count actually teached subjects
     *
     * @return Number of subjects where teacher participates
     */
    @Override
    public int getNumberOfTaughtSubjects() {
        Teacher tmpTeacher = (Teacher) userDao.findOne(getCurrentUser().getId());
        //TODO consider optimalization on dao layer
        return tmpTeacher.getListOfTaughtSubjects().size();
    }

    /**
     * Removes subject from list ob subjects
     *
     * @param subject  Subject to remove
     * @param subjects List of subjects
     */
    private void removeFromSubjects(Subject subject, List<Subject> subjects) {
        log.info("Removing subject with id " + (subject != null ? subject.getId() : null) + " from list of subjects.");
        Iterator<Subject> learnedSubjectsIt = subjects.iterator();
        while (learnedSubjectsIt.hasNext()) {
            if (learnedSubjectsIt.next().getId().longValue() == subject.getId().longValue()) {
                learnedSubjectsIt.remove();
                break;
            }
        }
    }

}
