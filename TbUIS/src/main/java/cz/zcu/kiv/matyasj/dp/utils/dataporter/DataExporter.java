package cz.zcu.kiv.matyasj.dp.utils.dataporter;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import java.util.List;

/**
 * This interface defines the method for exporting data from system database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface DataExporter {
    /**
     * This method creates system database data export into file.
     * Method returns file with exported data.
     *
     * @param fileName Name of file with exported data
     * @return File with exported data
     */
    boolean exportData(String fileName, List<BaseEntity> dbEntities);
}
