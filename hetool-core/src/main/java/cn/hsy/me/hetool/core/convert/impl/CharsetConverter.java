package cn.hsy.me.hetool.core.convert.impl;

import java.nio.charset.Charset;

import cn.hsy.me.hetool.core.convert.AbstractConverter;
import cn.hsy.me.hetool.core.util.CharsetUtil;

/**
 * 编码对象转换器
 * @author heshiyuan
 *
 */
public class CharsetConverter extends AbstractConverter<Charset>{
	private static final long serialVersionUID = 1L;

	@Override
	protected Charset convertInternal(Object value) {
		return CharsetUtil.charset(convertToStr(value));
	}

}
