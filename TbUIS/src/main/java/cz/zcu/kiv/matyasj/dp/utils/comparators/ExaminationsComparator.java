package cz.zcu.kiv.matyasj.dp.utils.comparators;

import cz.zcu.kiv.matyasj.dp.domain.university.ExaminationDate;

/**
 * Class holds static methods to compare two Examination dates
 *
 * @author Radek Vais
 * @version 2019-02-02
 */
public class ExaminationsComparator {

    /**
     * Method compares two examination dates by name and date.
     * First criteria is aplphabet of name.
     * Second criteria is date of exam.
     * @param ed1 first exam date to compare
     * @param ed2 second exam date to compare
     * @return the value 0 if exam dates are equal;
     *         a value less than 0 if name of ed1 is lexicographically less than the name ed2;
     *         and a value greater than 0 if name of ed1 is lexicographically greater than the name of ed2 argument.
     */
    public static int nameAscDateAsc (ExaminationDate ed1, ExaminationDate ed2 ) {
       int result = SubjectsComparator.NameAsc(ed1.getSubject(), ed2.getSubject());
       if (result == 0){
           result = ed1.getDateOfTest().compareTo(ed2.getDateOfTest());
       }
        return result;
    }

    /**
     * Default sort method for Examination Dates
     * Method compares two examination dates by name and date.
     * First criteria is aplphabet of name.
     * Second criteria is date of exam.
     * @param ed1 first exam date to compare
     * @param ed2 second exam date to compare
     * @return the value 0 if exam dates are equal;
     *         a value less than 0 if name of ed1 is lexicographically less than the name ed2;
     *         and a value greater than 0 if name of ed1 is lexicographically greater than the name of ed2 argument.
     */
    public static int defaultOrder(ExaminationDate ed1, ExaminationDate ed2){
        return nameAscDateAsc(ed1, ed2);
    }
}
