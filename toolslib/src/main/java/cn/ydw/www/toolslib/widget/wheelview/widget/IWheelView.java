/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.widget;

import java.util.HashMap;
import java.util.List;

import cn.ydw.www.toolslib.widget.wheelview.adapter.BaseWheelAdapter;

/**
 * 滚轮抽象接口
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public interface IWheelView<T> {

    /**
     * 滚轮数据是否循环，默认不循环
     */
    boolean LOOP = false;

    /**
     * 滚轮默认数量
     */
    int WHEEL_SIZE = 3;

    /**
     * 滚轮选中刻度是否可点击
     */
    boolean CLICKABLE = false;

    /**
     * 设置滚轮选中刻度是否可点击
     *
     * @param clickable 是否可点
     */
    void setWheelClickable(boolean clickable);

    /**
     * 设置滚轮是否循环滚动
     *
     * @param loop 是否循环
     */
    void setLoop(boolean loop);

    /**
     * 设置滚轮个数
     *
     * @param wheelSize 个数
     */
    void setWheelSize(int wheelSize);

    /**
     * 设置滚轮数据
     *
     * @param list 数据
     */
    void setWheelData(List<T> list);

    /**
     * 设置滚轮数据源适配器
     *
     * @param adapter 适配器
     */
    void setWheelAdapter(BaseWheelAdapter<T> adapter);

    /**
     * 连接副WheelView
     *
     * @param wheelView 副滚轮器
     */
    void join(WheelView wheelView);

    /**
     * 副WheelView数据
     *
     * @param map 数组
     */
    void joinDatas(HashMap<String, List<T>> map);


}
