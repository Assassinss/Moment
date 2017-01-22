package me.zsj.moment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author zsj
 */

public class AboutActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView version = (TextView) findViewById(R.id.version);
        version.setText(String.format("Version %s", BuildConfig.VERSION_NAME));

        LinearLayout github = (LinearLayout) findViewById(R.id.github);
        github.setOnClickListener(this);

        LinearLayout contacts = (LinearLayout) findViewById(R.id.contacts);
        contacts.setOnClickListener(this);

        LinearLayout sourceCode = (LinearLayout) findViewById(R.id.source_code);
        sourceCode.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.github) {
            Intent githubIntent = new Intent(Intent.ACTION_VIEW);
            githubIntent.setData(Uri.parse("https://github.com/Assassinss"));
            githubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(githubIntent);
        } else if (v.getId() == R.id.contacts) {
            Intent contactsIntent = new Intent(Intent.ACTION_SENDTO);
            contactsIntent.setData(Uri.parse("mailto:zhu948742@gmail.com"));
            contactsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(contactsIntent);
        } else if (v.getId() == R.id.source_code) {
            Intent sourceIntent = new Intent(Intent.ACTION_VIEW);
            sourceIntent.setData(Uri.parse("https://github.com/Assassinss/Moment"));
            sourceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(sourceIntent);
        }
    }
}
