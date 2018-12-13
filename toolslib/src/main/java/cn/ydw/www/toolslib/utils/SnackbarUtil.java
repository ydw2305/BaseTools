
package cn.ydw.www.toolslib.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ydw.www.toolslib.R;


/**
 * @author 杨德望 create on 2017/8/7.
 * 描述:
 */
@SuppressWarnings ({"unused", "WeakerAccess"})
public final class SnackbarUtil {
    public static final int Info = 1;
    public static final int Confirm = 2;
    public static final int Warning = 3;
    public static final int Alert = 4;


    public static int red = 0xfff44336;
    public static int green = 0xff4caf50;
    public static int blue = 0xff2195f3;
    public static int orange = 0xffffc107;

    /**
     * 短显示Snackbar，自定义颜色
     *
     * @param view 视图
     * @param message 内容
     * @param messageColor 内容颜色
     * @param backgroundColor 背景色
     * @return 控件
     */
    public static Snackbar ShortSnackbar(View view, String message, int messageColor, int backgroundColor) {
        if (view == null) return null;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        return snackbar;
    }

    /**
     * 长显示Snackbar，自定义颜色
     *
     * @param view 视图
     * @param message 内容
     * @param messageColor 内容颜色
     * @param backgroundColor 背景色
     * @return 控件
     */
    public static Snackbar LongSnackbar(View view, String message, @ColorInt int messageColor, @ColorInt int backgroundColor) {
        if (view == null) return null;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        return snackbar;
    }

    /**
     * 自定义时常显示Snackbar，自定义颜色
     *
     * @param view 视图
     * @param message 内容
     * @param messageColor 内容颜色
     * @param backgroundColor 背景色
     * @return 控件
     */
    public static Snackbar IndefiniteSnackbar(View view, String message, int duration, @ColorInt int messageColor, @ColorInt int backgroundColor) {
        if (view == null) return null;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
        setSnackbarColor(snackbar, messageColor, backgroundColor);
        return snackbar;
    }

    /**
     * 短显示Snackbar，可选预设类型
     *
     * @param view 视图
     * @param message 内容
     * @param type 样式
     * @return 控件
     */
    public static Snackbar ShortSnackbar(View view, String message, int type) {
        if (view == null) return null;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 长显示Snackbar，可选预设类型
     *
     * @param view 视图
     * @param message 内容
     * @param type 样式
     * @return 控件
     */
    public static Snackbar LongSnackbar(View view, String message, int type) {
        if (view == null) return null;
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        switchType(snackbar, type);
        return snackbar;
    }

    /**
     * 自定义时常显示Snackbar，可选预设类型
     *
     * @param view 视图
     * @param message 内容
     * @param duration 间隔时间
     * @param type 样式
     * @return 控件
     */
    public static Snackbar IndefiniteSnackbar(View view, String message, int duration, int type) {
        if (view != null){
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setDuration(duration);
            switchType(snackbar, type);
            return snackbar;
        }
        return null;
    }

    //选择预设类型
    private static void switchType(Snackbar snackbar, int type) {
        switch (type) {
            case Info:
                setSnackbarColor(snackbar, blue);
                break;
            case Confirm:
                setSnackbarColor(snackbar, green);
                break;
            case Warning:
                setSnackbarColor(snackbar, orange);
                break;
            case Alert:
                setSnackbarColor(snackbar, Color.YELLOW, red);
                break;
        }
    }

    /**
     * 设置Snackbar背景颜色
     *
     * @param snackbar 控件
     * @param backgroundColor 背景色
     */
    public static void setSnackbarColor(Snackbar snackbar, @ColorInt int backgroundColor) {
        if (snackbar != null){
            View view = snackbar.getView();
            view.setBackgroundColor(backgroundColor);
        }
    }

    /**
     * 设置Snackbar文字和背景颜色
     *
     * @param snackbar 控件
     * @param messageColor 消息颜色
     * @param backgroundColor 背景色
     */
    public static void setSnackbarColor(Snackbar snackbar, @ColorInt int messageColor, @ColorInt int backgroundColor) {
        if (snackbar != null) {
            View view = snackbar.getView();
            view.setBackgroundColor(backgroundColor);
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(messageColor);
        }
    }

    /**
     * 向Snackbar中添加view
     *
     * @param snackbar 控件
     * @param layoutId 新加的布局ID
     * @param index    新加布局在Snackbar中的位置
     */
    public static void SnackbarAddView(Snackbar snackbar, @LayoutRes int layoutId, int index) {
        if (snackbar != null){
            View snackbarview = snackbar.getView();
            Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbarview;

            View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId, null);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.gravity = Gravity.CENTER_VERTICAL;

            snackbarLayout.addView(add_view, index, p);
        }
    }
}
