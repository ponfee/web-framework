package cn.ponfee.web.framework.dao;

import cn.ponfee.web.framework.model.Payment;

/**
 * The Payment Dao
 *
 * @author Ponfee
 */
public interface IPaymentDao {

    int add(Payment payment);

    String getNextOrderId(String orderIdPrefix);
}
