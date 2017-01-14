package me.zsj.moment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import me.zsj.moment.model.Picture;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author zsj
 */

public class Parser {

    private static final String PIC_TAG = "pic";
    private static final String IMG_TAG = "img";
    private static final String SRC_KEY = "src";
    private static final String AVATAR_CONTAINER = "avatar-container";
    private static final String DOWNLOAD_TAG = "download";
    private static final String HREF_ATTR = "href";


    public static Func1<String, Observable<List<Picture>>> loadData() {
        return Parser::parse;
    }

    private static Observable<List<Picture>> parse(String html) {
        return Observable.defer(() -> {
            List<Picture> pictures = new ArrayList<>();
            Document document = Jsoup.parse(html);
            Elements elements = document.getElementsByClass(PIC_TAG);
            for (Element e : elements) {
                Picture picture = new Picture();
                picture.pictureUrl = e.getElementsByClass(PIC_TAG).attr(SRC_KEY);
                Elements avatar = e.getElementsByClass(AVATAR_CONTAINER);
                avatarInfo(picture, avatar);
                picture.downloadUrl = e.getElementsByClass(DOWNLOAD_TAG).attr(HREF_ATTR);
                if (picture.avatar != null && picture.avatarCover != null) {
                    pictures.add(picture);
                }
            }
            return Observable.just(pictures);
        });
    }

    private static void avatarInfo(Picture picture, Elements elements) {
        for (Element e : elements) {
            picture.avatarCover = e.getElementsByTag(IMG_TAG).attr(SRC_KEY);
            picture.avatar = e.text();
        }
    }

}
