package cn.ydw.www.toolslib.listener;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/22
 * 描述:
 * =========================================
 */
public interface NetMore2CallBack<A, B> {
    void onSuccess(A info);
    void onError(B info);
}
