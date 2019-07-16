package cn.ponfee.web.framework.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import cn.ponfee.web.framework.dao.IRegisterContentionDao;
import cn.ponfee.web.framework.model.RegisterContention;
import cn.ponfee.web.framework.service.IRegisterContentionService;
import code.ponfee.commons.model.Result;

/**
 * The RegisterContention service interface implementation
 * 
 * @author Ponfee
 */
@Service("registerContentionService")
public class RegisterContentionServiceImpl implements IRegisterContentionService {

    private @Resource IRegisterContentionDao dao;
    private @Resource PlatformTransactionManager txManager; // DataSourceTransactionManager

    @Override
    public Result<String> getOrContend(String attr, String key, String[] values) {
        String val = dao.get(attr, key);
        if (val != null) {
            return Result.success(val);
        }

        List<String> vals = Arrays.asList(values);
        Collections.shuffle(vals);
        for (String v : vals) {
            TransactionStatus status = txManager.getTransaction(
                new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW) // 开启新事务
            );
            try {
                dao.add(new RegisterContention(attr, key, v));
                txManager.commit(status);
                val = v;
                break;
            } catch (Exception e) {
                txManager.rollback(status);
            }
        }

        return Result.success(val);
    }

}
