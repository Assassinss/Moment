package me.zsj.moment;

import android.Manifest;
import android.app.WallpaperManager;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.Locale;

import me.zsj.moment.model.Picture;
import me.zsj.moment.rx.RxDownload;
import me.zsj.moment.rx.RxFile;
import me.zsj.moment.utils.AnimUtils;
import me.zsj.moment.utils.CircleTransform;
import me.zsj.moment.utils.TextureSizeUtils;
import me.zsj.moment.widget.FullImageView;
import rx.Observable;

import static com.bumptech.glide.Glide.with;


/**
 * @author zsj
 */

public class PictureActivity extends RxAppCompatActivity
        implements FullImageView.OnSingleTapListener{

    private static final String AUTHORITIES = BuildConfig.APPLICATION_ID + ".picture";
    public static final String PICTURE = "picture";

    private ProgressBar loading;
    private TextView loadingText;
    private LinearLayout picInfoContainer;

    private Picture picture;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.picture_activity);

        picture = getIntent().getParcelableExtra(PICTURE);

        picInfoContainer = (LinearLayout) findViewById(R.id.pic_info_container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(null);

        loading = (ProgressBar) findViewById(R.id.loading);
        loadingText = (TextView) findViewById(R.id.loading_text);

        TextView author = (TextView) findViewById(R.id.author);
        author.setText(getString(R.string.author, picture.avatar));
        ImageView avatar = (ImageView) findViewById(R.id.avatar);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        FullImageView fullImageView = (FullImageView) findViewById(R.id.full_picture);
        fullImageView.setOnSingleTapListener(this);

        with(this)
                .load(getString(R.string.picture_host, picture.avatarCover))
                .transform(new CircleTransform(this))
                .into(avatar);

        progressBar.setVisibility(View.VISIBLE);
        int targetSize = TextureSizeUtils.getMaxTextureSize() / 4;

        RequestManager requestManager = with(this);

        String url = getString(R.string.picture_host, picture.downloadUrl);

        requestManager.load(url)
                .thumbnail(requestManager
                        .load(url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .override(targetSize, targetSize)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(fullImageView);

    }

    @Override
    public void onSingleTap() {
        if (picInfoContainer.getVisibility() == View.VISIBLE) {
            AnimUtils.hidePicInfo(picInfoContainer);
        } else {
            AnimUtils.showPicInfo(picInfoContainer);
        }
    }

    private void download() {
        Observable.just(null)
                .compose(bindToLifecycle())
                .compose(ensurePermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter(granted -> {
                    if (granted) {
                        loading.setVisibility(View.VISIBLE);
                        loadingText.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        showTips(R.string.permission_required);
                        return false;
                    }
                })
                .flatMap(granted -> ensureDirectory("Moment"))
                .map(file -> new File(file, makeFileName()))
                .flatMap(file -> file.exists()
                        ? Observable.just(file)
                        : save(file))
                .doOnNext(file -> MediaScannerConnection.scanFile(getApplicationContext(),
                        new String[]{file.getPath()}, null, null))
                .subscribe(file -> {
                    loading.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    showTips(getString(R.string.save_path_tips, file.getPath()));
                });
    }

    private void setWallpaper() {
        Observable.just(null)
                .compose(bindToLifecycle())
                .compose(ensurePermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .filter(granted -> {
                    if (granted) {
                        loading.setVisibility(View.VISIBLE);
                        loadingText.setVisibility(View.VISIBLE);
                        return true;
                    } else {
                        showTips(R.string.permission_required);
                        return false;
                    }
                })
                .flatMap(granted -> download(picture.downloadUrl))
                .map(file -> FileProvider.getUriForFile(this, AUTHORITIES, file))
                .doOnNext(uri -> {
                    final WallpaperManager wm = WallpaperManager.getInstance(this);
                    startActivity(wm.getCropAndSetWallpaperIntent(uri));
                })
                .subscribe(uri -> {
                    loading.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                });
    }

    private Observable<File> save(File directory) {
        return download(picture.downloadUrl)
                .flatMap(file -> RxFile.copy(file, directory));
    }

    private String makeFileName() {
        return String.format(Locale.US, "%s.%s",
                picture.downloadUrl.substring(8, picture.downloadUrl.length() - 4), "jpg");
    }

    private Observable<File> download(String url) {
        return RxDownload.get(with(this),
                getString(R.string.picture_host, url));
    }

    private Observable.Transformer<Object, Boolean> ensurePermissions(String... permissions) {
        return RxPermissions.getInstance(this).ensure(permissions);
    }

    private Observable<File> ensureDirectory(String directory) {
        return RxFile.mkdirsIfNotExists(new File(
                Environment.getExternalStorageDirectory(), directory));
    }

    private void showTips(@StringRes int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void showTips(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picture_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.done) {
            setWallpaper();
        } else if (item.getItemId() == R.id.download) {
            download();
        }
        return super.onOptionsItemSelected(item);
    }

}
