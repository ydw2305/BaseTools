/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.widget.spinkit.animation.interpolator;

import android.view.animation.Interpolator;

public class Ease {
    public static Interpolator inOut() {
        return PathInterpolatorCompat.create(0.42f, 0f, 0.58f, 1f);
    }
}
