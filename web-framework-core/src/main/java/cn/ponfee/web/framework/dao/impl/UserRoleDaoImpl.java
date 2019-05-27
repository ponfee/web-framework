package cn.ponfee.web.framework.dao.impl;

import static code.ponfee.commons.util.MessageFormats.format;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IUserRoleDao;
import cn.ponfee.web.framework.dao.mapper.UserRoleMapper;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.UserRole;
import cn.ponfee.web.framework.util.Constants;

import code.ponfee.commons.jedis.JedisClient;

/**
 * The IUserRoleDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class UserRoleDaoImpl implements IUserRoleDao {

    private static final int CACHE_PERM_TM = 7200; // 权限缓存有效时间为2小时
    private static final String KEY_PREFIX = "role:cache:";
    private static final String PERMIT_KEY_PREFIX = KEY_PREFIX + "pem:#{uid}";

    private @Resource UserRoleMapper mapper;
    private @Resource JedisClient jedis;

    @Override
    public int add(List<UserRole> list) {
        int rows = mapper.insert(list);
        if (rows >= Constants.ONE_ROW_AFFECTED) {
            for (UserRole ur : list) {
                jedis.keysOps().del(format(PERMIT_KEY_PREFIX, ur.getUserId()));
            }
        }
        return rows;
    }

    @Override
    public int delByUserId(long userId) {
        int rows = mapper.delByUserId(userId);
        if (rows >= Constants.ONE_ROW_AFFECTED) {
            jedis.keysOps().del(format(PERMIT_KEY_PREFIX, userId));
        }
        return rows;
    }

    @Override
    public int delByRoleId(long roleId) {
        return mapper.delByRoleId(roleId);
    }

    @Override
    public List<Role> queryByUserId(long userId) {
        return mapper.queryByUserId(userId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> queryUserPermits(long userId) {
        String key = format(PERMIT_KEY_PREFIX, userId);
        List<String> permits = jedis.valueOps().getObject(key, ArrayList.class);
        if (permits == null) {
            // 缓存未命中
            permits = mapper.queryUserPermits(userId);
            jedis.valueOps().setObject(key, permits, CACHE_PERM_TM);
        }
        return permits;
    }
}
