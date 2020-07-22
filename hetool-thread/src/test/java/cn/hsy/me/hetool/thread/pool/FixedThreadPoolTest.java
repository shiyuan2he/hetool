package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author heshiyuan
 */
public class FixedThreadPoolTest {
    private Log log = LogFactory.get();

    @Test
    public void testFixedThreadPool(){
        IntStream.range(0, 100).forEach(i -> {
            long start = System.currentTimeMillis();
            FixedThreadPool.getInstance(10).getFixedThreadPool().execute(
                () -> {
                    try {
                        // 延长每个线程的执行时间，防止CachedThreadPool重复使用线程
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    log.info("{} -> threadId：{}, threadName:{}", i,
                            Thread.currentThread().getId(), Thread.currentThread().getName());
                }
            );
            long end = System.currentTimeMillis();
            log.info("耗时：{}", (end - start));
        });
        try {
            //延长主线程的执行时长
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testNewThread(){
        long start = System.currentTimeMillis();
        IntStream.range(0, 100).forEach(i -> {
            new Thread(() -> {
                try {
                    // 延长每个线程的执行时间，防止CachedThreadPool重复使用线程
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info("{} -> threadId：{}, threadName:{}", i,
                        Thread.currentThread().getId(), Thread.currentThread().getName());
            });
        });
        long end = System.currentTimeMillis();
        log.info("耗时：{}", (end - start));
    }
}