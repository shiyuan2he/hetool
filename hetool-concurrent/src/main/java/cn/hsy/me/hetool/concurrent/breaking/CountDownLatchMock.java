package cn.hsy.me.hetool.concurrent.breaking;
import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author heshiyuan
 * <p></p>
 * @date 2018/1/29 15:55
 * Copyright (c) 2018 shiyuan4work@sina.com All rights reserved.
 */
public abstract class CountDownLatchMock {
    Log log = LogFactory.get(CountDownLatchMock.class);
    /**
     * @description <p>并发模拟</p>
     * @author heshiyuan
     * @date 2018/1/29 15:58
     */
     public void execute() throws InterruptedException {
        // 并发数
        int concurrencyNumber = getConcurrentNo();
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch latch2 = new CountDownLatch(10);
        for (int i = 0; i < concurrencyNumber; i++) {
            new Thread(() -> {
                try {
                    log.info("线程{}到达闸门", Thread.currentThread().getName());
                    latch2.countDown();
                    latch.await();
                    doMethod();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        latch2.await();
        log.info("开闸，线程抢占开始");
        latch.countDown();
        if(0 != getSleepTime()){
            Thread.sleep(getSleepTime());
        }else{
            Thread.sleep(10000);
        }
        log.info("所有线程执行完毕...");
    }

    /**
     * 获取并发数
     * @return 并发数量
     * @author heshiyuan
     * @date 2018/1/29 15:56
     */
    public abstract int getConcurrentNo() ;

    /**
     * 被执行的方法体
     * @author heshiyuan
     * @date 2018/1/29 16:04
     */
    public abstract void doMethod();
    /**
     * <p>获取主线程休眠时间</p>
     * @return 主线程休眠时间
     * @author heshiyuan
     * @date 2018/1/29 16:06
     */
    public abstract int getSleepTime() ;
}
