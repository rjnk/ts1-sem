package cz.zcu.kiv.matyasj.dp.service;

import cz.zcu.kiv.matyasj.dp.domain.users.User;

/**
 * This interface defines methods of service objects providing functions for manipulation with data
 * related to a generic logged on user.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface UserService {
    /**
     * Method returns object of currently logged in user.
     *
     * @return object of currently logged in user
     */
    User getCurrentUser();

    /**
     * This method updates current user first name, last name and email
     *
     * @param firstName new first name of current user
     * @param lastName new last name of current user
     * @param email new email of current user
     * @return true if success, false otherwise
     */
    boolean updateUser(String firstName, String lastName, String email);
}
