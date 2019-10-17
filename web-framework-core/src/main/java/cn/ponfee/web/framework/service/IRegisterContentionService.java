package cn.ponfee.web.framework.service;

import code.ponfee.commons.model.Result;

/**
 * The RegisterContention service interface
 *
 * @author Ponfee
 */
public interface IRegisterContentionService {

    /**
     * 获取或抢占
     * 
     * @param typ
     * @param key
     * @param values
     * @return
     */
    Result<String> getOrContend(String typ, String key, String[] values);

}
