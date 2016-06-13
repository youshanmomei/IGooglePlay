package cn.qiuc.org.igoogleplay.ui.fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.lib.randomlayout.StellarMap;

/**
 * Created by admin on 2016/6/14.
 */
public class RecommendFragment extends BaseFragment {

    private ArrayList<String> list = new ArrayList<String>();
    private StellarMap stellarMap;
    private static final String TAG = "RecommendFragment";

    @Override
    protected View createSuccessView() {
        stellarMap = new StellarMap(getActivity());
        int padding = getResources().getDimensionPixelSize(R.dimen.stellar_map_padding);
        stellarMap.setInnerPadding(padding, padding, padding, padding);
        return stellarMap;
    }

    @Override
    protected Object loadData() {
        //TODO...
        return null;
    }

    class StellarAdapter implements StellarMap.Adapter {

        @Override
        public int getGroupCount() {
            return 3;
        }

        @Override
        public int getCount(int group) {
            return list.size()/3;
        }

        @Override
        public View getView(int group, int position, View convertView) {
            TextView textView = new TextView(getActivity());
            Random random = new Random();
//            textView.setTextColor(ColorUtils.genrateBeautifulColor());TODO...
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15 + random.nextInt());
            final String word = list.get(group * getCount(group) + position);
            textView.setText(word);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), word, Toast.LENGTH_SHORT);
                }
            });
            Log.e(TAG, "posi:" + position + " group:" + group);
            return textView;
        }

        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return (group+1)%getGroupCount();
        }

        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            return (group+1)%getGroupCount();
        }
    }

}
