package cz.zcu.kiv.matyasj.dp.domain;

import java.io.Serializable;

/**
 * This interface is used to create domain classes.
 *
 * @param <PK> type of primary key
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface IEntity<PK extends Serializable> extends Serializable {
    /**
     * This method get and returns primary key of this object.
     * This primary key represents object in database.
     *
     * @return Long number - primary key
     */
    PK getPK();
}
