package cz.zcu.kiv.matyasj.dp.service.porters;

import cz.zcu.kiv.matyasj.dp.dao.DatabaseDao;
import cz.zcu.kiv.matyasj.dp.service.PorterService;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.DataPorter;
import cz.zcu.kiv.matyasj.dp.utils.dataporter.dataTypes.ExportDataFormat;
import cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Objects of this class represent services for data porting (import/export) from and to database.
 * Objects are able to export/import data in XML and JSON format. For this objects use xmlDataPorter
 * and jsonDataPorter utility components.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Service
public class BasePorterService implements PorterService {
    /** DAO object for manipulation with database (create database dump/ erase database/ fill database) */
    private DatabaseDao databaseDao;
    /** XML data porter utility component for porting data in XML format */
    private final DataPorter xmlDataPorter;
    /** JSON data porter utility component for porting data in JSON format */
    private final DataPorter jsonDataPorter;
    /** Property loader for getting temporary folder path */
    private final PropertyLoader propertyLoader;
    /** String to date formatter which format date part of export file name*/
    private Format datePartExportFileNameFormatter;

    /**
     * Default BasePorterService constructor
     *
     * @param xmlDataPorter     XML data porter
     * @param jsonDataPorter    JSON data porter
     * @param propertyLoader    Application property loader
     * @param databaseDao       DAO object for database manipulation
     */
    @Autowired
    public BasePorterService(DataPorter xmlDataPorter, DataPorter jsonDataPorter, PropertyLoader propertyLoader, DatabaseDao databaseDao) {
        this.xmlDataPorter = xmlDataPorter;
        this.jsonDataPorter = jsonDataPorter;
        this.propertyLoader = propertyLoader;
        this.databaseDao = databaseDao;

        this.datePartExportFileNameFormatter = new SimpleDateFormat(propertyLoader.getProperty("exportDataFileNameDatePartFormat"));
    }

    /**
     * This method imports all data from file (in xml, json, ... format) into system database.
     *
     * @param file File with data which will be imported into database.
     * @return true if data was successfully saved in database
     */
    @Override
    public boolean importData(File file) {
        String[] fileExtension = file.getName().split("\\.");

        if (ExportDataFormat.XML.getFileExtension().equals(fileExtension[fileExtension.length - 1])) {
            if (databaseDao.eraseDatabase()) {
                return databaseDao.fillDatabase(xmlDataPorter.importData(file));
            }
        } else if (ExportDataFormat.JSON.getFileExtension().equals(fileExtension[fileExtension.length - 1])) {
            if (databaseDao.eraseDatabase()) {
                return databaseDao.fillDatabase(jsonDataPorter.importData(file));
            }
        }
        return false;
    }

    /**
     * This method exports all data from system database and creates file with exported data.
     *
     * @param exportDataFormat Format of exported data (exported file extension)
     * @return File with exported data
     */
    @Override
    public File exportData(String exportDataFormat) {
        /* XML export data */
        if (ExportDataFormat.XML.getFileExtension().equals(exportDataFormat)) {
            String fileName = System.getProperty("java.io.tmpdir") + File.separator + datePartExportFileNameFormatter.format(new Date()) + "_DB-export.xml";
            boolean success = xmlDataPorter.exportData(fileName, databaseDao.databaseDump());
            File exportedFile = new File(fileName);
            if (success && exportedFile.exists()) {
                return exportedFile;
            }
        }

        /* JSON export data */
        if (ExportDataFormat.JSON.getFileExtension().equals(exportDataFormat)) {
            String fileName = System.getProperty("java.io.tmpdir") + File.separator + datePartExportFileNameFormatter.format(new Date()) + "_DB-export.json";
            boolean success = jsonDataPorter.exportData(fileName, databaseDao.databaseDump());
            File exportedFile = new File(fileName);
            if (success && exportedFile.exists()) {
                return exportedFile;
            }
        }
        return null;
    }
}
