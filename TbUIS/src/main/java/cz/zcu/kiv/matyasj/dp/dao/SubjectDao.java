package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.university.Subject;

import java.util.List;

/**
 * This interface defines methods of DAOs for manipulation with Subject data in database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface SubjectDao extends GenericDao<Subject, Long>{
    /**
     * This method finds and returns list of all subject without selected subjects.
     *
     * @param excludedSubjects list of excluded subject.
     * @return List of Subjects
     */
    List<Subject> getSubjectsExceptSelected(List<Subject> excludedSubjects);
}
