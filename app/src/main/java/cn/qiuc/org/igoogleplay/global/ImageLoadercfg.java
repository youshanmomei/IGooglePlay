package cn.qiuc.org.igoogleplay.global;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import cn.qiuc.org.igoogleplay.R;

/**
 * Created by admin on 2016/5/15.
 */
public class ImageLoadercfg {

    public static DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.ic_default)
            .showImageForEmptyUri(R.mipmap.ic_default)
            .showImageOnFail(R.mipmap.ic_default).cacheInMemory(true)
            .cacheOnDisk(true).considerExifParams(true)
            .displayer(new SimpleBitmapDisplayer()).build();


        public static DisplayImageOptions fade_options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_default)
                .showImageForEmptyUri(R.mipmap.ic_default)
                .showImageOnFail(R.mipmap.ic_default).cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300)).build();
}
