package cn.ydw.www.toolslib.helper.download;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import cn.ydw.www.toolslib.ToolConstants;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/4
 * 描述: 消息结果回调, 主要用于回馈到UI界面
 * =========================================
 */
class DownloadResultHelper{

    /**
     * 发送下载成功的消息
     * @param mHandler 把持者
     * @param fileUrl 文件网址
     * @param localPath 本地地址
     */
    static void sendDownloadSuc(Handler mHandler, String fileUrl, String localPath, boolean isLocal) {
        if (mHandler != null && !TextUtils.isEmpty(fileUrl)) {
            Bundle mBundle = new Bundle();
            mBundle.putString(ToolConstants.DownloadPathUrl, fileUrl);
            mBundle.putString(ToolConstants.DownloadPathLocal, localPath);
            mBundle.putBoolean(ToolConstants.DownloadIsLocal, isLocal);
            mHandler.sendMessage(mHandler.obtainMessage(ToolConstants.DownloadState_Suc, mBundle));
        }
    }

    /**
     * 发送下载中的消息
     * @param mHandler 把持者
     * @param fileUrl 文件网址
     * @param progress 进度
     * @param speed 速度
     */
    static void sendDownloadWait(Handler mHandler, String fileUrl, float progress, float speed) {
        if (mHandler != null && !TextUtils.isEmpty(fileUrl)) {
            Bundle mBundle = new Bundle();
            mBundle.putString(ToolConstants.DownloadPathUrl, fileUrl);
            mBundle.putFloat(ToolConstants.DownloadProgress, progress);
            mBundle.putFloat(ToolConstants.DownloadSpeed, speed);
            mHandler.sendMessage(mHandler.obtainMessage(ToolConstants.DownloadState_Wait, mBundle));
        }
    }

    /**
     * 发送下载失败的消息
     * @param mHandler 把持者
     * @param fileUrl 文件网址
     */
    static void sendDownloadErr(Handler mHandler, String fileUrl) {
        if (mHandler != null && !TextUtils.isEmpty(fileUrl)) {
            Bundle mBundle = new Bundle();
            mBundle.putString(ToolConstants.DownloadPathUrl, fileUrl);
            mHandler.sendMessage(mHandler.obtainMessage(ToolConstants.DownloadState_Err, mBundle));
        }
    }

}
