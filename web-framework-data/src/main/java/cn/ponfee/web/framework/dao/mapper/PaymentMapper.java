package cn.ponfee.web.framework.dao.mapper;

import cn.ponfee.web.framework.model.Payment;

public interface PaymentMapper {

    int insert(Payment payment);

    String getNextOrderId(String orderIdPrefix);
}