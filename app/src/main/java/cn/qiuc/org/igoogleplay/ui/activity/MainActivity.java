package cn.qiuc.org.igoogleplay.ui.activity;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import cn.qiuc.org.igoogleplay.R;
import cn.qiuc.org.igoogleplay.lib.pagerslidingtab.PagerSlidingTab;
import cn.qiuc.org.igoogleplay.ui.adapter.MainPagerAdapter;

public class MainActivity extends BaseActivity {

    private DrawerLayout draw_layout;
    private PagerSlidingTab sliding_tab;
    private ViewPager view_pager;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_main);
        draw_layout = (DrawerLayout) findViewById(R.id.draw_layout);
        sliding_tab = (PagerSlidingTab) findViewById(R.id.sliding_tab);
        view_pager = (ViewPager) findViewById(R.id.view_pager_main);

        //set shadow
        draw_layout.setDrawerShadow(R.drawable.ic_drawer_shadow, GravityCompat.START);
    }

    @Override
    protected void initActionBar() {
        mActionBar = getSupportActionBar();
        mActionBar.setTitle(getString(R.string.app_name));
        //to show left button
        mActionBar.setDisplayHomeAsUpEnabled(true);
        //set the left button able to click
        mActionBar.setHomeButtonEnabled(true);

        //set toggle switch
        //used to open and close menu
        drawerToggle = new ActionBarDrawerToggle(this, draw_layout, R.drawable.ic_drawer, -1, -1);
        drawerToggle.syncState();
    }

    @Override
    protected void initListener() {
        //strip line sliding effect
        draw_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerToggle.onDrawerSlide(drawerView, slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerToggle.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerToggle.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                drawerToggle.onDrawerStateChanged(newState);
            }
        });
    }

    @Override
    protected void initDate() {
        view_pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        sliding_tab.setViewPager(view_pager);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
