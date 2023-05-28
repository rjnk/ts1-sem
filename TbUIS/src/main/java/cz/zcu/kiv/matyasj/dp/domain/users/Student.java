package cz.zcu.kiv.matyasj.dp.domain.users;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this domain class represent students in this system.
 * These objects have lists of currently studied subjects and already
 * absolved subjects.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@DiscriminatorValue("STUDENT")
@XmlRootElement
public class Student extends User{
    /** List of studied subjects */
    private List<Subject> listOfLearnedSubjects;
    /** List of absolved subjets */
    private List<Subject> listOfAbsolvedSubjects;

    /**
     * Default Student constructor
     */
    public Student(){
        super();
        listOfLearnedSubjects = new ArrayList<>();
        listOfAbsolvedSubjects = new ArrayList<>();
    }

    /**
     * Student constructor for testing purpose.
     *
     * @param firstName First name of student
     * @param lastName Last name of student
     * @param username Username of student
     * @param password Password string of student
     * @param email email of student
     */
    public Student(String firstName, String lastName, String username, String password, String email){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.email = email;
        this.enabled = 1;

        listOfLearnedSubjects = new ArrayList<>();
        listOfAbsolvedSubjects = new ArrayList<>();
    }

    /*  --- GETTERS AND SETTERS --- */

    @ManyToMany(fetch = FetchType.EAGER, targetEntity=Subject.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "student_learned_subjects", joinColumns = @JoinColumn(name = "student", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name= "subject", referencedColumnName = "id"))
    @Fetch(FetchMode.SELECT)
    public List<Subject> getListOfLearnedSubjects() {
        return listOfLearnedSubjects;
    }

    public void setListOfLearnedSubjects(List<Subject> listOfLearnedSubjects) {
        this.listOfLearnedSubjects = listOfLearnedSubjects;
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity=Subject.class, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable(name = "student_absolved_subjects", joinColumns = @JoinColumn(name = "student", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name= "subject", referencedColumnName = "id"))
    @Fetch(FetchMode.SELECT)
    public List<Subject> getListOfAbsolvedSubjects() {
        return listOfAbsolvedSubjects;
    }

    public void setListOfAbsolvedSubjects(List<Subject> listOfAbsolvedSubjects) {
        this.listOfAbsolvedSubjects = listOfAbsolvedSubjects;
    }

    @Override
    public String toString() {
        return "Student [User="+super.toString()+", absolvedSubjects=" + listOfAbsolvedSubjects + ", learned subjects="+listOfLearnedSubjects+"]";
    }

    @Override
    public int hashCode() {
        int result = listOfLearnedSubjects != null ? listOfLearnedSubjects.hashCode() : 0;
        result = 31 * result + (listOfAbsolvedSubjects != null ? listOfAbsolvedSubjects.hashCode() : 0);
        result = (int) (getId() * 5);
        return result;
    }
}
