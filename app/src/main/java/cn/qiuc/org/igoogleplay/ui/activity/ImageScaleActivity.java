package cn.qiuc.org.igoogleplay.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.lib.photoview.HackyViewPager;

public class ImageScaleActivity extends AppCompatActivity {

    private HackyViewPager view_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_scale);

        view_pager = (HackyViewPager) findViewById(R.id.view_pager);

        List<String> list = getIntent().getStringArrayListExtra("imageUrl");
        int currentItem = getIntent().getIntExtra("currentIndex", 0);
//        ImageScaleAdapter adapter = new ImageScaleAdapter(list);//TODO...
//
//        view_pager.setAdapter(adapter);
//        view_pager.setCurrentItem(currentItem);


    }
}
