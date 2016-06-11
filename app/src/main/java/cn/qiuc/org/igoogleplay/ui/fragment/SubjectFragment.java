package cn.qiuc.org.igoogleplay.ui.fragment;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.Subject;
import cn.qiuc.org.igoogleplay.http.HttpHelper;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.adapter.SubjectAdapter;
import cn.qiuc.org.igoogleplay.util.JsonUtil;

/**
 * Created by admin on 2016/6/12.
 */
public class SubjectFragment extends BaseFragment {
    private static final String TAG = "SubjectFragment";
    private ListView listView;
    private SubjectAdapter adapter;
    private ArrayList<Subject> list = new ArrayList<Subject>();
    private PullToRefreshListView refreshListView;

    @Override
    protected View createSuccessView() {
        refreshListView = (PullToRefreshListView) View.inflate(getActivity(), R.layout.ptr_listview, null);
        refreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        listView = refreshListView.getRefreshableView();
        listView.setSelector(android.R.color.transparent);

        refreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
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

        adapter = new SubjectAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return refreshListView;
    }
    


    @Override
    protected Object loadData() {
        String url = String.format(Url.SUBJECT, list.size() == 0 ? 0 : list.size() + 1);
        final ArrayList<Subject> appList = (ArrayList<Subject>) JsonUtil.parseJsonToList(HttpHelper.get(url), new TypeToken<List<Subject>>() {
        }.getType());

        if (appList != null) {
            list.addAll(appList);
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();

                    if (appList.size() < 20) {
                        Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT);
                    }

                    refreshListView.setMode(appList.size() >= 20 ? PullToRefreshBase.Mode.BOTH : PullToRefreshBase.Mode.PULL_FROM_START);
                    refreshListView.onRefreshComplete();
                }
            });
        } else {
            return null;
        }

        return list;
    }
}
