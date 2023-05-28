package cz.zcu.kiv.matyasj.dp.service.restoreDB;

import cz.zcu.kiv.matyasj.dp.dao.DatabaseDao;
import cz.zcu.kiv.matyasj.dp.service.RestoreDBService;
import cz.zcu.kiv.matyasj.dp.utils.init.Initializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Objects of this class represent services for database restoring.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Service
public class BaseRestoreDBService implements RestoreDBService {
    /** DAO object for manipulation with all data in system database */
    private final DatabaseDao databaseDao;
    /** Initializer objects allows init/reinit database state */
    private final Initializer initializer;

    /**
     * BaseRestoreDBService constructor
     *
     * @param databaseDao DAO object for manipulation with all data in system database
     * @param initializer Initializer objects allows init/reinit database state
     */
    @Autowired
    public BaseRestoreDBService(DatabaseDao databaseDao, Initializer initializer) {
        this.databaseDao = databaseDao;
        this.initializer = initializer;
    }

    /**
     * This method makes restoring application database. First of all method removes all data from database. Than method
     * reinit database by Initializer object.
     *
     * @return true if restore operation was successfully completed, false otherwise
     */
    @Override
    public boolean restoreDB() {
        if (databaseDao.eraseDatabase()) {
            return initializer.reinit();
        }
        return false;
    }
}
