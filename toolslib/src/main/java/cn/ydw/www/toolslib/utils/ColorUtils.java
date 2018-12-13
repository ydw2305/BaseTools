/*******************************************************************************
 * <p>Copyright(c) 2017</p>
 * <p>
 * @autor 杨德望
 * @date ${DATE}
 * @see ${PACKAGE_NAME}
 * </p>
 *
 ******************************************************************************/

package cn.ydw.www.toolslib.utils;

/**
 * @author 杨德望
 * @date 2017/9/27.
 */
public final class ColorUtils {
    /**
     * HSL颜色标准
     * <p>
     * <p>
     * HSL色彩模式是工业界的一种颜色标准，是通过对色调(H)、饱和度(S)、亮度(L)三个颜色通道的变化以及
     *      它们相互之间的叠加来得到各式各样的颜 色的，HSL即是代表色调，饱和度，亮度三个通道的颜色，
     *      这个标准几乎包括了人类视力所能感知的所有颜色，是目前运用最广的颜色系统之一。
     * HSL色彩模式使用HSL模型为图像中每一个像素的HSL分量分配一个0~255范围内的强度值。HSL图像只使用
     *      三种通道，就可以使它们按照不同的比例混合，在屏幕上重现16777216种颜色。
     * 在 HSL 模式下，每种 HSL 成分都可使用从 0到 255的值。（其中L是从黑（0）到白（255）渐变） 。
     */

    /**
     * RGB→HSL的算法描述。
     * <p>
     * 步骤1：把RGB值转成【0，1】中数值。
     * 步骤2：找出R,G和B中的最大值。
     * 步骤3：设L=(maxcolor + mincolor)/2
     * 步骤4：如果最大和最小的颜色值相同，即表示灰色，那么S定义为0，而H未定义并在程 序中通常写成0。
     * 步骤5：否则，测试L：
     * 　　If L<0.5, S=(maxcolor-mincolor)/(maxcolor + mincolor)
     * 　　If L>=0.5, S=(maxcolor-mincolor)/(2.0-maxcolor-mincolor)
     * 步骤6: If R=maxcolor, H=(G-B)/(maxcolor-mincolor)
     * 　　If G=maxcolor, H=2.0+(B-R)/(maxcolor-mincolor)
     * 　　If B=maxcolor, H=4.0+(R-G)/(maxcolor-mincolor)
     * 步骤7：从第6步的计算看，H分成0～6区域。RGB颜色空间是一个立方体而HSL颜色空间是两个六角形锥体，
     * 其中的L是RGB立方体的主对角线。因此，RGB立方体的顶点：红、黄、绿、青、蓝和品红就成为HSL六
     * 角形的顶点，而数值0～6就告诉我们H在哪个部分。H=H*60.0,如果H为负值，则加360。
     *
     * @param rgb RGB 色彩模式
     * @return HSL色彩模式
     */
    public static ColorHSL colorRGBToHSL(ColorRGB rgb) {
        ColorHSL hsl = new ColorHSL();

        float r, g, b, h, s, l;
        r = rgb.r / 255.0f;
        g = rgb.g / 255.0f;
        b = rgb.b / 255.0f;

        float maxColor = Math.max(r, Math.max(g, b));
        float minColor = Math.min(r, Math.min(g, b));

        if (maxColor == minColor) {
            h = 0.0f;
            s = 0.0f;
            l = r;
        } else {
            l = (minColor + maxColor) / 2;

            if (l < 0.5)
                s = (maxColor - minColor) / (maxColor + minColor);
            else
                s = (float) ((maxColor - minColor) / (2.0 - maxColor -
                        minColor));

            if (r == maxColor)
                h = (g - b) / (maxColor - minColor);
            else if (g == maxColor)
                h = (float) (2.0 + (b - r) / (maxColor - minColor));
            else
                h = (float) (4.0 + (r - g) / (maxColor - minColor));

            h /= 6;
            if (h < 0)
                h++;
        }

        hsl.h = (int) Math.round(h * 360.0);
        hsl.s = s;
        hsl.l = l;

        return hsl;
    }

    /**
     * HSL→RGB的算法描述。
     * <p>
     * 步骤1：If S=0,表示灰色，定义R,G和B都为L.
     * 步骤2：否则，测试L:
     * 　　 If L<0.5,temp2=L*(1.0+S)
     * 　　 If L>=0.5,temp2=L+S-L*S
     * 步骤3：temp1=2.0*-temp2
     * 步骤4：把H转换到0～1。
     * 步骤5：对于R,G,B，计算另外的临时值temp3。方法如下：
     * 　　 for R, temp3=H+1.0/3.0
     * 　　 for G, temp3=H
     * 　　 for B, temp3=H-1.0/3.0
     * 　　 if temp3<0, temp3=temp3+1.0
     * 　　 if temp3>1, temp3=temp3-1.0
     * 步骤6：对于R,G,B做如下测试：
     * 　　 If 6.0*temp3<1,color=temp1+(temp2-temp1)*6.0*temp3
     * 　　 Else if 2.0*temp3<1,color=temp2
     * 　　 Else if 3.0*temp3<2,
     * 　　 color=temp1+(temp2-temp1)*((2.0/3.0)-temp3)*6.0
     * 　　 Else color=temp1
     *
     * @param hsl HSL 色彩模式
     * @return RGB 色彩模式
     */
    public static ColorRGB colorHSLToRGB(ColorHSL hsl) {
        ColorRGB rgb = new ColorRGB();

        float r, g, b, h, s, l;
        float temp1, temp2, tempr, tempg, tempb;
        h = hsl.h / 360.0f;
        s = hsl.s;
        l = hsl.l;


        if (s == 0) {
            r = g = b = l;
        } else {
            if (l < 0.5)
                temp2 = l * (1 + s);
            else
                temp2 = (l + s) - (l * s);
            temp1 = 2 * l - temp2;
            tempr = (float) (h + 1.0 / 3.0);
            if (tempr > 1)
                tempr--;
            tempg = h;
            tempb = (float) (h - 1.0 / 3.0);
            if (tempb < 0)
                tempb++;

            // Red
            if (tempr < 1.0 / 6.0)
                r = (float) (temp1 + (temp2 - temp1) * 6.0 * tempr);
            else if (tempr < 0.5)
                r = temp2;
            else if (tempr < 2.0 / 3.0)
                r = (float) (temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempr)
                        * 6.0);
            else
                r = temp1;

            // Green
            if (tempg < 1.0 / 6.0)
                g = (float) (temp1 + (temp2 - temp1) * 6.0 * tempg);
            else if (tempg < 0.5)
                g = temp2;
            else if (tempg < 2.0 / 3.0)
                g = (float) (temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempg)
                        * 6.0);
            else
                g = temp1;

            // Blue
            if (tempb < 1.0 / 6.0)
                b = (float) (temp1 + (temp2 - temp1) * 6.0 * tempb);
            else if (tempb < 0.5)
                b = temp2;
            else if (tempb < 2.0 / 3.0)
                b = (float) (temp1 + (temp2 - temp1) * ((2.0 / 3.0) - tempb)
                        * 6.0);
            else
                b = temp1;
        }

        rgb.r = (int) Math.round(r * 255.0);
        rgb.g = (int) Math.round(g * 255.0);
        rgb.b = (int) Math.round(b * 255.0);

        return rgb;
    }

    public static class ColorHSL {
        public int h; //色相
        public float s; //饱和度
        public float l; //亮度
    }

    public static class ColorRGB {
        public int r; //红
        public int g; //绿
        public int b; //蓝
    }
}