package cz.zcu.kiv.matyasj.dp.utils.comparators;

import cz.zcu.kiv.matyasj.dp.domain.users.User;

/**
 * Class holds static methods to compare two Users
 *
 * @author Radek Vais
 * @version 2020-04-10
 */
public class UsersComparator {

    /**
     * Method compares two users by last name
     *
     * @param u1 first user to compare
     * @param u2 second user to compare
     * @return the value 0 if user names are equal;
     * a value less than 0 if name of u1 is lexicographically less than the name u2;
     * and a value greater than 0 if name of u1 is lexicographically greater than the name of u2 argument.
     */
    public static int LastNameAsc(User u1, User u2) {
        return u1.getLastName().compareToIgnoreCase(u2.getLastName());
    }
}
