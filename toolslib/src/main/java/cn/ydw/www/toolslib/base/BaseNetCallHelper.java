package cn.ydw.www.toolslib.base;

import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/6/4
 * 描述: 请求的基类
 * =========================================
 */
public abstract class BaseNetCallHelper {
    public WeakReference<Activity> act;
    public WeakReference<Context> context;
    public String searchKey = "";

    public BaseNetCallHelper(Activity act) {
        this.act = new WeakReference<>(act);
        this.context = new WeakReference<>((Context) act);
    }

    public BaseNetCallHelper(Context context) {
        this.context = new WeakReference<>(context);
    }

    //重设基本参数
    public abstract void resetParam();

    //下拉刷新
    public abstract void call2Refresh();
    //上拉加载更多
    public abstract void call2LoadMore();

}
