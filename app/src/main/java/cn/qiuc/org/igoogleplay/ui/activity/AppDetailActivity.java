package cn.qiuc.org.igoogleplay.ui.activity;

import android.text.Layout;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.bean.SafeInfo;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.manager.DownloadInfo;
import cn.qiuc.org.igoogleplay.manager.DownloadManager;
import cn.qiuc.org.igoogleplay.ui.fragment.ContentPage;
import cn.qiuc.org.igoogleplay.util.JsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppDetailActivity extends BaseActivity implements DownloadManager.DownloadObserver, View.OnClickListener {

    OkHttpClient client = new OkHttpClient();
    private String packageName;
    private AppInfo appInfo;
    private ImageView iv_icon;
    private ScrollView sv_container;
    private ImageView iv_icon1;
    private TextView tv_name;
    private TextView tv_download_num;
    private TextView tv_version;
    private TextView tv_date;
    private TextView tv_size;
    private RatingBar rb_star;
    private RatingBar rl_safe;
    private LinearLayout ll_safe_des;
    private ImageView iv_save_1;
    private ImageView iv_save_2;
    private ImageView iv_save_3;
    private ImageView iv_arrow;
    private ImageView iv_des_1;
    private ImageView iv_des_2;
    private ImageView iv_des_3;
    private TextView tv_des_1;
    private TextView tv_des_2;
    private TextView tv_des_3;
    private ImageView iv_screen_1;
    private ImageView iv_screen_2;
    private ImageView iv_screen_3;
    private ImageView iv_screen_4;
    private ImageView iv_screen_5;
    private TextView tv_intro;
    private TextView tv_author;
    private ImageView iv_intro_arrow;
    private LinearLayout ll_intro;
    private ProgressBar pb_progress;
    private TextView btn_download;


    @Override
    protected void initView() {
        ContentPage contentPage = new ContentPage(this) {

            @Override
            protected View createSuccessView() {
                View view = View.inflate(AppDetailActivity.this, R.layout.activity_detail, null);
                sv_container = (ScrollView) view.findViewById(R.id.sv_container);
                iv_icon1 = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_app_name);
                tv_download_num = (TextView) view.findViewById(R.id.tv_download_num);
                tv_version = (TextView) view.findViewById(R.id.tv_version);
                tv_date = (TextView) view.findViewById(R.id.tv_date);
                tv_size = (TextView) view.findViewById(R.id.tv_size);
                rb_star = (RatingBar) view.findViewById(R.id.rb_star);

                //safe info
                rl_safe = (RatingBar) view.findViewById(R.id.rl_safe);
                ll_safe_des = (LinearLayout) view.findViewById(R.id.ll_safe_des);
                iv_save_1 = (ImageView) view.findViewById(R.id.iv_save_1);
                iv_save_2 = (ImageView) view.findViewById(R.id.iv_save_2);
                iv_save_3 = (ImageView) view.findViewById(R.id.iv_save_3);

                iv_arrow = (ImageView) view.findViewById(R.id.iv_arrow);
                iv_des_1 = (ImageView) view.findViewById(R.id.iv_des_1);
                iv_des_2 = (ImageView) view.findViewById(R.id.iv_des_2);
                iv_des_3 = (ImageView) view.findViewById(R.id.iv_des_3);
                tv_des_1 = (TextView) view.findViewById(R.id.tv_des_1);
                tv_des_2 = (TextView) view.findViewById(R.id.tv_des_2);
                tv_des_3 = (TextView) view.findViewById(R.id.tv_des_3);

                //app screen
                iv_screen_1 = (ImageView) view.findViewById(R.id.iv_screen_1);
                iv_screen_2 = (ImageView) view.findViewById(R.id.iv_screen_2);
                iv_screen_3 = (ImageView) view.findViewById(R.id.iv_screen_3);
                iv_screen_4 = (ImageView) view.findViewById(R.id.iv_screen_4);
                iv_screen_5 = (ImageView) view.findViewById(R.id.iv_screen_5);

                //app intro
                tv_intro = (TextView) view.findViewById(R.id.tv_intro);
                tv_author = (TextView) view.findViewById(R.id.tv_author);
                iv_intro_arrow = (ImageView) view.findViewById(R.id.iv_intro_arrow);
                ll_intro = (LinearLayout) view.findViewById(R.id.ll_intro);

                //download app
                pb_progress = (ProgressBar) view.findViewById(R.id.pb_progress);
                btn_download = (TextView) view.findViewById(R.id.btn_download);

                return view;
            }

            @Override
            protected Object loadData() {
                return AppDetailActivity.this.loadData();
            }
        };

        setContentView(contentPage);
        packageName = getIntent().getStringExtra("packageName");

        DownloadManager.getmInstatnce().registerDownloadObserver(this);
    }

    protected Object loadData() {
        try {
            Request request = new Request.Builder().url(String.format(Url.APP_DETAIL, packageName)).build();
            Response response = client.newCall(request).execute();

            appInfo = JsonUtil.parseJsonToBean(response.body().string(), AppInfo.class);

            if (appInfo != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pb_progress.setMax((int) appInfo.size);
                        refreshUI();
                    }
                });
            } else {
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return appInfo;
    }

    private void refreshUI() {
        showAppInfo();

        showAppSafe();

        showAppScreen();

        showAppIntro();

        ll_safe_des.measure(0, 0);
//        targetHeight = ll_safe_des.getMeasureHeight();TODO...
        ll_safe_des.getLayoutParams().height = 0;
        ll_safe_des.requestLayout();

        DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(appInfo);
        if (downloadInfo != null) {
//            refreshDownloadState(downloadInfo);
        }
    }

    private void showAppInfo() {
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.iconUrl, iv_icon, ImageLoadercfg.fade_options);
        tv_name.setText(appInfo.name);
        tv_download_num.setText("下载：" + appInfo.downloadNum);
        tv_version.setText("版本：" + appInfo.version);
        tv_date.setText("日期：" + appInfo.date);
        tv_size.setText("大小：" + Formatter.formatFileSize(this, appInfo.size));
        rb_star.setRating(appInfo.stars);
    }


    private void showAppSafe() {
        SafeInfo safeInfo1 = appInfo.safe.get(0);
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo1.safeUrl, iv_save_1, ImageLoadercfg.fade_options);
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo1.safeDesUrl, iv_des_1, ImageLoadercfg.fade_options);
        tv_des_1.setText(safeInfo1.safeDes);

        if (appInfo.safe.size() > 1) {
            SafeInfo safeInfo2 = appInfo.safe.get(1);
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo2.safeUrl, iv_save_2, ImageLoadercfg.fade_options);
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo2.safeDesUrl, iv_des_2, ImageLoadercfg.fade_options);
            tv_des_2.setText(safeInfo2.safeDes);
        } else {
            iv_save_2.setVisibility(View.GONE);
            iv_des_2.setVisibility(View.GONE);
            tv_des_2.setVisibility(View.GONE);
        }

        if (appInfo.safe.size() > 2) {
            SafeInfo safeInfo3 = appInfo.safe.get(2);
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo3.safeUrl, iv_save_3, ImageLoadercfg.fade_options);
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + safeInfo3.safeDesUrl, iv_des_3, ImageLoadercfg.fade_options);
            tv_des_3.setText(safeInfo3.safeDes);
        } else {
            iv_save_3.setVisibility(View.GONE);
            iv_des_3.setVisibility(View.GONE);
            tv_des_3.setVisibility(View.GONE);
        }
    }

    private void showAppScreen() {
        iv_screen_1.setOnClickListener(this);
        iv_screen_2.setOnClickListener(this);
        iv_screen_3.setOnClickListener(this);
        iv_screen_4.setOnClickListener(this);
        iv_screen_5.setOnClickListener(this);

        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.screen.get(0), iv_screen_1, ImageLoadercfg.fade_options);
        if (appInfo.screen.size() > 1) {
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.screen.get(1), iv_screen_2, ImageLoadercfg.fade_options);
        } else {
            iv_screen_2.setVisibility(View.GONE);
        }

        if (appInfo.screen.size() > 2) {
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.screen.get(2), iv_screen_3, ImageLoadercfg.fade_options);
        } else {
            iv_screen_3.setVisibility(View.GONE);
        }

        if (appInfo.screen.size() > 3) {
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.screen.get(3), iv_screen_4, ImageLoadercfg.fade_options);
        } else {
            iv_screen_4.setVisibility(View.GONE);
        }

        if (appInfo.screen.size() > 4) {
            ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.screen.get(4), iv_screen_5, ImageLoadercfg.fade_options);
        } else {
            iv_screen_5.setVisibility(View.GONE);
        }

    }

    private void showAppIntro() {
        tv_intro.setText(appInfo.des);
        tv_author.setText(appInfo.autor);

        totalHeight = getTextViewHeight(tv_intro);
        calculateLimitLinesHeight();

    }

    private int totalHeight;
    private int limitLineHeight;

    private int getTextViewHeight(TextView textView){
        Layout layout = textView.getLayout();
        int desired = layout.getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();

        return desired+padding;
    }

    private void calculateLimitLinesHeight(){
        //calculate 5 lines height
        tv_intro.setMaxLines(5);
        tv_intro.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                tv_intro.getViewTreeObserver().removeGlobalOnLayoutListener(this);//TODO
                limitLineHeight = tv_intro.getHeight();
                tv_intro.setMaxLines(Integer.MAX_VALUE);

                tv_intro.getLayoutParams().height = limitLineHeight;
                tv_intro.requestLayout();
            }
        });
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void initListener() {
        rl_safe.setOnClickListener(this);
        ll_intro.setOnClickListener(this);
        btn_download.setOnClickListener(this);
    }

    @Override
    protected void initDate() {

    }

    @Override
    public void onDownloadStateChange(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadProgressChnage(DownloadInfo downloadInfo) {

    }

    @Override
    public void onClick(View v) {

    }
}
