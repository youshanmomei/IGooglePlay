package cn.qiuc.org.igoogleplay.ui.fragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.io.IOException;
import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.bean.Home;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.activity.AppDetailActivity;
import cn.qiuc.org.igoogleplay.ui.adapter.HomeAdapter;
import cn.qiuc.org.igoogleplay.ui.adapter.HomePicAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by admin on 2016/5/13.
 */
public class HomeFragment extends BaseFragment {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switchPage();
        }
    };
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private HomeAdapter adapter;
    private ViewPager viewPager;
    private OkHttpClient okHttpClient = new OkHttpClient();

    private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
    private HomePicAdapter picAdapter;

    private void switchPage() {
        int page = (viewPager.getCurrentItem()+1)%picAdapter.getCount();
        viewPager.setCurrentItem(page);
        handler.sendEmptyMessageDelayed(0, 2000);
    }
    
    @Override
    protected View createSuccessView() {
        refreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView = refreshListView.getRefreshableView();
        listView.setSelector(android.R.color.transparent);
        View header = View.inflate(getActivity(), R.layout.header_home, null);
        viewPager = (ViewPager) header.findViewById(R.id.view_pager);

        calculateViewPagerHeight();

        initListener();

        adapter = new HomeAdapter(getActivity(), list);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);

        return refreshListView;

    }

    private void initListener() {
        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(final PullToRefreshBase<ListView> refreshView) {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshListView.onRefreshComplete();
                    }
                });
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                contentPage.loadDataAndRefreshView();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AppDetailActivity.class);
                intent.putExtra("packageName", list.get(position - 2).packageName);
                startActivity(intent);
            }
        });
    }

    /**
      * calculate in accordance with the picture of the height should be displayed
     */
    private void calculateViewPagerHeight() {
        int screenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int height = screenWidth * 3 / 8;
        viewPager.getLayoutParams().height = height;
        viewPager.requestLayout();
    }

    Home home = null;

    @Override
    protected Object loadData() {
        String url = String.format(Url.HOME, list.size() == 0 ? 0 : list.size() + 1);
        Request build = new Request.Builder().url(url).build();
        String response = null;
        try {
            response = okHttpClient.newCall(build).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(response)) {
            if (list.size() == 0) {
                return null;
            } else {
                runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT);
                        refreshListView.onRefreshComplete();
                    }
                });
            }
        } else {
            home = Home.fromJson(response);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    if (home != null) {
                        if (home.getPicture() != null && home.getPicture().size() > 0) {
                            picAdapter = new HomePicAdapter(getActivity(), home.getPicture());
                            viewPager.setAdapter(picAdapter);
                            switchPage();
                        }

                        list.addAll(home.getList());
                        adapter.notifyDataSetChanged();
                    }
                    refreshListView.onRefreshComplete();
                }
            });
        }

        return home;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
    }
}
