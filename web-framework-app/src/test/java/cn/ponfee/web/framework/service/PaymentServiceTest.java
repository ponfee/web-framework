/* 
 * Copyright (c) 2019, S.F. Express Inc. All rights reserved.
 */
package cn.ponfee.web.framework.service;

import static org.junit.Assert.assertNotNull;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.ponfee.web.framework.BaseTest;
import cn.ponfee.web.framework.model.Payment;
import code.ponfee.commons.util.Dates;

@RunWith(SpringJUnit4ClassRunner.class)
public class PaymentServiceTest extends BaseTest<IPaymentService> {

    @Test
    public void add1() {
        Payment p = new Payment();
        p.setUserId(123L);
        p.setOrderNo("0");
        p.setOrderAmt(1L);
        p.setBizType(1);
        p.setStatus(0);
        assertNotNull(getBean().add(p));
        System.out.println(p.getOrderId());
        System.out.println(p.getId());
    }

    @Test
    public void add2() throws InterruptedException {
        AtomicBoolean flag = new AtomicBoolean(true);
        int count = 10;
        Thread[] ts = new Thread[count];
        for (int i = 0; i < count; i++) {
            ts[i] = new AThread(i, flag);
        }
        for (int i = 0; i < count; i++) {
            ts[i].start();
        }

        Thread.sleep(10000);
        flag.set(false);
        for (int i = 0; i < count; i++) {
            ts[i].join();
        }
        System.out.println("==========================End==========================");
    }

    private class AThread extends Thread {
        private final AtomicBoolean flag;

        private AThread(int workId, AtomicBoolean flag) {
            this.flag = flag;
        }

        @Override
        public void run() {
            Payment p = new Payment();
            while (flag.get()) {
                p.setUserId(123L);
                p.setOrderNo("0");
                p.setBizType(1);
                p.setOrderAmt(1L);
                p.setStatus(0);
                getBean().add(p);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(Dates.format(new Date(), "'D'yyyyMMdd'1'"));
    }
}
