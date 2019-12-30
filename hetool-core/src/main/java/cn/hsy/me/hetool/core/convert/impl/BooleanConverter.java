package cn.hsy.me.hetool.core.convert.impl;

import cn.hsy.me.hetool.core.convert.AbstractConverter;
import cn.hsy.me.hetool.core.util.BooleanUtil;

/**
 * 波尔转换器
 * @author heshiyuan
 *
 */
public class BooleanConverter extends AbstractConverter<Boolean>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Boolean convertInternal(Object value) {
		if(boolean.class == value.getClass()){
			return Boolean.valueOf((boolean)value);
		}
		String valueStr = convertToStr(value);
		return Boolean.valueOf(BooleanUtil.toBoolean(valueStr));
	}

}
