package cn.qiuc.org.igoogleplay.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2016/5/20.
 */
public class ProgressArc extends View{
    private final static int START_PROGRESS = -90;
    private static final int SET_PROGRESS_END_TIME = 1000;
    private static final float RATIO = 360f;
    public final static int PROGRESS_STYLE_NO_PROGRESS = -1;
    public final static int PROGRESS_STYLE_DOWNLOADING = 0;
    public final static int PROGRESS_STYLE_WAITING = 1;
    private int mDrawableForegroundResId;

    private Drawable mDrawableForeground;
    private int mStyle;
    private Paint mPaint;
    private boolean mUserCenter = false;
    private RectF mArcRect;
    private OnProgressChangeListener mProgressChangeListener;
    private int mArcDiameter;
    private long mStartTime;
    private int mEndTime;
    private float mStartProgress;
    private float mCurrentProgress;
    private float mProgress;
    private int mProgressColor;
    private float mSweep;

    public ProgressArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        int strokeWidth = 2;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);

        mUserCenter = false;
        mArcRect = new RectF();
    }

    public ProgressArc(Context context) {
        super(context);
        int strokeWidth = 2;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);

        mUserCenter = false;
        mArcRect = new RectF();
    }

    public void setProgressChangeListener(OnProgressChangeListener listener){
        mProgressChangeListener = listener;
    }

    public void setForegroundRessource(int resId) {
        if (mDrawableForegroundResId == resId) {
            return;
        }
        this.mDrawableForegroundResId = resId;
        mDrawableForeground = getResources().getDrawable(mDrawableForegroundResId);
        invalidateSafe();
    }

    public void setArcDiameter(int diameter) {
        mArcDiameter = diameter;
    }

    public void setProgressColor(int progressColor) {
        int mProgressColor1 = progressColor;
        mPaint.setColor(progressColor);
    }

    public void setStyle(int style) {
        this.mStyle = style;
        if (mStyle == PROGRESS_STYLE_WAITING) {
            invalidateSafe();
        }

    }

    public void setProgress(float progress, boolean smooth) {
        float mProgress = progress;
        if (mProgress == 0) {
            mCurrentProgress = 0;
        }
        mStartProgress = this.mCurrentProgress;
        mStartTime = System.currentTimeMillis();
        if (smooth) {
            mEndTime = SET_PROGRESS_END_TIME;
        } else {
            mEndTime = 0;
        }

        invalidateSafe();
    }

    private void invalidateSafe() {
        invalidate();
    }

    protected void onMeasure(int widthMeasureSpe, int heightMeasureSpec) {
        int width = 0;
        int height = 0;

        int widthMode = MeasureSpec.getMode(widthMeasureSpe);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpe);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = mDrawableForeground == null ? 0 : mDrawableForeground.getIntrinsicWidth();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = mDrawableForeground == null ? 0 : mDrawableForeground.getIntrinsicHeight();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }

        //calculate the progress bar area
        mArcRect.left = (width - mArcDiameter)* 0.5f;
        mArcRect.top = (height - mArcDiameter) * 0.5f;
        mArcRect.right = (width + mArcDiameter) * 0.5f;
        mArcRect.bottom = (height + mArcDiameter) * 0.5f;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawableForeground != null) {
            mDrawableForeground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            mDrawableForeground.draw(canvas);
        }
        drawArc(canvas);
    }

    protected void drawArc(Canvas canvas) {
        if (mStyle == PROGRESS_STYLE_DOWNLOADING || mStyle == PROGRESS_STYLE_WAITING) {
            float factor;
            long currentTime = System.currentTimeMillis();

            if (mProgress == 100) {
                factor = 1;
            } else {
                if (currentTime - mStartTime < 0) {
                    factor = 0;
                }else if (currentTime - mStartTime > mEndTime) {
                    factor = -1;
                } else {
                    factor = (currentTime - mStartTime) / (float) mEndTime;
                }
            }

            mPaint.setColor(mProgressColor);
            mCurrentProgress = mStartProgress + factor * (mProgress - mStartProgress);
            mSweep = mCurrentProgress * RATIO;
            canvas.drawArc(mArcRect, START_PROGRESS, mSweep, mUserCenter, mPaint);
            if (factor != 1 && mStyle == PROGRESS_STYLE_DOWNLOADING) {
                invalidate();
            }
            if (mCurrentProgress > 0) {
                notifyProgressChanged(mCurrentProgress);
            }

        }
    }

    private void notifyProgressChanged(float currentProgress) {
        if (mProgressChangeListener != null) {
            mProgressChangeListener.onProgressChange(currentProgress);
        }
    }

    public static interface OnProgressChangeListener {
        void onProgressChange(float smoothProgress);
    }
}
