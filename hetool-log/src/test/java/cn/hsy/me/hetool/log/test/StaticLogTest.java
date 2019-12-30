package cn.hsy.me.hetool.log.test;

import cn.hsy.me.hetool.log.StaticLog;
import org.junit.Test;

public class StaticLogTest {
	@Test
	public void test() {
		StaticLog.debug("This is static {} log", "debug");
		StaticLog.info("This is static {} log", "info");
	}
}
