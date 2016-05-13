package cn.qiuc.org.igoogleplay.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * Created by admin on 2016/5/13.
 */
public class HomeFragment extends BaseFragment {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switchPage();
        }
    };

    private void switchPage() {
//        int page = (viewPager.getCurrentItem()+1)%picAdapter.fetCount();
//        viewPager.setCurrentItem(paage);
        handler.sendEmptyMessageDelayed(0, 2000);
    }
    
    @Override
    protected Object loadData() {
        return null;
    }

    @Override
    protected View createSuccessView() {
//        PullTORefresh inflate = View.inflate(getActivity(), R.layout.ptr_listview, null); TODO...
        return null;
    }
}
