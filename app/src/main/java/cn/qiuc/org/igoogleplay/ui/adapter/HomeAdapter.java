package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.global.IGooglePlayApplication;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.manager.DownloadInfo;
import cn.qiuc.org.igoogleplay.manager.DownloadManager;
import cn.qiuc.org.igoogleplay.ui.view.ProgressArc;
import cn.qiuc.org.igoogleplay.util.CommonUtils;

/**
 * Created by admin on 2016/5/15.
 */
public class HomeAdapter extends BasicAdapter<AppInfo>{

    public HomeAdapter(Context context, ArrayList<AppInfo> list) {
        super(context, list);
    }
    HomeHolder holder;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adaptet_home, null);
        }
        AppInfo appInfo = list.get(position);
        holder = HomeHolder.getHolder(convertView);
        holder.mProgressArc.setForegroundRessource(R.mipmap.ic_download);

        //whether to drawer progressBar or not
        holder.mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
//        holder.action_text.setText(R.String.app_state_download);
        holder.action_txt.setText(R.string.app_state_download);
        holder.setAppInfo(appInfo);

        holder.tv_app_name.setText(appInfo.name);
        holder.rb_rating.setRating(appInfo.stars);
        holder.tv_size.setText(Formatter.formatFileSize(context, appInfo.size));
        holder.tv_des.setText(appInfo.des);

        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.iconUrl, holder.iv_icon, ImageLoadercfg.options, animateFirstListener);

        return convertView;
    }


    static class HomeHolder implements DownloadManager.DownloadObserver {

        AppInfo appInfo;
        RelativeLayout item_action;
        FrameLayout action_progress;
        TextView action_txt;
        ProgressArc mProgressArc;

        ImageView iv_icon;
        TextView tv_app_name, tv_size, tv_des;
        RatingBar rb_rating;

        public HomeHolder(View convertView) {
            iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
            tv_app_name = (TextView) convertView.findViewById(R.id.tv_app_name);
            tv_size = (TextView) convertView.findViewById(R.id.tv_size);
            tv_des = (TextView) convertView.findViewById(R.id.tv_des);
            rb_rating = (RatingBar) convertView.findViewById(R.id.rb_rating);

            item_action = (RelativeLayout) convertView.findViewById(R.id.item_action);
            action_progress = (FrameLayout) convertView.findViewById(R.id.action_progress);
            action_txt = (TextView) convertView.findViewById(R.id.action_txt);

            mProgressArc = new ProgressArc(IGooglePlayApplication.getContext());
            int arcDiameter = (int) CommonUtils.getDimens(R.dimen.progress_arc_width);
            mProgressArc.setArcDiameter(arcDiameter);

            mProgressArc.setProgressColor(IGooglePlayApplication.getContext().getResources().getColor(R.color.progress));
            int size = arcDiameter + 2;
            action_progress.addView(mProgressArc, new ViewGroup.LayoutParams(size, size));

            DownloadManager.getmInstatnce().registerDownloadObserver(this);
            item_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(HomeHolder.this.appInfo);
                    if (downloadInfo == null) {
                        DownloadManager.getmInstatnce().download(HomeHolder.this.appInfo);
                    } else {
                        if (downloadInfo.state == DownloadManager.STATE_DOWNLOADING) {
                            DownloadManager.getmInstatnce().pause(HomeHolder.this.appInfo);
                        }else if (downloadInfo.state == DownloadManager.STATE_FINISH) {
                            DownloadManager.getmInstatnce().installApk(HomeHolder.this.appInfo);
                        } else {
                            DownloadManager.getmInstatnce().download(HomeHolder.this.appInfo);
                        }
                    }

                }
            });
        }

        public void setAppInfo(AppInfo appInfo) {
            this.appInfo = appInfo;
            DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(appInfo);
            if (downloadInfo != null) {
                if (downloadInfo.id==appInfo.id){
                    refreshDownloadState(downloadInfo);
                }
            }
        }

        public static HomeHolder getHolder(View convertView) {
            HomeHolder viewHoler = (HomeHolder) convertView.getTag();
            if (viewHoler == null) {
                viewHoler = new HomeHolder(convertView);
                convertView.setTag(viewHoler);
            }
            return viewHoler;
        }

        @Override
        public void onDownloadStateChange(DownloadInfo downloadInfo) {
            refreshDownloadState(downloadInfo);
        }

        @Override
        public void onDownloadProgressChnage(final DownloadInfo downloadInfo) {
            CommonUtils.runOnUIThread(new Runnable() {

                @Override
                public void run() {
                    if (appInfo != null && appInfo.id == downloadInfo.id) {
                        mProgressArc.setForegroundRessource(R.mipmap.ic_pause);
                        mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
                        float percent = downloadInfo.currentLength * 100f / downloadInfo.size;
                        mProgressArc.setProgress(percent / 100f, true);
                        action_txt.setText((int) (percent) + "%");
                    }
                }
            });
        }


        private void refreshDownloadState(final DownloadInfo downloadInfo) {
            if (appInfo != null && appInfo.id == downloadInfo.id) {
                CommonUtils.runOnUIThread(new Runnable(){

                    @Override
                    public void run() {
                        switch (downloadInfo.state) {
                            case DownloadManager.STATE_NONE:
                                mProgressArc.setForegroundRessource(R.mipmap.ic_download);
                                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                                action_txt.setText(R.string.app_state_download);
                                break;
                            case DownloadManager.STATE_WAITTING:
                                mProgressArc.setForegroundRessource(R.mipmap.ic_pause);
                                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
                                float percent = downloadInfo.currentLength * 100f / downloadInfo.size;
                                mProgressArc.setProgress(percent / 100f, false);
                                action_txt.setText(R.string.app_state_download);
                                break;
                            case DownloadManager.STATE_FINISH:
                                mProgressArc.setForegroundRessource(R.mipmap.ic_install);
                                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                                action_txt.setText(R.string.app_state_download);
                                break;
                            case DownloadManager.STATE_PAUSE:
                                mProgressArc.setForegroundRessource(R.mipmap.ic_resume);
                                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                                action_txt.setText(R.string.app_state_paused);
                                break;
                            case DownloadManager.STATE_ERROR:
                                mProgressArc.setForegroundRessource(R.mipmap.ic_redownload);
                                mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
                                action_txt.setText(R.string.app_state_error);
                                break;
                        }
                    }
                });
            }
        }

    }

}
