package cz.zcu.kiv.matyasj.dp.utils.comparators;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;

/**
 * Class holds static methods to compare two Subjects
 *
 * @author Radek Vais
 * @version 2020-04-10
 */
public class SubjectsComparator {

    /**
     * Method compares two subjects by name
     *
     * @param sb1 first subject to compare
     * @param sb2 second subject to compare
     * @return the value 0 if subject names are equal;
     * a value less than 0 if name of sb1 is lexicographically less than the name sb2;
     * and a value greater than 0 if name of sb1 is lexicographically greater than the name of sb2 argument.
     */
    public static int NameAsc(Subject sb1, Subject sb2) {
        return sb1.getName().compareToIgnoreCase(sb2.getName());
    }
}
