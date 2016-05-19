package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.manager.DownloadInfo;
import cn.qiuc.org.igoogleplay.manager.DownloadManager;

/**
 * Created by admin on 2016/5/15.
 */
public class HomeAdapter extends BasicAdapter<AppInfo>{

    public HomeAdapter(Context context, ArrayList<AppInfo> list) {
        super(context, list);
    }

    HomeHolder holder;
/*

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adaptet_home, null);
        }
        AppInfo appInfo = list.get(position);
        holder = HomeHolder.getHolder(convertView);
        holder.mProgressArc.setForegroundResource(R.mipmap.ic_download);

        //whether to drawer progressBar or not
        holder.mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
        holder.action_text.setText(R.String.app_state_download);
        holder.setAppInfo(appInfo);

        holder.tv_app_name.setText(appInfo.name);
        holder.rb_rating.setRating(appInfo.stars);
        holder.tc_size.setText(Formatter.formatFileSize(context, appInfo.size));
        holder.tv_des.setText(appInfo.des);

        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.iconUrl, holder.iv_icon, ImageLoadercfg.options, animateFirstListener);

        return convertView;
    }
*/

    static class HomeHolder implements DownloadManager.DownloadObserver {

        public static HomeHolder getHolder(View convertView) {
            return null;
        }

        @Override
        public void onDownloadStateChange(DownloadInfo downloadInfo) {

        }

        @Override
        public void onDownloadProgressChnage(DownloadInfo downloadInfo) {

        }
    }


}
