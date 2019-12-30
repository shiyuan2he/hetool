package cn.hsy.me.hetool.core.io.resource;

import java.io.File;

import cn.hsy.me.hetool.core.io.FileUtil;

/**
 * Web root资源访问对象
 * 
 * @author heshiyuan
 * @since 4.1.11
 */
public class WebAppResource extends FileResource {
	private static final long serialVersionUID = 1L;

	/**
	 * 构造
	 * 
	 * @param path 相对于Web root的路径
	 */
	public WebAppResource(String path) {
		super(new File(FileUtil.getWebRoot(), path));
	}

}
