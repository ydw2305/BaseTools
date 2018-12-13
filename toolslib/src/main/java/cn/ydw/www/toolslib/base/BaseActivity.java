package cn.ydw.www.toolslib.base;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import cn.ydw.www.toolslib.widget.ToastView;


/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/1/6.
 * 描    述：基本活动页, 仅作为继承使用
 * =====================================
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseTools.BaseToolCallBack{
    public BaseActivity act = this;
    private BaseTools mBaseTools;

    @Override
    public ToastView getToastView() {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        return mBaseTools.getToast();
    }

    @Override
    public void showToast(CharSequence s, boolean... isError) {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        mBaseTools.showToast(s, isError);
    }

    @Override
    public void showToastLong(CharSequence s, boolean... isError) {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        mBaseTools.showToastLong(s, isError);
    }

    @Override
    public void showToast(CharSequence s, int gold, int ex) {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        mBaseTools.showToast(s, gold, ex);
    }

    @Override
    public void showDialog(CharSequence s) {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        mBaseTools.showDialog(s);
    }

    @Override
    public void hintDialog() {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        mBaseTools.hintDialog();
    }

    @Override
    public Handler getBackgroundHandler() {
        if (mBaseTools == null) {
            mBaseTools = new BaseTools(act);
        }
        return mBaseTools.getBackgroundHandler();
    }

    @Override
    public void onDestroy() {
        setVisible(false);
        super.onDestroy();
        if (mBaseTools != null) {
            mBaseTools.destroyBackgroundHandler();
        }
    }
}
