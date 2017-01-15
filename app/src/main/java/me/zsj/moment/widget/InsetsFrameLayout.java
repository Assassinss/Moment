package me.zsj.moment.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import me.zsj.moment.R;

/**
 * @author zsj
 */

public class InsetsFrameLayout extends FrameLayout implements View.OnApplyWindowInsetsListener {

    private LinearLayout pictureInfoLayout;


    public InsetsFrameLayout(Context context) {
        this(context, null);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsetsFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setOnApplyWindowInsetsListener(this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        pictureInfoLayout = (LinearLayout) findViewById(R.id.pic_info_container);
    }

    @Override
    public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
        int b = insets.getSystemWindowInsetBottom();
        int r = insets.getSystemWindowInsetRight();

        pictureInfoLayout.setPadding(
                pictureInfoLayout.getPaddingLeft(),
                pictureInfoLayout.getPaddingTop(),
                pictureInfoLayout.getPaddingRight() + r,
                pictureInfoLayout.getPaddingBottom() + b);

        setOnApplyWindowInsetsListener(null);

        return insets.consumeSystemWindowInsets();
    }

}
