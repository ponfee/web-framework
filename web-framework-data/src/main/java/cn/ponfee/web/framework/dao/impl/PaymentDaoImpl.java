package cn.ponfee.web.framework.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IPaymentDao;
import cn.ponfee.web.framework.dao.mapper.PaymentMapper;
import cn.ponfee.web.framework.model.Payment;

/**
 * The IPaymentDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class PaymentDaoImpl implements IPaymentDao {

    private @Resource PaymentMapper mapper;

    @Override
    public int add(Payment payment) {
        return mapper.insert(payment);
    }

    @Override
    public String getNextOrderId(String orderIdPrefix) {
        return mapper.getNextOrderId(orderIdPrefix);
    }

}
