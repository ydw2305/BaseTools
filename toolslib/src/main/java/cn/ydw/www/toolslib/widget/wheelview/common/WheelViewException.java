/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/
package cn.ydw.www.toolslib.widget.wheelview.common;

/**
 * 滚轮异常类
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public class WheelViewException extends RuntimeException {

    public WheelViewException() {
        super();
    }

    public WheelViewException(String detailMessage) {
        super(detailMessage);
    }

    public WheelViewException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public WheelViewException(Throwable throwable) {
        super(throwable);
    }
}
