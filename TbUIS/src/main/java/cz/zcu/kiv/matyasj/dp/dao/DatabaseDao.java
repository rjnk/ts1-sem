package cz.zcu.kiv.matyasj.dp.dao;


import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import java.util.List;

/**
 * This interface defines methods of DAOs for manipulation with all data in system database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface DatabaseDao extends GenericDao<BaseEntity, Long>{
    /**
     * This method erase all data from system database without backup.
     *
     * @return true if operation has been successfully completed, false otherwise
     */
    boolean eraseDatabase();

    /**
     * This method create dump of all data in system database and return them in list of domain objects.
     *
     * @return List of all data in system database.
     */
    List<BaseEntity> databaseDump();

    /**
     * This method fills a database of data created as a backup. This data are
     * given in parameter as list of domain objects.
     *
     * @param entities list of instances of db entities
     * @return true if operation has been successfully completed, false otherwise
     */
    boolean fillDatabase(List<BaseEntity> entities);

    /**
     * This method simply returns array of all domain classes used in system.
     *
     * @return array of all domain classes.
     */
    Class[] getDomainClasses();
}
