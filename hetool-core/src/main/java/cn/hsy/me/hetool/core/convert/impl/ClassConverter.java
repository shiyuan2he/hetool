package cn.hsy.me.hetool.core.convert.impl;

import cn.hsy.me.hetool.core.convert.AbstractConverter;
import cn.hsy.me.hetool.core.util.ClassUtil;

/**
 * 类转换器<br>
 * 将类名转换为类
 * @author heshiyuan
 *
 */
public class ClassConverter extends AbstractConverter<Class<?>>{
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Class<?> convertInternal(Object value) {
		String valueStr = convertToStr(value);
		try {
			return ClassUtil.getClassLoader().loadClass(valueStr);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
