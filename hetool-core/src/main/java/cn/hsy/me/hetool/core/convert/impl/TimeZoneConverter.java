package cn.hsy.me.hetool.core.convert.impl;

import java.util.TimeZone;

import cn.hsy.me.hetool.core.convert.AbstractConverter;

/**
 * TimeZone转换器
 * @author heshiyuan
 *
 */
public class TimeZoneConverter extends AbstractConverter<TimeZone>{
	private static final long serialVersionUID = 1L;

	@Override
	protected TimeZone convertInternal(Object value) {
		return TimeZone.getTimeZone(convertToStr(value));
	}

}
