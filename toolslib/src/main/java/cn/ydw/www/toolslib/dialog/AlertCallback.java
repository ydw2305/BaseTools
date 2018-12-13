package cn.ydw.www.toolslib.dialog;

import android.widget.EditText;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/8/17
 * 描述:
 * =========================================
 */
public interface AlertCallback {
    int surePosition = 0, cancelPosition = -1, cancelOutside = -2;

    interface OnSheetItemListener{
        /**
         * 列表式的选择弹窗
         * @param mAlert 控件
         * @param position 每一行的角标,除了以下按钮小于0, 其他基本按钮从0开始算
         *                 取消的角标为{@link AlertCallback#cancelPosition},
         *                 点击外部的角标为 {@link AlertCallback#cancelOutside}
         * @param itemMsg 每一行的显示文字
         */
        void onAlertItemClick(SheetAlertDialog mAlert, int position, String itemMsg);
    }
    interface OnBtnListener{
        /**
         * 对话框式的选择弹窗
         * @param mAlert 控件
         * @param position 确定或取消, 除了以下消极按钮小于0, 其他基本按钮从0开始算
         *                 确定的角标为{@link AlertCallback#surePosition},
         *                 取消的角标为{@link AlertCallback#cancelPosition},
         *                 点击外部的角标为 {@link AlertCallback#cancelOutside}
         * @param mInput 输入框
         */
        void onBtnClick(InputAlertDialog mAlert, int position, EditText mInput);
    }
}
