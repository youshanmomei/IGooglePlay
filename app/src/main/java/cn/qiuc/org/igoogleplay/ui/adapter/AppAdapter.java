package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;

/**
 * Created by admin on 2016/6/7.
 */
public class AppAdapter extends BasicAdapter<AppInfo>{

    private AppHolder holder;

    public AppAdapter(Context context, ArrayList<AppInfo> list) {
        super(context, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_home, null);
        }
        AppInfo appInfo = list.get(position);
        holder = AppHolder.getHolder(convertView);

        holder.tv_app_name.setText(appInfo.name);
        holder.rb_rating.setRating(appInfo.stars);
        holder.tv_size.setText(Formatter.formatFileSize(context, appInfo.size));
        holder.tv_des.setText(appInfo.des);

        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+appInfo.iconUrl, holder.iv_icon, ImageLoadercfg.options, animateFirstListener);

        return convertView;
    }

    static class AppHolder{
        ImageView iv_icon;
        TextView tv_app_name, tv_size, tv_des;
        RatingBar rb_rating;

        public AppHolder(View convertView) {
            iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            tv_size = (TextView) convertView.findViewById(R.id.tv_app_name);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            rb_rating = (RatingBar) convertView.findViewById(R.id.rb_rating);
        }

        public static AppHolder getHolder(View convertView) {
            AppHolder viewHolder = (AppHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new AppHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }

    }
}
