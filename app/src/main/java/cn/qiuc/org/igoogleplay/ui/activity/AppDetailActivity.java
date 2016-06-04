package cn.qiuc.org.igoogleplay.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.text.Layout;
import android.text.format.Formatter;
import android.util.Log;
import android.view.MenuItem;
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

public class AppDetailActivity extends BaseActivity implements DownloadManager.DownloadObserver, View.OnClickListener,ViewTreeObserver.OnGlobalLayoutListener {

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
    private int targetHeight;
    private final String TAG = "AppDetailActivity";


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
        targetHeight = ll_safe_des.getMeasuredHeight();
        ll_safe_des.getLayoutParams().height = 0;
        ll_safe_des.requestLayout();

        DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(appInfo);
        if (downloadInfo != null) {
            refreshDownloadState(downloadInfo);
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

    private int getTextViewHeight(TextView textView) {
        Layout layout = textView.getLayout();
        int desired = layout.getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();

        return desired + padding;
    }

    private void calculateLimitLinesHeight() {
        //calculate 5 lines height
        tv_intro.setMaxLines(5);
        tv_intro.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
//                tv_intro.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                limitLineHeight = tv_intro.getHeight();
                tv_intro.setMaxLines(Integer.MAX_VALUE);

                tv_intro.getLayoutParams().height = limitLineHeight;
                tv_intro.requestLayout();
            }
        });
    }

    @Override
    protected void initActionBar() {
        getSupportActionBar().setTitle(getString(R.string.app_detail));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isExpand = false;
    private boolean isIntroExpand = false;

    @Override
    public void onClick(View v) {
        final ValueAnimator animator;
        switch (v.getId()) {
            case R.id.rl_safe:
                animator = isExpand ? ValueAnimator.ofInt(targetHeight, 0) : ValueAnimator.ofInt(0, targetHeight);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        ll_safe_des.getLayoutParams().height = (int) animator.getAnimatedValue();
                        ll_safe_des.requestLayout();
                    }
                });

                animator.setDuration(300);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isExpand = !isExpand;
                        iv_arrow.setBackgroundResource(isIntroExpand ? R.mipmap.arrow_up : R.mipmap.arrow_down);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                break;

            case R.id.ll_intro:
                animator = isIntroExpand ? ValueAnimator.ofInt(totalHeight, limitLineHeight) : ValueAnimator.ofInt(limitLineHeight, totalHeight);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        tv_intro.getLayoutParams().height = (int) animator.getAnimatedValue();
                        if (!isIntroExpand) {
                            sv_container.smoothScrollTo(0, sv_container.getHeight());
                        }
                        tv_intro.requestLayout();
                    }
                });
                animator.setDuration(300);
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isIntroExpand = !isIntroExpand;
                        iv_intro_arrow.setBackgroundResource(isIntroExpand ? R.mipmap.arrow_up : R.mipmap.arrow_down);

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                animator.start();
                break;
            case R.id.btn_download:
                DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(appInfo);
                if (downloadInfo == null) {
                    //need to download
                    DownloadManager.getmInstatnce().download(appInfo);
                } else {
                    if (downloadInfo.state==DownloadManager.STATE_DOWNLOADING || downloadInfo.state == DownloadManager.STATE_WAITTING) {
                        DownloadManager.getmInstatnce().pause(appInfo);
                    }else if (downloadInfo.state == DownloadManager.STATE_FINISH) {
                        DownloadManager.getmInstatnce().installApk(appInfo);
                    } else {
                        DownloadManager.getmInstatnce().download(appInfo);
                    }
                }
                break;
            case R.id.iv_screen_1:
                enterImageScaleActivity(0);
                break;
            case R.id.iv_screen_2:
                enterImageScaleActivity(1);
                break;
            case R.id.iv_screen_3:
                enterImageScaleActivity(2);
                break;
            case R.id.iv_screen_4:
                enterImageScaleActivity(3);
                break;
            case R.id.iv_screen_5:
                enterImageScaleActivity(4);
                break;
        }
    }

    private void enterImageScaleActivity(int currentIndex) {
        Intent intent = new Intent(this, ImageScaleActivity.class);
        intent.putStringArrayListExtra("imageUrl", appInfo.screen);
        intent.putExtra("currentIndex", currentIndex);
        startActivity(intent);
    }

    @Override
    public void onDownloadStateChange(DownloadInfo downloadInfo) {
        refreshDownloadState(downloadInfo);
    }

    private void refreshDownloadState(final DownloadInfo downloadInfo) {
        if (appInfo == null || downloadInfo.id == appInfo.id) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (downloadInfo.state) {
                    case DownloadManager.STATE_NONE:
                        Log.e(TAG, "需要下载");
                        btn_download.setText("下载");
                        btn_download.setBackgroundResource(R.drawable.selector_btn_download);
                        break;
                    case DownloadManager.STATE_WAITTING:
                        Log.e(TAG, "任务等待职工");
                        btn_download.setText("等待中");
                        btn_download.setBackgroundResource(0);
                        break;
                    case DownloadManager.STATE_FINISH:
                        Log.e(TAG, "任务完成");
                        btn_download.setText("安装");
                        btn_download.setBackgroundResource(R.drawable.selector_btn_download);
                        break;
                    case DownloadManager.STATE_PAUSE:
                        Log.e(TAG, "继续下载");
                        btn_download.setText("暂停任务");
                        btn_download.setBackgroundResource(R.drawable.selector_btn_download);
                        break;
                    case DownloadManager.STATE_ERROR:
                        Log.e(TAG, "任务出错");
                        btn_download.setText("重新下载");
                        btn_download.setBackgroundResource(R.drawable.selector_btn_download);
                        break;
                }
            }
        });
    }

    @Override
    public void onDownloadProgressChnage(final DownloadInfo downloadInfo) {
        if (appInfo == null || downloadInfo.id!=appInfo.id) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_download.setBackgroundResource(0);

                float percent = downloadInfo.currentLength * 100f / downloadInfo.size;
                btn_download.setText(percent + "%");
                pb_progress.setProgress((int) downloadInfo.currentLength);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getmInstatnce().unregisterDownloadObserver(this);
    }

    @Override
    public void onGlobalLayout() {

    }
}
