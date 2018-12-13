package cn.ydw.www.toolslib.helper.download;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Date;

import cn.ydw.www.toolslib.ToolConstants;
import cn.ydw.www.toolslib.utils.FileUtils;
import cn.ydw.www.toolslib.utils.Logger;
import cn.ydw.www.toolslib.utils.MyTimeUtils;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/14
 * 描述: 下载器
 * =========================================
 */
class SingleDownloader extends AsyncTaskLoader<Bundle> {
    private Bundle args;


    SingleDownloader(Context context, Bundle args) {
        super(context);
        this.args = args;
    }

    public void setArgs(Bundle args) {
        this.args = args;
    }

    @Override
    public Bundle loadInBackground() {
        String fileUrl = args.getString(ToolConstants.DownloadPathUrl);
        DownloadCallback mDownloadCallback = (DownloadCallback) args.getSerializable(ToolConstants.DownloadCallback);
        if (TextUtils.isEmpty(fileUrl)) {
            return null;
        }
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url
                    .openConnection();
            // 设置连接超时时间
            conn.setConnectTimeout(15000);
            // 设置下载数据超时时间
            conn.setReadTimeout(15000);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // 服务端错误响应
                return null;
            }
            is = conn.getInputStream();
            // 获取文件名
            String displayName = fileUrl.substring(fileUrl.lastIndexOf("/"));
            File mFile = FileUtils.getNewCacheFile(getContext(), displayName, null);
            if (mFile == null) return null;
            fos = new FileOutputStream(mFile);


            long fileLength;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileLength = conn.getContentLengthLong();
            } else {
                fileLength = conn.getContentLength();//获取文件的总长度
            }
            byte[] buffer = new byte[1024];// 创建字节缓存
            int len;//每次下载的长度
            int num = 0;
            long fileLen = fileLength / 20;//更新20次
            float loadedLen = 0;// 当前已下载文件大小
            long lastLoadedTime = System.currentTimeMillis();// 上个阶段的时间
            while (-1 != (len = is.read(buffer))) {
                loadedLen += Math.abs(len);
                fos.write(buffer, 0, len);
                if (fileLength > 0 && loadedLen > fileLen * num) {
                    num++;
                    //推送进度
                    if (mDownloadCallback != null) {
                        float rate = len  / ((System.currentTimeMillis() - lastLoadedTime) / 1000f);// byte/s
                        mDownloadCallback.onDownLoadProgress(fileUrl, (loadedLen / fileLength), rate);//每次更新5进度, 总长100
                    }
                }
            }
            fos.flush();

            String time = MyTimeUtils.getTimeDate2Str(new Date(), MyTimeUtils.yMdHms);
            args.putString(ToolConstants.DownloadTimeEnd, time);
            args.putString(ToolConstants.DownloadPathLocal, mFile.getAbsolutePath());

            return args;
        } catch (MalformedURLException e) {
            Logger.e("下载异常", "MalformedURLException");
        } catch (SocketTimeoutException e) {
            // 处理超时异常，提示用户在网络良好情况下重试
            Logger.e("下载异常", "SocketTimeoutException");
        } catch (IOException e) {
            Logger.e("下载异常", "IOException");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                Logger.e("关流异常", e);
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                Logger.e("关流异常", e);
            }
        }
        return null;
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
