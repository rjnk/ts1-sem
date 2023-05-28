package cz.zcu.kiv.matyasj.dp.utils.dataporter.json;

import cz.zcu.kiv.matyasj.dp.dao.DatabaseDao;
import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.DataPorter;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.dataTypes.DataContainer;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Objects of this class are used as components for data export/import in JSON format.
 * Objects use JAXB library with EclipseLink MOXy provider.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Component
public class JSONDataPorter implements DataPorter {
    /** JAXB context for creation of JAXB Marshaller/UnMarshaller */
    private JAXBContext jaxbContext;
    /** Shared system logger */
    private final Logger log = LogManager.getLogger();

    /**
     * Base JSONDataPorter constructor.
     *
     * @param databaseDao DAO object for database manipulation
     */
    @Autowired
    public JSONDataPorter(DatabaseDao databaseDao) {
        Class[] domainClasses = databaseDao.getDomainClasses();
        Class[] domainClassesWithDataContainer = new Class[domainClasses.length + 1];

        System.arraycopy(domainClasses, 0, domainClassesWithDataContainer, 0, domainClasses.length);

        domainClassesWithDataContainer[domainClasses.length] = DataContainer.class;

        try {
            jaxbContext = JAXBContext.newInstance(domainClassesWithDataContainer);
        } catch (JAXBException e) {
            log.error("Marshaling error: " + e.getMessage(), e);
        }
    }

    /**
     * This method creates system database data export into file in JSON format.
     * Method returns file with exported data.
     *
     * @param fileName Name of file with exported data
     * @return File with exported data
     */
    @Override
    public boolean exportData(String fileName, List<BaseEntity> dbEntities) {
        // Special data container for collection all entities from db
        DataContainer data = new DataContainer();

        // Get all data entities from db by databaseDao
        for (BaseEntity entity : dbEntities) {
            data.getEntities().add(entity);
        }

        // Try to save data into JSON file
        try {
            File file = new File(fileName);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
            jaxbMarshaller.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(data, file);
            log.info("Data exported to file " + fileName + ".");
        } catch (JAXBException e) {
            log.error("Marshaling error: " + e.getMessage(), e);
            return false;
        }

        return true;
    }

    /**
     * This method creates data import into system database in JSON format.
     * This data is stored in the file given in the method parameter.
     *
     * @param f JSON File with data to import
     * @return true, if operation is successfully completed, false otherwise
     */
    @Override
    public List<BaseEntity> importData(File f) {
        if (f == null || !f.exists()) {
            log.error("Imported file does not exist!");
            return null;
        }

        try {
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
            jaxbUnmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);

            DataContainer data = (DataContainer) jaxbUnmarshaller.unmarshal(f);
            log.info("Data imported from file" + f.getName() + ".");
            return data.getEntities();
        } catch (JAXBException e) {
            log.error("Error during JAXB unmarshalling has occurred!");
            e.printStackTrace();
        }

        return null;
    }
}