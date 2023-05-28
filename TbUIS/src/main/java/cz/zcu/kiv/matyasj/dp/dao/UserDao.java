package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.users.User;

import java.util.List;

/**
 * This interface defines methods of DAOs for manipulation with User data in database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface UserDao extends GenericDao<User, Long> {
    /**
     * Create new user in database by existent user instance
     *
     * @param user instance of new user
     * @return instance of user with id if success
     */
    User create(User user);

    /**
     * Find user by username.
     *
     * @param username username of user searching for.
     * @return found user or null
     */
    User findByUsername(String username);

    /**
     * Find all users from database and return them.
     *
     * @return List of all users
     */
    List<User> findAllUsers();
}
