package me.zsj.moment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.SeekBar;

import me.zsj.moment.utils.PreferenceManager;
import me.zsj.moment.widget.FontsTextView;

/**
 * @author zsj
 */

public class MomentMuzeiSettings extends AppCompatActivity {

    private static final int[] HOURS = new int[] {
            3, 6, 9, 12
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.moment_muzei_settings);

        AppCompatCheckBox checkBox = (AppCompatCheckBox) findViewById(R.id.checkBox);
        boolean isWifi = PreferenceManager.getBooleanValue(this, "isWifi", true);
        checkBox.setChecked(isWifi);

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                PreferenceManager.putBoolean(this, "isWifi", isChecked));

        int progress = PreferenceManager.getIntValue(this, "hour", 6);

        FontsTextView refreshText = (FontsTextView) findViewById(R.id.refresh_text);
        refreshText.setText(getString(R.string.refresh_text, progress));

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);

        seekBar.setProgress(filterHour(progress));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                PreferenceManager.putInt(MomentMuzeiSettings.this, "hour", HOURS[progress]);
                refreshText.setText(getString(R.string.refresh_text, HOURS[progress]));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                PreferenceManager.putInt(MomentMuzeiSettings.this, "hour", HOURS[progress]);
                refreshText.setText(getString(R.string.refresh_text, HOURS[progress]));
            }
        });
    }

    private int filterHour(int hour) {
        int progress = 0;
        switch (hour) {
            case 3:
                progress = 0;
                break;
            case 6:
                progress = 1;
                break;
            case 9:
                progress = 2;
                break;
            case 12:
                progress = 3;
                break;
            default:
                break;
        }
        return progress;
    }

}
