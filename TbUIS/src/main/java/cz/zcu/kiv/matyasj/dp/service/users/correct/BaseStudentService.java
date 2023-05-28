package cz.zcu.kiv.matyasj.dp.service.users.correct;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Grade;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.StudentService;
import cz.zcu.kiv.matyasj.dp.service.users.BaseUserService;
import cz.zcu.kiv.matyasj.dp.utils.comparators.ExaminationsComparator;
import cz.zcu.kiv.matyasj.dp.utils.comparators.StudentsComparator;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Objects of this class represent services providing functions for manipulation with data
 * related to students. BaseStudentService provide method for enroll/unenroll subjects to
 * student, register/unregister examination date for student, etc.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
 */
@Service
public class BaseStudentService extends BaseUserService implements StudentService {
    /**
     * DAO object for manipulation with subject data in database
     */
    protected SubjectDao subjectDao;
    /**
     * DAO object for manipulation with user data in database
     */
    protected UserDao userDao;
    /**
     * DAO object for manipulation with exam date data in database
     */
    protected ExaminationDateDao examinationDateDao;
    /**
     * DAO object for manipulation with grade data in database
     */
    protected GradeDao gradeDao;
    /**
     * DAO object for grade types
     */
    protected GradeTypeDao gradeTypeDao;
    /**
     * Application property loader
     */
    protected PropertyLoader propertyLoader;
    /**
     * Shared system logger
     */
    private final Logger log = LogManager.getLogger();

    /**
     * BaseStudentService constructor for initialization objects used by spring.
     *
     * @param subjectDao         DAO object for manipulation with subject data in database
     * @param userDao            DAO object for manipulation with user data in database
     * @param examinationDateDao DAO object for manipulation with exam date data in database
     * @param gradeDao           DAO object for manipulation with grade data in database
     * @param gradeTypeDao       DAO object for manipulation with grade type data in database
     * @param propertyLoader     Application property loader
     */
    @Autowired
    public BaseStudentService(SubjectDao subjectDao, UserDao userDao, ExaminationDateDao examinationDateDao, GradeDao gradeDao, GradeTypeDao gradeTypeDao, PropertyLoader propertyLoader) {
        this.subjectDao = subjectDao;
        this.userDao = userDao;
        this.examinationDateDao = examinationDateDao;
        this.gradeDao = gradeDao;
        this.gradeTypeDao = gradeTypeDao;
        this.propertyLoader = propertyLoader;
    }

    /**
     * This method returns list of subjects that one particular
     * student studies.
     *
     * @param studentId database student id
     * @return List of studied subjects
     */
    @Override
    public List<Subject> getStudiedSubjectsList(Long studentId) {

        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Getting list of studied subjects for student with id " + studentId + ".");
            return sortListOfSubjects(((Student) student).getListOfLearnedSubjects());
        }
        log.error("Getting list of studied subjects failed.");
        return null;
    }

    /**
     * This method returns list of subjects that one particular student absolved.
     *
     * @param studentId database student id
     * @return List of absolved subjects
     */
    @Override
    public List<Subject> getAbsolvedSubjectsList(Long studentId) {

        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Getting list of absolved subjects for student with id " + studentId + ".");
            return sortListOfSubjects(new ArrayList<>(((Student) student).getListOfAbsolvedSubjects()));
        }
        log.error("Getting list of absolved subjects failed.");
        return null;
    }

    /**
     * This method returns list of subjects that one particular
     * student NOT studies or did not graduated.
     *
     * @param studentId database student id
     * @return List of non-studied subjects
     */
    @Override
    public List<Subject> getOtherSubjectsList(Long studentId) {

        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Getting list of other subjects for student with id " + studentId + ".");
            List<Subject> listOfLearnedSubjects = ((Student) student).getListOfLearnedSubjects();
            List<Subject> listOfAbsolvedSubjects = ((Student) student).getListOfAbsolvedSubjects();
            List<Subject> excludedSubjects = new ArrayList<>();
            excludedSubjects.addAll(listOfAbsolvedSubjects);
            excludedSubjects.addAll(listOfLearnedSubjects);

            return sortListOfSubjects(subjectDao.getSubjectsExceptSelected(excludedSubjects));
        }
        log.error("Getting list of other subjects failed.");
        return null;
    }

    /**
     * Get list of all exam dates registered by one particular student.
     *
     * @param studentId database student id
     * @return List of students examination dates
     */
    @Override
    public List<ExaminationDate> getExaminationDatesList(Long studentId) {

        User student = userDao.findOne(studentId);
        if (student instanceof Student) {
            log.info("Getting list of examination dates for student with id " + studentId + ".");
            List<ExaminationDate> examinationDateList = examinationDateDao.getAllExaminationDatesOfStudent((Student) student);
            return sortListOfExamDates(examinationDateList);
        }
        log.error("Getting list of examination dates failed.");
        return null;
    }

    /**
     * This method returns a list of all the exam dates not registered
     * by one particular student.
     *
     * @param studentId database student id
     * @return List of students examination dates
     */
    @Override
    public List<ExaminationDate> getNotRegisteredExaminationDatesList(Long studentId) {
        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Getting list of not registered examination dates for student with id " + studentId + ".");
            List<ExaminationDate> allExamDates = examinationDateDao.getAllExaminationDatesOfStudent((Student) student);

            Iterator<ExaminationDate> aExaminationDateIterator = allExamDates.iterator();

            // Remove registered Examination dates
            while (aExaminationDateIterator.hasNext()) {
                ExaminationDate examinationDate = aExaminationDateIterator.next();
                for (Student s : examinationDate.getParticipants()) {
                    if (s.getId().longValue() == studentId.longValue()) {
                        aExaminationDateIterator.remove();
                    }
                }
                examinationDate.getParticipants().sort(StudentsComparator::lastNameAsc);
            }
            return sortListOfExamDates(allExamDates);
        }
        log.error("Getting list of not registered examination dates failed.");
        return null;
    }

    /**
     * Get list of exam dates registered by one particular student. In list
     * are not included exam dates with evaluation for particular student.
     *
     * @param studentId database student id
     * @return List of students for examination dates
     */
    @Override
    public List<ExaminationDate> getStudentExaminationDatesList(Long studentId) {

        User student = userDao.findOne(studentId);
        if (student instanceof Student) {

            log.info("Getting list of examination dates for student with id " + studentId + ".");
            List<Grade> gradeList = gradeDao.findGradesByStudent((Student) student);
            List<ExaminationDate> examinationDateList = examinationDateDao.getExaminationDateOfStudent((Student) student);

            // Remove exam dates of already graduated subjects
            for (Grade g : gradeList) {
                examinationDateList.removeIf(examinationDate -> (examinationDate.getSubject().getId().longValue() == g.getSubject().getId().longValue() && examinationDate.getDateOfTest().equals(g.getDayOfGrant())));
            }

            for (ExaminationDate ed : examinationDateList) {
                ed.getParticipants().sort(StudentsComparator::lastNameAsc);
            }

            return sortListOfExamDates(examinationDateList);
        }
        log.error("Getting list of examination dates failed.");
        return null;
    }

    /**
     * This method returns list of grades of one particular student.
     *
     * @param student student object
     * @return list of student's grades/evaluation
     */
    @Override
    public List<Grade> getStudentGrades(Student student) {

        if (student != null) {
            log.info("Getting list of grades for student with id " + student.getId() + ".");
            return gradeDao.findGradesByStudent(student);
        }
        log.error("Getting list of grades failed.");
        return null;
    }


    /**
     * This method returns list of subjects that one particular student
     * studies. Students are enrolled for the
     * date of examination for these subjects.
     *
     * @param studentId database student id
     * @return List of studied subjects
     */
    @Override
    public List<Subject> getSubjectsWithRegisteredExamDate(Long studentId) {

        List<ExaminationDate> registerExamDates = getStudentExaminationDatesList(studentId);
        List<Subject> subjectWithRegisteredExamDate = new ArrayList<>();

        if (registerExamDates == null) {
            log.error("Getting list of subjects with registered exam date failed.");
            return null;
        }
        log.info("Getting list of subjects with registered exam date for student with id " + studentId + ".");

        for (ExaminationDate e : registerExamDates) {
            subjectWithRegisteredExamDate.add(e.getSubject());
        }

        return sortListOfSubjects(subjectWithRegisteredExamDate);
    }

    /**
     * This method enrolls subject for the student.
     *
     * @param studentId database id of student
     * @param subjectId database id of subject
     * @return true if subject was successfully unregistered, false otherwise.
     */
    @Override
    public boolean setStudiedSubject(Long studentId, Long subjectId) {

        User student = userDao.findOne(studentId);
        if (student instanceof Student) {
            log.info("Setting studied subject with id " + subjectId + " to student with id " + studentId + ".");

            // Check max subject count for student
            int maxSubjectsNumber = Integer.parseInt(propertyLoader.getProperty("studentMaxSubjects"));
            if (((Student) student).getListOfLearnedSubjects().size() >= maxSubjectsNumber) {
                log.warn("Student " + student.getFirstName() + " " + student.getLastName() + " is trying to enroll more than max subject count(" + maxSubjectsNumber + ")!");
                return false;
            }

            // Test if is object already set.
            for (Subject subject : ((Student) student).getListOfLearnedSubjects()) {
                if (subject.getId().longValue() == subjectId.longValue()) {
                    return false;
                }
            }
            // Test if is object already absolved.
            for (Subject subject : ((Student) student).getListOfAbsolvedSubjects()) {
                if (subject.getId().longValue() == subjectId.longValue()) {
                    return false;
                }
            }

            Subject newSubject = subjectDao.findOne(subjectId);
            // Non-existent subject
            if (newSubject == null) {
                return false;
            }

            ((Student) student).getListOfLearnedSubjects().add(newSubject);

            student = userDao.save(student);

            if (student != null) {
                return true;
            }
        }

        log.error("Setting studied subject failed.");
        return false;
    }

    /**
     * This method unenrolls subject for the student.
     *
     * @param studentId database id of student
     * @param subjectId database id of subject
     * @return true if subject was successfully unenrolled, false otherwise.
     */
    @Override
    public boolean unsetStudiedSubject(Long studentId, Long subjectId) {
        User student = userDao.findOne(studentId);

        if (student == null) {
            log.error("Unsetting studied subject failed.");
            return false;
        }

        if (student instanceof Student) {
            log.info("Unsetting studied subject with id " + subjectId + " from student with id " + studentId + ".");

            List<ExaminationDate> examinationDateList = getStudentExaminationDatesList(studentId);
            List<Subject> listOfLearnedSubjects = ((Student) student).getListOfLearnedSubjects();
            for (ExaminationDate e : examinationDateList) {
                if (e.getSubject().getId().longValue() == subjectId.longValue()) {
                    unsetExaminationDate(studentId, e.getId());
                }
            }
            for (Subject s : listOfLearnedSubjects) {
                if (s.getId().longValue() == subjectId.longValue()) {
                    listOfLearnedSubjects.remove(s);
                    student = userDao.save(student);
                    if (student != null) {
                        return true;
                    }
                }
            }
            log.warn("Try to unset not studied subject!");
        }
        log.error("Unsetting studied subject failed.");
        return false;
    }

    /**
     * This method registers student on specific examination date.
     *
     * @param studentId  database id of student
     * @param examDateId database id of exam date
     * @return true if examination date was successfully registered, false otherwise.
     */
    @Override
    public boolean setExaminationDate(Long studentId, Long examDateId) {

        ExaminationDate DateTmp = examinationDateDao.findOne(examDateId);
        if (DateTmp == null) {
            log.error("Setting examination date failed.");
            return false;
        }

        if (DateTmp.getParticipants().size() == DateTmp.getMaxParticipants()) {
            log.error("Setting examination date failed.");
            return false;
        }

        // Test if student is learning subject of exam date
        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Setting examination date with id " + examDateId + " to student with id " + studentId + ".");
            for (Subject subject : ((Student) student).getListOfLearnedSubjects()) {
                if (subject.getId().longValue() == DateTmp.getSubject().getId().longValue()) {

                    List<ExaminationDate> myExams = getExaminationDatesList(studentId);
                    myExams = myExams.stream()
                            .filter(ed -> ed.getSubject().equals(subject)).collect(Collectors.toList());
                    int constraint = Integer.parseInt(propertyLoader.getProperty("studentMaxExamDate"));

                    if(myExams.size() > constraint){
                        log.info("Student reach maximum count of tries.");
                        return false;
                    }

                    ExaminationDate date = examinationDateDao.registerStudentOnTerm(examDateId, studentId);

                    if (date != null) {
                        return true;
                    }
                }
            }
        }
        log.error("Setting examination date failed.");
        return false;
    }

    /**
     * This method unregisters student on specific examination date.
     *
     * @param studentId  database id of student
     * @param examDateId database id of exam date
     * @return true if examination date was successfully unregistered, false otherwise.
     */
    @Override
    public boolean unsetExaminationDate(Long studentId, Long examDateId) {

        User student = userDao.findOne(studentId);
        if (student instanceof Student) {
            log.info("Unsetting examination date with id " + examDateId + " from student with id " + studentId + ".");
            ExaminationDate date = examinationDateDao.unregisterStudentOnTerm(examDateId, (Student) student);
            if (date != null) {
                return true;
            }
        }
        log.error("Unsetting examination date failed.");
        return false;
    }

    /**
     * This method returns number of obtained credits.
     *
     * @param studentId database id of student
     * @return number of obtained credits.
     */
    @Override
    public int getStudentTotalCredits(Long studentId) {

        int totalCredits;
        User student = userDao.findOne(studentId);

        if (student instanceof Student) {
            log.info("Getting total number of credits for student with id " + studentId + ".");
            totalCredits = ((Student) student).getListOfAbsolvedSubjects().stream().mapToInt(Subject::getCreditRating).sum();
            return totalCredits;
        }
        log.error("Getting total number of credits failed.");
        return -1;
    }

    /**
     * This method returns title
     *
     * @return Title
     */
    @Override
    public String setTitle() {
        return "";
    }

    /**
     * This method indicates whether to change view
     * that is shown after removal of subject
     *
     * @return Indication for showing overview after removal of subject
     */
    @Override
    public boolean afterRemoveShowOverview() {
        return false;
    }

    /**
     * This method indicates whether overview
     * changes view to other exam
     *
     * @return Indication whether change overview to other exam page
     */
    @Override
    public boolean changeOverviewToOtherExam() {
        return false;
    }

    /**
     * This method makes sure to show teacher column
     *
     * @return Indication for hiding teacher column
     */
    @Override
    public boolean hideTeacherColumn() {
        return false;
    }

    /**
     * This method leaves basic color of participants button
     *
     * @return Indication whether to change color
     */
    @Override
    public boolean changeParticipantsButtonColor() {
        return false;
    }

    /**
     * This method makes sure to not duplicate last participant
     *
     * @return Indication whether to duplicate
     */
    @Override
    public boolean duplicateLastParticipant() {
        return false;
    }

    /**
     * This method makes sure to show all unenroll buttons
     *
     * @return Indication of hiding unenroll button
     */
    @Override
    public boolean hideUnenrollButton() {
        return false;
    }

    /**
     * This method makes sure to show correct number of participants
     *
     * @return Indication of changing number of participants
     */
    @Override
    public boolean changeNumberOfParticipants() {
        return false;
    }

}