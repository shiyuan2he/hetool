package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;
import org.junit.Test;

import java.util.stream.IntStream;

/**
 * @author heshiyuan
 */
public class ThreadPoolTest {
    private Log log = LogFactory.get();

    @Test
    public void testFixedThreadPool(){
        long start = System.currentTimeMillis();
        IntStream.range(0, 100).forEach(i -> FixedThreadPool.getInstance(10).getFixedThreadPool().execute(() -> {}));
        long end = System.currentTimeMillis();
        // 137
        log.info("耗时：{}", (end - start));
        try {
            //延长主线程的执行时长
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testNewThread(){
        long start = System.currentTimeMillis();
        IntStream.range(0, 100).forEach(i -> {
            new Thread(() -> {});
        });
        long end = System.currentTimeMillis();
        //  4
        log.info("耗时：{}", (end - start));
        try {
            //延长主线程的执行时长
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}