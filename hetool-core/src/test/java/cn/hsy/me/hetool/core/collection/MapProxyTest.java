package cn.hsy.me.hetool.core.collection;

import cn.hsy.me.hetool.core.map.MapProxy;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class MapProxyTest {
	
	@Test
	public void mapProxyTest() {
		Map<String, String> map = new HashMap<>();
		map.put("a", "1");
		map.put("b", "2");
		
		MapProxy mapProxy = new MapProxy(map);
		Integer b = mapProxy.getInt("b");
		Assert.assertEquals(new Integer(2), b);
		
		Set<Object> keys = mapProxy.keySet();
		Assert.assertFalse(keys.isEmpty());
		
		Set<Entry<Object, Object>> entrys = mapProxy.entrySet();
		Assert.assertFalse(entrys.isEmpty());
	}
}
