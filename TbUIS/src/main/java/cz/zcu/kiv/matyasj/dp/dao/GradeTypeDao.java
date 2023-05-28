package cz.zcu.kiv.matyasj.dp.dao;

import cz.zcu.kiv.matyasj.dp.domain.university.GradeType;

import java.util.List;

/**
 * This interface defines method of DAOs for manipulation with GradeType data in database.
 *
 * @author Jiri Matyas
 * @version 2019-24-11
*/
public interface GradeTypeDao extends GenericDao<GradeType, Long>{
    /**
     * This method returns all type of grades in system (e.g. typically A, B, C, D, E, F).
     *
     * @return list of grade types
     */
    List<GradeType> getAllGradeTypes();
}
