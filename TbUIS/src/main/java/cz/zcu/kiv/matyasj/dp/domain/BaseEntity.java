package cz.zcu.kiv.matyasj.dp.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

/**
 * Objects of this class represent generic entity of db model.
 * These objects can be saved in the system database.
 * Creating a new domain object is simply an extension of this class.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@MappedSuperclass
public abstract class BaseEntity implements IEntity<Long>{
    /** Unique identification number for identification in the database */
    protected Long id;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method get and returns primary key of this object.
     * This primary key represents object in database.
     *
     * @return Long number - primary key
     */
    @Transient
    public Long getPK() {
        return getId();
    }

    /**
     * This method creates and returns string which represents this object.
     *
     * @return String which represent this object
     */
    public abstract String toString();
}
