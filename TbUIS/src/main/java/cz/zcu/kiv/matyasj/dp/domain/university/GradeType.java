package cz.zcu.kiv.matyasj.dp.domain.university;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Instance of this domain class represent types of grade. GradeType objects are used
 * for new evaluation creating.
 * Typically used GradeTypes are: A, B, C, D, E, F
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@Table(name="grade_types")
@XmlRootElement
public class GradeType extends BaseEntity {
    /** String name of GradeType (e.g. "A")*/
    private String name;

    /*  --- GETTERS AND SETTERS --- */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GradeType{" +
                "name=" + name +
               '}';
    }
}
