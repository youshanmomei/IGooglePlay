package cn.qiuc.org.igoogleplay.ui.fragment;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.qiuc.org.igoogleplay.http.HttpHelper;
import cn.qiuc.org.igoogleplay.http.Url;
import cn.qiuc.org.igoogleplay.ui.view.FlowLayout2;
import cn.qiuc.org.igoogleplay.util.ColorUtils;
import cn.qiuc.org.igoogleplay.util.DrawableUtil;
import cn.qiuc.org.igoogleplay.util.JsonUtil;

/**
 * Created by admin on 2016/6/19.
 */
public class HotFragment extends BaseFragment {
    private final String TAG = "HotFragment";
    private ArrayList<String> list = new ArrayList<String>();
    private ScrollView scrollView;
    private FlowLayout2 flowLayout;

    @Override
    protected View createSuccessView() {
        scrollView = new ScrollView(getActivity());
        flowLayout = new FlowLayout2(getActivity());
        flowLayout.setPadding(10, 10, 10, 10);
        flowLayout.setHorizontalSpacing(12);
        flowLayout.setVerticalSpacing(12);
        scrollView.addView(flowLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return scrollView;
    }

    @Override
    protected Object loadData() {
        List<String> hotList = (List<String>) JsonUtil.parseJsonToList(HttpHelper.get(String.format(Url.HOT, 0)), new TypeToken<List<String>>(){}.getType());

        if (hotList != null && hotList.size() > 0) {
            list.addAll(hotList);

            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < list.size(); i++) {
                        final TextView textView = new TextView(getActivity());
                        textView.setTextColor(Color.WHITE);
                        textView.setGravity(Gravity.CENTER);

                        GradientDrawable normalDrawable = DrawableUtil.generateDrawable(ColorUtils.genrateBeautifulColor(), 6);
                        GradientDrawable pressedDrawable = DrawableUtil.generateDrawable(Color.parseColor("#aaaaaa"), 6);

                        textView.setBackgroundDrawable(DrawableUtil.generateSelector(normalDrawable, pressedDrawable));
                        textView.setPadding(12, 5, 12, 5);
                        textView.setText(list.get(i));
//                        flowLayout.addView(textView);

                        textView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(getActivity(), textView.getText().toString(), Toast.LENGTH_SHORT);
                            }
                        });
                    }
                }
            });
        } else {
            return null;
        }

        return list;
    }
}
