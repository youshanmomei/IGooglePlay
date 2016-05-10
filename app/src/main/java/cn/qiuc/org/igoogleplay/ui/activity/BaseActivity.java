package cn.qiuc.org.igoogleplay.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by admin on 2016/5/4.
 */
public abstract class BaseActivity extends ActionBarActivity {
    protected android.support.v7.app.ActionBar mActionBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initActionBar();
        initListener();
        initDate();
    }

    protected abstract void initView();
    protected abstract void initActionBar();
    protected abstract void initListener();
    protected abstract void initDate();
}
