package cn.qiuc.org.igoogleplay.ui.activity;

import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.fragment.ContentPage;
import cn.qiuc.org.igoogleplay.util.JsonUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppDetailActivity extends BaseActivity {

    OkHttpClient client = new OkHttpClient();
    private String packageName;
    private AppInfo appInfo;
    private ImageView iv_icon;

    @Override
    protected void initView() {
        ContentPage contentPage = new ContentPage(this){

            @Override
            protected View createSuccessView() {
                View view = View.inflate(AppDetailActivity.this, R.layout.activity_detail, null);
                //TODO...


                return view;
            }

            @Override
            protected Object loadData() {
                return AppDetailActivity.this.loadData();
            }
        };
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
//                        pb_progress.setMax((int) appInfo.size);
//                        refreshUI();
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
/*

    private void refreshUI(){
        showAppInfo();

        showAppSafe();

        showAppScreen();

        showAppIntro();

        ll_safe_des.measure(0, 0);
        targetHeight = ll_safe_des.getMeasureHeight();
        ll_safe_des.getLayoutParams().height = 0;
        ll_safe_des.requestLayout();

        DownloadInfo downloadInfo = DownloadManager.getmInstatnce().getDownloadInfo(appInfo);
        if (downloadInfo != null) {
            refreshDownloadState(downloadInfo)
        }
    }
*/

    private void showAppInfo() {
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + appInfo.iconUrl, iv_icon, ImageLoadercfg.fade_opt_ions);
        //TODO
    }

    @Override
    protected void initActionBar() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initDate() {

    }
}
