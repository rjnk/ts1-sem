package cz.zcu.kiv.matyasj.dp.service;

/**
 * This interface defines the database recovery method of service object of the service for database restoring.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface RestoreDBService {
    /**
     * This method allows you to restore the database to its initial state after running the
     * UIS application.
     *
     * @return true, if operation has been successfully finished, false otherside
     */
    boolean restoreDB();
}
