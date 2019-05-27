package cn.ponfee.web.framework.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IPermitDao;
import cn.ponfee.web.framework.dao.mapper.PermitMapper;
import cn.ponfee.web.framework.model.Permit;

import code.ponfee.commons.jedis.JedisClient;

/**
 * The IPermitDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class PermitDaoImpl implements IPermitDao {

    private @Resource PermitMapper mapper;
    private @Resource JedisClient jedisClient;

    @Override
    public int add(Permit permit) {
        return mapper.insert(permit);
    }

    @Override
    public int update(Permit permit) {
        return mapper.update(permit);
    }

    @Override
    public int delByPermitIds(List<String> ids) {
        return mapper.deleteByPermitIds(ids);
    }

    @Override
    public Permit getByPermitId(String id) {
        return mapper.getByPermitId(id);
    }

    @Override
    public List<Permit> queryAll() {
        return mapper.listAll();
    }

}
