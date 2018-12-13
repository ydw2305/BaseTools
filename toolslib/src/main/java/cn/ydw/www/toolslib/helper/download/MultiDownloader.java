package cn.ydw.www.toolslib.helper.download;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import cn.ydw.www.toolslib.utils.Logger;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/3
 * 描述:
 * =========================================
 */
class MultiDownloader extends AsyncTask<Void, Float, String> {

    private String fileUrl;// 文件下载地址
    private File mDesFile; // 要存储的目标文件
    private Handler mHandler;

    MultiDownloader(File mCacheDirectory, String fileUrl, Handler mHandler) {
        this.mHandler = mHandler;
        if (TextUtils.isEmpty(fileUrl) || fileUrl.length() < 6) {
            return;
        }
        this.fileUrl = fileUrl;
        int mLastIndexOf = fileUrl.lastIndexOf("/");
        if (mLastIndexOf >= 6) {
            // 获取文件名
            String displayName = fileUrl.substring(mLastIndexOf);
            mDesFile = new File(mCacheDirectory, displayName);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        if (TextUtils.isEmpty(fileUrl) || mDesFile == null) return null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间
            conn.setConnectTimeout(15000);
            // 设置下载数据超时时间
            conn.setReadTimeout(15000);
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                // 服务端错误响应
                return null;
            }
            is = conn.getInputStream();
            fos = new FileOutputStream(mDesFile);

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
            long loadedLen = 0;// 当前已下载文件大小
            long lastLoadedTime = System.currentTimeMillis();// 上个阶段的时间
            while (-1 != (len = is.read(buffer))) {
                loadedLen += Math.abs(len);
                fos.write(buffer, 0, len);
                if (fileLength > 0 && loadedLen > fileLen * num) {
                    num++;
                    //推送进度
                    long time = (System.currentTimeMillis() - lastLoadedTime);
                    if (time <= 0) {
                        time = 1000;
                    }
                    float rate = len  / (time / 1000f * 1024);// kb/ 10s
                    publishProgress((loadedLen * 1f / fileLength), (rate));//每次更新5进度, 总长100
                    lastLoadedTime = System.currentTimeMillis();
                }
            }
            fos.flush();
//            mFileModel.endTime = MyTimeUtils.getTimeDate2Str(new Date(), MyTimeUtils.yMdHms);
            return mDesFile.getAbsolutePath();
        } catch (MalformedURLException e) {
            Logger.e("下载异常, MalformedURLException", e);
        } catch (SocketTimeoutException e) {
            // 处理超时异常，提示用户在网络良好情况下重试
            Logger.e("下载异常, SocketTimeoutException", e);
        } catch (IOException e) {
            Logger.e("下载异常, IOException", e);
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
    protected void onProgressUpdate(Float... values) {
        super.onProgressUpdate(values);
        if (values != null && values.length > 1) {
            float progress = values[0];
            float speed = values[1];
            DownloadResultHelper.sendDownloadWait(mHandler, fileUrl, progress, speed);
        }
    }

    @Override
    protected void onPostExecute(String localPath) {
        super.onPostExecute(localPath);
        if (TextUtils.isEmpty(localPath)) {
            DownloadResultHelper.sendDownloadErr(mHandler, fileUrl);
        } else {
            DownloadResultHelper.sendDownloadSuc(mHandler, fileUrl, localPath, false);
        }
    }
}
