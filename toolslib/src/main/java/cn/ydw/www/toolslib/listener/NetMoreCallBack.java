package cn.ydw.www.toolslib.listener;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/22
 * 描述:
 * =========================================
 */
public interface NetMoreCallBack {
    void onSuccess(Object... info);
    void onError(Object... msg);
}
