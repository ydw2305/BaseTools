package cn.ydw.www.toolslib.widget.recyclerview.listener;

import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/9/1
 * 描述:
 * =========================================
 */
public interface SwipeListener {
    /**
     * 滑出去之后
     *
     * @param position  角标
     * @param direction 方向 {@link ItemTouchHelper#LEFT ... 之类的}
     *                   有六个类型的值：LEFT、RIGHT、START、END、UP、DOWN
     */
    void onSwiped(int position, int direction);

    /**
     * 正在滑动的时候
     *
     * @param level     层数
     * @param direction 方向 (注: 二进制运算 LEFT、RIGHT、UP、DOWN)
     */
    void onTopSwiping(int level, int direction);
}
