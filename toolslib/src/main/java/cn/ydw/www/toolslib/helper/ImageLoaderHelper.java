package cn.ydw.www.toolslib.helper;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.os.Bundle;

import java.util.HashMap;
import java.util.List;

import cn.ydw.www.toolslib.model.ImageFileModel;
import cn.ydw.www.toolslib.utils.ImageQueryLoader;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/10/22
 * 描述: 加载本地所有图片的辅助器, 可以多次重载
 * =========================================
 */
public class ImageLoaderHelper{
    private static final int ImageLoaderId = 20;
    public static final String AllImgKey = "totalImages";
    public static final String AllImgName = "所有图片";
    private Context mContext;
    private LoaderManager mLoaderManager;
    private ImageQueryLoader mLoader;
    private Callback mCallback;

    public ImageLoaderHelper(Context context, LoaderManager mLoaderManager) {
        mContext = context;
        this.mLoaderManager = mLoaderManager;
    }

    public void postImageLoad() {
        if (mLoader == null) {
            mLoaderManager.initLoader(ImageLoaderId, null, new ImageLoader());
        } else {
            mLoader.onContentChanged();
        }
    }

    public ImageLoaderHelper setImageLoadListener(Callback mCallback) {
        this.mCallback = mCallback;
        return this;
    }

    // 图片加载者,
    private class ImageLoader implements LoaderManager.LoaderCallbacks<HashMap<String, List<ImageFileModel>>> {

        @Override
        public Loader<HashMap<String, List<ImageFileModel>>> onCreateLoader(int id, Bundle args) {
            if (mLoader == null) {
                mLoader = new ImageQueryLoader(mContext);
            }
            return mLoader;
        }

        @Override
        public void onLoadFinished(Loader<HashMap<String, List<ImageFileModel>>> loader, HashMap<String, List<ImageFileModel>> data) {
//                StringBuilder dirStr = new StringBuilder("总文件夹情况 = [");
//                Set<String> mKeyStr = data.keySet();
//                for (String dir: mKeyStr) {
//                    dirStr.append("\'").append(dir).append("\', ");
//
//                    StringBuilder fileStr = new StringBuilder("文件夹: " + dir + " = [");
//                    List<ImageFileModel> mImageFiles = data.get(dir);
//                    for (ImageFileModel file: mImageFiles) {
//                        fileStr.append("\'").append(file.path).append("\', ");
//                    }
//                    fileStr.append("总数: ").append(mImageFiles.size()).append( " ]; \n");
//                    Logger.e(fileStr.toString());
//                }
//                dirStr.append("总数: ").append(mKeyStr.size()).append( " ]; \n");
//                Logger.e(dirStr.toString());

            if (mCallback != null) {
                mCallback.onImageLoadEnd(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<HashMap<String, List<ImageFileModel>>> loader) {
//                Logger.e("启动检索 ---- onLoaderReset");

        }
    }

    public interface Callback {
        void onImageLoadEnd(HashMap<String, List<ImageFileModel>> data);
    }
}
