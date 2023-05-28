package cz.zcu.kiv.matyasj.dp.utils.dataporter.dataTypes;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Objects of this class are used as wrappers for database data during export/ipmort.
 * DataContainer contains list of domain entities which are consistent with
 * system domain model. This container can be processed using the JAXB library
 * and used as a root element.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@XmlRootElement
public class DataContainer {
    /** List of domain objects from system domain model */
    private ArrayList<BaseEntity> entities;

    /** Base DataContainer constructor */
    public DataContainer() {
        entities = new ArrayList<>();
    }

    /** Entities list getter */
    public ArrayList<BaseEntity> getEntities() {
        return entities;
    }

    /**
     * Entities list setter
     */
    @XmlElementWrapper(name = "entities")
    @XmlElementRef(name = "entity")
    public void setEntities(ArrayList<BaseEntity> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (BaseEntity e : entities) {
            sb.append(e.getClass().getName()).append(" - ").append(e);
        }
        return sb.toString();
    }
}
