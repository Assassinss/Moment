package me.zsj.moment.muzei;

import android.content.Context;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import java.util.List;
import java.util.Random;

import me.zsj.moment.Parser;
import me.zsj.moment.R;
import me.zsj.moment.Worker;
import me.zsj.moment.model.Picture;
import me.zsj.moment.utils.NetUtils;
import me.zsj.moment.utils.PreferenceManager;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zsj
 */

public class MomentArtSource extends RemoteMuzeiArtSource {

    private static final String SOURCE_NAME = "MomentArtSource";

    public MomentArtSource() {
        super(SOURCE_NAME);
    }

    /**
     * Remember to call this constructor from an empty constructor!
     *
     * @param name
     */
    public MomentArtSource(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        boolean isWifi = PreferenceManager.getBooleanValue(getApplicationContext(),
                "isWifi", true);

        Context context = getApplicationContext();

        if ((isWifi && NetUtils.isWifiConnected(context)) ||
                !isWifi && NetUtils.checkNet(context)) {
            String url = getString(R.string.pic_category_url, 21, 1);
            Worker.getInstance().start(url)
                    .filter(text -> text != null)
                    .flatMap(Parser.loadData())
                    .filter(pictures -> pictures != null)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::setArtwork, Throwable::printStackTrace);
        }
    }

    private void setArtwork(List<Picture> pictures) {
        Random random = new Random();

        int index = random.nextInt(pictures.size());

        Picture picture = pictures.get(index);

        Uri imageUri = Uri.parse("http://ssyer.com" + picture.downloadUrl);

        publishArtwork(new Artwork.Builder()
                .imageUri(imageUri)
                .token(picture.downloadUrl)
                .build());

        int updateTime = PreferenceManager.getIntValue(getApplicationContext(),
                "hour", 6);

        scheduleUpdate(System.currentTimeMillis() + updateTime * 60 * 60 * 1000);
    }

}
