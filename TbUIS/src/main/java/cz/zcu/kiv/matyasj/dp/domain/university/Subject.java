package cz.zcu.kiv.matyasj.dp.domain.university;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Objects of this domain class represent subjects in this system.
 * These objects have lists of students who are studying this subject, students who absolved this subject
 * and Teachers whose taught this subject.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@Table(name="subjects")
@XmlRootElement
public class Subject extends BaseEntity{
    /** Name of this subject */
    private String name;
    /** Number of credits for this subject */
    private int creditRating;
    /** List of teachers who taught this subject */
    private List<Teacher> teachers;
    /** List of students who are studying this subject */
    private List<Student> listOfStudents;
    /** List of students who absolved this subject */
    private List<Student> listOfAbsolvents;

    /**
     * Default Subject constructor
     */
    public Subject(){
        super();
        /* Init lists */
        teachers = new ArrayList<>();
        listOfStudents = new ArrayList<>();
        listOfAbsolvents = new ArrayList<>();
    }

    /**
     * Constructor for testing purpose.
     *
     * @param name Name of Subject
     * @param creditRating number of credits for this subject
     */
    public Subject(String name, int creditRating){
        super();

        this.name = name;
        this.creditRating = creditRating;
        /* Init lists */
        teachers = new ArrayList<>();
        listOfStudents = new ArrayList<>();
        listOfAbsolvents = new ArrayList<>();
    }

    /*  --- GETTERS AND SETTERS --- */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(int creditRating) {
        this.creditRating = creditRating;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "listOfTaughtSubjects", targetEntity = Teacher.class, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.SELECT)
    @XmlTransient
    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "listOfLearnedSubjects", targetEntity = Student.class , cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.SELECT)
    @XmlTransient
    public List<Student> getListOfStudents() {
        return listOfStudents;
    }

    public void setListOfStudents(List<Student> listOfStudents) {
        this.listOfStudents = listOfStudents;
    }

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "listOfAbsolvedSubjects", targetEntity = Student.class, cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @Fetch(FetchMode.SELECT)
    @XmlTransient
    public List<Student> getListOfAbsolvents() {
        return listOfAbsolvents;
    }

    public void setListOfAbsolvents(List<Student> listOfAbsolvents) {
        this.listOfAbsolvents = listOfAbsolvents;
    }

    @Override
    public String toString() {
        return "Subject[id="+getId()+", name="+getName()+", creditRating="+getCreditRating()+", teachersSize="+getTeachers().size()+", studentsSize="+getListOfStudents().size()+", absolventsSize="+getListOfAbsolvents().size()+"]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return  id.equals(subject.id) &&
                creditRating == subject.creditRating &&
                Objects.equals(name, subject.name);
    }
}