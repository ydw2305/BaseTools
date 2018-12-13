package cn.ydw.www.toolslib.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * =====================================
 * 作    者: 杨德望
 * 版    本：${VERSION_CODE}.
 * 创建日期：2018/1/30.
 * 描    述： 懒加载片段
 * =====================================
 */
public abstract class BaseLazyFragment extends BasicFragment {

    protected boolean isVisible;
    private boolean isPrepared;
    private boolean isFirst = true;

    public BaseLazyFragment() {
        // Required empty public constructor
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
            onInvisibleLoad();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isPrepared = true;
        lazyLoad();
    }

    private void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        if (isFirst) {
            initData();
            onVisibleLoad(true);
            isFirst = false;
        } else {
            onVisibleLoad(false);
        }
    }
    //页面显示的时候进行动作
    public void onVisibleLoad(boolean isFirst){
        //do something
    }
    //页面隐藏的时候进行动作
    public void onInvisibleLoad() {
        //do something
    }

    public boolean isFirst() {
        return isFirst;
    }

    public abstract void initData();
}
