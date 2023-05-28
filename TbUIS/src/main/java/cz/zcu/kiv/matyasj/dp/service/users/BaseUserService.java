package cz.zcu.kiv.matyasj.dp.service.users;

import cz.zcu.kiv.matyasj.dp.dao.UserDao;
import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import cz.zcu.kiv.matyasj.dp.domain.users.User;
import cz.zcu.kiv.matyasj.dp.service.UserService;
import cz.zcu.kiv.matyasj.dp.utils.comparators.ExaminationsComparator;
import cz.zcu.kiv.matyasj.dp.utils.comparators.StudentsComparator;
import cz.zcu.kiv.matyasj.dp.utils.comparators.SubjectsComparator;
import cz.zcu.kiv.matyasj.dp.utils.comparators.UsersComparator;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;

import java.util.Comparator;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This abstract class is used as a base for a user services (StudentService, TeacherService). The part of methods in this
 * class sorts list of subjects and users alphabetically.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public abstract class BaseUserService implements UserService {

    private final Logger log = LogManager.getLogger();
    /** User DAO object for manipulation with user data in database */
    @Autowired
    private UserDao userDao;
    /** Application property loader */
    @Autowired
    protected PropertyLoader propertyLoader;


    /**
     * Method returns object of currently logged in user.
     *
     * @return object of currently logged in user
     */
    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserUsername = authentication.getName();

        return userDao.findByUsername(currentUserUsername);
    }

    /**
     * This method updates current user first name, last name and email
     *
     * @param firstName new first name of current user
     * @param lastName  new last name of current user
     * @param email     new email of current user
     * @return true if success, false otherwise
     */
    @Override
    public boolean updateUser(String firstName, String lastName, String email) {
        User currentUser = getCurrentUser();

        if (firstName == null || firstName.length() < Integer.parseInt(propertyLoader.getProperty("minFirstNameLength"))) {
            log.warn("Length of first name is too short! Update of current user is being canceled.");
            return false;
        }

        if (firstName.length() > Integer.parseInt(propertyLoader.getProperty("maxFirstNameLength"))) {
            log.warn("Length of first name is too long! Update of current user is being canceled.");
            return false;
        }

        if (lastName == null || lastName.length() < Integer.parseInt(propertyLoader.getProperty("minLastNameLength"))) {
            log.warn("Length of last name is too short! Update of current user is being canceled.");
            return false;
        }

        if (lastName.length() > Integer.parseInt(propertyLoader.getProperty("maxLastNameLength"))) {
            log.warn("Length of last name is too long! Update of current user is being canceled.");
            return false;
        }

        if (email == null || email.length() < Integer.parseInt(propertyLoader.getProperty("minEmailLength"))) {
            log.warn("Length of email is too short! Update of current user is being canceled.");
            return false;
        }

        if (email.length() > Integer.parseInt(propertyLoader.getProperty("maxEmailLength"))) {
            log.warn("Length of email is too long! Update of current user is being canceled.");
            return false;
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setEmail(email);

        currentUser = userDao.save(currentUser);
        if (currentUser != null) {
            return true;
        }
        return false;
    }

    /**
     * This method sorts list of subjects alphabetically by name of subjects and
     * teachers whose teaches subject by first name.
     *
     * @param subjectList list of subjects which should be sorted.
     * @return sorted list
     */
    protected List<Subject> sortListOfSubjects(List<Subject> subjectList) {
        // Sort Subjects alphabetically
        subjectList.sort(SubjectsComparator::NameAsc);
        for (Subject s : subjectList) {
            s.getTeachers().sort(UsersComparator::LastNameAsc);
            s.getListOfStudents().sort(StudentsComparator::lastNameAsc);
            s.getListOfAbsolvents().sort(StudentsComparator::lastNameAsc);
        }
        return subjectList;
    }


    /**
     * This method sorts list of exam dates by name and date of exam.
     *
     * @param examinationDateList list of exam dates which should be sorted.
     * @return sorted list
     */
    protected List<ExaminationDate> sortListOfExamDates(List<ExaminationDate> examinationDateList) {
        // Sort ExamDates by date
        examinationDateList.sort(ExaminationsComparator::nameAscDateAsc);
        return examinationDateList;
    }


    /**
     * This method sorts list of users alphabetically by last name
     *
     * @param usersList list of users which should be sorted.
     * @return sorted list
     */
    protected List<? extends User> sortListOfUsers(List<? extends User> usersList) {
        // Sort Subjects alphabetically
        usersList.sort(UsersComparator::LastNameAsc);

        // Sort Users in lists alphabetically
        for (User user : usersList) {
            if (user instanceof Student) {
                Student s = (Student) user;
                s.getListOfLearnedSubjects().sort(SubjectsComparator::NameAsc);
                s.getListOfAbsolvedSubjects().sort(SubjectsComparator::NameAsc);
            } else if (user instanceof Teacher) {
                Teacher t = (Teacher) user;
                t.getListOfTaughtSubjects().sort(SubjectsComparator::NameAsc);
            }
        }
        return usersList;
    }
}
