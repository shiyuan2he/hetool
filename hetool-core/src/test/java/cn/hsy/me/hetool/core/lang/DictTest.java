package cn.hsy.me.hetool.core.lang;

import cn.hsy.me.hetool.core.date.DateTime;
import cn.hsy.me.hetool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;

public class DictTest {
	@Test
	public void dictTest(){
		Dict dict = Dict.create()
				.set("key1", 1)//int
				.set("key2", 1000L)//long
				.set("key3", DateTime.now());//Date
		
		Long v2 = dict.getLong("key2");
		Assert.assertEquals(Long.valueOf(1000L), v2);
	}
}
