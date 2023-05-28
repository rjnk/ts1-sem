package cz.zcu.kiv.matyasj.dp.utils.dataporter.dataTypes;

/**
 * ExportDataFormat enum is used for distinction of export format used
 * by data porter objects
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public enum ExportDataFormat {
    /** ExportDataFormat values */
    JSON("json"), XML("xml");
    /** Data file String extension */
    private String fileExtension;

    /**
     * Base ExportDataFormat constructor
     *
     * @param fileExtension Data file extension
     */
    ExportDataFormat(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
