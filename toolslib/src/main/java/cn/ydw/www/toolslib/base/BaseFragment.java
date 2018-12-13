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
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/2/6.
 * 描    述：
 * =====================================
 */
public abstract class BaseFragment extends BasicFragment {

    private boolean isFirst = true;

    public BaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isFirst) {
            initData();
            isFirst = false;
        } else {
            reloadData();
        }
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void reloadData() {
        // reset data
    }
    public abstract void initData();
}
