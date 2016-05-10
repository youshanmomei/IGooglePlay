package cn.qiuc.org.igoogleplay.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import cn.qiuc.org.igoogleplay.ui.fragment.BaseFragment;
import cn.qiuc.org.igoogleplay.ui.fragment.FragmentFactory;

/**
 * Created by admin on 2016/5/4.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
    private String[] tabTitles = {"首页","应用","游戏","专题","推荐","分类","热门"};

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        BaseFragment fragment = FragmentFactory.createFragment(position);
        return fragment;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
