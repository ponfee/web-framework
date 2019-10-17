package cn.ponfee.web.framework.service;

import cn.ponfee.web.framework.model.Payment;
import code.ponfee.commons.model.Result;

/**
 * The PaymentService service interface
 *
 * @author Ponfee
 */
public interface IPaymentService {

    /**
     * Adds a payment
     * 
     * @param payment the payment
     * @return a string of orderId
     */
    Result<String> add(Payment payment);

}
