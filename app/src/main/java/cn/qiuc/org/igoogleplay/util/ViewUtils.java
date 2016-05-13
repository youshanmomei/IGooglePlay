package cn.qiuc.org.igoogleplay.util;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * Created by admin on 2016/5/13.
 */
public class ViewUtils {
    /**
     * remove itself from parent view
     * @param view
     */
    public static void removeSelfFromParent(View view) {
        if (view != null) {
            ViewParent parent = view.getParent();
            if (parent != null && parent instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }
        }
    }

    /**
     * to determine whether the electric shock falls on M
     * @param ev
     * @param v
     * @return
     */
    public static boolean isTouchInView(MotionEvent ev, View v) {
        int[] vLoc = new int[2];
        v.getLocationOnScreen(vLoc);
        float motionX = ev.getX();
        float motionY = ev.getY();
        return motionX>=vLoc[0]&& motionX<=(vLoc[0] + v.getWidth()) && motionY>=vLoc[1] && motionY<=(vLoc[1]+v.getHeight());
    }
}
