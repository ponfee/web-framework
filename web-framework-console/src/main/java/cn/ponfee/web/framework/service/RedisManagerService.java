package cn.ponfee.web.framework.service;

import cn.ponfee.web.framework.model.RedisKey;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;

/**
 * Redis manager service interface
 * 
 * @author Ponfee
 */
public interface RedisManagerService {

    Page<RedisKey> query4page(PageRequestParams params);

    void addOrUpdateRedisEntry(String key, String value, Long expire, 
                               String dataType, String valueType);

    void delete(String... keys);

    void refresh();

}
