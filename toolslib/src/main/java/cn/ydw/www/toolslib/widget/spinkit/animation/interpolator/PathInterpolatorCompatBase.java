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

import android.graphics.Path;
import android.view.animation.Interpolator;

/**
 * Base implementation for path interpolator compatibility.
 */
class PathInterpolatorCompatBase {

    private PathInterpolatorCompatBase() {
        // prevent instantiation
    }

    public static Interpolator create(Path path) {
        return new PathInterpolatorDonut(path);
    }

    public static Interpolator create(float controlX, float controlY) {
        return new PathInterpolatorDonut(controlX, controlY);
    }

    public static Interpolator create(float controlX1, float controlY1,
                                      float controlX2, float controlY2) {
        return new PathInterpolatorDonut(controlX1, controlY1, controlX2, controlY2);
    }
}
