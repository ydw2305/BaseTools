/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;

/**
 * 滚轮工具类
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public class WheelUtils {

    private static final String TAG = "WheelView";

    /**
     * 打印日志
     *
     * @param msg 字符串
     */
    public static void log(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.d(TAG, msg);
        }
    }

    /**
     * 判断集合是否为空
     *
     * @param c 集合
     * @param <V> 参数类型
     * @return 是否空
     */
    public static <V> boolean isEmpty(Collection<V> c) {
        return (c == null || c.size() == 0);
    }

    /**
     * 遍历查找TextView
     *
     * @param view 视图
     * @return 文本
     */
    public static TextView findTextView(View view) {
        if (view instanceof TextView) {
            return (TextView) view;
        } else {
            if (view instanceof ViewGroup) {
                return findTextView(((ViewGroup) view).getChildAt(0));
            } else {
                return null;
            }
        }
    }

    /**
     * dip转换到px
     *
     * @param context 上下文
     * @param dp dp
     * @return px
     */
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * sp转换到px
     *
     * @param context 上下文
     * @param sp sp
     * @return px
     */
    public static int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

}
