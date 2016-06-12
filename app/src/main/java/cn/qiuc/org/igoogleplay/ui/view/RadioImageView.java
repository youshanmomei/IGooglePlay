package cn.qiuc.org.igoogleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by admin on 2016/6/13.
 */
public class RadioImageView extends ImageView {
    private float ratio;//width/height

    public RadioImageView(Context context) {
        this(context, null);
    }

    public RadioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ratio = attrs.getAttributeFloatValue("http://schemas.android.com/apk/res/cn.qiuc.org.igoogleplay", "riv_ratio", 0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        if (ratio != 0) {
            int height = (int) (width / ratio);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
