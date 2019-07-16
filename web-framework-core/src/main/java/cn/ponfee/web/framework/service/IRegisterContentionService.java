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
     * @param attr
     * @param key
     * @param values
     * @return
     */
    Result<String> getOrContend(String attr, String key, String[] values);

}
