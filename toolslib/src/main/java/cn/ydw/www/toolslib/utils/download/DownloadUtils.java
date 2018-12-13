package cn.ydw.www.toolslib.utils.download;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Locale;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.ToolConstants;
import cn.ydw.www.toolslib.utils.FileUtils;
import cn.ydw.www.toolslib.utils.Logger;

/**
 * @author 杨德望
 * @date 2017/11/28.
 * 描述: 下载资源工具类
 */

public class DownloadUtils implements DownloadTask.OnMsgListener {

    private Activity mContext;

    private RemoteViews mNotifiviews;
    private Notification mNotifi;
    private NotificationManager mNotifiMgr;
    private OnFileStorageListener ofsl;

    private boolean needShowNotify, needShowProgress, needCache = true;
    private ProgressDialog pDialog;

    public DownloadUtils(Activity mContext) {
        this.mContext = mContext;
        setNeedShowNotify(true);
        setNeedShowProgress(true);
    }
    public DownloadUtils(Activity mContext, boolean NeedShowNotify, boolean NeedShowProgress) {
        this.mContext = mContext;
        setNeedShowNotify(NeedShowNotify);
        setNeedShowProgress(NeedShowProgress);
    }

    /**
     * 呼叫软件升级
     *
     * @param url         网址
     * @param serverVCode 版本号
     * @param newContent  新特性说明
     */
    public void callUpdate(final String url, int serverVCode, String newContent, final OnFileStorageListener fl) {
        int versionCode = FileUtils.getVersionCode(mContext);
        if (versionCode >= serverVCode) {
            return;
        }
        if (TextUtils.isEmpty(url) || !url.contains(".apk")) {
            Toast.makeText(mContext, "服务器异常, 无法获取更新数据!", Toast.LENGTH_SHORT).show();
            return;
        }

//        mDownloadUtils.setNeedShowNotify(false); //不显示通知栏
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        DialogInterface.OnClickListener pl = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //  更新
                callDownload(url, fl);
            }
        };
        if (TextUtils.isEmpty(newContent)) {
            newContent = "发现新版本";
        }
        builder.setTitle("是否更新？")
                .setMessage(newContent)
                .setPositiveButton("确定", pl)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }


    /**
     * 呼叫下载
     * @param url 网址
     * @param ofsl 回调
     */
    public synchronized void callDownload(String url, OnFileStorageListener ofsl) {
        WeakReference<DownloadTask> mReference = ToolConstants.mCacheNetCall.get(url);
        if (mReference != null && mReference.get() != null) return; //是否正在下载
        if (needCache) {
            String path = ToolConstants.mCacheDownloadFile.get(url);
            if (!TextUtils.isEmpty(path)) {
                if (ofsl != null)
                    ofsl.fileStorage(path);
                return;
            }
        }
        String mLowerUrl = url.toLowerCase(Locale.getDefault());
        boolean isHttp = mLowerUrl.startsWith("http://");
        boolean isHttps = mLowerUrl.startsWith("https://");
        if (!isHttp && !isHttps) {
            Toast.makeText(mContext, "网址错误!", Toast.LENGTH_SHORT).show();
            return;
        }
        int lastIndexOf = url.lastIndexOf("/");
        if (lastIndexOf < url.length()-1) {
            File mCacheDirectory = FileUtils.getCacheDirectory(
                    mContext, Environment.DIRECTORY_DOWNLOADS);
            String substring = url.substring(lastIndexOf + 1, url.length());
            Logger.e("文件名: " + substring);
            File mFile = new File(mCacheDirectory, substring);
            if (mFile.exists() && needCache) { //存在文件, 那么加入缓存
                ToolConstants.mCacheDownloadFile.put(url, mFile.getAbsolutePath());
                Logger.e("打印", "复用缓存");
                if (ofsl != null) {
                    ofsl.fileStorage(mFile.getAbsolutePath());
                }
            } else {
                Logger.e("打印", "开启下载");
                this.ofsl = ofsl;
                // : 2018/2/13
                DownloadTask mTask = new DownloadTask(mFile.getAbsolutePath(), this);
                mTask.setTag(url);
                if (needCache) {
                    ToolConstants.mCacheNetCall.put(url, new WeakReference<>(mTask));
                }
                mTask.execute(url);
            }
        } else {//只有网址头
            if (ofsl != null) {
                ofsl.fileStorage(null);
            }
        }
    }

    //是否显示通知栏
    public void setNeedShowNotify(boolean needShowNotify) {
        this.needShowNotify = needShowNotify;
    }

    //是否显示进度条
    public void setNeedShowProgress(boolean needShowProgress) {
        this.needShowProgress = needShowProgress;
    }
    //是否缓存文件
    public void setNeedCache(boolean needCache) {
        this.needCache = needCache;
    }

    /**
     * 销毁联网请求
     * @param Tag 网址
     */
    public void destoryTask(String Tag){
        WeakReference<DownloadTask> mReference = ToolConstants.mCacheNetCall.get(Tag);
        if (mReference != null && mReference.get() != null ) {
            if ( !mReference.get().isCancelled()) {
                DownloadTask mTask = mReference.get();
                boolean mCancel = mTask.cancel(false);
                if (mCancel) {
                    ToolConstants.mCacheNetCall.remove(Tag);
                }
            } else ToolConstants.mCacheNetCall.remove(Tag);
        }
    }

    @Override
    public void onPreExecute(String tag) {
        // 发送通知显示升级进度
        if (needShowNotify)
            sendNotify(mContext);
        // 显示进度
        if (needShowProgress)
            sendProgress(mContext);

    }

    @Override
    public void onProgressUpdate(String tag, Integer... values) {
        // 更新通知
        if (needShowNotify)
            updateNotify(values[0]);
        if (needShowProgress)
            updateProgress(values[0]);
        if (ofsl != null) {
            ofsl.onLoadProgress(values[0]);
        }
    }

    @Override
    public void onPostExecute(String tag, String s) {
        ToolConstants.mCacheNetCall.remove(tag);
        if (needShowNotify)
            finishNotify(mContext, s);
        if (needShowProgress)
            finishProgress(s);
        if (ofsl != null)
            ofsl.fileStorage(s);
        ToolConstants.mCacheDownloadFile.put(tag, s);
    }

    @Override
    public void onError(String tag, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        ToolConstants.mCacheNetCall.remove(tag);
    }
    // FIXME 注: 发送通知栏的时候, 8.0系统可能存在异常,
    //启动通知栏
    private void sendNotify(Context mContext) {
        Intent intent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                intent, 0);
        if (mNotifiviews == null)
            mNotifiviews = new RemoteViews(mContext.getPackageName(), R.layout.custom_notify);
        mNotifiviews.setTextViewText(R.id.tv_title, "正在下载");
        mNotifiviews.setViewVisibility(R.id.tv_subtitle, View.VISIBLE);
        mNotifiviews.setViewVisibility(R.id.progressBar1, View.VISIBLE);

        if (mNotifi == null) {
            //noinspection deprecation
            mNotifi = new NotificationCompat.Builder(mContext)
                    .setContent(mNotifiviews)
                    .setAutoCancel(true)
                    // 单击后自动删除
                    // .setOngoing(true)// 无法删除的通知
                    // 定制通知布局
                    .setSmallIcon(R.mipmap.icon_logo_round).setTicker("ticker")
//                .setWhen(System.currentTimeMillis()).setSound(Uri.parse(""))
//				.setVibrate(new long[] { 0, 100, 300, 400 })
                    .setContentIntent(contentIntent).build();
        }
        if (mNotifiMgr == null)
            mNotifiMgr = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        assert mNotifiMgr != null;
        mNotifiMgr.notify(12345, mNotifi);
    }

    //更新通知栏
    private void updateNotify(int loadedLen) {
        if (mNotifiviews == null || mNotifiMgr == null) return;
        mNotifiviews.setTextViewText(R.id.tv_subtitle, loadedLen + "%");
        mNotifiviews.setProgressBar(R.id.progressBar1, 100,
                loadedLen, false);
        // mNotifiviews.setViewVisibility(R.id.tv_title, View.INVISIBLE);
        mNotifiMgr.notify(12345, mNotifi);
    }

    //结束通知栏
    private void finishNotify(Context mContext, String APK_UPGRADE) {
        if (mNotifiviews == null || mNotifiMgr == null || mNotifi == null) return;
        if (!TextUtils.isEmpty(APK_UPGRADE)) { //
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(APK_UPGRADE)),
                    "application/vnd.android.package-archive");

            mNotifi.contentIntent = PendingIntent.getActivity(mContext, 0,
                    intent, 0);
        }

        mNotifiviews.setTextViewText(R.id.tv_title, TextUtils.isEmpty(APK_UPGRADE) ? "下载失败!" : "下载完成，请点击完成升级");
        mNotifiviews.setViewVisibility(R.id.tv_subtitle, View.INVISIBLE);
        mNotifiviews.setViewVisibility(R.id.progressBar1, View.INVISIBLE);
        mNotifiMgr.notify(12345, mNotifi);
    }

    //启动进度条
    private void sendProgress(Context mContext) {
        pDialog = new ProgressDialog(mContext);
        pDialog.setTitle("提示");
        pDialog.setMessage("正在下载..");
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置水平进度条
        pDialog.setMax(100);
//                pDialog.setProgress(0);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    //更新进度条
    private void updateProgress(int loadedLen) {
        if (pDialog != null) {
            pDialog.setProgress(loadedLen);//当前进度值的设置
//                pDialogs.incrementProgressBy(values[0]);
        }
    }

    //结束进度条
    private void finishProgress(String APK_UPGRADE) {
        if (pDialog != null) {
            if (!TextUtils.isEmpty(APK_UPGRADE)) {
                pDialog.setMessage("下载完成...");
                pDialog.setProgress(100);
            } else {
                pDialog.setMessage("下载失败!");
            }
            pDialog.dismiss();
        }
    }



}
