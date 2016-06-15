package cn.qiuc.org.igoogleplay.ui.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.Category;
import cn.qiuc.org.igoogleplay.bean.CategoryInfo;
import cn.qiuc.org.igoogleplay.http.HttpHelper;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.adapter.CategoryAdapter;
import cn.qiuc.org.igoogleplay.util.JsonUtil;

/**
 * Created by admin on 2016/6/16.
 */
public class CategoryFragment extends BaseFragment {
    private static final String TAG = "CategoryFragment";

    private ArrayList<CategoryInfo> list = new ArrayList<CategoryInfo>();
    private ListView listView;
    private CategoryAdapter adapter;

    @Override
    protected View createSuccessView() {
        listView = (ListView) View.inflate(getActivity(), R.layout.listview, null);
        adapter = new CategoryAdapter(getActivity(), list);

        listView.setAdapter(adapter);
        return listView;
    }


    String url = String.format(Url.CATEGORY, list.size() == 0 ? 0 : list.size() + 1);

    @Override
    protected Object loadData() {
        ArrayList<Category> data = (ArrayList<Category>) JsonUtil.parseJsonToList(HttpHelper.get(url), new TypeToken<List<Category>>() {
        }.getType());
        if (data != null) {
            ArrayList<CategoryInfo> categoryInfoList = new ArrayList<>();
            for (int i = 0; i < data.size(); i++) {
                Category category = data.get(i);
                categoryInfoList.add(new CategoryInfo(category.title));
                categoryInfoList.addAll(category.infos);
            }

            for (CategoryInfo c : categoryInfoList) {
                Log.e(TAG, c.toString());
            }
            list.addAll(categoryInfoList);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetInvalidated();
                }
            });
        } else {
            list = null;
        }
        return list;
    }
}
