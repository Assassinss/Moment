package me.zsj.moment.rx;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import rx.Observable;

/**
 * @author zsj
 */

public class RxFile {

    private RxFile() {
        throw new AssertionError("No instance");
    }

    public static Observable<File> copy(final File from, final File to) {
        return Observable.defer(() -> {
            FileInputStream input = null;
            FileOutputStream output = null;

            try {
                input = new FileInputStream(from);
                output = new FileOutputStream(to);

                FileChannel inputChannel = input.getChannel();
                FileChannel outputChannel = output.getChannel();

                inputChannel.transferTo(0, inputChannel.size(), outputChannel);

                return Observable.just(to);
            } catch (IOException e) {
                return Observable.error(e);
            } finally {
                closeQuietly(input);
                closeQuietly(output);
            }
        });
    }

    public static Observable<File> mkdirsIfNotExists(final File file) {
        return Observable.defer(() -> {
            if (file.mkdirs() || file.isDirectory()) {
                return Observable.just(file);
            } else {
                return Observable.error(new IOException("Failed to mkdirs " + file.getPath()));
            }
        });
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {
            }
        }
    }

}
