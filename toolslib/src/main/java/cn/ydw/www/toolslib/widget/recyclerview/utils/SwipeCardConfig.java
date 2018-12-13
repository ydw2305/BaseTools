package cn.ydw.www.toolslib.widget.recyclerview.utils;

import android.content.Context;
import android.util.TypedValue;


/**
 * 初始化一些配置信息、固定数据
 */
public class SwipeCardConfig {
    //屏幕上最多同时显示几个Item
    public static int MAX_SHOW_COUNT;

    //每一级Scale相差0.05f，translationY相差15dp,translationZ相差0.5dp左右
    public static float SCALE_X_GAP; // x 轴的缩放量, 即每层都会水平缩放这个数量, 如 0.05f 表示每层缩小0.05倍, 注意不大于1
    public static float SCALE_Y_GAP; // y 轴的缩放量, 即每层都会垂直缩放这个数量, 如 0.05f 表示每层缩小0.05倍, 注意不大于1
    public static float ROTATE_GAP; // 旋转量, 即每层都会旋转这个数量, 如 15f 表示每层顺时针旋转15度
    public static float ROTATE_OFFSET; // 旋转量差值, 即每层的选择量都会因为这个变化
    public static int TRANS_X_GAP; // x 轴的偏移量, 即每层都会按照 x 轴偏移这个数量, 如 10 表示每层往右偏移 10px
    public static int TRANS_Y_GAP; // y 轴的偏移量, 即每层都会按照 y 轴偏移这个数量, 如 10 表示每层往下偏移 10px
    public static int TRANS_Z_GAP;// z 轴的偏移量, 即每层都会按照 z 轴偏移这个数量, 如 10 表示每层增高 10px

    static {
        // 最大显示数
        MAX_SHOW_COUNT = 4;
        // 缩放
        SCALE_X_GAP = 0f;
        SCALE_Y_GAP = 0f;
        // 旋转
        ROTATE_GAP = 6f;
        ROTATE_OFFSET = 0.3f;
        // 位移
        TRANS_X_GAP = 0;
        TRANS_Y_GAP = 0;
        TRANS_Z_GAP = 1;
    }

}
