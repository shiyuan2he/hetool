package cn.hsy.me.hetool.core.convert.impl;

import cn.hsy.me.hetool.core.convert.AbstractConverter;

/**
 * 字符串转换器
 * @author heshiyuan
 *
 */
public class StringConverter extends AbstractConverter<String>{
	private static final long serialVersionUID = 1L;

	@Override
	protected String convertInternal(Object value) {
		return convertToStr(value);
	}

}
