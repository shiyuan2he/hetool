package cn.hsy.me.hetool.thread.pool;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;
import org.junit.Test;

import java.util.stream.IntStream;
/**
 * @author heshiyuan
 */
public class ScheduledThreadPoolTest {
    private Log log = LogFactory.get();
    @Test
    public void getScheduledThreadPool() {
        IntStream.range(0, 100).forEach(i -> {
            ScheduledThreadPool.getInstance().getScheduledThreadPool()
                    .execute(() -> log.info("{}", i));
        });
    }
}