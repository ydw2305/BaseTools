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

import cn.ydw.www.toolslib.widget.wheelview.common.WheelData;
import cn.ydw.www.toolslib.widget.wheelview.widget.WheelItem;

/**
 * 滚轮图片和文本适配器
 *
 * @author venshine
 */
public class SimpleWheelAdapter extends BaseWheelAdapter<WheelData> {

    private Context mContext;

    public SimpleWheelAdapter(Context context) {
        mContext = context;
    }

    @Override
    public View bindView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new WheelItem(mContext);
        }
        WheelItem item = (WheelItem) convertView;
        item.setImage(mList.get(position).getId());
        item.setText(mList.get(position).getName());
        return convertView;
    }

}
