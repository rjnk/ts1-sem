package cz.zcu.kiv.matyasj.dp.domain.university;

import cz.zcu.kiv.matyasj.dp.domain.BaseEntity;
import cz.zcu.kiv.matyasj.dp.domain.users.Student;
import cz.zcu.kiv.matyasj.dp.domain.users.Teacher;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

/**
 * Objects of this domain class represent Grades in this system.
 * These objects have reference to the teacher who grade granted, student who was evaluated
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
@Entity
@Table(name="grades")
@XmlRootElement
public class Grade extends BaseEntity {
    /** Type of grade (A, B, C, ...)*/
    private GradeType typeOfGrade;
    /** Date and time of evaluation */
    private Date dayOfGrant;
    /** Evaluation is for this student */
    private Student owner;
    /** Evaluation is created by this Teacher */
    private Teacher whoGradeGranted;
    /** Exam date where is evaluation created */
    private ExaminationDate testWhereWasGradeGranted;
    /** Grade is created on this subject */
    private Subject subject;

    /*  --- GETTERS AND SETTERS --- */

    @ManyToOne(fetch = FetchType.EAGER)
    public GradeType getTypeOfGrade() {
        return typeOfGrade;
    }

    public void setTypeOfGrade(GradeType typeOfGrade) {
        this.typeOfGrade = typeOfGrade;
    }

    public Date getDayOfGrant() {
        return dayOfGrant;
    }

    public void setDayOfGrant(Date dayOfGrant) {
        this.dayOfGrant = dayOfGrant;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Student getOwner() {
        return owner;
    }

    public void setOwner(Student owner) {
        this.owner = owner;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Teacher getWhoGradeGranted() {
        return whoGradeGranted;
    }

    public void setWhoGradeGranted(Teacher whoGradeGranted) {
        this.whoGradeGranted = whoGradeGranted;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public ExaminationDate getTestWhereWasGradeGranted() {
        return testWhereWasGradeGranted;
    }

    public void setTestWhereWasGradeGranted(ExaminationDate testWhereWasGradeGranted) {
        this.testWhereWasGradeGranted = testWhereWasGradeGranted;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return "Grade{" +
                " Id=" + id +
                ", GradeType=" + typeOfGrade +
                ", dayOfGrant=" + dayOfGrant+
                ", owner=" + owner+
                ", whoGradeGranted=" + whoGradeGranted+
                ", subject=" + subject+
                '}';
    }
}
