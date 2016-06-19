package cn.qiuc.org.igoogleplay.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/6/20.
 */
public class FlowLayout2 extends ViewGroup{

    private final String TAG = "FlowLayout2";

    private int verticalSpacing, horizontalSpacing;
    private List<Line> lineList;

    public FlowLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayout2(Context context) {
        super(context);
        init();
    }

    private void init() {
        lineList = new ArrayList<>();
    }

    private Line line;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        resetLines();

        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int unpadWidth = measuredWidth - getPaddingLeft() - getPaddingRight();
        int totalHeight = getPaddingTop() + getPaddingBottom();

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            if (line==null) line = new Line();
            TextView childView = (TextView) getChildAt(i);

            childView.measure(0, 0);

            if (line.getViewList().size() == 0) {
                line.addView(childView);
            } else {
                if (line.getWidth() + childView.getMeasuredWidth() + horizontalSpacing > unpadWidth) {
                    lineList.add(line);
                }

                line = new Line();
                line.addView(childView);

                if (i == (getChildCount() - 1)) {
                    lineList.add(line);
                } else {
                    line.addView(childView);
                    if (i == (getChildCount() - 1)) {
                        lineList.add(line);
                    }
                }

            }
        }

        for (int i = 0; i < lineList.size(); i++) {
            totalHeight += lineList.get(i).getHeight();
        }

        totalHeight += (lineList.size() - 1) * verticalSpacing;
        setMeasuredDimension(measuredWidth, totalHeight);

    }

    private void resetLines() {
        lineList.clear();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);

            if (i != 0) {
                paddingTop += line.getHeight() + verticalSpacing;
            }

            float remainSpacing = getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - line.getWidth();
            float splitSpacing = remainSpacing/line.getViewList().size();

            for (int j = 0; j < line.getViewList().size(); j++) {
                TextView childView = (TextView) line.getViewList().get(i);
                int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth() + splitSpacing), MeasureSpec.EXACTLY);
                int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(line.getHeight(), MeasureSpec.EXACTLY);
                childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

                if (j == 0) {
                    childView.layout(paddingLeft, paddingTop, paddingLeft + childView.getMeasuredWidth(), paddingTop + childView.getMeasuredHeight());
                } else {
                    View lastView = line.getViewList().get(j - 1);
                    int left = lastView.getRight() + horizontalSpacing;
                    int top = paddingTop;
                    childView.layout(left, top, left+childView.getMeasuredWidth(), top+childView.getMeasuredHeight());
                }
            }

        }
    }

    private class Line {
        private ArrayList<View> viewList;
        private int width;
        private int height;

        public ArrayList<View> getViewList(){
            return viewList;
        }

        public int getWidth(){
            return width;
        }

        public int getHeight(){
            return height;
        }

        public Line() {
            viewList = new ArrayList<View>();
        }

        public void addView(View view) {
            if (!viewList.contains(view)) {
                viewList.add(view);

                if (viewList.size() == 1) {
                    width = view.getMeasuredWidth();
                } else {
                    width += (horizontalSpacing + view.getMeasuredHeight());
                }
                height = Math.max(height, view.getMeasuredHeight());

            }

        }

    }

    public void setHorizontalSpacing(int i) {
        this.horizontalSpacing = i;
    }

    public void setVerticalSpacing(int i) {
        this.verticalSpacing = i;
    }
}
