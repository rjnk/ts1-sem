package cz.zcu.kiv.matyasj.dp.domain.users;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this domain class represent teachers in this system.
 * These objects have lists of currently taught subjects.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@DiscriminatorValue("TEACHER")
@XmlRootElement
public class Teacher extends User {
    /** List of taught subjects */
    private List<Subject> listOfTaughtSubjects;

    /**
     * Base Teacher constructor
     */
    public Teacher(){
        super();
        this.listOfTaughtSubjects = new ArrayList<>();
    }

    /**
     * Teacher constructor for testing purpose.
     *
     * @param firstName First name of teacher
     * @param lastName Last name of teacher
     * @param username Username of teacher
     * @param password Password string of teacher
     * @param email email of teacher
     */
    public Teacher(String firstName, String lastName, String username, String password, String email){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = 1;

        listOfTaughtSubjects = new ArrayList<>();
    }

    /*  --- GETTERS AND SETTERS --- */

    @ManyToMany(fetch = FetchType.EAGER, targetEntity=Subject.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "teacher_teaches_subjects", joinColumns = @JoinColumn(name = "teacher", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name= "subject", referencedColumnName = "id"))
    public List<Subject> getListOfTaughtSubjects() {
        return listOfTaughtSubjects;
    }

    public void setListOfTaughtSubjects(List<Subject> listOfTeachesSubjects) {
        this.listOfTaughtSubjects = listOfTeachesSubjects;
    }

    @Override
    public String toString() {
        return "Teacher [User="+super.toString()+", teached subjects=" + listOfTaughtSubjects.size()+ this.id + "]";
    }
}
