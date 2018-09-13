package com.duan.blogos.util.file;

import java.io.File;
import java.io.IOException;

/**
 * Created on 2018/4/4.
 *
 * @author DuanJiaNing
 */
public class FileUtils {

    /**
     * 将文件保存到指定路径
     *
     * @param file     文件
     * @param fullPath 路径
     */
    public static boolean saveFileTo(MultipartFile file, String fullPath) {
        try {
            file.transferTo(new File(fullPath));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

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

    public static void moveFile(File oldFile, File newFile) throws IOException {
        org.apache.commons.io.FileUtils.moveFile(oldFile, newFile);
    }
}
