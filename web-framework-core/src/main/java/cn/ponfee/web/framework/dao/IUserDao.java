package cn.ponfee.web.framework.dao;

import java.util.List;
import java.util.Map;

import cn.ponfee.web.framework.model.User;
import code.ponfee.commons.model.Page;

/**
 * The database operator interface for user table
 *
 * @author Ponfee
 */
public interface IUserDao {

    int batchAdd(List<User> userList);

    int update(User user);

    int changeStatus(long id, long updateBy, int status);

    int logicDelete(long[] uids, long updateBy);

    boolean checkUsernameIsExists(String username);

    User getByUsername(String username);

    User getById(Long userId);

    Page<Map<String, Object>> query4page(Map<String, ?> params);

}
