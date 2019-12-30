package cn.hsy.me.hetool.core.lang;

import cn.hsy.me.hetool.core.text.Simhash;
import cn.hsy.me.hetool.core.util.StrUtil;
import org.junit.Assert;
import org.junit.Test;

public class SimhashTest {
	
	@Test
	public void simTest() {
		String text1 = "我是 一个 普通 字符串";
		String text2 = "我是 一个 普通 字符串";
		
		Simhash simhash = new Simhash();
		long hash = simhash.hash(StrUtil.split(text1, ' '));
		Assert.assertTrue(hash != 0);
		
		simhash.store(hash);
		boolean duplicate = simhash.equals(StrUtil.split(text2, ' '));
		Assert.assertTrue(duplicate);
	}
}
