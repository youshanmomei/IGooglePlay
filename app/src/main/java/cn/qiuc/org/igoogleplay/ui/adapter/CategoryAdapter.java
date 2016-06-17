package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.CategoryInfo;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;

/**
 * Created by admin on 2016/6/16.
 */
public class CategoryAdapter extends BasicAdapter<CategoryInfo>{
    public CategoryAdapter(Context context, ArrayList<CategoryInfo> list) {
        super(context, list);
    }

    private final int ITEM_TYPE_TITLE = 0;
    private final int ITEM_TYPE_INFO = 1;

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        CategoryInfo info = list.get(position);
        return TextUtils.isEmpty(info.title)?ITEM_TYPE_INFO:ITEM_TYPE_TITLE;
    }

    InfoHolder infoHolder;
    TitleHolder titleHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryInfo categoryInfo = list.get(position);
        switch (getItemViewType(position)) {
            case ITEM_TYPE_INFO:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.adapter_category_info, null);
                }
                infoHolder = InfoHolder.getHolder(convertView);
                if (!TextUtils.isEmpty(categoryInfo.name1)) {
                    infoHolder.rl_1.setVisibility(View.VISIBLE);
                    infoHolder.tv_1.setText(categoryInfo.name1);
                    ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + categoryInfo.url1, infoHolder.iv_1, ImageLoadercfg.fade_options);
                } else {
                    infoHolder.rl_1.setVisibility(View.INVISIBLE);
                }

                if (!TextUtils.isEmpty(categoryInfo.name2)) {
                    infoHolder.rl_2.setVisibility(View.VISIBLE);
                    infoHolder.tv_2.setText(categoryInfo.name2);
                    ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + categoryInfo.url2, infoHolder.iv_2, ImageLoadercfg.fade_options);
                } else {
                    infoHolder.rl_2.setVisibility(View.INVISIBLE);
                }

                if (!TextUtils.isEmpty(categoryInfo.name3)) {
                    infoHolder.rl_3.setVisibility(View.VISIBLE);
                    infoHolder.tv_3.setText(categoryInfo.name3);
                    ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX + categoryInfo.url3, infoHolder.iv_3, ImageLoadercfg.fade_options);
                } else {
                    infoHolder.rl_3.setVisibility(View.INVISIBLE);
                }
                break;
            case ITEM_TYPE_TITLE:
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.adapter_category_title, null);
                }
                titleHolder = TitleHolder.getHolder(convertView);
                titleHolder.tv_title.setText(categoryInfo.title);
                break;
        }

        return super.getView(position, convertView, parent);
    }

    private static class TitleHolder{
        TextView tv_title;

        public TitleHolder(View convertView) {
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        }

        public static TitleHolder getHolder(View convertView) {
            TitleHolder titleHolder = (TitleHolder) convertView.getTag();
            if (titleHolder == null) {
                titleHolder = new TitleHolder(convertView);
                convertView.setTag(titleHolder);
            }
            return titleHolder;
        }
    }

    private static class InfoHolder {
        RelativeLayout rl_1, rl_2, rl_3;
        TextView tv_1, tv_2, tv_3;
        ImageView iv_1, iv_2, iv_3;

        public InfoHolder(View convertView) {
            rl_1 = (RelativeLayout) convertView.findViewById(R.id.rl_1);
            rl_2 = (RelativeLayout) convertView.findViewById(R.id.rl_2);
            rl_3 =(RelativeLayout) convertView.findViewById(R.id.rl_3);
            tv_1 = (TextView) convertView.findViewById(R.id.tv_1);
            tv_2 = (TextView) convertView.findViewById(R.id.tv_2);
            tv_3 = (TextView) convertView.findViewById(R.id.tv_3);
            iv_1 = (ImageView) convertView.findViewById(R.id.iv_1);
            iv_2 = (ImageView) convertView.findViewById(R.id.iv_2);
            iv_3 = (ImageView) convertView.findViewById(R.id.iv_3);
        }

        public static InfoHolder getHolder(View convertView){
            InfoHolder infoHolder = (InfoHolder) convertView.getTag();
            if (infoHolder == null) {
                infoHolder = new InfoHolder(convertView);
                convertView.setTag(infoHolder);
            }
            return infoHolder;
        }
    }
}
