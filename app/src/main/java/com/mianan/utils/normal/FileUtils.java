package com.mianan.utils.normal;

import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.mianan.utils.AppContracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Created by FengChaoQun
 * on 2016/7/11
 */
public class FileUtils {
    //  SDK存储空间
    public static String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator;
    //    文件夹
    public static String ROOT_PATH = EXTERNAL_PATH + AppContracts.APP_NAME + File.separator;
    //   相机拍照存储相册
    public static String CameraPhotoPath = ROOT_PATH + "Camera" + File.separator;
    //  保存的图片
    public static String SAVED_IAMGE = ROOT_PATH + "SAVED_IMAGE" + File.separator;
    //  临时图片
    public static String TEMP_IMAGE = SAVED_IAMGE + "temp.jpg";
    public static String TEMP_IMAGE2 = SAVED_IAMGE + "temp2.jpg";
    //  Glide缓存目录
    public static String GLIDE_CACHE = ROOT_PATH + "IMAGE_CACHE" + File.separator;

    /*
    **describe:存储单位
    */
    public static int KB = 1024;
    public static int MB = 1024 * KB;
    public static int GB = 1024 * MB;

    public static boolean copyFileTo(File srcFile, File destFile) throws IOException {

        if (srcFile.isDirectory() || destFile.isDirectory())

            return false;// 判断是否是文件

        if (!destFile.exists()) {
            if (destFile.createNewFile()) {
                Log.d("创建文件成功:", destFile.getAbsolutePath());
            } else {
                Log.d("创建文件失败:", "error");
                return false;
            }
        }

        FileInputStream fis = new FileInputStream(srcFile);

        FileOutputStream fos = new FileOutputStream(destFile);

        int readLen = 0;

        byte[] buf = new byte[1024];

        while ((readLen = fis.read(buf)) != -1) {

            fos.write(buf, 0, readLen);

        }

        fos.flush();

        fos.close();

        fis.close();

        return true;

    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(java.io.File file) {

        long size = 0;
        try {
            java.io.File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);

                } else {
                    size = size + fileList[i].length();

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 删除指定目录下文件及目录
     *
     * @param deleteThisPath
     * @param
     * @return
     */
    public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {// 处理目录
                    File files[] = file.listFiles();
                    for (int i = 0; i < files.length; i++) {
                        deleteFolderFile(files[i].getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {// 如果是文件，删除
                        file.delete();
                    } /*else {// 目录
                        if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
                            file.delete();
                        }
                    }*/
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }

    public static String getCameraPhotoPath() {
        File file = new File(CameraPhotoPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        return CameraPhotoPath;
    }

    public static String getSavedIamge() {
        File file = new File(SAVED_IAMGE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return SAVED_IAMGE;
    }

    public static String getGlideCache() {
        File file = new File(GLIDE_CACHE);
        if (!file.exists()) {
            file.mkdirs();
        }
        return GLIDE_CACHE;
    }

    public static String getTempImage() {
        File file = new File(getSavedIamge(), "temp.jpg");
        Log.d("path", SAVED_IAMGE + "temp.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TEMP_IMAGE;
    }

    public static String getTempImage2() {
        File file = new File(getSavedIamge(), "temp2.jpg");
        Log.d("path", SAVED_IAMGE + "temp2.jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return TEMP_IMAGE2;
    }

    public static File getTempImageFile() {
        File file = new File(getTempImage());
        return file;
    }

    public static File getTempImageFile2() {
        File file = new File(getTempImage2());
        return file;
    }

    public static Uri newPhotoPath() {
        File file = new File(getCameraPhotoPath(), UUID.randomUUID().toString() + ".jpg");
        return Uri.fromFile(file);
    }

    public static File newImageFile() {
        return new File(getSavedIamge(), UUID.randomUUID().toString() + ".jpg");
    }
}
