package org.egc.commons.files;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created by lp on 2017/5/26.
 * update houzhiwei
 */
public class FilesOperation {

    private static final Logger logger = LoggerFactory.getLogger(FilesOperation.class);

    public static long copyFile(String srcFilePath, String destDirPath, String destFileName) {
        long copySizes = 0;
        File srcFile = new File(srcFilePath);
        File destDir = new File(destDirPath);
        if (!srcFile.exists()) {
            copySizes = -1;
        } else if (!destDir.exists()) {
            copySizes = -1;
        } else if (destFileName == null) {
            copySizes = -1;
        } else {
            try {
                FileChannel fcin = new FileInputStream(srcFile).getChannel();
                FileChannel fcout = new FileOutputStream(new File(destDir, destFileName)).getChannel();
                long size = fcin.size();
                fcin.transferTo(0, fcin.size(), fcout);
                fcin.close();
                fcout.close();
                copySizes = size;
            } catch (FileNotFoundException e) {
                logger.error("Copy File Operation fail, because the file to copy can not be found", e);
            } catch (IOException e) {
                logger.error("Copy File Operation fail, because can not write", e);
            }
        }
        return copySizes;
    }

    public static Boolean deleteDiskFile(String path) {
        Boolean delete = true;
        File deleteFile = new File(path);
        if (deleteFile.isFile() && deleteFile.exists()) {
            if (deleteFile.delete()) {
                delete = true;
                logger.info("delete file of " + path + " in the server folder successfully");
            } else {
                logger.info("delete file of " + path + " in the server folder failure");
                delete = false;
            }
        }
        return delete;
    }

    public static void deleteDiskFolder(File file)
    {
        Boolean delete = true;
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteDiskFolder(files[i]);
                }
            }
            file.delete();
        }
    }


    /**
     * 获取文件后缀。
     * 使用{@link org.egc.commons.util.FileUtil#getFileExtension(String)}
     * @param filename
     * @return 文件后缀
     */
    @Deprecated
    public static String getFileSuffix(String filename) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(filename), "Filename can not be null or empty");
        File file = new File(filename);
        String name = file.getName();
        String suffix = name.substring(name.lastIndexOf(".") + 1);
        return suffix;
    }
}
