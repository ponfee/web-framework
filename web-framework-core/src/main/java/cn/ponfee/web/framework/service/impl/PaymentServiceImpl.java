package cn.ponfee.web.framework.service.impl;

import java.util.Date;
import java.util.stream.IntStream;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import cn.ponfee.web.framework.dao.IPaymentDao;
import cn.ponfee.web.framework.model.Payment;
import cn.ponfee.web.framework.service.IPaymentService;
import cn.ponfee.web.framework.service.IRegisterContentionService;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.util.Dates;
import code.ponfee.commons.util.Networks;

/**
 * The Payment service interface implementation
 *
 * @author Ponfee
 */
@Service("paymentService")
public class PaymentServiceImpl implements IPaymentService, InitializingBean {

    private static final Object LOCK = new Object();

    private static String serverId;
    private static String currentOrderId;

    private @Resource IRegisterContentionService rcService;
    private @Resource IPaymentDao paymentDao;

    /*@Override
    public Result<String> add(Payment payment) {
        payment.setOrderId(Dates.format(new Date(), "yyyyMMdd") + workId);
        dao.add(payment);
        return Result.success(payment.getOrderId());
    }*/

    @Override
    public Result<String> add(Payment payment) {
        payment.setOrderId(this.getNextOrderId());
        paymentDao.add(payment);
        return Result.success(payment.getOrderId());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String[] values = IntStream.range(0, 100).mapToObj(
            x -> StringUtils.leftPad(String.valueOf(x), 2, '0')
        ).toArray(String[]::new);
        serverId = rcService.getOrContend("PAY", Networks.getHostIp(), values).getData();
        if (StringUtils.isEmpty(serverId)) {
            throw new NullPointerException("Server id can't be null.");
        }
    }

    private String getNextOrderId() {
        synchronized (LOCK) {
            String date = Dates.format(new Date(), "yyyyMMdd");
            if (currentOrderId == null || !currentOrderId.startsWith(date, 0)) {
                currentOrderId = paymentDao.getNextOrderId(date + serverId);
            } else {
                int nextSeq = Integer.parseInt(currentOrderId.substring(10)) + 1;
                currentOrderId = currentOrderId.substring(0, 10) 
                               + StringUtils.leftPad(String.valueOf(nextSeq), 8, '0');
            }
            return currentOrderId;
        }
    }

}
