package cn.ydw.www.toolslib.listener;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/22
 * 描述:
 * =========================================
 */
public interface NetListCallBack<T> {
    void onSuccess(boolean isRefresh, boolean isFirst, T info);
    void onError(boolean isRefresh, boolean isFirst, String msg);
}
