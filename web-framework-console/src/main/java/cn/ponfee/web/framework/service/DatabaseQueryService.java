package cn.ponfee.web.framework.service;

import java.util.LinkedHashMap;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;

/**
 * Database dynamic query service interface
 * 
 * @author Ponfee
 */
public interface DatabaseQueryService {

    Page<LinkedHashMap<String, Object>> query4page(PageRequestParams params);

}
