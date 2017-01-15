package me.zsj.moment.widget;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.OverScroller;

import me.zsj.moment.utils.ScreenUtils;

/**
 * @author zsj
 */

public class FullImageView extends ImageView implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private Matrix imageMatrix;
    private RectF imageBound;
    private OverScroller scroller;
    private FlingRunnable currentFlingRunnable;

    private int screenWidth;
    private int screenHeight;
    private int viewRealWidth;

    private OnSingleTapListener onSingleTapListener;


    public void setOnSingleTapListener(OnSingleTapListener onSingleTapListener) {
        this.onSingleTapListener = onSingleTapListener;
    }

    public FullImageView(Context context) {
        this(context, null);
    }

    public FullImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setScaleType(ScaleType.MATRIX);

        imageMatrix = new Matrix();
        imageBound = new RectF();

        screenWidth = ScreenUtils.getWidth(context);
        screenHeight = ScreenUtils.getHeight(context);

        scroller = new OverScroller(context);
        gestureDetector = new GestureDetector(context, getOnGestureListener());

        setOnTouchListener(this);
    }

    private boolean firstIn = true;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable background = getDrawable();

        if (background != null && firstIn) {
            firstIn = false;
            int width = background.getIntrinsicWidth();
            int height = background.getIntrinsicHeight();

            float ratio = (float) screenHeight / (float) height;
            viewRealWidth = (int) (width * ratio);

            imageBound.set(0, 0, viewRealWidth, screenHeight);
            imageMatrix.postScale(ratio, ratio);
            imageMatrix.postTranslate(-(viewRealWidth - screenWidth) / 2f, 0);

            setImageMatrix(imageMatrix);
        }

    }

    private GestureDetector.SimpleOnGestureListener getOnGestureListener() {
        return new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                    float distanceX, float distanceY) {
                if (viewRealWidth > screenWidth) {
                    FullImageView.this.onScroll(distanceX);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                currentFlingRunnable = new FlingRunnable();
                currentFlingRunnable.fling((int) velocityX, (int) velocityY);
                post(currentFlingRunnable);
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (onSingleTapListener != null) {
                    onSingleTapListener.onSingleTap();
                }
                return super.onSingleTapConfirmed(e);
            }
        };
    }

    private RectF getMatrixRectF() {
        Matrix matrix = imageMatrix;
        RectF rect = new RectF();
        Drawable d = getDrawable();
        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }

    private void onScroll(float distanceX) {
        imageMatrix.postTranslate(-distanceX, 0);
        checkMatrixBounds();
        setImageMatrix(imageMatrix);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            cancelFling();
            return true;
        }
        return false;
    }

    private void checkMatrixBounds() {
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        final float viewWidth = getWidth();
        if (rect.left > 0) {
            deltaX = -rect.left;
        }
        if (rect.right < viewWidth) {
            deltaX = viewWidth - rect.right;
        }
        imageMatrix.postTranslate(deltaX, deltaY);
    }

    private void cancelFling() {
        if (currentFlingRunnable != null) {
            currentFlingRunnable.cancelFling();
            currentFlingRunnable = null;
        }
    }

    private class FlingRunnable implements Runnable {

        private int mCurrentX;

        void cancelFling() {
            scroller.forceFinished(true);
        }

        void fling(int velocityX, int velocityY) {
            RectF rect = getMatrixRectF();
            final int minX;
            final int maxX;
            final int minY = 0;
            final int maxY = 0;

            final int startX = Math.round(-rect.left);

            final int viewWidth = getWidth();

            if (viewWidth < rect.width()) {
                minX = 0;
                maxX = Math.round(rect.width() - viewWidth);
            } else {
                minX = maxX = startX;
            }

            mCurrentX = startX;

            if (startX != maxX) {
                scroller.fling(startX, 0, velocityX, velocityY, minX, maxX, minY, maxY);
            }
        }

        @Override
        public void run() {
            if (scroller.isFinished()) return;

            if (scroller.computeScrollOffset()) {
                int newX = scroller.getCurrX();
                imageMatrix.postTranslate(newX - mCurrentX, 0);
                checkMatrixBounds();
                setImageMatrix(imageMatrix);

                mCurrentX = newX;
                postOnAnimation(currentFlingRunnable);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        imageMatrix = null;
        imageBound = null;
        if (currentFlingRunnable != null)
            currentFlingRunnable.cancelFling();
        super.onDetachedFromWindow();
    }

    public interface OnSingleTapListener {

        void onSingleTap();

    }
}
