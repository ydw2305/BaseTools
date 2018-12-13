/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date  ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.widget.spinkit.style;

import android.animation.ValueAnimator;
import android.graphics.Rect;

import cn.ydw.www.toolslib.widget.spinkit.animation.SpriteAnimatorBuilder;
import cn.ydw.www.toolslib.widget.spinkit.sprite.RectSprite;
import cn.ydw.www.toolslib.widget.spinkit.sprite.Sprite;
import cn.ydw.www.toolslib.widget.spinkit.sprite.SpriteContainer;


public class CubeGrid extends SpriteContainer {

    @Override
    public Sprite[] onCreateChild() {
        int delays[] = new int[]{
                200, 300, 400
                , 100, 200, 300
                , 0, 100, 200
        };
        GridItem[] gridItems = new GridItem[9];
        for (int i = 0; i < gridItems.length; i++) {
            gridItems[i] = new GridItem();
            gridItems[i].setAnimationDelay(delays[i]);
        }
        return gridItems;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        bounds = clipSquare(bounds);
        int width = (int) (bounds.width() * 0.33f);
        int height = (int) (bounds.height() * 0.33f);
        for (int i = 0; i < getChildCount(); i++) {
            int x = i % 3;
            int y = i / 3;
            int l = bounds.left + x * width;
            int t = bounds.top + y * height;
            Sprite sprite = getChildAt(i);
            sprite.setDrawBounds(l, t, l + width, t + height);
        }
    }

    private class GridItem extends RectSprite {
        @Override
        public ValueAnimator onCreateAnimation() {
            float fractions[] = new float[]{0f, 0.35f, 0.7f, 1f};
            return new SpriteAnimatorBuilder(this).
                    scale(fractions, 1f, 0f, 1f, 1f).
                    duration(1300).
                    easeInOut(fractions)
                    .build();
        }
    }
}
