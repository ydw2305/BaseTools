package cn.ydw.www.toolslib.helper.download;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import java.io.File;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.ydw.www.toolslib.ToolConstants;
import cn.ydw.www.toolslib.model.DownloadFileModel;
import cn.ydw.www.toolslib.utils.FileUtils;
import cn.ydw.www.toolslib.utils.Logger;
import cn.ydw.www.toolslib.utils.MyTimeUtils;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/12
 * 描述: 单一下载, 其余的都是加入队列缓存, (先进来的先下载)
 * =========================================
 */
public final class SingleDownloadHelper {

    private static final int cacheNum = 3; // 可以缓存多少个缓存器数,
    private static final boolean needLog = false;

    private Context mContext;
    private LoaderManager mLoaderManager;

    private LinkedList<Bundle> cacheHelperList = new LinkedList<>();// 缓存下载器数据,
    private Map<String, DownloadFileModel> cacheFileList = new LinkedHashMap<>();// 结果状态存储, 缓存下载成功或下载失败的地址

    private boolean isCallDownload = false; // 是否在下载状态
    private boolean isScrolled = false;// 当前状态是否在滑动, 滑动则停止
    private String nowID; // 当前的id, 避免重复执行同一个下载事件

    private DownloadListener mLoaderListener;

    public SingleDownloadHelper(Context context, LoaderManager mLoaderManager) {
        mContext = context;
        this.mLoaderManager = mLoaderManager;
        mLoaderListener = new DownloadListener(mContext);
    }

    /**
     * 设置当前是否是滑动的状态
     *
     * @param scrolled 是否是滑动
     */
    public void setScrolled(boolean scrolled) {
        isScrolled = scrolled;
    }

    /**
     * 请求下载
     *
     * @param url 下载id, 唯一值, 这里传入下载网址
     */
    public void postDownLoad(String url, DownloadCallback mDownloadCallback) {
        if (isScrolled) return;
        if (TextUtils.isEmpty(url) || TextUtils.equals(nowID, url)) {
            return;
        }
        nowID = url;
        // 校验是否添加到缓存器
        if (checkHasLocalCache(url, mDownloadCallback)) return;
        if (needLog)
            Logger.e("申请下载 = " + url);
        // 发送请求
        Bundle args = new Bundle();
        args.putString(ToolConstants.DownloadPathUrl, url);
        args.putSerializable(ToolConstants.DownloadCallback, mDownloadCallback);
        if (isCallDownload && cacheNum > 0) { // 若当前正在下载, 那么添加到缓存器
            if (cacheHelperList.size() >= cacheNum) {
                Bundle mRemove = cacheHelperList.remove(cacheNum - 1);
                cacheFileList.remove(mRemove.getString(ToolConstants.DownloadPathUrl));
            }
            String time = MyTimeUtils.getTimeDate2Str(new Date(), MyTimeUtils.yMdHms);
            args.putString(ToolConstants.DownloadTimeWait, time);
            cacheHelperList.add(args);

        } else {
            isCallDownload = true;
            String time = MyTimeUtils.getTimeDate2Str(new Date(), MyTimeUtils.yMdHms);
            args.putString(ToolConstants.DownloadTimeStart, time);
            mLoaderManager.restartLoader(100, args, mLoaderListener);
        }
    }

    /**
     * 重新发送下载请求, 这里是下载缓存里面的数据
     */
    private void postCacheDownload() {
        // 发送缓存的加载
        if (cacheHelperList.size() > 0) {
            Bundle args = cacheHelperList.remove(0);
            String time = MyTimeUtils.getTimeDate2Str(new Date(), MyTimeUtils.yMdHms);
            args.putString(ToolConstants.DownloadTimeStart, time);
            mLoaderManager.restartLoader(100, args, mLoaderListener);
        } else if (isCallDownload) {
            isCallDownload = false;
        }
    }

    /**
     * 校验是否本地缓存过了
     *
     * @param url 网址
     * @return true 表示已本地缓存过了
     */
    private boolean checkHasLocalCache(String url, DownloadCallback mDownloadCallback) {
        // 校验是否添加到缓存器
        if (cacheFileList.keySet().contains(url)) { // 如果下载过一次
            DownloadFileModel resultValue = cacheFileList.get(url);
            if (resultValue.state != ToolConstants.DownloadState_Wait) {
                if (mDownloadCallback != null) {
                    mDownloadCallback.onDownloadResult(url, resultValue.localPath, true);
                }
            }
            return true;
        } else {
            // 校验是否本地缓存了
            int mLastIndexOf = url.lastIndexOf("/");
            if (mLastIndexOf >= 0) {
                DownloadFileModel mFileModel = new DownloadFileModel();
                mFileModel.fileUrl = url;

                String displayName = url.substring(mLastIndexOf);
                File mCacheDirectory = FileUtils.getCacheDirectory(mContext, null);
                File mFile = new File(mCacheDirectory, displayName);
                if (mFile.exists()) {
                    mFileModel.state = ToolConstants.DownloadState_Suc;
                    mFileModel.localPath = mFile.getAbsolutePath();
                    cacheFileList.put(url, mFileModel);
                    if (mDownloadCallback != null) {
                        mDownloadCallback.onDownloadResult(url, mFile.getAbsolutePath(), true);
                    }
                    return true;
                }
                mFileModel.state = ToolConstants.DownloadState_Wait;
                cacheFileList.put(url, mFileModel);
            }
        }
        return false;
    }


    /////////////////////////////////////// 加载监听 ////////////////////////////////////////////
    private class DownloadListener implements LoaderManager.LoaderCallbacks<Bundle> {
        private Context mContext;

        DownloadListener(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public Loader<Bundle> onCreateLoader(int id, Bundle args) {
            return new SingleDownloader(mContext, args);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Bundle> loader, Bundle args) {
            try {
                if (args != null) {
                    String endTime = args.getString(ToolConstants.DownloadTimeEnd);
                    String startTime = args.getString(ToolConstants.DownloadTimeStart);
                    String waitTime = args.getString(ToolConstants.DownloadTimeWait);
                    String urlPath = args.getString(ToolConstants.DownloadPathUrl);
                    String localPath = args.getString(ToolConstants.DownloadPathLocal);
                    DownloadCallback mDownloadCallback =
                            (DownloadCallback) args.getSerializable(ToolConstants.DownloadCallback);
                    if (needLog) {
                        String resultValue = "来自 = " + urlPath +
                                "; 生成地址 = " + localPath +
                                "; 请求时间 = " + startTime +
                                "; 结束时间 = " + endTime +
                                "; 等待时间 = " + waitTime;

                        Logger.e(resultValue);
                    }
                    DownloadFileModel mFileModel = cacheFileList.get(urlPath);
                    mFileModel.state = TextUtils.isEmpty(localPath) ?
                            ToolConstants.DownloadState_Err : ToolConstants.DownloadState_Suc;
                    mFileModel.localPath = localPath;

                    if (mDownloadCallback != null) {
                        mDownloadCallback.onDownloadResult(urlPath, localPath, false);// 网络返回的
                    }
                }
            } catch (Exception e) {
                if (needLog)
                    Logger.e("下载结束异常", e);
            }
            postCacheDownload();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Bundle> loader) {

        }
    }



}
