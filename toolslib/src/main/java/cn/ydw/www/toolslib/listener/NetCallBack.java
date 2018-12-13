package cn.ydw.www.toolslib.listener;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/22
 * 描述:
 * =========================================
 */
public interface NetCallBack<T> {
    void onSuccess(T info);
    void onError(String msg);
}
