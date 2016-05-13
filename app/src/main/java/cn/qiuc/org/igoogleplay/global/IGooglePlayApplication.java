package cn.qiuc.org.igoogleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by admin on 2016/5/13.
 */
public class IGooglePlayApplication extends Application{

    private static Context context;
    private static Handler mHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mHandler = new Handler();

    }

    public static Handler getMainHandler() {
        return null;
    }

    public static Context getContext() {
        return context;
    }
}
