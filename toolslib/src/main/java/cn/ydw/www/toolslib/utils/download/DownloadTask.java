package cn.ydw.www.toolslib.utils.download;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

/**
 * @author 杨德望
 * Create on 2018/1/25
 * 描述: 下载文件工具类, 需要传入文件存储文件夹路径, 和回调监听;
 *
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {
    private String Tag = "";

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    private String mOutputPath;
    private OnMsgListener oml;

    /**
     * 初始化
     * @param outputPath 输出路径, 全文件路径, 文件夹加文件名和后缀
     * @param oml 回调
     */
    DownloadTask(String outputPath, OnMsgListener oml) {
        mOutputPath = outputPath;
        this.oml = oml;
    }

    @Override
    protected String doInBackground(String... params) {
        if (TextUtils.isEmpty(mOutputPath)) return null;
        String imgUrl = params[0];
        InputStream is = null;
        FileOutputStream fos = null;
//        String fileUrl;
        try {
            URL url = new URL(imgUrl);
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
            //获取文件的总长度
            int fileLength = conn.getContentLength();
            is = conn.getInputStream();

            File apkFile = new File(mOutputPath);
            // 如果文件夹不存在则创建
            if (!apkFile.getParentFile().exists()) {
                //noinspection ResultOfMethodCallIgnored
                apkFile.getParentFile().mkdirs();
            }
            fos = new FileOutputStream(apkFile);
            byte[] buffer = new byte[1024];
            int len;//每次下载的长度
            int num = 0, file1Len = fileLength / 20;//更新20次
            float loadedLen = 0;// 当前已下载文件大小
            while (-1 != (len = is.read(buffer))) {
                loadedLen += Math.abs(len);
                fos.write(buffer, 0, len);
                if (loadedLen > file1Len * num) {
                    num++;
                    //推送进度
                    publishProgress(Math.round(loadedLen / fileLength * 100));//每次更新5进度, 总长100
                }
            }
            fos.flush();
            return mOutputPath;
        } catch (MalformedURLException e) {
            if (oml != null)
                oml.onError(Tag, "网址异常!");
            Log.e("下载异常", "MalformedURLException");
        } catch (SocketTimeoutException e) {
            // 处理超时异常，提示用户在网络良好情况下重试
            Log.e("下载异常", "SocketTimeoutException");
            if (oml != null) {
                oml.onError(Tag, "处理超时!");
            }
        } catch (IOException e) {
            Log.e("下载异常", "IOException");
            if (oml != null) {
                oml.onError(Tag, "文件存储异常!");
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        // 发送通知显示升级进度
        if (oml != null) {
            oml.onPreExecute(Tag);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (oml != null)
            oml.onProgressUpdate(Tag, values);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        if (oml != null) {
            if (TextUtils.isEmpty(s)) {
                oml.onError(Tag, "下载失败, 文件异常!");
            } else
                oml.onPostExecute(Tag, s);

        }
    }

    public interface OnMsgListener {
        void onPreExecute(String tag);

        void onProgressUpdate(String tag, Integer... values);

        void onPostExecute(String tag, String s);

        void onError(String tag, String msg);
    }
}
