package cn.ydw.www.toolslib.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.utils.Logger;
import cn.ydw.www.toolslib.widget.ToastView;

/**
 * ========================================
 *
 * @author 杨德望 create on 2018/6/23
 * 描述:
 * =========================================
 */
public final class BaseTools {

    private Context act;
    private ToastView mToast;
    private ProgressDialog mDialog;
    private Handler mBackgroundHandler;

    BaseTools(Context act) {
        this.act = act;
    }

    /**
     * 获取弹窗控件
     * @return 弹窗控件
     */
    public ToastView getToast() {
        return mToast;
    }

    /**
     * 展示toast 弹窗
     * @param s 文本消息
     * @param isError 是否是错误提示, 只需填一个参数就好
     */
    public void showToast(CharSequence s, boolean... isError) {
        if (mToast == null) {
            mToast = new ToastView(act);
        }
        mToast.isShowLong(false)
                .showToast(s, isError);
    }

    /**
     * 展示toast 弹窗, 长
     * @param s 文本消息
     * @param isError 是否是错误提示, 只需填一个参数就好
     */
    public void showToastLong(CharSequence s, boolean... isError) {
        if (mToast == null) {
            mToast = new ToastView(act);
        }
        mToast.isShowLong(true)
                .showToast(s, isError);
    }

    /**
     * 展示toast 弹窗, 显示金币经验
     * @param s 文本消息
     * @param gold 金币数
     * @param ex 经验数
     */
    public void showToast(CharSequence s, int gold, int ex) {
        if (mToast == null) {
            mToast = new ToastView(act);
        }
        mToast.isShowLong((gold > 0 || ex > 0))
                .showToast(s, gold, ex);
    }


    /**
     * 展示弹窗
     */
    public void showDialog(CharSequence s){
        try {
            if (mDialog == null) {
                mDialog = new ProgressDialog(act, R.style.BlueProgressTheme);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setCancelable(true);
            }
            mDialog.setMessage(s);
            if (!mDialog.isShowing()) {
                mDialog.show();
            }
        } catch (Exception e) {
            Logger.e("展示弹窗异常", e);
        }
    }

    /**
     * 隐藏弹窗
     */
    public void hintDialog(){
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }
        } catch (Exception e) {
            Logger.e("消除弹窗异常", e);
        }
    }

    public void destroyDialog(){
        try {
            if (mDialog != null) {
                mDialog.dismiss();
                mDialog.onDetachedFromWindow();
                mDialog = null;
            }
        } catch (Exception e) {
            Logger.e("销毁弹窗异常", e);
        }
    }

    /**
     * 启调后台子线程
     * @return 线程把持
     */
    public Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("bgThread");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    public void destroyBackgroundHandler() {
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    public interface BaseToolCallBack{
        /**
         * 获取弹窗控件
         * @return 弹窗控件
         */
        ToastView getToastView();

        /**
         * 展示toast 弹窗, 短
         * @param s 文本消息
         * @param isError 是否是错误提示, 只需填一个参数就好
         */
        void showToast(CharSequence s, boolean... isError);

        /**
         * 展示toast 弹窗, 长
         * @param s 文本消息
         * @param isError 是否是错误提示, 只需填一个参数就好
         */
        void showToastLong(CharSequence s, boolean... isError);

        /**
         * 展示toast 弹窗, 显示金币经验
         * @param s 文本消息
         * @param gold 金币数
         * @param ex 经验数
         */
        void showToast(CharSequence s, int gold, int ex);


        /**
         * 展示弹窗
         */
        void showDialog(CharSequence s);

        /**
         * 隐藏弹窗
         */
        void hintDialog();

        Handler getBackgroundHandler();
    }
}
