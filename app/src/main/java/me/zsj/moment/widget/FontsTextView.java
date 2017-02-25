package me.zsj.moment.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author zsj
 */

public class FontsTextView extends TextView {


    public FontsTextView(Context context) {
        this(context, null);
    }

    public FontsTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FontsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                "fonts/AlexBrush-Regular.ttf");
        setTypeface(typeface);

    }
}
