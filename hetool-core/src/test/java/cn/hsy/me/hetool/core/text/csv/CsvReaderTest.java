package cn.hsy.me.hetool.core.text.csv;

import cn.hsy.me.hetool.core.io.resource.ResourceUtil;
import cn.hsy.me.hetool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

public class CsvReaderTest {
	
	@Test
	public void readTest() {
		CsvReader reader = new CsvReader();
		CsvData data = reader.read(ResourceUtil.getReader("test.csv", CharsetUtil.CHARSET_UTF_8));
		Assert.assertEquals("关注\"对象\"", data.getRow(0).get(2));
	}
}
