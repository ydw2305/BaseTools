/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import cn.ydw.www.toolslib.widget.wheelview.widget.WheelItem;


/**
 * 滚轮数组适配器
 *
 * @author venshine
 */
public class ArrayWheelAdapter<T> extends BaseWheelAdapter<T> {

    private Context mContext;

    public ArrayWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mContext);
        }
        WheelItem wheelItem = (WheelItem) convertView;
        T item = getItem(position);
        if (wheelItem instanceof CharSequence) {
            wheelItem.setText((CharSequence) item);
        } else {
            wheelItem.setText(item != null ? item.toString() : "");
        }
        return convertView;
    }

}
