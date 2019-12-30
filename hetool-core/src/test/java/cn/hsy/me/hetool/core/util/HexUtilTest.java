package cn.hsy.me.hetool.core.util;

import cn.hsy.me.hetool.core.util.HexUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * HexUtil单元测试
 * @author heshiyuan
 *
 */
public class HexUtilTest {
	
	@Test
	public void hexStrTest(){
		String str = "我是一个字符串";
		
		String hex = HexUtil.encodeHexStr(str, CharsetUtil.CHARSET_UTF_8);
		String decodedStr = HexUtil.decodeHexStr(hex);
		
		Assert.assertEquals(str, decodedStr);
	}
	
	@Test
	public void toUnicodeHexTest() {
		String unicodeHex = HexUtil.toUnicodeHex('\u2001');
		Assert.assertEquals("\\u2001", unicodeHex);
		
		unicodeHex = HexUtil.toUnicodeHex('你');
		Assert.assertEquals("\\u4f60", unicodeHex);
	}
	
	@Test
	public void isHexNumberTest() {
		String a = "0x3544534F444";
		boolean isHex = HexUtil.isHexNumber(a);
		Assert.assertTrue(isHex);
	}
}
