package cn.ponfee.web.framework.service.impl;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;

import cn.ponfee.web.framework.service.DatabaseQueryService;
import code.ponfee.commons.data.MultipleDataSourceContext;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.mybatis.SqlMapper;

/**
 * Database dynamic query service implementation
 * 
 * @author Ponfee
 */
@Service
public class DatabaseQueryServiceImpl implements DatabaseQueryService {

    private @Resource SqlMapper sqlMapper;

    @SuppressWarnings("unchecked")
    @Override
    public Page<LinkedHashMap<String, Object>> query4page(PageRequestParams params) {
        String sql, datasource;
        if (StringUtils.isBlank(sql = params.getString("sql"))) {
            return Page.of(Collections.emptyList());
        }

        PageHelper.startPage(params.getPageNum(), params.getPageSize());
        try {
            if (StringUtils.isNotBlank(datasource = params.getString("datasource"))) {
                MultipleDataSourceContext.set(datasource);
            }

            List<?> list = sqlMapper.selectList(sql, LinkedHashMap.class);
            return Page.of((List<LinkedHashMap<String, Object>>) list);
        } finally {
            MultipleDataSourceContext.remove();
        }
    }

}
