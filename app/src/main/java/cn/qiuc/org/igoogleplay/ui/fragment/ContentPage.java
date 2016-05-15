package cn.qiuc.org.igoogleplay.ui.fragment;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.qiuc.org.igoogleplay.R;

/**
 * Created by admin on 2016/5/12.
 */
public abstract class ContentPage extends RelativeLayout {

    private static final int MSG_REFRESH_VIEW = 1000;
    private PageState mState = PageState.STATE_LOADING;//the state of the current page, the initialized is loading state
    private View loadingView;
    private View emptyView;
    private View errorView;
    private View successView;

    /**
     * page state enumeration
     */
    public enum PageState {
        STATE_LOADING(1), STATE_SUCCESS(2), STATE_EMPTY(3), STATE_ERROR(4);
        private int value;

        PageState(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }

    }


    public ContentPage(Context context) {
        super(context);
        initPage();
        loadDataAndRefreshView();
    }

    /**
     * init page state
     */
    private void initPage() {
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        //1. first, add all view to ContentPage
        if (loadingView == null) {
            loadingView = View.inflate(getContext(), R.layout.page_loading, null);
            addView(loadingView, params);
        }
        if (emptyView == null) {
            emptyView = View.inflate(getContext(), R.layout.page_empty, null);
            addView(emptyView, params);
        }
        if (errorView == null) {
            errorView = View.inflate(getContext(), R.layout.page_error, null);
            Button button = (Button) errorView.findViewById(R.id.btn_reload);
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mState = PageState.STATE_LOADING;
                    showPage();
                    loadDataAndRefreshView();
                }
            });
            addView(errorView, params);
        }
        if (successView == null) {
            successView = createSuccessView();
            if (successView != null) {
                addView(successView, params);
            } else {
                throw new IllegalArgumentException("the method createSuccessView() can not return null");
            }
        }

        showPage();
    }

    /**
     * according to state to control which display view
     */
    private void showPage() {
        loadingView.setVisibility(mState == PageState.STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        emptyView.setVisibility(mState == PageState.STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
        errorView.setVisibility(mState == PageState.STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
        successView.setVisibility(mState == PageState.STATE_SUCCESS ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * loading data, and according to load data to refresh view
     */
    public void loadDataAndRefreshView() {
        //use a single thread pool to request
        ExecutorService threadPool = Executors.newFixedThreadPool(1);
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //lazy loading
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //1. to get the requested data page status
                Object loadResult = loadData();
                mState = checkData(loadResult);

                //2.according to the state after requested the data to refresh the view
                //because it is in the child thread
                //so send messagess through the handler
                handler.sendEmptyMessage(MSG_REFRESH_VIEW);
            }
        });
    }

    /**
     * if Object is null, return error.
     * if not null, if it is a List, and size is 0, return empty, else is success
     * @param object
     * @return
     */
    private PageState checkData(Object object) {
        if (object == null) {
            return PageState.STATE_ERROR;
        } else {
            if (object instanceof List<?>) {
                List<?> list = (List<?>) object;
                if (list.size() == 0) {
                    return PageState.STATE_EMPTY;
                } else {
                    return PageState.STATE_SUCCESS;
                }
            } else {
                return PageState.STATE_SUCCESS;
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH_VIEW:
                    showPage();
                    break;
            }
        }
    };


    /**
     * due to the successView uncertainty
     *
     * @return
     */
    protected abstract View createSuccessView();

    /**
     * method of loading data
     * not sure how to load, so it is an abstract method.
     * but the method return a PageState, used to refresh View.
     * @return
     */
    protected abstract Object loadData();
}
