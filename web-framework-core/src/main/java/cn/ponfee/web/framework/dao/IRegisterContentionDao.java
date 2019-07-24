package cn.ponfee.web.framework.dao;

import cn.ponfee.web.framework.model.RegisterContention;

/**
 * The RegisterContention Dao interface
 *
 * @author Ponfee
 */
public interface IRegisterContentionDao {

    int add(RegisterContention rc);

    String get(String type, String ckey);
}
