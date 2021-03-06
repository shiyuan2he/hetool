package cn.hsy.me.hetool.log.dialect.console;

import cn.hsy.me.hetool.log.Log;
import cn.hsy.me.hetool.log.LogFactory;

/**
 * 利用System.out.println()打印日志
 * @author heshiyuan
 *
 */
public class ConsoleLogFactory extends LogFactory {
	
	public ConsoleLogFactory() {
		super("Hetool Console Logging");
	}

	@Override
	public Log createLog(String name) {
		return new ConsoleLog(name);
	}

	@Override
	public Log createLog(Class<?> clazz) {
		return new ConsoleLog(clazz);
	}

}
