package cn.hsy.me.hetool.core.swing;

import cn.hsy.me.hetool.core.io.FileUtil;
import org.junit.Ignore;
import org.junit.Test;

public class RobotUtilTest {

	@Test
	@Ignore
	public void captureScreenTest() {
		RobotUtil.captureScreen(FileUtil.file("e:/screen.jpg"));
	}
}
