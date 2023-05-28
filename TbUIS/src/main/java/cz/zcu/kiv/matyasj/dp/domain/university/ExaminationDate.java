package cz.zcu.kiv.matyasj.dp.domain.university;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Objects of this domain class represent examination dates in this system.
 * These objects have reference of teacher who created exam date, subject for which is exam date created and
 * list of participants of term.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@Table(name="examination_dates")
@XmlRootElement
public class ExaminationDate extends BaseEntity{
    /** Date of this exam date */
    private Date dateOfTest;
    /** List of (Students) participants of this exam date */
    private List<Student> participants;
    /** List of (Grades) grades on this exam date */
    private List<Grade> grades;
    /** Teacher who creates this exam date. */
    private Teacher teacher;
    /** For this subject is exam date created. */
    private Subject subject;
    /** Maximal number of participants on this exam date. */
    private int maxParticipants;

    /**
     * Default constructor for creating Examination dates.
     * This constructor (without parameter) is used by hibernate.
     */
    public ExaminationDate(){
        super();
        participants = new ArrayList<>();
    }

    /**
     * Constructor for creating Examination dates.
     * This constructor can be used for testing.
     *
     * @param dateOfTest Date of this exam date.
     * @param maxParticipants Maximal number of participants
     */
    public ExaminationDate(Date dateOfTest, int maxParticipants){
        super();
        this.dateOfTest = dateOfTest;
        this.maxParticipants = maxParticipants;

        participants = new ArrayList<>();
    }

    /*  --- GETTERS AND SETTERS --- */

    public Date getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(Date dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    @ManyToMany(fetch = FetchType.EAGER, targetEntity=Student.class, cascade={ CascadeType.MERGE, CascadeType.PERSIST })
    @JoinTable(name = "examination_dates_participants", joinColumns = @JoinColumn(name = "examdate", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name= "student", referencedColumnName = "id"))
    @Fetch(FetchMode.SELECT)
    public List<Student> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Student> participants) {
        this.participants = participants;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    // Cascade Remove i necessary for correct DB restore
    // Default mechanism for deletion use wrong order of DAO objects
    @OneToMany(fetch = FetchType.EAGER,
            mappedBy = "testWhereWasGradeGranted",
            targetEntity = Grade.class,
            cascade = {CascadeType.REMOVE})
    @Fetch(FetchMode.SELECT)
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    @Override
    public String toString() {
        return "ExaminationDate{" +
                "dateOfTest=" + dateOfTest +
                ", participants=" + participants +
                ", teacher=" + teacher +
                ", subject=" + subject +
                ", maxParticipants=" + maxParticipants +
                '}';
    }
}
