package me.zsj.moment;

import android.app.Application;

import com.umeng.analytics.MobclickAgent;

/**
 * @author zsj
 */

public class MomentApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MobclickAgent.setDebugMode(BuildConfig.DEBUG);
    }
}
