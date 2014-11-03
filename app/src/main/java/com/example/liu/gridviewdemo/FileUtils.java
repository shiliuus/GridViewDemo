package com.example.liu.gridviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liu on 2014/11/2.
 */
public class FileUtils {
    //SD卡根目录
    private static String mSDRootPath = Environment.getExternalStorageDirectory().getPath();

    //手机缓存目录
    private static String mDataRootPath = null;

    //保存image目录名
    private final static String FOLDER_NAME = "/MyImage";

    public FileUtils(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }

    //获取存储image的目录
    private String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSDRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }

    //保存image的方法， 有sd卡存储到sd卡，没有就存储到手机目录
    public void saveBitmap(String fileName, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return;
        }
        String path = getStorageDirectory();
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(path + File.separator + fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    //从手机或者sd卡获取bitmap
    public Bitmap getBitmap(String fileName) {
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);
    }

    public boolean isFileExists (String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }

    public long getFileSize(String fileName) {
        return new File(getStorageDirectory() + File.separator + fileName).length();
    }

    //删除sd卡或者手机的缓存图片和目录
    public void deleteFolder() {
        File dirFile = new File(getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i=0; i<children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
        dirFile.delete();
    }
}
