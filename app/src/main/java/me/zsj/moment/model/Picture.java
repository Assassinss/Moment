package me.zsj.moment.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author zsj
 */

public class Picture implements Parcelable{

    public String pictureUrl;
    public String avatar;
    public String avatarCover;
    public String downloadUrl;


    public Picture() {}

    protected Picture(Parcel in) {
        pictureUrl = in.readString();
        avatar = in.readString();
        avatarCover = in.readString();
        downloadUrl = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(Parcel in) {
            return new Picture(in);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pictureUrl);
        dest.writeString(avatar);
        dest.writeString(avatarCover);
        dest.writeString(downloadUrl);
    }
}
