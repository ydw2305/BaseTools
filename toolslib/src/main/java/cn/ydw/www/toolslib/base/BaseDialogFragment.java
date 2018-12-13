package cn.ydw.www.toolslib.base;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;

import cn.ydw.www.toolslib.widget.ToastView;

/**
 * @author 杨德望
 * 描述: 基本弹窗片段
 */
public abstract class BaseDialogFragment extends DialogFragment implements BaseTools.BaseToolCallBack{

    private boolean isFirst = true;
    private View mRootView;
    public FragmentActivity act;
    private BaseTools mBaseTools;

    public BaseDialogFragment(){
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogFragmentStyle();
        act = getActivity();
    }
    // 设置弹窗的风格, 子类可以重写, 然后修改弹窗风格
    public void setDialogFragmentStyle(){
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog mDialog = super.onCreateDialog(savedInstanceState);
        Window mDialogWindow = mDialog.getWindow();
        if (mDialogWindow != null) {
            mDialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
        }
        return mDialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            isFirst = true;
            mRootView = initView(inflater, container, savedInstanceState);
        }
        ViewParent parent = mRootView.getParent();
        if(parent != null && parent instanceof ViewGroup){
            ViewGroup viewgroup = (ViewGroup)parent;
            viewgroup.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirst) {
            initData();
            isFirst = false;
        }
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container,
                                  Bundle savedInstanceState);

    public abstract void initData();

    public View getRootView() {
        return mRootView;
    }

    public boolean isFirst() {
        return isFirst;
    }

    // 设置弹窗是否可以外部点击消除
    public void setCanceledOnTouchOutside(boolean cancelAble) {
        Dialog mDialog = getDialog();
        if (mDialog != null) {
            mDialog.setCanceledOnTouchOutside(cancelAble);
        }
    }

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
    public void onDestroyView() {
        super.onDestroyView();
        if (mBaseTools != null) {
            mBaseTools.destroyBackgroundHandler();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBaseTools != null) {
            mBaseTools.destroyBackgroundHandler();
        }
    }
    // 设置填充整个window, (注: 感觉没啥用, 没必要调用, )
    private void setFullWindow(){
        Window win = getDialog().getWindow();
        if (win == null) return;
        String colorStr = "#50000000";// 设置半透明灰
        // 一定要设置Background，如果不设置，window属性设置无效
        //noinspection deprecation
        win.setBackgroundDrawable( new ColorDrawable(Color.parseColor(colorStr)));

        DisplayMetrics dm = new DisplayMetrics();
        FragmentActivity act = getActivity();
        if (act != null) {
            act.getWindowManager().getDefaultDisplay().getMetrics( dm );
        }

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width =  ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }
}
