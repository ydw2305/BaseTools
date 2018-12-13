package cn.ydw.www.toolslib.helper.coordinator;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

/**
 * ========================================
 *
 * @author 杨德望
 * Create on 2018/10/16
 * 描述:
 * =========================================
 */
public class BehaviorFooter extends CoordinatorLayout.Behavior<View> {


    public BehaviorFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 判断child的布局是否依赖 dependency
     * 根据逻辑来判断返回值，返回 false 表示不依赖，返回 true 表示依赖
     * 在一个交互行为中，Dependent View 的变化决定了另一个相关 View 的行为。
     * 在这个例子中， Button 就是 Dependent View，因为 TextView 跟随着它。
     * 实际上 Dependent View 就相当于我们前面介绍的被观察者
     */
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Toolbar;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        child.setY(dependency.getY() + dependency.getHeight());
        return true;
    }
}
