package me.zsj.moment.rx;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author zsj
 */

public class RxDownload {

    private RxDownload() {
        throw new AssertionError("No instance");
    }

    public static Observable<File> get(RequestManager requestManager, String url) {
        return Observable.create(new Observable.OnSubscribe<File>() {
            @Override
            public void call(Subscriber<? super File> subscriber) {
                try {
                    subscriber.onNext(requestManager.load(url)
                            .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get());
                } catch (InterruptedException | ExecutionException e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

}
