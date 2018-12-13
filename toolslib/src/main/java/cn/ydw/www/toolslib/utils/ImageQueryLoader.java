package cn.ydw.www.toolslib.utils;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.ydw.www.toolslib.helper.ImageLoaderHelper;
import cn.ydw.www.toolslib.model.ImageFileModel;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/10/22
 * 描述:
 * =========================================
 */
public final class ImageQueryLoader extends AsyncTaskLoader<HashMap<String, List<ImageFileModel>>>{

    public ImageQueryLoader(Context context) {
        super(context);
    }

    @Override
    public HashMap<String, List<ImageFileModel>> loadInBackground() {
        HashMap<String,List<ImageFileModel>> allPhotosTemp = new HashMap<>();//所有照片
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projImage = { MediaStore.Images.Media._ID
                , MediaStore.Images.Media.DATA
                ,MediaStore.Images.Media.SIZE
                ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.MIME_TYPE};
        final Cursor mCursor = getContext().getContentResolver().query(mImageUri,
                projImage,
                MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=? or "
                        + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{"image/jpeg", "image/jpg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if(mCursor != null){
            List<ImageFileModel> firstData = new LinkedList<>();
            allPhotosTemp.put(ImageLoaderHelper.AllImgKey, firstData);
            while (mCursor.moveToNext()) {
                // 获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                long size = mCursor.getLong(mCursor.getColumnIndex(MediaStore.Images.Media.SIZE))/1024;
                String displayName = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
                String mimeType = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE));

                // 获取该图片的父路径名
                File mParentFile = new File(path).getParentFile();
                String dirPath = mParentFile.getAbsolutePath();
                String dirName = mParentFile.getName();

                // 格式化数据
                ImageFileModel mImageFile = new ImageFileModel(path, dirPath, displayName, dirName, mimeType, size);
                firstData.add(mImageFile); // 加载数据到全部图片
                //存储对应关系
                if (allPhotosTemp.containsKey(dirPath)) {
                    List<ImageFileModel> data = allPhotosTemp.get(dirPath);
                    data.add(mImageFile);
//                            Logger.e("getAllPhotoInfo  "+data.size()+",path="+data.get(0).getPath()+",name="+data.get(0).getDisplayName());
                } else {
                    List<ImageFileModel> data = new LinkedList<>();
                    data.add(mImageFile);
                    allPhotosTemp.put(dirPath,data);
//                            Logger.e("getAllPhotoInfo  else "+data.size()+",path="+data.get(0).getPath()+",name="+data.get(0).getDisplayName());
                }
            }
            mCursor.close();
        }
        return allPhotosTemp;
    }


    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

}
