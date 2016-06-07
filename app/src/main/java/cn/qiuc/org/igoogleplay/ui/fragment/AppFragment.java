package cn.qiuc.org.igoogleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.AppInfo;
import cn.qiuc.org.igoogleplay.http.HttpHelper;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.adapter.AppAdapter;
import cn.qiuc.org.igoogleplay.util.JsonUtil;

/**
 * Created by admin on 2016/6/6.
 */
public class AppFragment extends BaseFragment {

    private final String TAG = "AppFragment";
    private ArrayList<AppInfo> list = new ArrayList<AppInfo>();
    private PullToRefreshListView refreshListView;
    private ListView listView;

    private AppAdapter adapter;

    @Override
    protected Object loadData() {
        String url = String.format(Url.App, list.size() == 0 ? 0 : list.size() + 1);
        List<AppInfo> appList = (List<AppInfo>) JsonUtil.parseJsonToList(HttpHelper.get(url), new TypeToken<List<AppInfo>>() {}.getType());

        if (appList != null) {
            list.addAll(appList);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                    refreshListView.onRefreshComplete();
                }
            });
        }

        return null;
    }

    @Override
    protected View createSuccessView() {
        refreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView = refreshListView.getRefreshableView();
        listView.setSelector(android.R.color.transparent);

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

        adapter = new AppAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return refreshListView;
    }


}
