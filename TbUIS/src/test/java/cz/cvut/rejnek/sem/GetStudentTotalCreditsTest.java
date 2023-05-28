package cz.cvut.rejnek.sem;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import cz.zcu.kiv.matyasj.dp.dao.*;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.service.users.correct.BaseStudentService;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class GetStudentTotalCreditsTest {
    UserDao userDao;
    SubjectDao subjectDao;
    GradeDao gradeDao;
    ExaminationDateDao examinationDateDao;
    GradeTypeDao gradeTypeDao;
    PropertyLoader propertyLoader;

    Student student;

    @Before
    public void setUp() {
        // student service constructor dependencies
        userDao = mock(UserDao.class);
        subjectDao = mock(SubjectDao.class);
        examinationDateDao = mock(ExaminationDateDao.class);
        gradeDao = mock(GradeDao.class);
        gradeTypeDao = mock(GradeTypeDao.class);
        propertyLoader = mock(PropertyLoader.class);

        // student
        student = mock(Student.class);
        when(student.getId()).thenReturn(1L);
        when(student.getListOfAbsolvedSubjects()).thenReturn(new ArrayList<>());
    }

    @Test
    public void getStudentTotalCredits_4plus5_9() {
        // setup student mock
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Math", 5));
        subjects.add(new Subject("Science", 4));
        when(student.getListOfAbsolvedSubjects()).thenReturn(subjects);

        // setup userDao mock
        when(userDao.findOne(student.getId())).thenReturn(student);

        // test
        var studentService = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
        var result = studentService.getStudentTotalCredits(student.getId());
        assertEquals(9, result);
    }

    @Test
    public void getStudentTotalCredits_noSubjects_0() {
        // setup mock
        when(userDao.findOne(student.getId())).thenReturn(student);

        // test
        var studentService = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
        var result = studentService.getStudentTotalCredits(student.getId());
        assertEquals(0, result);
    }

    @Test
    public void getStudentTotalCredits_nullStudent_minus1() {
        // setup
        when(userDao.findOne(student.getId())).thenReturn(null);

        // test
        var studentService = new BaseStudentService(subjectDao, userDao, examinationDateDao, gradeDao, gradeTypeDao, propertyLoader);
        var result = studentService.getStudentTotalCredits(student.getId());
        assertEquals(-1, result);
    }
}
