package cn.ponfee.web.framework.service.impl;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
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
    public Result<String> getOrContend(String typ, String key, String[] values) {
        String val = dao.get(typ, key);
        if (val != null) {
            return Result.success(val);
        }

        List<String> vals = Arrays.asList(values);
        //Collections.shuffle(vals);
        for (String v : vals) {
            TransactionStatus status = txManager.getTransaction(
                new DefaultTransactionDefinition(PROPAGATION_REQUIRES_NEW) // 开启新事务
            );
            try {
                dao.add(new RegisterContention(typ, key, v));
                txManager.commit(status);
                val = v;
                break;
            } catch (Exception e) {
                txManager.rollback(status);
            }
        }

        if (val == null) {
            val = dao.get(typ, key);
        }

        return Result.success(val);
    }

}
