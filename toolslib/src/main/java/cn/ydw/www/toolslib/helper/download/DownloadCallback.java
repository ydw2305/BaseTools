package cn.ydw.www.toolslib.helper.download;

import java.io.Serializable;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/14
 * 描述: 下载回调监听器
 * =========================================
 */
public interface DownloadCallback extends Serializable {
    /**
     * 下载结果
     * @param fileUrl 请求的网址
     * @param localPath 本地地址
     * @param isLocal 是否本地缓存调用
     */
    void onDownloadResult(String fileUrl, String localPath, boolean isLocal);

    /**
     * 下载进度
     * @param fileUrl 请求的网址
     * @param progress 进度, 百分之几
     * @param speed 下载速度, byte/s
     */
    void onDownLoadProgress(String fileUrl, float progress, float speed);
}
