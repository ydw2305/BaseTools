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

import java.io.Serializable;

/**
 * 滚轮数据
 *
 * @author venshine
 */
@SuppressWarnings ({"unused","WeakerAccess"})
public class WheelData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 资源id
     */
    private int id;

    /**
     * 数据名称
     */
    private String name;

    public WheelData() {
    }

    public WheelData(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "WheelData{" + "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
