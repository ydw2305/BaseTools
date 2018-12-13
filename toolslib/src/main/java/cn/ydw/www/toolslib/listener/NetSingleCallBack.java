package cn.ydw.www.toolslib.listener;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/22
 * 描述:
 * =========================================
 */
public interface NetSingleCallBack<T> {
    int CodeNetSuc = 0, CodeNetErr = -1, CodeNetWait = -2;
    void onNetResult(int netCode, String errMsg, T info);
}
