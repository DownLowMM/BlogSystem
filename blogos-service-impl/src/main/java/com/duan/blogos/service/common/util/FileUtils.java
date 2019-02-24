package com.duan.blogos.service.common.util;

import java.io.File;

/**
 * Created on 2019/2/22.
 *
 * @author DuanJiaNing
 */
public class FileUtils {

    /**
     * 删除文件
     *
     * @param path 路径
     * @return 成功删除返回 true
     */
    public static boolean deleteFileIfExist(String path) {
        File file = new File(path);
        return file.exists() && file.isFile() && file.delete();
    }

    /**
     * 创建目录，如果不存在的话
     *
     * @param path 路径
     * @return 成功创建返回 true
     */
    public static boolean mkdirsIfNotExist(String path) {
        File dir = new File(path);
        return (!dir.exists() || dir.isFile()) && dir.mkdirs();
    }
}
