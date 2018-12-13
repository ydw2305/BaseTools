package cn.ydw.www.toolslib.utils.download;

/**
 * @author xiaomu
 * Created by Administrator on 2017/11/28.
 */

public abstract class OnFileStorageListener {
    public abstract void fileStorage(String fileLocalUrl);
    public void onLoadProgress(int progress) {}
}
