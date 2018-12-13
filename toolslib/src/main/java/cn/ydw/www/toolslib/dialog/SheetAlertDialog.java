package cn.ydw.www.toolslib.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.ydw.www.toolslib.R;
import cn.ydw.www.toolslib.base.BaseDialogFragment;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/8
 * 描述: 底部选择弹窗
 * =========================================
 */
@SuppressWarnings({"unused", "JavaDoc"})
public class SheetAlertDialog extends BaseDialogFragment implements View.OnClickListener,
        AdapterView.OnItemClickListener {

    private String title, msg, cancel = "取消";
    private String[] destructive, normal;
    private AlertCallback.OnSheetItemListener mItemClickListener;
    private boolean canTouchOutsideEnable = true;
    private Object sheetTag;
    private int sheetId;

    private TextView mTvTitle;
    private TextView mTvMsg;
    private TextView mTvCancel;
    private MyAlertAdapter mAlertAdapter;

    /** @hide */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog mDialog = super.onCreateDialog(savedInstanceState);
        Window mWindow = mDialog.getWindow();
        if (mWindow != null) {
            mWindow.setWindowAnimations(R.style.AnimBottom);
        }
        return mDialog;
    }
    /** @hide */
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.alert_view_sheet_body, container, false);
    }
    /** @hide */
    @Override
    public void initData() {
        mTvTitle = getRootView().findViewById(R.id.tv_alert_title);
        mTvMsg = getRootView().findViewById(R.id.tv_alert_msg);
        ListView lvBody = getRootView().findViewById(R.id.lv_alert_body);
        mTvCancel = getRootView().findViewById(R.id.tv_alert_cancel);
        mTvCancel.setOnClickListener(this);
        lvBody.setAdapter(mAlertAdapter = new MyAlertAdapter());
        lvBody.setOnItemClickListener(this);

        setTitle(title);
        setMsg(msg);
        setCancelBtnTxt(cancel);
        setAlertTxt(destructive, normal);

        getRootView().findViewById(R.id.rel_alert_bg).setOnClickListener(this);

    }
    /** @hide */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_alert_cancel) {
            if (mItemClickListener != null) {
                mItemClickListener.onAlertItemClick(this,
                        AlertCallback.cancelPosition, mTvCancel.getText().toString());
            }
            dismiss();
        } else if (v.getId() == R.id.rel_alert_bg) {
            if (canTouchOutsideEnable) {
                if (mItemClickListener != null) {
                    mItemClickListener.onAlertItemClick(this,
                            AlertCallback.cancelOutside, "It's outside touch");
                }
                dismiss();
            }
        }
    }
    /** @hide */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mItemClickListener != null) {
            mItemClickListener.onAlertItemClick(this,
                    position, mAlertAdapter.getItem(position));
        }
        dismiss();
    }
    /**
     * 设置标题,
     * @param title 标题
     * @return this
     */
    public SheetAlertDialog setTitle(String title) {
        if (mTvTitle != null) {
            if (TextUtils.isEmpty(title)) {
                if (mTvTitle.getVisibility() != View.GONE) {
                    mTvTitle.setVisibility(View.GONE);
                }
            } else {
                if (mTvTitle.getVisibility() != View.VISIBLE) {
                    mTvTitle.setVisibility(View.VISIBLE);
                }
                mTvTitle.setText(title);
            }
        } else {
            this.title = title;
        }
        return this;
    }
    /**
     * 设置内容
     * @param msg 内容
     * @return this
     */
    public SheetAlertDialog setMsg(String msg) {
        if (mTvMsg != null) {
            if (TextUtils.isEmpty(msg)) {
                if (mTvMsg.getVisibility() != View.GONE) {
                    mTvMsg.setVisibility(View.GONE);
                }
            } else {
                if (mTvMsg.getVisibility() != View.VISIBLE) {
                    mTvMsg.setVisibility(View.VISIBLE);
                }
                mTvMsg.setText(msg);
            }
        }  else {
            this.msg = msg;
        }
        return this;
    }

    /**
     * 设置取消按钮
     * @param cancel 按钮提示
     * @return this
     */
    public SheetAlertDialog setCancelBtnTxt(String cancel) {
        if (mTvCancel != null) {
            if (TextUtils.isEmpty(cancel)) {
                if (mTvCancel.getVisibility() != View.GONE) {
                    mTvCancel.setVisibility(View.GONE);
                }
            } else {
                if (mTvCancel.getVisibility() != View.VISIBLE) {
                    mTvCancel.setVisibility(View.VISIBLE);
                }
                mTvCancel.setText(cancel);
            }
        } else {
            this.cancel = cancel;
        }
        return this;
    }

    /**
     * 设置内容文字
     * @param destructive 高亮文字组
     * @param normal 标准文字组
     * @return this
     */
    public SheetAlertDialog setAlertTxt(String[] destructive, String[] normal) {
        if (mAlertAdapter != null) {
            mAlertAdapter.updateInfo(destructive, normal);
        } else {
            this.destructive =  destructive;
            this.normal = normal;
        }
        return this;
    }

    /**
     * 设置点击事件
     * @param l 事件
     * @return this
     */
    public SheetAlertDialog setOnItemClickListener(AlertCallback.OnSheetItemListener l) {
        this.mItemClickListener = l;
        return this;
    }

    /**
     * 设置是否可以点击外部取消
     * @param canTouchOutsideEnable 是否外部取消 (默认可以取消)
     * @return this
     */
    public SheetAlertDialog setCanTouchOutsideEnable(boolean canTouchOutsideEnable) {
        this.canTouchOutsideEnable = canTouchOutsideEnable;
        return this;
    }

    /**
     * 获取特殊标记 (自定义的)
     * @return 标记
     */
    public Object getSheetTag() {
        return sheetTag;
    }

    /**
     * 设置特殊标记
     * @param sheetTag 标记
     */
    public SheetAlertDialog setSheetTag(Object sheetTag) {
        this.sheetTag = sheetTag;
        return this;
    }

    /**
     * 获取控件的自定义id
     * @return id
     */
    public int getSheetId() {
        return sheetId;
    }

    /**
     * 设置控件的自定义id
     * @param sheetId 控件id
     */
    public void setSheetId(int sheetId) {
        this.sheetId = sheetId;
    }

    // 显示
    public void show(FragmentManager manager) {
        show(manager, null);
    }

    // ----------------------------- 内容列表适配器---------------------------------------
    private class MyAlertAdapter extends BaseAdapter {
        private String[] destructive; // 高亮文字
        private String[] normal; // 普通文字
        private final LayoutInflater mInflater;

        MyAlertAdapter() {
            mInflater = LayoutInflater.from(act);
        }

        @Override
        public int getCount() {
            int size = 0;
            if (destructive != null) {
                size += destructive.length;
            }
            if (normal != null) {
                size += normal.length;
            }
            return size;
        }

        @Override
        public String getItem(int position) {
            if (destructive != null && position < destructive.length) {
                return destructive[position];
            } else if (normal != null) {
                int firstSize = destructive == null? 0: destructive.length;
                if (position >= firstSize && position < (firstSize + normal.length)) {
                    return normal[position - firstSize];
                }
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        // 更新数据
        public void updateInfo(String[] destructive, String[] normal) {
            this.destructive = destructive;
            this.normal = normal;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView tvInfo;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.alert_view_sheet_item, parent, false);
                tvInfo = convertView.findViewById(R.id.tv_alert_itemInfo);
                convertView.setTag(tvInfo);
            } else {
                tvInfo = (TextView) convertView.getTag();
            }
            String mItemInfo = getItem(position);
            if (mItemInfo != null) {
                tvInfo.setText(mItemInfo);
            }
            int firstSize = destructive == null? 0: destructive.length;
            tvInfo.setSelected(position < firstSize);
            return convertView;
        }

    }
}
