package cn.ydw.www.toolslib.model;

import cn.ydw.www.toolslib.ToolConstants;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/12/3
 * 描述:
 * =========================================
 */
public class DownloadFileModel {
    public String fileUrl;
    public String localPath;
    public int state = ToolConstants.DownloadState_Wait;

//    public String waitTime; // 添加了, 但未开始下载的启动时间
//    public String startTime; // 正式开始下载的启动时间
//    public String endTime; // 下载结束的终止时间

    public DownloadFileModel() {
    }

    public DownloadFileModel(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public boolean equals(Object mo) {
        if (this == mo) return true;
        if (mo == null || getClass() != mo.getClass()) return false;

        DownloadFileModel mthat = (DownloadFileModel) mo;

        return fileUrl != null ? fileUrl.equals(mthat.fileUrl) : mthat.fileUrl == null;
    }

    @Override
    public int hashCode() {
        return fileUrl != null ? fileUrl.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DownloadFileModel{" +
                "fileUrl='" + fileUrl + '\'' +
                ", localPath='" + localPath + '\'' +
                ", state=" + state +
//                ", waitTime='" + waitTime + '\'' +
//                ", startTime='" + startTime + '\'' +
//                ", endTime='" + endTime + '\'' +
                '}';
    }
}
