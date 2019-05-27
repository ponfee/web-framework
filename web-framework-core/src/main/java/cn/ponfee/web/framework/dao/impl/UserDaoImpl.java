package cn.ponfee.web.framework.dao.impl;

import static code.ponfee.commons.util.MessageFormats.format;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IUserDao;
import cn.ponfee.web.framework.dao.mapper.UserMapper;
import cn.ponfee.web.framework.dao.mapper.UserRoleMapper;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.util.Constants;
import code.ponfee.commons.jedis.JedisClient;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/**
 * The IUserDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class UserDaoImpl implements IUserDao {

    private static final int CACHE_USER_TM = 86400;
    private static final String KEY_PREFIX = "user:cache:";
    private static final String UNAME_KEY_PREFIX = KEY_PREFIX + "unm:#{uname}";

    private @Resource UserMapper mapper;
    private @Resource UserRoleMapper userRolemapper;
    private @Resource JedisClient jedis;

    @Override
    public int batchAdd(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)) {
            return 0;
        }
        return mapper.batchInsert(userList);
    }

    @Override
    public int update(User user) {
        int rows = mapper.update(user);
        invalidUserCache(user.getId());
        return rows;
    }

    @Override
    public int changeStatus(long uid, long updateBy, int status) {
        int rows = mapper.changeStatus(uid, updateBy, status);
        invalidUserCache(uid);
        return rows;
    }

    @Override
    public int logicDelete(long[] uids, long updateBy) {
        int rows = mapper.logicDelete(uids, updateBy);
        if (rows >= Constants.ONE_ROW_AFFECTED) {
            for (long uid : uids) {
                invalidUserCache(uid);
            }
        }
        return rows;
    }

    @Override
    public boolean checkUsernameIsExists(String username) {
        return mapper.checkUsernameIsExists(username);
    }

    @Override
    public User getByUsername(String username) {
        String key = format(UNAME_KEY_PREFIX, username);
        User user = jedis.valueOps().getObject(key, User.class, CACHE_USER_TM);
        if (user == null) {
            user = mapper.getByUsername(username);
            if (user != null) {
                user.setRoles(userRolemapper.queryByUserId(user.getId()));
                jedis.valueOps().setObject(key, user, CACHE_USER_TM);
            }
        }
        return user;
    }

    @Override public User getById(Long userId) {
        return mapper.getById(userId);
    }

    @Override
    public Page<Map<String, Object>> query4page(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return new Page<>(mapper.query4list(params));
    }

    private void invalidUserCache(long userId) {
        User user = mapper.getById(userId);
        if (user != null) {
            jedis.keysOps().del(format(UNAME_KEY_PREFIX, user.getUsername()));
        }
    }

}
