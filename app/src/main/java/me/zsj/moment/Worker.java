package me.zsj.moment;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

/**
 * @author zsj
 */

public class Worker {

    private static Worker sInstance;
    private OkHttpClient client;


    private Worker() {
        client = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();
    }

    public static Worker getInstance() {
        if (sInstance == null) {
            sInstance = new Worker();
        }
        return sInstance;
    }

    public Observable<String> start(String url) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(get(url));
            } catch (IOException e) {
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
            }
        });
    }

    private String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
