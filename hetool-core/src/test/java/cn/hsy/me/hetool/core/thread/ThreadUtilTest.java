package cn.hsy.me.hetool.core.thread;

import org.junit.Assert;
import org.junit.Test;

public class ThreadUtilTest {
	
	@Test
	public void executeTest() {
		final boolean isValid = true;
		
		ThreadUtil.execute(new Runnable() {
			
			@Override
			public void run() {
				Assert.assertTrue(isValid);
			}
		});
		
	}
}
