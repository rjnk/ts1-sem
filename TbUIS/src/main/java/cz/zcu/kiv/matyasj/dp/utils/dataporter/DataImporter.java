package cz.zcu.kiv.matyasj.dp.utils.dataporter;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import java.io.File;
import java.util.List;

/**
 * This interface defines the method for importing data into system database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface DataImporter {
    /**
     * This method creates data import into system database. This data
     * is stored in the file given in the method parameter.
     *
     * @param f File with data to import
     * @return true, if operation is successfully completed, false otherwise
     */
    List<BaseEntity> importData(File f);
}
