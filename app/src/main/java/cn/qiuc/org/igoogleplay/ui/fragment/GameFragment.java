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
 * Created by admin on 2016/6/11.
 */
public class GameFragment extends BaseFragment {
    private static final String TAG = "GameFragment";
    private PullToRefreshListView refreshListView;
    private ListView listView;
    private AppAdapter adapter;
    private ArrayList<AppInfo> list = new ArrayList<AppInfo>();

    @Override
    protected Object loadData() {
        String url = String.format(Url.GAME, list.size() == 0 ? 0 : list.size() + 1);
        final ArrayList<AppInfo> appList = (ArrayList<AppInfo>) JsonUtil.parseJsonToList(HttpHelper.get(url), new TypeToken<List<AppInfo>>(){}.getType());

        if (appList != null) {
            list.addAll(appList);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                    //if the load data is less than 20 , that is not enough.
                    //then there is no need to load more
                    refreshListView.setMode(appList.size() >= 20 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                    refreshListView.onRefreshComplete();
                }
            });
        } else {
            return null;
        }

        return list;
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
                        refreshView.onRefreshComplete();
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

        return listView;
    }
}
