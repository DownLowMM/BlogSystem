package com.duan.blogos.util.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created on 2018/9/13.
 *
 * @author DuanJiaNing
 */
public interface MultipartFile {

    String getName();

    String getOriginalFilename();

    String getContentType();

    boolean isEmpty();

    long getSize();

    byte[] getBytes() throws IOException;

    InputStream getInputStream() throws IOException;

    void transferTo(File dest) throws IOException, IllegalStateException;


}
