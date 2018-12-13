package cn.ydw.www.toolslib.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ydw.www.toolslib.widget.ToastView;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/11/26
 * 描述:
 * =========================================
 */
public abstract class BasicFragment extends Fragment implements BaseTools.BaseToolCallBack {

    public View mRootView;
    public FragmentActivity act;
    private BaseTools mBaseTools;

    public BasicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();
        setHasOptionsMenu(true); //设置片段可以使用菜单栏
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState);
        }
        return mRootView;
    }

    public View getRootView() {
        return mRootView;
    }

    public abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

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
        super.onDestroy();
        if (mBaseTools != null) {
            mBaseTools.destroyBackgroundHandler();
        }
    }
}
