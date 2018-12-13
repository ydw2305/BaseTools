/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.ydw.www.toolslib.R;


/**
 * @author XiaoMu
 * Created by Administrator on 2017/5/17.
 */
@Deprecated
public class MyToastUtils {

    /***----------------------以下都是被命名规范后的工具方法体 -------------------->>>> **/
    /**
     * 展示弹窗 在底部
     * @param mContext 活动页
     * @param showView 需要展示的视图
     */
    public static PopupWindow showWindowsInBottom(final Context mContext, final View showView){
        if (showView == null)
            return null;
        PopupWindow popview = getPopupWindow(showView);
        popview.setAnimationStyle(R.style.AnimBottom);
        //取父布局
        View parent = ((ViewGroup) ((Activity)mContext).findViewById(android.R.id.content)).getChildAt(0);
        //显示底部居中
        popview.showAtLocation(parent, Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
        return popview;
    }
    /**
     * 展示弹窗 在中间,
     * @param act 活动页
     * @param showView 需要展示的视图
     */
    public static PopupWindow showWindowsInCenter(Activity act, View showView){
        if (showView == null)
            return null;
        PopupWindow popview = getPopupWindow(showView);
        //取父布局
        View parent = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        //显示底部居中
        popview.showAtLocation(parent, Gravity.CENTER, 0, 0);
        popview.setFocusable(true);
        // 设置点击其他地方就消失
        popview.setOutsideTouchable(true);
        //设置软件盘模式
        popview.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popview.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return popview;
    }
    /**
     * 产生popupwindow对象, 需要传入要显示的视图, 返回PopupWindow
     */
    @NonNull
    private static PopupWindow getPopupWindow(@NonNull View showView) {
        PopupWindow popview = new PopupWindow(showView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popview.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popview.setFocusable(true);
        // 设置点击其他地方就消失
        popview.setOutsideTouchable(true);
        //设置软件盘模式
        popview.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popview.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return popview;
    }

}
