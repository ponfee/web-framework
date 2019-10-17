package cn.ponfee.web.framework.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IRegisterContentionDao;
import cn.ponfee.web.framework.dao.mapper.RegisterContentionMapper;
import cn.ponfee.web.framework.model.RegisterContention;

/**
 * The RegisterContentionDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class RegisterContentionDaoImpl implements IRegisterContentionDao {

    private @Resource RegisterContentionMapper mapper;

    @Override
    public int add(RegisterContention rc) {
        return mapper.insert(rc);
    }

    @Override
    public String get(String typ, String key) {
        return mapper.get(typ, key);
    }

}
