package me.zsj.moment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

/**
 * @author zsj
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.about_activity);

        TextView sourceCode = (TextView) findViewById(R.id.source_code);
        sourceCode.setText(Html.fromHtml(getString(R.string.source_code)));
        sourceCode.setMovementMethod(new LinkMovementMethod());

        TextView appVersion = (TextView) findViewById(R.id.app_version);
        appVersion.setText(String.format("v%s", BuildConfig.VERSION_NAME));

        TextView openSource = (TextView) findViewById(R.id.open_source);
        openSource.setText(Html.fromHtml(getString(R.string.open_source_used)));
        openSource.setMovementMethod(new LinkMovementMethod());
    }

}
