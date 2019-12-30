package cn.hsy.me.hetool.core.convert.impl;

import java.util.Locale;

import cn.hsy.me.hetool.core.convert.AbstractConverter;
import cn.hsy.me.hetool.core.util.StrUtil;

/**
 * 
 * {@link Locale}对象转换器<br>
 * 只提供String转换支持
 * 
 * @author heshiyuan
 * @since 4.5.2
 */
public class LocaleConverter extends AbstractConverter<Locale> {
	private static final long serialVersionUID = 1L;

	@Override
	protected Locale convertInternal(Object value) {
		try {
			String str = convertToStr(value);
			if (StrUtil.isEmpty(str)) {
				return null;
			}

			final String[] items = str.split("_");
			if (items.length == 1) {
				return new Locale(items[0]);
			}
			if (items.length == 2) {
				return new Locale(items[0], items[1]);
			}
			return new Locale(items[0], items[1], items[2]);
		} catch (Exception e) {
			// Ignore Exception
		}
		return null;
	}

}
