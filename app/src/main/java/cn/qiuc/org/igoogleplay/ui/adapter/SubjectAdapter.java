package cn.qiuc.org.igoogleplay.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.bean.Subject;
import cn.qiuc.org.igoogleplay.global.ImageLoadercfg;
import cn.qiuc.org.igoogleplay.http.Url;

/**
 * Created by admin on 2016/6/12.
 */
public class SubjectAdapter extends BasicAdapter<Subject>{

    public SubjectAdapter(Context context, ArrayList<Subject> list) {
        super(context, list);
    }

    SubjectViewHolder holder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_subject, null);
        }
        holder = SubjectViewHolder.getHolder(convertView);
        Subject subject = list.get(position);

        holder.tv_text.setText(subject.des);
        ImageLoader.getInstance().displayImage(Url.IMAGE_PREFIX+subject.url, holder.iv_image, ImageLoadercfg.options, animateFirstListener);

        return convertView;
    }

    static class SubjectViewHolder{
        TextView tv_text;
        ImageView iv_image;

        public SubjectViewHolder(View convertView){
            tv_text = (TextView)convertView.findViewById(R.id.tv_text);
            iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        }

        public static SubjectViewHolder getHolder(View convertView){
            SubjectViewHolder viewHolder = (SubjectViewHolder)convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new SubjectViewHolder(convertView);
                convertView.setTag(viewHolder);
            }
            return viewHolder;
        }
    }
}
