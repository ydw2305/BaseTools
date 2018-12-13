package android.support.v7.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Change the package method <code>absorbGlows</code> to be protected so we could
 * override it in the subclass.
 *
 * @author 杨德望
 * Create on 2018/10/22
 */

public class TempRecyclerView extends RecyclerView {

    protected TempRecyclerView(Context context) {
        this(context, null);
    }

    protected TempRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected TempRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void absorbGlows(int velocityX, int velocityY) {}
}
