package me.zsj.moment;

import android.net.Uri;
import android.support.v4.content.FileProvider;

/**
 * @author zsj
 */

public class PictureFileProvider extends FileProvider {

    @Override
    public String getType(Uri uri) {
        return "image/jpg";
    }

}
