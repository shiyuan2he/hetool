package cn.hsy.me.hetool.core.io.file;

import java.io.File;

/**
 * @author heshiyuan
 * @date 2020/2/21 20:58
 */
public class FileDeleter {

    /**
     * 删除文件
     * @param file
     */
    public static void delete(File file) {
        if (!file.exists()) {return;}

        if (file.isFile() || file.list() == null) {
            file.delete();
            System.out.println("删除了" + file.getName());
        } else {
            File[] files = file.listFiles();
            for (File a : files) {
                delete(a);
            }
            file.delete();
            System.out.println("删除了" + file.getName());
        }
    }
}
