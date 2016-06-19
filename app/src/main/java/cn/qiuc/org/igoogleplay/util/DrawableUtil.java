package cn.qiuc.org.igoogleplay.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by admin on 2016/6/19.
 */
public class DrawableUtil {

    /**
     * become a picture
     * @param argb
     * @param radius
     * @return
     */
    public static GradientDrawable generateDrawable(int argb, float radius) {
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setGradientType(GradientDrawable.RECTANGLE);
        gradientDrawable.setColor(argb);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    /**
     * generating a state selector
     * @param normal
     * @param pressed
     * @return
     */
    public static StateListDrawable generateSelector(Drawable normal, Drawable pressed){
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
        stateListDrawable.addState(new int[]{}, normal);

        return stateListDrawable;
    }

}
