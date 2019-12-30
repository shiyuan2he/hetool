package cn.hsy.me.hetool.core.thread;

import cn.hsy.me.hetool.core.lang.Console;
import cn.hsy.me.hetool.core.util.RandomUtil;
import org.junit.Ignore;
import org.junit.Test;

public class ConcurrencyTesterTest {

	@Test
	@Ignore
	public void concurrencyTesterTest() {
		ConcurrencyTester tester = ThreadUtil.concurrencyTest(100, new Runnable() {

			@Override
			public void run() {
				long delay = RandomUtil.randomLong(100, 1000);
				ThreadUtil.sleep(delay);
				Console.log("{} test finished, delay: {}", Thread.currentThread().getName(), delay);
			}
		});
		Console.log(tester.getInterval());
	}
}
