package cz.zcu.kiv.matyasj.dp.service;

import java.io.File;

/**
 * This interface defines methods of service objects for a data porting (import/export) from and to database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface PorterService {

    /**
     * This method imports all data from file (in xml, json, ... format) into system database.
     *
     * @param file File with data which will be imported into database.
     * @return true if data was successfully saved in database
     */
    boolean importData(File file);

    /**
     * This method exports all data from system database and creates file with exported data.
     *
     * @param exportDataFormat Format of exported data (exported file extension)
     * @return File with exported data
     */
    File exportData(String exportDataFormat);
}
