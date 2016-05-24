package cn.qiuc.org.igoogleplay.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
        initImageLoader(getApplicationContext());
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    public static Context getContext() {
        return context;
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); //50MB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs();

        ImageLoader.getInstance().init(config.build());
    }
}
