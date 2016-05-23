package cn.qiuc.org.igoogleplay.util;

import cn.qiuc.org.igoogleplay.global.IGooglePlayApplication;

/**
 * Created by admin on 2016/5/23.
 */
public class CommonUtils {
    public static void runOnUIThread(Runnable runable) {
        IGooglePlayApplication.getMainHandler().post(runable);
    }

    public static float getDimens(int resId) {
        return IGooglePlayApplication.getContext().getResources().getDimension(resId);
    }

    public static String getString(int resId) {
        return IGooglePlayApplication.getContext().getResources().getString(resId);
    }

}
