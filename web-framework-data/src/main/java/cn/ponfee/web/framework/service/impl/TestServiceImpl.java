package cn.ponfee.web.framework.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.ponfee.web.framework.dao.mapper.TestMapper;
import cn.ponfee.web.framework.service.TestService;
import code.ponfee.commons.data.DataSourceNaming;

@Service
public class TestServiceImpl implements TestService {

    @Resource
    private TestMapper mapper;

    //@Transactional
    @DataSourceNaming("#root[0]") // #root=arg[]
    @Override
    public void testTransaction(String datasource, boolean bool) {
        mapper.delete(datasource, "1");
        if (bool) {
            System.out.println(1 / 0);
        }
        mapper.delete(datasource, "2");
    }

}
