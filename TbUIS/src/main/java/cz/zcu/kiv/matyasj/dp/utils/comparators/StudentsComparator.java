package cz.zcu.kiv.matyasj.dp.utils.comparators;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.User;

/**
 * Class holds static methods to compare two Setudents
 *
 * @author Radek Vais
 * @version 2019-02-02
 */
public class StudentsComparator {

    /**
     * Method compares two students by last name
     * @param st1 first student to compare
     * @param st2 second student to compare
     *
     * @return the value 0 if last names are equal;
     *         a value less than 0 if name of st1 is lexicographically less than the name st2;
     *         and a value greater than 0 if name of st1 is lexicographically greater than the name of st2 argument.
     */
    public static int lastNameAsc (Student st1, Student st2 ) {
        return UsersComparator.LastNameAsc(st1, st2);
    }
}
