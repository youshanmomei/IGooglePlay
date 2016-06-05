package cn.qiuc.org.igoogleplay.ui.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.global.IGooglePlayApplication;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.lib.photoview.PhotoViewAttacher;

/**
 * Created by admin on 2016/6/5.
 */
public class ImageScaleAdapter extends PagerAdapter{
    private final List<String> list;
    private ImageView imageView;

    public ImageScaleAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(IGooglePlayApplication.getContext(), R.layout.adapter_image_scale, null);
        imageView = (ImageView) view.findViewById(R.id.imageview);
        final PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(imageView);
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + list.get(position), imageView, ImageLoadercfg.fade_options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                photoViewAttacher.update();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
