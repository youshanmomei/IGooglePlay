package cn.qiuc.org.igoogleplay.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.qiuc.org.igoogleplay.global.IGooglePlayApplication;
import cn.qiuc.org.igoogleplay.util.ViewUtils;

/**
 * Created by admin on 2016/5/4.
 */
public abstract class BaseFragment extends Fragment {
    protected ContentPage contentPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (contentPage == null) {
            contentPage = new ContentPage(getActivity()) {
                @Override
                protected View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }

                @Override
                protected Object loadData() {
                    return BaseFragment.this.loadData();
                }
            };
        } else {
            ViewUtils.removeSelfFromParent(contentPage);
        }

        return contentPage;
    }

    protected abstract Object loadData();
    protected abstract View createSuccessView();


    protected void runOnUIThread(Runnable runnable){
        IGooglePlayApplication.getMainHandler().post(runnable);
    }
}
