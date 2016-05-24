package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;

/**
 * Created by admin on 2016/5/15.
 */
public class HomePicAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<String> urlList;

    public HomePicAdapter(Context context, ArrayList<String> urlList) {
        super();
        this.context = context;
        this.setUrlList(urlList);
    }

    @Override
    public int getCount() {
        return urlList==null?0:urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = (ImageView) View.inflate(context, R.layout.adapter_home_pic, null);

        //must use this to init
        //or will throw Exception: java.lang.IllegalStateException: ImageLoader must be init with configuration before using
//        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
        //or init it in IGooglePlayApplication
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + urlList.get(position), imageView, ImageLoadercfg.options);
        ((ViewGroup) container).addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewGroup)container).removeView((View)object);
    }

    public void setUrlList(ArrayList<String> urlList) {
        this.urlList = urlList;
    }
}
