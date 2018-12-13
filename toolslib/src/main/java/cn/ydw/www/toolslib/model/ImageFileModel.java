package cn.ydw.www.toolslib.model;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/10/22
 * 描述: 图片文件模型
 * =========================================
 */
public final class ImageFileModel {
    public String path; // 路径唯一!
    public String parentPath;// 文件夹路径
    public String displayName; //本图片的名字
    public String parentDisplayName; //文件夹名字
    public String mimeType;// 图片后缀
    public File mFile; // 本图片文件
    public long size;
    private Object tag;


    public ImageFileModel(String path) {
        this(path, null, null, null, null, -1);
    }

    public ImageFileModel(String path, String parentPath, String displayName, String parentDisplayName, String mimeType, long size) {
        if (TextUtils.isEmpty(path))
            throw new IllegalArgumentException("The image path not null!");
        this.mFile = new File(path);
        if (!mFile.exists() ) {
            parentPath = "errorDir";
            displayName = "errorName";
            mimeType = "image/jpeg";
            size = 0;
        } else {
            if (TextUtils.isEmpty(parentPath)) {
                parentPath = mFile.getParent();
            }
            if (TextUtils.isEmpty(parentDisplayName)) {
                parentDisplayName = mFile.getParentFile().getName();
            }
            if (TextUtils.isEmpty(displayName)) {
                displayName = mFile.getName();
            }
            if (TextUtils.isEmpty(mimeType)) {
                mimeType = "image/jpeg";
            }
            if (size == -1) {
                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(mFile);
                    size = fis.available();
                } catch (Exception e) {
                    size = 0;
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        this.path = path;
        this.parentPath = parentPath;
        this.parentDisplayName = parentDisplayName;
        this.displayName = displayName;
        this.mimeType = mimeType;
        this.size = size;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object mo) {
        if (this == mo) return true;
        if (mo == null || getClass() != mo.getClass()) return false;

        ImageFileModel mthat = (ImageFileModel) mo;

        return path != null ? path.equals(mthat.path) : mthat.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ImageFileModel{" +
                "path='" + path + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", displayName='" + displayName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + size +
                ", tag=" + tag +
                '}';
    }
}
