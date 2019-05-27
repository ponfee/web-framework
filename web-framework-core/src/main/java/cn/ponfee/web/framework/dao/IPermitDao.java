package cn.ponfee.web.framework.dao;

import java.util.List;

import cn.ponfee.web.framework.model.Permit;

/**
 * The Permit Dao
 *
 * @author Ponfee
 */
public interface IPermitDao {

    int add(Permit permit);

    int update(Permit permit);

    int delByPermitIds(List<String> ids);

    Permit getByPermitId(String id);

    List<Permit> queryAll();
}
